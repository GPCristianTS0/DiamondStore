package com.Clover.prueba.data.controller;

import static com.Clover.prueba.utils.Constantes.CONST_ESTA_SEMANA;
import static com.Clover.prueba.utils.Constantes.CONST_ESTE_DIA;
import static com.Clover.prueba.utils.Constantes.CONST_ESTE_MES;
import static com.Clover.prueba.utils.Constantes.CONST_THIS_YEAR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.Clover.prueba.data.dao.FinancieroDAO;
import com.Clover.prueba.data.models.ReporteFinanciero;

import java.time.*;
import java.util.Date;

public class ControllerFinanciero {
    private final FinancieroDAO financieroDAO;

    public ControllerFinanciero(Context context) {
        financieroDAO = new FinancieroDAO(context);
    }
    public ReporteFinanciero getReporteFinanciero(String rango) {
        String fechaInicial = "";
        String fechaFin = "";

        //Calculo de fechas
        LocalDate fechaHoy = LocalDate.now();
        if (rango.equals(CONST_ESTA_SEMANA)) {
            fechaInicial = fechaHoy.minusDays(6).toString();
        }
        if (rango.equals(CONST_ESTE_MES)){
            fechaInicial = fechaHoy.minusMonths(1).toString();
        }
        if (rango.equals(CONST_THIS_YEAR)){
            fechaInicial = fechaHoy.minusYears(1).toString();
        }
        fechaFin = fechaHoy.toString();

        if (rango.equals(CONST_ESTE_DIA)) {
            fechaInicial = fechaHoy.toString();
            fechaFin = fechaHoy.plusDays(1).toString();
        }

        //Formacion del reporte
        ReporteFinanciero reporte = new ReporteFinanciero();
        reporte.setVentasTotales(financieroDAO.getVentasTotales(fechaInicial, fechaFin));
        reporte.setCostoMercancia(financieroDAO.getCostoMercancia(fechaInicial, fechaFin));
        reporte.setCantidadVentas(financieroDAO.getCantidadVentas(fechaInicial, fechaFin));
        reporte.setGanancias(reporte.getVentasTotales() - reporte.getCostoMercancia());
        reporte.setMargenGlobal((int) ((reporte.getGanancias() / reporte.getVentasTotales()) * 100));
        reporte.setTotalEfectivo(financieroDAO.getTotalPorMetodoPago("Efectivo", fechaInicial, fechaFin));
        reporte.setTotalTarjeta(financieroDAO.getTotalPorMetodoPago("Tarjeta", fechaInicial, fechaFin));
        @SuppressLint("DefaultLocale") double f  = Double.parseDouble(String.format("%.2f", reporte.getVentasTotales() / reporte.getCantidadVentas()));
        reporte.setTicketPromedio(f);

        String formatoAgrupacion;

        switch (rango) {
            case CONST_ESTE_DIA:
                formatoAgrupacion = "%H"; // Agrupar por Hora (08, 09, 10...)
                break;
            case CONST_THIS_YEAR:
                formatoAgrupacion = "%Y-%m"; // Agrupar por Mes (2026-01, 2026-02...)
                break;
            default:
                // Para Semana y Mes, agrupamos por Día
                formatoAgrupacion = "%Y-%m-%d";
                break;
        }

        reporte.setDatosGrafica(financieroDAO.getDatosGraficaDinamica(fechaInicial, fechaFin, formatoAgrupacion));
        return reporte;
    }
}
