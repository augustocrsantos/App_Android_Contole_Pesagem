package com.example.controla_peso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.entidade.EntidadeCadastroBovino;

import java.io.ByteArrayInputStream;

public class ImagemTelaCheiaActivit  extends AppCompatActivity {
    private ImageView imagem;
    private int id;
    private byte[] outImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagem_tela_cheia);

        imagem = (ImageView) findViewById(R.id.imageView_tela_cheia);
        Intent it = getIntent();
        id = it.getIntExtra("id",0);
        preencheCamposEdicao(id);
        /*
        byte[] outImage = it.getByteArrayExtra("imagem");
        if(outImage != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            imagem.setImageBitmap(imageBitmap);
            imagem.setRotation(90);
        }

         */

    }
    public void preencheCamposEdicao(int id){
        daoCadastroBovino dao = new daoCadastroBovino(this);
        if(id != 0){
           EntidadeCadastroBovino entidade =  dao.buscaCadastroId(id);
            outImage= entidade.getFoto();
            if(outImage != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                imagem.setImageBitmap(imageBitmap);
                imagem.setRotation(90);
            }

        }
    }
}
