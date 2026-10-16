package com.Clover.prueba.domain.reparaciones.DTO;

public class ResultadoContadores {
    public final int recibido;
    public final int enReparacion;
    public final int listo;

    public ResultadoContadores(int recibido, int enReparacion, int listo) {
        this.recibido = recibido;
        this.enReparacion = enReparacion;
        this.listo = listo;
    }
}
