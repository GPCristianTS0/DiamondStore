package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Reparaciones;
import java.util.ArrayList;

public interface IReparaciones {
    boolean addReparacion(Reparaciones reparacion);
    boolean updateReparacion(Reparaciones reparacion);
    boolean updateEstadoReparacion(int idReparacion, String estado, String fechaEntrega, int idCorte);
    boolean deleteReparacion(int idReparacion);
    Reparaciones getReparacionById(int idReparacion);
    ArrayList<Reparaciones> getReparacionesByEstado(String estado);
    ArrayList<Reparaciones> getReparacionesByTipoServicio(String tipoServicio);
    ArrayList<Reparaciones> getReparacionesByCorte(int idCorte);
    ArrayList<Reparaciones> getAllReparaciones();
    double getIngresosReparacionesByCorte(int idCorte, String tipoPago);
    double getIngresosTotalesReparaciones(String fechaInicio, String fechaFin);
}
