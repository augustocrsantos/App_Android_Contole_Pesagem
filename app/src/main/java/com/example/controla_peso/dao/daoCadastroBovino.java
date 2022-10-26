package com.example.controla_peso.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.example.controla_peso.entidade.EntidadeDados;
import com.example.controla_peso.funcoes.ConverteBoolean;

public class daoCadastroBovino {
    private SQLiteDatabase bd;
    private ConverteBoolean conveterBoolean;

    public daoCadastroBovino(Context context){
        CriaBancoDados auxBd = new CriaBancoDados(context);
        bd = auxBd.getWritableDatabase();

        conveterBoolean = new ConverteBoolean();
    }

    public void inserir(EntidadeCadastroBovino entidade) {
        ContentValues valores = new ContentValues();
        valores.put("cad_codigo_brico", entidade.getCadCodigoBrinco());
        valores.put("cad_data", entidade.getCadData());
        valores.put("cad_raca", entidade.getRaca());
        valores.put("cad_observacao", entidade.getObservacao());
        valores.put("foto",entidade.getFoto());
        valores.put("vendido",conveterBoolean.booleantoString(entidade.isVendido()));

        bd.insert("tblcadastrobovino", null, valores);
    }
    public void update(EntidadeCadastroBovino entidade){
        String table = "tblcadastrobovino";
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(entidade.getId()) };

        ContentValues valores = new ContentValues();
        valores.put("cad_codigo_brico", entidade.getCadCodigoBrinco());
        valores.put("cad_data", entidade.getCadData());
        valores.put("cad_raca", entidade.getRaca());
        valores.put("cad_observacao", entidade.getObservacao());
        valores.put("foto",entidade.getFoto());
        valores.put("vendido",conveterBoolean.booleantoString(entidade.isVendido()));

        bd.update(table,valores,whereClause,whereArgs);
    }
    public void excluirDadosPesagem(EntidadeCadastroBovino entidade){
        String table = "tblcadastrobovino";
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(entidade.getId()) };
        bd.delete(table,whereClause, whereArgs);
    }

    /**
     * Retorna uma lista de bovinos
     * @param filtro
     * @return
     */
    public List<EntidadeCadastroBovino> buscarCadastro(String filtro, boolean vendido){
        List<EntidadeCadastroBovino> list = new ArrayList<>();
        String bovinoVendido = "";
        String brinco = "";
        String sql = "";

        if (filtro != null) {
            brinco = " where cad_codigo_brico = " + " '" + filtro + "' ";
        }else {

            if (vendido) {
                bovinoVendido = " where vendido = 'true' ";
            } else {
                bovinoVendido = " where vendido is null or vendido = 'false'";
            }
        }

            sql = "select  id, cad_codigo_brico, cad_data, cad_raca, cad_observacao, foto, vendido  from tblcadastrobovino "
                    +bovinoVendido + brinco ;

            Cursor cursor = bd.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                EntidadeCadastroBovino u = new EntidadeCadastroBovino();
                u.setId(cursor.getLong(0));
                u.setCadCodigoBrinco(cursor.getString(1));
                u.setCadData(cursor.getString(2));
                u.setRaca(cursor.getString(3));
                u.setObservacao(cursor.getString(4));
                u.setFoto(cursor.getBlob(5));
                u.setVendido(conveterBoolean.stringtoBolean(cursor.getString(6)));
                list.add(u);

            }while(cursor.moveToNext());
        }

        return(list);
    }


    public EntidadeCadastroBovino buscaCadastroId (int i){
        String[] colunas = new String[]{"id", "cad_codigo_brico", "cad_data", "cad_raca", "cad_observacao","foto", "vendido"};
        String selection = null;
        String[] selectionArgs = null;
        selection = " id =? ";
        selectionArgs = new String[]{String.valueOf(i)};

        Cursor cursor = bd.query("tblcadastrobovino", colunas, selection, selectionArgs, null, null, "id ASC");
        cursor.moveToFirst();
        EntidadeCadastroBovino u = new EntidadeCadastroBovino();
        u.setId(cursor.getLong(0));
        u.setCadCodigoBrinco(cursor.getString(1));
        u.setCadData(cursor.getString(2));
        u.setRaca(cursor.getString(3));
        u.setObservacao(cursor.getString(4));
        u.setFoto(cursor.getBlob(5));
        String vendido = cursor.getString(6);
        u.setVendido(conveterBoolean.stringtoBolean(vendido));


        return u;
    }

    /*
    Verifica se existe pesagem cadastrada para o boi pesquisado
     */
    public  List<Integer> VerificaPesoCadastrado(String filtro){
        List<Integer> list = new ArrayList<Integer>();
        String[] colunas = new String[]{"id"};

        String selection = null;
        String[] selectionArgs = null;

        if(filtro != null) {
            selection = "codigo_brico =? ";
            selectionArgs = new String[]{filtro};


            Cursor cursor = bd.query("tbldados", colunas, selection, selectionArgs, null, null, "id ASC");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    list.add(Integer.parseInt(cursor.getString(0)));

                } while (cursor.moveToNext());
            }

            return (list);
        }else {
            return null;
        }


    }

    /**
     * Verifica se o codigo do brinco já é cadastrado
     * @param codigoBrinco
     * @return
     */
    public boolean VerificaExiste(String codigoBrinco) {
        String[] colunas = new String[]{"id"};

        String selection = null;
        String[] selectionArgs = null;

        if (codigoBrinco != null) {
            selection = "cad_codigo_brico =? ";
            selectionArgs = new String[]{codigoBrinco};

        }
            Cursor cursor = bd.query("tblcadastrobovino", colunas, selection, selectionArgs, null, null, "id ASC");

            if (cursor.getCount() > 0) {
                return false;
            } else {
                return true;
            }

    }
    public int contCadastros(){

        Cursor cursor = bd.rawQuery("select id from tblcadastrobovino where vendido is null or vendido = 'false'", null);

        if (cursor!=null ) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Retorna uma lista de id e codigo brinco bovinos
     * @param filtro
     * @return
     */
    public List<EntidadeCadastroBovino> listaId(String filtro){
        List<EntidadeCadastroBovino> list = new ArrayList<>();
        String[] colunas = new String[]{"id", "cad_codigo_brico"};


        String selection = null;
        String[] selectionArgs = null;

        if(filtro != null){
            selection = "cad_codigo_brico =? ";
            selectionArgs = new String[]{filtro};
        }

        Cursor cursor = bd.query("tblcadastrobovino", colunas, selection, selectionArgs, null, null, "id ASC");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                EntidadeCadastroBovino u = new EntidadeCadastroBovino();
                u.setId(cursor.getLong(0));
                u.setCadCodigoBrinco(cursor.getString(1));
                list.add(u);

            }while(cursor.moveToNext());
        }

        return(list);
    }
}
