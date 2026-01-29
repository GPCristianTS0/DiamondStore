package com.Clover.prueba.data.controller;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TRANSFERENCIA;

import android.content.Context;

import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.GastosDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IGastos;
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

    public CorteCajaController(Context context) {
        daoCorteCaja = new CorteCajaDAO(context);
        gastosDAO = new GastosDAO(context);
        ventasDAO = new VentasDAO(context);
    }

    public void iniciarTurno(double montoInicial) {
        CorteCaja corteCaja = new CorteCaja();
        corteCaja.setMonto_inicial(montoInicial);
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        corteCaja.setFecha_apertura(fechaHora);
        corteCaja.setEstado(ABIERTO_CONST);
        daoCorteCaja.iniciarCorte(corteCaja);
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
        corteCaja.setVentas_totales(daoCorteCaja.getVentasTotalesCorte(corteCaja.getFecha_apertura()));
        corteCaja.setVentas_efectivo(ventasDAO.getVentasMetodoPago(CONST_METODO_EFECTIVO, corteCaja.getId_corte()));
        corteCaja.setVentas_tarjeta(ventasDAO.getVentasMetodoPago(CONST_METODO_TARJETA, corteCaja.getId_corte()));
        corteCaja.setVentas_transferencia(ventasDAO.getVentasMetodoPago(CONST_METODO_TRANSFERENCIA, corteCaja.getId_corte()));
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
        corteCaja.setGastos_efectivo(gastosDAO.sumarTotalGastosByCorte(id_corte, "Efectivo"));
        corteCaja.setGastos_transferencia(gastosDAO.sumarTotalGastosByCorte(id_corte, "Transferencia"));
        corteCaja.setVentas_efectivo(ventasDAO.getVentasMetodoPago("Efectivo", id_corte));
        corteCaja.setVentas_tarjeta(ventasDAO.getVentasMetodoPago("Tarjeta", id_corte));
        corteCaja.setVentas_totales(corteCaja.getMonto_inicial() + corteCaja.getVentas_totales() + corteCaja.getAbonos_totales() - corteCaja.getGastos_totales());
        return corteCaja;
    }
}