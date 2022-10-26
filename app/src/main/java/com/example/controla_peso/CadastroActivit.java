package com.example.controla_peso;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_peso.dao.daoDados;
import com.example.controla_peso.entidade.EntidadeDados;

import java.util.Date;
import java.util.List;

public class CadastroActivit extends AppCompatActivity {
    private Button BtSalvar;
    private EditText Etcodigo;
    private EditText EtDate;
    private EditText Etpeso;
    private EditText Ettara;
    private ImageButton ImBcodigo;

    EntidadeDados entidade = new EntidadeDados();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_peso);
        ImBcodigo = (ImageButton)findViewById(R.id.imageButton_adicionar);
        Etcodigo = (EditText)findViewById(R.id.editTextText_codigo);
        Etpeso = (EditText)findViewById(R.id.editTextText_peso);
        Ettara = (EditText)findViewById(R.id.editTextText_tara);

        EtDate =(EditText)findViewById(R.id.editTextDate);
        CarregaData(EtDate);
        //pega dados da outra activit
        Intent it = getIntent();
        //Recuperei a string da outra activity
        String informacao = it.getStringExtra("codigo_brinco");
        Etcodigo.setText(informacao);
        Etcodigo.setEnabled(false);

        BtSalvar = (Button)findViewById(R.id.button_salvar);
        BtSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              double ultimoPeso = buscarUlitmPeso(Etcodigo.getText().toString());
              double pesoAtual = Double.parseDouble(Etpeso.getText().toString());
                double ganhoPeso;
              if(ultimoPeso == 0){
                  ganhoPeso = 0;
              }else{
                  ganhoPeso = pesoAtual - ultimoPeso;
              }

               Salvar(ganhoPeso);
            }
        });
        ImBcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChamaCadastro();
            }
        });
        /*
         Habilita botão voltar da actionBar
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        // getSupportActionBar().setTitle("teste");     //Titulo para ser exibido na sua
    }

    /*
      controla o evento de voltar do teclado fisico
     */
    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        ChamaPesquisaPesagens();
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_pesagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id== R.id.menu_cadpesagem_sair){
            ChamaPesquisaPesagens();
        }
        if(id == android.R.id.home){
            ChamaPesquisaPesagens();
        }

        return super.onOptionsItemSelected(item);
    }
    public void ChamaPesquisaPesagens(){
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }


    private void Salvar(double ganho){
        try {
            if(Validar(Etcodigo,Etpeso,Ettara)) {
                entidade.setCodigoBrinco(Etcodigo.getText().toString());
                entidade.setData(EtDate.getText().toString());
                entidade.setPeso(Calculapeso(Etpeso, Ettara));
                entidade.setTara(Double.parseDouble(Ettara.getText().toString()));
                entidade.setGanhoPeso(ganho);


                daoDados bd = new daoDados(this);
                bd.inserir(entidade);
                Toast.makeText(this, "DADOS salvos com sucesso", Toast.LENGTH_SHORT).show();
                VoltarTelaBovinosCadastrados();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"ERRO AO SALVAR O DADOS"+e,Toast.LENGTH_SHORT).show();
        }
    }
    private Double buscarUlitmPeso(String codigo){
        daoDados bd = new daoDados(this);
        List<Double> buscar = bd.buscaultimoPeso(codigo);
        if(buscar.isEmpty()){
            return Double.valueOf(0);
        }else {
            if(buscar.size() >0){
                double dados =  buscar.get(buscar.size()-1);
                return dados;
            }else {
                return Double.valueOf(0);
            }

        }

    }
    public void VoltarTelaBovinosCadastrados(){
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }

    public Double Calculapeso(EditText peso, EditText tara){
        double varPeso = 0;
        double varTara = 0;
        if(peso.getText().toString()!= null || peso.getText().toString() != ""){
            varPeso =  Double.parseDouble(peso.getText().toString());
        }
        if(tara.getText().toString()!= null || tara.getText().toString() != ""){
           varTara = Double.parseDouble(tara.getText().toString());
        }

        double pesoReal = varPeso - varTara;
        return pesoReal;
    }
    public void CarregaData(EditText editTextData){
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        editTextData.setText(dataFormatada.toString());
    }
    public boolean Validar(EditText codigo,EditText peso, EditText tara){
        if(codigo.getText().toString() == null || codigo.getText().toString().equals("")){
            Toast.makeText(this,"CAMPO CODIGO DO BRINCO DEVE SER PREENCHIDO",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(peso.getText().toString() == null || peso.getText().toString().equals("")){
            Toast.makeText(this,"CAMPO PESO DEVE SER PREENCHIDO",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (tara.getText().toString() == null || tara.getText().toString().equals("")){
            Toast.makeText(this,"CAMPO TARA DEVE SER PREENCHIDO",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void ChamaCadastro(){
        Intent intent = new Intent(this, PesquisaCadBovinoActivity.class);
        intent.putExtra("chamada", "cadastro");
        startActivity(intent);
    }


}
