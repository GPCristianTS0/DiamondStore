package com.Clover.prueba.domain.reparaciones.constantes;

public enum EstadoReparcion {
    EN_REPARACION("EN REPARACION"),
    RECIBIDO("RECIBIDO"),
    ENTREGADO("ENTREGADO"),
    LISTO("LISTO"),
    CANCELADO("CANCELADO");

    private final String estado;

    EstadoReparcion(String estado) {
        this.estado = estado;
    }

    public String getEstado(){
        return estado;
    }
    public static String[] getArray(){
        String[] estados = new String[EstadoReparcion.values().length];
        EstadoReparcion[] estadosEnum = EstadoReparcion.values();
        for (int i = 0; i <estadosEnum.length; i++) {
            estados[i] = estadosEnum[i].estado;
        }
        return estados;
    }
    public static EstadoReparcion fromString(String texto){

        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado no valido");
        }

        for (EstadoReparcion estado : EstadoReparcion.values()) {
            if (estado.estado.equalsIgnoreCase(texto.trim())) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + texto);
    }
}
