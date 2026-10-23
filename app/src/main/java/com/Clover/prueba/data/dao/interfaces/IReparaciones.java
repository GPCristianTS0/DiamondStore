package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;

import java.util.ArrayList;

public interface IReparaciones {
    boolean addReparacion(Reparaciones reparacion);
    boolean updateReparacion(Reparaciones reparacion);
    boolean updateEstateReparacion(String folio, EstadoReparcion estado) throws Exception;
    public boolean updateListoReparacion(String idReparacion, String diagnostico, int costoRefacciones) throws Exception;
    boolean deleteReparacion(int idReparacion);
    Reparaciones getReparacionById(int idReparacion);
    ArrayList<Reparaciones> getReparacionesByEstado(String estado);
    ArrayList<Reparaciones> getReparacionesByTipoServicio(String tipoServicio);
    ArrayList<Reparaciones> getReparacionesByCorte(int idCorte);
    ArrayList<Reparaciones> getAllReparaciones();
    double getIngresosReparacionesByCorte(int idCorte, String tipoPago);
    double getIngresosTotalesReparaciones(String fechaInicio, String fechaFin);
}
