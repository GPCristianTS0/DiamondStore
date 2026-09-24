package com.Clover.prueba.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IFinanciero;
import com.Clover.prueba.data.database.CloverBD;

import java.util.LinkedHashMap;

public class FinancieroDAO implements IFinanciero {
    private final SQLiteDatabase db;

    public FinancieroDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    private String formatFechaInicio(String fecha) {
        if (fecha == null || fecha.isEmpty()) return "";
        return fecha.contains(" ") ? fecha : fecha + " 00:00:00";
    }

    private String formatFechaFin(String fecha) {
        if (fecha == null || fecha.isEmpty()) return "";
        return fecha.contains(" ") ? fecha : fecha + " 23:59:59";
    }

    @Override
    public double getVentasTotales(String fechaInicial, String fechaFin) {
        String fInit = formatFechaInicio(fechaInicial);
        String fEnd = formatFechaFin(fechaFin);

        String sqlVentas = "SELECT IFNULL(SUM(monto), 0) FROM ventas WHERE fecha_Hora BETWEEN ? AND ?";
        String sqlRep = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE estado = 'ENTREGADO' AND fecha_entrega BETWEEN ? AND ?";
        double totalVentas = 0;
        double totalRep = 0;
        try (Cursor cursor = db.rawQuery(sqlVentas, new String[]{fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalVentas = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        try (Cursor cursor = db.rawQuery(sqlRep, new String[]{fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalRep = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales Reparaciones: " + e.getMessage());
        }
        return totalVentas + totalRep;
    }

    @Override
    public double getCostoMercancia(String fechaInicial, String fechaFin) {
        String fInit = formatFechaInicio(fechaInicial);
        String fEnd = formatFechaFin(fechaFin);

        String sql = "SELECT IFNULL(SUM(d.precio_neto_historial * d.cantidad), 0) FROM detalles_venta d INNER JOIN ventas v ON d.id_venta = v.id_venta WHERE v.fecha_Hora BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fInit, fEnd})) {
            if (cursor.moveToFirst()){
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCostoMercancia: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getCantidadVentas(String fechaInicial, String fechaFin) {
        String fInit = formatFechaInicio(fechaInicial);
        String fEnd = formatFechaFin(fechaFin);

        String sqlVentas = "SELECT COUNT(*) FROM ventas WHERE fecha_Hora BETWEEN ? AND ?";
        String sqlRep = "SELECT COUNT(*) FROM reparaciones WHERE estado = 'ENTREGADO' AND fecha_entrega BETWEEN ? AND ?";
        int totalVentas = 0;
        int totalRep = 0;
        try (Cursor cursor = db.rawQuery(sqlVentas, new String[]{fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalVentas = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCantidadVentas: " + e.getMessage());
        }
        try (Cursor cursor = db.rawQuery(sqlRep, new String[]{fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalRep = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCantidadVentas Reparaciones: " + e.getMessage());
        }
        return totalVentas + totalRep;
    }

    @Override
    public double getTotalPorMetodoPago(String metodoPago, String fechaInicial, String fechaFin) {
        String fInit = formatFechaInicio(fechaInicial);
        String fEnd = formatFechaFin(fechaFin);

        String sqlVentas = "SELECT IFNULL(SUM(monto), 0) FROM ventas WHERE tipo_pago = ? AND fecha_Hora BETWEEN ? AND ?";
        String sqlRep = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE tipo_pago = ? AND estado = 'ENTREGADO' AND fecha_entrega BETWEEN ? AND ?";
        double totalVentas = 0;
        double totalRep = 0;
        try (Cursor cursor = db.rawQuery(sqlVentas, new String[]{metodoPago, fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalVentas = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getTotalPorMetodoPago: " + e.getMessage());
        }
        try (Cursor cursor = db.rawQuery(sqlRep, new String[]{metodoPago, fInit, fEnd})) {
            if (cursor.moveToFirst()){
                totalRep = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getTotalPorMetodoPago Reparaciones: " + e.getMessage());
        }
        return totalVentas + totalRep;
    }

    public LinkedHashMap<String, Double> getDatosGraficaDinamica(String fechaInicio, String fechaFin, String formatoSQLite) {
        String fInit = formatFechaInicio(fechaInicio);
        String fEnd = formatFechaFin(fechaFin);

        LinkedHashMap<String, Double> datos = new LinkedHashMap<>();
        String sql = "SELECT periodo, SUM(total) FROM (" +
                "SELECT strftime(?, fecha_Hora) as periodo, monto as total FROM ventas WHERE fecha_Hora BETWEEN ? AND ? " +
                "UNION ALL " +
                "SELECT strftime(?, fecha_entrega) as periodo, monto_total as total FROM reparaciones WHERE estado = 'ENTREGADO' AND fecha_entrega BETWEEN ? AND ?" +
                ") GROUP BY periodo ORDER BY periodo ASC";

        try (Cursor cursor = db.rawQuery(sql, new String[]{formatoSQLite, fInit, fEnd, formatoSQLite, fInit, fEnd})) {
            if (cursor.moveToFirst()) {
                do {
                    String etiqueta = cursor.getString(0);
                    double total = cursor.getDouble(1);
                    if (etiqueta != null) {
                        datos.put(etiqueta, total);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDatosGraficaDinamica: " + e.getMessage());
        }
        return datos;
    }
}
