package com.example.controla_peso;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.dao.daoDados;
import com.example.controla_peso.funcoes.MascaraData;

public class TotalizadorActivit extends AppCompatActivity {
    private Button botaoPesqusisar;
    private TextView totalPeso;
    private TextView mediaPeso;
    private EditText dataPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.totalizador);

        totalPeso = (TextView)findViewById(R.id.textViewTotalPesoData);
        mediaPeso = (TextView)findViewById(R.id.textViewMediaPesoData);
        dataPesquisa = (EditText)findViewById(R.id.editTextdataPesquisa);
        dataPesquisa.addTextChangedListener(MascaraData.Mask.insert("##/##/####", dataPesquisa));

        botaoPesqusisar = (Button) findViewById(R.id.buttonPesquisaData);
        botaoPesqusisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if(id == android.R.id.home){
            ChamaPesquisaPesagens();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ChamaPesquisaPesagens() {
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }


    /*
      Calcular peso total do rebanho
      */
        private void total(){
            try {
                daoDados dao = new daoDados(this);
                if (!dataPesquisa.getText().equals("") && dataPesquisa.getText() != null) {
                    Double PesoTotal = dao.verificaTotal(dataPesquisa.getText().toString());
                    if(PesoTotal > Double.valueOf(0)){
                        totalPeso.setText("Total do peso do rebanho: " + Math.round(PesoTotal) + " (Kg)");
                        Pesomedio(PesoTotal);
                    }else{
                        Toast.makeText(this, "Não a pesagem cadastrada na data informada", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "O campo Data deve ser preencuido, Verifique!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(this, "Erro ao calcular peso total do rebanho", Toast.LENGTH_SHORT).show();
            }

    }
    private void Pesomedio(double total){
            try {
                daoCadastroBovino cadastro = new daoCadastroBovino(this);
                int quantidade = cadastro.contCadastros();
                double quantidadeConvet = Double.valueOf(quantidade);
                if (total > 0) {
                    double media = total / quantidadeConvet;
                    mediaPeso.setText("Peso médio do rebanho: " + Math.round(media)+" (Kg)");
                }
            }catch (Exception e){
                Toast.makeText(this, "Erro ao calcular peso médio do rebanho", Toast.LENGTH_SHORT).show();
            }

    }
}
