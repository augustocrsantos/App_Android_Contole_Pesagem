package com.example.controla_peso.entidade;

import android.os.Parcel;
import android.os.Parcelable;

public class EntidadeCadastroBovino {

    private long id;
    private String cadCodigoBrinco;
    private String cadData;
    private String raca;
    private String observacao;
    private boolean vendido;
    private byte[] foto;
    private String caminhoFoto;

    public EntidadeCadastroBovino(){

    }
    public EntidadeCadastroBovino(long id, String cadCodigoBrinco, String raca, String observacao, boolean vendido,
                                  byte[] foto, String caminhoFoto){
        this.id = id;
        this.cadCodigoBrinco = cadCodigoBrinco;
        this.raca = raca;
        this.observacao = observacao;
        this.vendido = vendido;
        this.foto = foto;
        this.caminhoFoto = caminhoFoto;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCadCodigoBrinco() {
        return cadCodigoBrinco;
    }

    public void setCadCodigoBrinco(String cadCodigoBrinco) {
        this.cadCodigoBrinco = cadCodigoBrinco;
    }

    public String getCadData() {
        return cadData;
    }

    public void setCadData(String cadData) {
        this.cadData = cadData;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }


    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return " Codigo Brinco: " + cadCodigoBrinco+ " Data: " +
                cadData +"\n"+ " Raça: " + raca +"\n"+" Observação: " + observacao +
                ((isVendido())?" VENDIDO":"");
    }


}
