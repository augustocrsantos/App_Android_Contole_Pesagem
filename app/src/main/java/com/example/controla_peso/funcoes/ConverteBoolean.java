package com.example.controla_peso.funcoes;

import java.util.Objects;

public class ConverteBoolean {

    public String booleantoString(boolean campo){
    if(campo == false){
        return "false";
    }else{
        return "true";
    }

    }
    public boolean stringtoBolean(String campo){
        if(campo == null || campo.equals("false")) {
         return false;
        }else{
           return true;
        }
    }



}
