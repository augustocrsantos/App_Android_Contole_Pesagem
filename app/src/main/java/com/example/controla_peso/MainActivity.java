package com.example.controla_peso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button BtEntrar;
    private ImageView imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // CriaBancoDados banco = new CriaBancoDados();
        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.canva_boi, null);

        BtEntrar = (Button) findViewById(R.id.button_entrar);

        BtEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChamaCadastro();
            }
        });
    }

    private void ChamaCadastro(){
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }
}