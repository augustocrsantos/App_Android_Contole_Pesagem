package com.example.controla_peso.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CriaBancoDados extends SQLiteOpenHelper {
    private static final String NOME_BD = "bancopesagem";
    private static final int VERSAO_BD = 12;

    public CriaBancoDados(Context ctx){
        super(ctx, NOME_BD, null, VERSAO_BD);
    }
    @Override
    public void onCreate(SQLiteDatabase bd) {
        try{
            bd.execSQL("create table tbldados(id integer primary key autoincrement, codigo_brico text not null," +
                    " data text, peso real not null, tara real, ganho_peso real);");

            CriaTabelaCadastroBovino(bd);

            Log.e(null,"Tabela criada com sucesso");
        }catch (Exception e){
            Log.e(null,"Erro ao criar banco"+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
      //  bd.execSQL("drop table tbldados;");
      //  bd.execSQL("drop table tblcadastrobovino;");
      //  onCreate(bd);
    }
    public void CriaTabelaCadastroBovino(SQLiteDatabase bd){
        bd.execSQL("create table tblcadastrobovino(id integer primary key autoincrement, cad_codigo_brico text not null," +
                " cad_data text, cad_raca text, cad_observacao text, caminho_foto text, vendido text, foto blob);");

    }

}


