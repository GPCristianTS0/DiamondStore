package com.Clover.prueba.data.controller;

import android.content.Context;

import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.models.CorteCaja;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CorteCajaController {
    private final String ABIERTO_CONST = "Abierto";
    private final String CERRADO_CONST = "Cerrado";
    private final ICorteCaja daoCorteCaja ;
    public CorteCajaController(Context context) {
        daoCorteCaja = new CorteCajaDAO(context);
    }

    public boolean iniciarTurno(double montoInicial) {
        CorteCaja corteCaja = new CorteCaja();
        corteCaja.setMonto_inicial(montoInicial);
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        corteCaja.setFecha_apertura(fechaHora);
        corteCaja.setEstado(ABIERTO_CONST);
        return daoCorteCaja.iniciarCorte(corteCaja);
    }
    public double calcularDineroEsperado(double abonos, double gastos){
        CorteCaja corteCaja = daoCorteCaja.getCorteActual();
        if (corteCaja == null) return 0;
        double total = daoCorteCaja.getVentasTotalesCorte(corteCaja.getFecha_apertura());
        return corteCaja.getMonto_inicial() + abonos + total - gastos;
    }
    public boolean cerrarTurno(double dineroEnCaja) {
        CorteCaja corteCaja = daoCorteCaja.getCorteActual();
        if (corteCaja == null) return false;
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        double total = daoCorteCaja.getVentasTotalesCorte(corteCaja.getFecha_apertura());
        corteCaja.setFecha_cierre(fechaHora);
        corteCaja.setVentas_totales(total);
        //corteCaja.setAbonos_totales(abonos);
        //corteCaja.setGastos_totales(gastos);
        corteCaja.setDinero_en_caja(dineroEnCaja);
        //double esperado = corteCaja.getMonto_inicial() + abonos + total - gastos;
        //corteCaja.setDiferencia(dineroEnCaja - esperado);
        corteCaja.setEstado(CERRADO_CONST);
        return daoCorteCaja.cerrarCorte(corteCaja);
    }

    public boolean isCorteAbierto() {
        return daoCorteCaja.getEstadoCorte();
    }

}
