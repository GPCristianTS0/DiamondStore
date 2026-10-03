package com.Clover.prueba.domain.corteCaja;

import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_TRANSFERENCIA;

import android.content.Context;

import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.GastosDAO;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IGastos;
import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.CorteCaja;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CorteCajaController {
    private final String ABIERTO_CONST = "Abierto";
    private final String CERRADO_CONST = "Cerrado";
    private final ICorteCaja daoCorteCaja ;
    private final IGastos gastosDAO;
    private CorteCaja corteCaja;
    private final IVentas ventasDAO;
    private final IReparaciones reparacionesDAO;

    public CorteCajaController(Context context) {
        daoCorteCaja = new CorteCajaDAO(context);
        gastosDAO = new GastosDAO(context);
        ventasDAO = new VentasDAO(context);
        reparacionesDAO = new ReparacionesDAO(context);
    }

    public boolean cerrarTurno(double dineroEnCaja) {
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        corteCaja.setFecha_cierre(fechaHora);
        corteCaja.setDinero_en_caja(dineroEnCaja);
        corteCaja.setEstado(CERRADO_CONST);
        return daoCorteCaja.cerrarCorte(corteCaja);
    }
    public CorteCaja getCorteActual() {
        corteCaja = daoCorteCaja.getCorteActual();
        if (corteCaja == null) return null;

        double ingRepEfectivo = reparacionesDAO.getIngresosReparacionesByCorte(corteCaja.getId_corte(), CONST_METODO_EFECTIVO);
        double ingRepTarjeta = reparacionesDAO.getIngresosReparacionesByCorte(corteCaja.getId_corte(), CONST_METODO_TARJETA);
        double ingRepTransferencia = reparacionesDAO.getIngresosReparacionesByCorte(corteCaja.getId_corte(), CONST_METODO_TRANSFERENCIA);

        corteCaja.setVentas_efectivo(ventasDAO.getVentasMetodoPago(CONST_METODO_EFECTIVO, corteCaja.getId_corte()) + ingRepEfectivo);
        corteCaja.setVentas_tarjeta(ventasDAO.getVentasMetodoPago(CONST_METODO_TARJETA, corteCaja.getId_corte()) + ingRepTarjeta);
        corteCaja.setVentas_transferencia(ventasDAO.getVentasMetodoPago(CONST_METODO_TRANSFERENCIA, corteCaja.getId_corte()) + ingRepTransferencia);
        corteCaja.setVentas_totales(daoCorteCaja.getVentasTotalesCorte(corteCaja.getFecha_apertura()) + ingRepEfectivo + ingRepTarjeta + ingRepTransferencia);

        double d = gastosDAO.sumarTotalGastosByCorte(corteCaja.getId_corte(), CONST_METODO_EFECTIVO);
        corteCaja.setGastos_totales(d);
        double esperado = corteCaja.getMonto_inicial() + corteCaja.getVentas_efectivo() + corteCaja.getAbonos_totales() - d;
        corteCaja.setDinero_en_caja(esperado);
        return corteCaja;
    }
    public double getVentasTotalesCorte(int id, String metodoPago) {
        return gastosDAO.sumarTotalGastosByCorte(id, metodoPago);
    }
    public double calcularDiferencia(double dineroEnCaja) {
        return dineroEnCaja - corteCaja.getDinero_en_caja();
    }
    public boolean isCorteAbierto() {
        return daoCorteCaja.getEstadoCorte();
    }
    public void setDiferencia(double diferencia) {
        corteCaja.setDiferencia(diferencia);
    }
    public ArrayList<CorteCaja> getCortes(){
        return daoCorteCaja.getCortes(CERRADO_CONST);
    }

    public CorteCaja getCorteCaja(int id_corte){
        CorteCaja corteCaja = daoCorteCaja.getCorte(id_corte);
        if (corteCaja == null) return null;

        double ingRepEfectivo = reparacionesDAO.getIngresosReparacionesByCorte(id_corte, CONST_METODO_EFECTIVO);
        double ingRepTarjeta = reparacionesDAO.getIngresosReparacionesByCorte(id_corte, CONST_METODO_TARJETA);
        double ingRepTransferencia = reparacionesDAO.getIngresosReparacionesByCorte(id_corte, CONST_METODO_TRANSFERENCIA);

        corteCaja.setGastos_efectivo(gastosDAO.sumarTotalGastosByCorte(id_corte, CONST_METODO_EFECTIVO));
        corteCaja.setGastos_transferencia(gastosDAO.sumarTotalGastosByCorte(id_corte, CONST_METODO_TRANSFERENCIA));
        corteCaja.setVentas_efectivo(ventasDAO.getVentasMetodoPago(CONST_METODO_EFECTIVO, id_corte) + ingRepEfectivo);
        corteCaja.setVentas_tarjeta(ventasDAO.getVentasMetodoPago(CONST_METODO_TARJETA, id_corte) + ingRepTarjeta);
        corteCaja.setVentas_transferencia(ventasDAO.getVentasMetodoPago(CONST_METODO_TRANSFERENCIA, id_corte) + ingRepTransferencia);
        corteCaja.setVentas_totales(corteCaja.getMonto_inicial() + corteCaja.getVentas_efectivo() + corteCaja.getVentas_tarjeta() + corteCaja.getVentas_transferencia() + corteCaja.getAbonos_totales() - corteCaja.getGastos_totales());
        return corteCaja;
    }
}