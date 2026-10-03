package com.Clover.prueba.domain.corteCaja;

public enum EstadoCaja {
    ABIERTO("Abierto"),
    CERRADO("Cerrado");

    private final String valor;
    EstadoCaja(String valor) {
        this.valor = valor;
    }
    public String getValor(){
        return valor;
    }
}
