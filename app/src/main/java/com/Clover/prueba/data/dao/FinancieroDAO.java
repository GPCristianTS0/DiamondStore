package com.Clover.prueba.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IFinanciero;
import com.Clover.prueba.data.database.CloverBD;

import java.util.LinkedHashMap;

public class FinancieroDAO implements IFinanciero {
    private SQLiteDatabase db;
    private Context context;

    public FinancieroDAO(Context context) {
        this.context = context;
        db = CloverBD.getInstance(context).getWritableDatabase();
    }


    @Override
    public double getVentasTotales(String fechaInicial, String fechaFin) {
        String sql = "SELECT IFNULL(SUM(monto), 0) FROM ventas WHERE fecha_Hora BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaInicial, fechaFin})) {
            if (cursor.moveToFirst()){
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public double getCostoMercancia(String fechaInicial, String fechaFin) {
        String sql = "SELECT IFNULL(SUM(d.precio_neto_historial * d.cantidad), 0) FROM detalles_venta d INNER JOIN ventas v ON d.id_venta = v.id_venta WHERE v.fecha_Hora BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaInicial, fechaFin})) {
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
        String sql = "SELECT COUNT(*) FROM ventas WHERE fecha_Hora BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaInicial, fechaFin})) {
            if (cursor.moveToFirst()){
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCantidadVentas: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public double getTotalPorMetodoPago(String metodoPago, String fechaInicial, String fechaFin) {
        String sql = "SELECT IFNULL(SUM(monto), 0) FROM ventas WHERE tipo_pago = ? AND fecha_Hora BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{metodoPago, fechaInicial, fechaFin})) {
            if (cursor.moveToFirst()){
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getTotalPorMetodoPago: " + e.getMessage());
        }
        return 0;
    }
    public LinkedHashMap<String, Double> getDatosGraficaDinamica(String fechaInicio, String fechaFin, String formatoSQLite) {
        LinkedHashMap<String, Double> datos = new LinkedHashMap<>();
        String sql = "SELECT strftime(?, fecha_Hora) as periodo, IFNULL(SUM(monto), 0) " +
                "FROM ventas " +
                "WHERE fecha_Hora BETWEEN ? AND ? " +
                "GROUP BY periodo " +
                "ORDER BY periodo ASC";
        try(Cursor cursor = db.rawQuery(sql, new String[]{formatoSQLite, fechaInicio, fechaFin})) {
            if (cursor.moveToFirst()) {
                do {
                    String etiqueta = cursor.getString(0);
                    double total = cursor.getDouble(1);
                    datos.put(etiqueta, total);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDatosGraficaDinamica: " + e.getMessage());
        }
        return datos;
    }
}
