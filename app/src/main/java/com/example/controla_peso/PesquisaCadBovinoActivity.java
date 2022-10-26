package com.example.controla_peso;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.dao.daoDados;
import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.example.controla_peso.entidade.EntidadeDados;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class PesquisaCadBovinoActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;
    private FloatingActionButton botaoAdicionar;
    private ListView listaDados;
    private Button botaoFiltrar;
    private EditText filtroCadCodigo;
    private List<EntidadeCadastroBovino> buscar;
    private String chamada;
    private TextView totalCadastro;
    private Switch switchPesqCadVendido;
    private  daoCadastroBovino dao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesquisa_cadastro);

        Intent it = getIntent();
        //Recuperei a string da outra activity
        chamada = it.getStringExtra("chamada");
        totalCadastro = (TextView)findViewById(R.id.textViewTotal);
        switchPesqCadVendido = (Switch)findViewById(R.id.switchPesqCadVendido);
        filtroCadCodigo =(EditText)findViewById(R.id.editTextText_pesquisaCad_codigo);
        listaDados = (ListView)findViewById(R.id.listView_pesquisaCad);
        PesquisaDados(null);
        VerificaQuantidadeCadastrada();

        botaoAdicionar = (FloatingActionButton)findViewById(R.id.floating_adicionar);
        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChamaCadastroBovino();
            }
        });

       listaDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               try {
                   if(chamada != null){
                       if (chamada.equals("cadastro")) {
                           ChamaCadastro(i);}

                   } else {
                           ExibeMensagemSelecionaAcao(i);
                   }
               }catch (Exception e){
                   Log.e("Erro", String.valueOf(e));
               }
           }
       });

        botaoFiltrar = (Button)findViewById(R.id.buttonPesquisarCad) ;
        botaoFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaDados.setAdapter(null);
                PesquisaDados(filtroCadCodigo.getText().toString());
            }
        });
        /*
         Habilita botão voltar da actionBar
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa_cadastrados, menu);
        return true;
    }
    /**
     * controla o evento de voltar do teclado fisico
     */
     @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        ChamaPesquisaPesagens();
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_cadastrar_bovino){
            // return true;
            ChamaCadastroBovino();
        }
        if (id == R.id.menu_pesquisa_pesagem) {
            // return true;
            ChamaPesquisaPesagens();
        }
        if(id == R.id.menu_cadastrar_bovino_cad_pesagem){
            ChamaCadastroPesagem();
        }

        if(id== R.id.menu_cadastrar_bovino_sair){
            finishAffinity();
        }if(id == android.R.id.home){
            ChamaPesquisaPesagens();

        }

        return super.onOptionsItemSelected(item);
    }

    public void PesquisaDados(String filtro){
         dao = new daoCadastroBovino(this);
        if(filtro!=null){
            if(filtro.equals("")){
                filtro = null;
            }
        }
        try{
            buscar =  dao.buscarCadastro(filtro,switchPesqCadVendido.isChecked());
            ArrayAdapter<EntidadeCadastroBovino> adapter = new ArrayAdapter<EntidadeCadastroBovino>(this, android.R.layout.simple_list_item_1,buscar);
            listaDados.setAdapter(adapter);

            if(buscar.isEmpty() || buscar == null){
                Toast.makeText(this,"NÃO EXISTEM DADOS A SER EXIBIDOS",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            return;
        }

    }
    private void VerificaQuantidadeCadastrada() {
        dao = new daoCadastroBovino(this);
        totalCadastro.setText(String.valueOf(dao.contCadastros()));
    }


    /*
      Retorna o codigo da linha selecionada
     */
    public EntidadeCadastroBovino SelecionaCodigo(int index){
        EntidadeCadastroBovino entidade = new EntidadeCadastroBovino();
        String codigo = null;
        if(buscar.size()>0){
            entidade = buscar.get(index);
            codigo = entidade.getCadCodigoBrinco();
        }
        return entidade;
    }
    public void ChamaCadastroPesagem(){
        Intent intent = new Intent(this, CadastroActivit.class);
        startActivity(intent);
    }

    public void ChamaCadastro(int index){
        Intent it = new Intent(this, CadastroActivit.class);
            it.putExtra("codigo_brinco", SelecionaCodigo(index).getCadCodigoBrinco());
        startActivity(it);
    }
    public void ChamaCadastroBovino(){
        Intent intent = new Intent(this, CadastroBovinoActivit.class);
        startActivity(intent);
    }
    public void ChamaPesquisaPesagens(){
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }
    public void ChamaEditar(EntidadeCadastroBovino entidade){
        Intent intent = new Intent(this, CadastroBovinoActivit.class);
        intent.putExtra("id", String.valueOf(entidade.getId()));
        intent.putExtra("chamada","editar");
        startActivity(intent);
    }


    public void ExibeMensagemSelecionaAcao(int i) {
        final CharSequence[] items = {"Excluir", "Editar/Visualizar", "Cancelar"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Selecione uma opção");
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        if (VerificaExistePesagemCadastrada(SelecionaCodigo(i).getCadCodigoBrinco())) {
                            ExibeMensagemExistemPesagens();
                        } else {
                            ExluirPesagem(SelecionaCodigo(i));
                            PesquisaDados(null);
                        }
                        break;
                    case 1:
                        ChamaEditar(SelecionaCodigo(i));
                        break;
                    case 2:
                        break;
                }
            }

        });
        dialogBuilder.create().show();
    }

    public void ExluirPesagem (EntidadeCadastroBovino entidade){
        daoCadastroBovino dao = new daoCadastroBovino(this);
        dao.excluirDadosPesagem(entidade);
        VerificaQuantidadeCadastrada();
    }
    /*
    Verifica se exixtem pesagens cadastradas
     */
    public Boolean VerificaExistePesagemCadastrada(String codigoBrinco){
        daoCadastroBovino bd = new daoCadastroBovino(this);
        List<Integer> buscar = bd.VerificaPesoCadastrado(codigoBrinco);
        if(buscar.isEmpty()){
            return false;
        }else {
            return true;
        }
    }
    public void ExibeMensagemExistemPesagens() {
        new AlertDialog.Builder(this)
                .setTitle("Verifique!")
                .setMessage("Não foi possivel excluir este cadastro,existem pesagens cadastradas" +
                        "para este animal")
                .setPositiveButton("OK", null).show();
    }

}

