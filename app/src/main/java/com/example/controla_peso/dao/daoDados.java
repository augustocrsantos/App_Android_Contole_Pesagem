package com.example.controla_peso.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.example.controla_peso.entidade.EntidadeDados;
import com.example.controla_peso.funcoes.ConverteBoolean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class daoDados {
    private SQLiteDatabase bd;
    private ConverteBoolean conveterBoolean;

    public daoDados(Context context){
        CriaBancoDados auxBd = new CriaBancoDados(context);
        bd = auxBd.getWritableDatabase();
        conveterBoolean = new ConverteBoolean();
    }

    public void inserir(EntidadeDados entidade) {
        ContentValues valores = new ContentValues();
        valores.put("codigo_brico", entidade.getCodigoBrinco());
        valores.put("peso", entidade.getPeso());
        valores.put("data", entidade.getData());
        valores.put("tara", entidade.getTara());
        valores.put("ganho_peso", entidade.getGanhoPeso());

        bd.insert("tbldados", null, valores);
    }
    public void excluirDadosPesagem(EntidadeDados entidade){
        String table = "tbldados";
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(entidade.getId()) };
        bd.delete(table,whereClause, whereArgs);
    }
    public void updateDados(EntidadeDados entidade){
        ContentValues valores = new ContentValues();
        valores.put("codigo_brico", entidade.getCodigoBrinco());
        valores.put("peso", entidade.getPeso());
        valores.put("data", entidade.getData());
        valores.put("tara", entidade.getTara());
        valores.put("ganho_peso", entidade.getGanhoPeso());

        String table = "tbldados";
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(entidade.getId()) };

        bd.update(table,valores,whereClause,whereArgs);
    }

    public List<EntidadeDados> buscar(String filtro){
        List<EntidadeDados> list = new ArrayList<EntidadeDados>();
        String[] colunas = new String[]{"id", "codigo_brico", "data", "peso", "ganho_peso"};

        String selection = null;
        String[] selectionArgs = null;

        if(filtro != null){
             selection = "codigo_brico =? ";
             selectionArgs = new String[]{filtro};
        }


        Cursor cursor = bd.query("tbldados", colunas, selection, selectionArgs, null, null, "data ASC");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                EntidadeDados u = new EntidadeDados();
                u.setId(cursor.getLong(0));
                u.setCodigoBrinco(cursor.getString(1));
                u.setData(cursor.getString(2));
                u.setPeso(Double.parseDouble(cursor.getString(3)));
                u.setGanhoPeso(Double.parseDouble(cursor.getString(4)));
                list.add(u);

            }while(cursor.moveToNext());
        }

        return(list);
    }

    public List<EntidadeDados> Pesquisar(String Codigo_brico, boolean vendido){
        List<EntidadeDados> list = new ArrayList<EntidadeDados>();
        String bovinoVendido = "";
        String brinco = "";
        String sql = "";
        try {
            if (Codigo_brico != null) {
                brinco = " where codigo_brico = " + " '" + Codigo_brico + "' ";
            } else {
                if (vendido) {
                    bovinoVendido = " where tblcadastrobovino.vendido = 'true' ";
                } else {
                    bovinoVendido = " where tblcadastrobovino.vendido is null or tblcadastrobovino.vendido = 'false'";
                }
            }

            sql = "select  tbldados.id, codigo_brico, data, peso, ganho_peso, tblcadastrobovino.vendido from tbldados inner join tblcadastrobovino on " +
                    "tblcadastrobovino.cad_codigo_brico = tbldados.codigo_brico  "+bovinoVendido + brinco ;


            Cursor cursor = bd.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    EntidadeDados u = new EntidadeDados();
                    u.setId(cursor.getLong(0));
                    u.setCodigoBrinco(cursor.getString(1));
                    u.setData(cursor.getString(2));
                    u.setPeso(Double.parseDouble(cursor.getString(3)));
                    u.setGanhoPeso(Double.parseDouble(cursor.getString(4)));
                    u.setVendido(conveterBoolean.stringtoBolean(cursor.getString(5)));
                    //list.stream().filter(entidadeDados -> vendido != true);
                    list.add(u);
                } while (cursor.moveToNext());
            }
            return (list);

        }catch (Exception e){
            return null;
        }

    }

    public  List<Double> buscaultimoPeso(String filtro){
        List<Double> list = new ArrayList<Double>();
        String[] colunas = new String[]{"peso"};

        String selection = null;
        String[] selectionArgs = null;

        if(filtro != null) {
            selection = "codigo_brico =? ";
            selectionArgs = new String[]{filtro};


            Cursor cursor = bd.query("tbldados", colunas, selection, selectionArgs, null, null, "id ASC");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    list.add(Double.parseDouble(cursor.getString(0)));

                } while (cursor.moveToNext());
            }

            return (list);
        }else {
            return null;
        }

    }
      public Double verificaTotal(String data){
              Double total = Double.valueOf(0);
              String concatData = "'"+data+"'";
              Cursor cursor = bd.rawQuery("select sum(peso) as total from tbldados where data ="+concatData, null);
              if (cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  total = cursor.getDouble(0);
                  return total;
              } else {
                  return Double.valueOf(0);
              }

      }

    public EntidadeDados buscaPeso (String codigo){
        if(codigo != null) {

            String[] colunas = new String[]{"peso"};
            String selection = "codigo_brico =? ";
            String[] selectionArgs = new String[]{codigo};

            Cursor cursor = bd.query("tbldados", colunas, selection, selectionArgs, null, null, null);
            if(cursor.getCount()>1){
                cursor.moveToLast();
            }else{
                cursor.moveToFirst();
            }

            EntidadeDados u = new EntidadeDados();
            if(cursor.getCount() !=0 ){
                u.setPeso(cursor.getDouble(0));
                return u;
            }
           return null;
        }else{
            return null;
        }

    }
}
