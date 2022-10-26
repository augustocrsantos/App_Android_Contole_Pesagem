package com.example.controla_peso.entidade;

import java.util.Date;

public class EntidadeDados {

    private long Id;
    private String codigoBrinco;
    private double peso;
    private double tara;
    private String data;
    private double ganhoPeso;
    private boolean vendido;

    public EntidadeDados(){

    }
    public EntidadeDados(long Id, String codigoBrinco, double peso,double tara, String data, double ganhoPeso, boolean vendido) {
        this.Id = Id;
        this.codigoBrinco = codigoBrinco;
        this.peso = peso;
        this.tara = tara;
        this.data = data;
        this.ganhoPeso = ganhoPeso;
        this.vendido = vendido;

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }


    public String getCodigoBrinco() {
        return codigoBrinco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCodigoBrinco(String codigoBrinco) {
        this.codigoBrinco = codigoBrinco;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTara() {
        return tara;
    }

    public void setTara(double tara) {
        this.tara = tara;
    }

    public double getGanhoPeso() {
        return ganhoPeso;
    }

    public void setGanhoPeso(double ganhoPeso) {
        this.ganhoPeso = ganhoPeso;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    @Override
    public String toString() {
        return " Codigo: " + codigoBrinco+ " Data: " +
                data +"\n"+ " Peso: " + Double.toString(peso) +"\n"+" Ganho de peso: " + Double.toString(ganhoPeso)+
                ((isVendido())?" VENDIDO":"");
    }
}
