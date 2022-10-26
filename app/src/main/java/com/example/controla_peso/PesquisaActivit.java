package com.example.controla_peso;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.dao.daoDados;
import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.example.controla_peso.entidade.EntidadeDados;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PesquisaActivit extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private FloatingActionButton botaoAdicionar;
    private ListView listaDados;
    private Button botaoFiltrar;
    private EditText filtroCodigo;
    private List<EntidadeDados> buscar;
    private Switch switchPesquisarVendido;
    private List<EntidadeCadastroBovino> buscaId;
    private double valor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesquisar_peso);
        switchPesquisarVendido = (Switch)findViewById(R.id.switchPesquisarVendido);
        filtroCodigo = (EditText)findViewById(R.id.editTextText_pesquisa_codigo);
        daoDados dao = new daoDados(this);
        listaDados = (ListView)findViewById(R.id.listView_pesquisa);

        PesquisaDados(null, false);

        listaDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExibeMensagem(i);
            }
        });

        botaoFiltrar = (Button)findViewById(R.id.buttonPesquisar);
        botaoFiltrar.setFocusable(true);
        botaoFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaDados.setAdapter(null);
                PesquisaDados(filtroCodigo.getText().toString(),switchPesquisarVendido.isChecked());

            }
        });

        botaoAdicionar = (FloatingActionButton)findViewById(R.id.floating_adicionar);
        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ChamaCadastro();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // return true;
            ChamaCadastroBovino();
        }if(id == R.id.menu_cadastar_pesagem){
            ChamaCadastro();
        }if(id ==R.id.menu_sair){
            finishAffinity();
        }if(id == R.id.menu_pesquisar_cadastro){
            ChamaPesquisaCadastro();
        }if(id == R.id.menu_totalizar){
            ChamaTotalizador();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ChamaTotalizador(){
        Intent intent = new Intent(this, TotalizadorActivit.class);
        startActivity(intent);
    }

        private void ChamaPesquisaCadastro(){
        Intent intent = new Intent(this, PesquisaCadBovinoActivity.class);
        startActivity(intent);
    }
    public void ChamaCadastro(){
        Intent intent = new Intent(this, CadastroActivit.class);
        startActivity(intent);
    }

  public void PesquisaDados(String filtro, boolean vendido){
      daoDados dao = new daoDados(this);
      if(filtro!=null){
          if(filtro.equals("")){
              filtro = null;
          }
      }
     buscar = dao.Pesquisar(filtro,vendido);
      ArrayAdapter<EntidadeDados> adapter = new ArrayAdapter<EntidadeDados>(this, android.R.layout.simple_list_item_1,buscar);
      listaDados.setAdapter(adapter);

      if(buscar.isEmpty() || buscar == null){
          Toast.makeText(this,"NÃO EXISTEM DADOS A SER EXIBIDOS",Toast.LENGTH_SHORT).show();
      }
  }
    public void ChamaCadastroBovino(){
        Intent intent = new Intent(this, CadastroBovinoActivit.class);
        startActivity(intent);
    }
    public void ExibeMensagem(int index) {
            new AlertDialog.Builder(this)
                .setTitle("Excluir pesagem")
                .setMessage("Tem certeza que deseja excluir este dado de pesagem?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            ExluirPesagem(SelecionaCodigo(index));
                            PesquisaDados(null,switchPesquisarVendido.isChecked());

                    } }).setNegativeButton("NÃO", null) .show();
    }

    public EntidadeDados SelecionaCodigo(int index){
        EntidadeDados entidade = new EntidadeDados();
        String codigo = null;
        long id;
        if(buscar.size()>0){
            entidade = buscar.get(index);
            codigo = entidade.getCodigoBrinco();
            id = entidade.getId();
        }
        return entidade;
    }
    public void ExluirPesagem (EntidadeDados entidade){
        daoDados dao = new daoDados(this);
        dao.excluirDadosPesagem(entidade);
    }




}
