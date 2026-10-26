package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.CorteCaja;

import java.util.ArrayList;

public class CorteCajaDAO implements ICorteCaja {
    private SQLiteDatabase db;

    public CorteCajaDAO(Context context){
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public boolean iniciarCorte(CorteCaja corteCaja) {
        try {
            ContentValues values = new ContentValues();
            values.put("fecha_apertura", corteCaja.getFecha_apertura());
            values.put("monto_inicial", corteCaja.getMonto_inicial());
            values.put("estado", corteCaja.getEstado());
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);
            values.put("eliminado", 0);
            long filas = db.insert("cortes_cajas", null, values);
            return filas != -1;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en iniciarCorte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean cerrarCorte(CorteCaja corteCaja) {
        try {
            ContentValues values = new ContentValues();
            values.put("fecha_cierre", corteCaja.getFecha_cierre());
            values.put("ventas_totales", corteCaja.getVentas_totales());
            values.put("ventas_efectivo", corteCaja.getVentas_efectivo());
            values.put("ventas_tarjeta", corteCaja.getVentas_tarjeta());
            values.put("abonos_totales", corteCaja.getAbonos_totales());
            values.put("gastos_totales", corteCaja.getGastos_totales());
            values.put("gastos_efectivo", corteCaja.getGastos_efectivo());
            values.put("gastos_transferencia", corteCaja.getGastos_transferencia());
            values.put("dinero_en_caja", corteCaja.getDinero_en_caja());
            values.put("diferencia", corteCaja.getDiferencia());
            values.put("estado", corteCaja.getEstado());
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);
            int filas = db.update("cortes_cajas", values, "id_corte = ?", new String[]{String.valueOf(corteCaja.getId_corte())});
            return filas != 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en cerrarCorte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public double getVentasTotalesCorte(String fechaHora) {
        String sqlVentas = "SELECT IFNULL(SUM(monto), 0) FROM ventas WHERE fecha_Hora >= ? AND eliminado = 0";
        String sqlRep = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE estado = 'ENTREGADO' AND fecha_entrega >= ? AND eliminado = 0";
        double totalVentas = 0;
        double totalRep = 0;
        try (Cursor cursor = db.rawQuery(sqlVentas, new String[]{fechaHora})){
            if (cursor.moveToFirst()) {
                totalVentas = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        try (Cursor cursor = db.rawQuery(sqlRep, new String[]{fechaHora})){
            if (cursor.moveToFirst()) {
                totalRep = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotalesRep: " + e.getMessage());
        }
        return totalVentas + totalRep;
    }

    @Override
    public boolean getEstadoCorte() {
        String sql = "SELECT id_corte FROM cortes_cajas WHERE estado = 'Abierto' AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getEstadoCorte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public CorteCaja getCorteActual() {
        String sql = "SELECT * FROM cortes_cajas WHERE estado = 'Abierto' AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                CorteCaja corteCaja = new CorteCaja();
                corteCaja.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                corteCaja.setFecha_apertura(cursor.getString(cursor.getColumnIndexOrThrow("fecha_apertura")));
                corteCaja.setFecha_cierre(cursor.getString(cursor.getColumnIndexOrThrow("fecha_cierre")));
                corteCaja.setMonto_inicial(cursor.getDouble(cursor.getColumnIndexOrThrow("monto_inicial")));
                corteCaja.setVentas_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("ventas_totales")));
                corteCaja.setAbonos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("abonos_totales")));
                corteCaja.setGastos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("gastos_totales")));
                corteCaja.setDinero_en_caja(cursor.getDouble(cursor.getColumnIndexOrThrow("dinero_en_caja")));
                corteCaja.setDiferencia(cursor.getDouble(cursor.getColumnIndexOrThrow("diferencia")));
                corteCaja.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                return corteCaja;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCorteActual: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void vaciarTabla() {
        ContentValues values = new ContentValues();
        values.put("eliminado", 1);
        values.put("fecha_actualizacion", System.currentTimeMillis());
        values.put("sincronizado", 0);
        db.update("cortes_cajas", values, null, null);
    }

    @Override
    public ArrayList<CorteCaja> getCortes(String estado) {
        ArrayList<CorteCaja> cortes = new ArrayList<>();
        String sql = "SELECT * FROM cortes_cajas WHERE estado = ? AND eliminado = 0 ORDER BY id_corte DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{estado})) {
            while (cursor.moveToNext()){
                CorteCaja corteCaja = new CorteCaja();
                corteCaja.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                corteCaja.setFecha_apertura(cursor.getString(cursor.getColumnIndexOrThrow("fecha_apertura")));
                corteCaja.setFecha_cierre(cursor.getString(cursor.getColumnIndexOrThrow("fecha_cierre")));
                corteCaja.setMonto_inicial(cursor.getDouble(cursor.getColumnIndexOrThrow("monto_inicial")));
                corteCaja.setVentas_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("ventas_totales")));
                corteCaja.setAbonos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("abonos_totales")));
                corteCaja.setGastos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("gastos_totales")));
                corteCaja.setDinero_en_caja(cursor.getDouble(cursor.getColumnIndexOrThrow("dinero_en_caja")));
                corteCaja.setDiferencia(cursor.getDouble(cursor.getColumnIndexOrThrow("diferencia")));
                corteCaja.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                cortes.add(corteCaja);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCortes: " + e.getMessage());
        }
        return cortes;
    }

    @Override
    public CorteCaja getCorte(int id_corte) {
        String sql = "SELECT * FROM cortes_cajas WHERE id_corte = ? AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id_corte)})){
            if (cursor.moveToFirst()) {
                CorteCaja corteCaja = new CorteCaja();
                corteCaja.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                corteCaja.setFecha_apertura(cursor.getString(cursor.getColumnIndexOrThrow("fecha_apertura")));
                corteCaja.setFecha_cierre(cursor.getString(cursor.getColumnIndexOrThrow("fecha_cierre")));
                corteCaja.setMonto_inicial(cursor.getDouble(cursor.getColumnIndexOrThrow("monto_inicial")));
                corteCaja.setVentas_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("ventas_totales")));
                corteCaja.setVentas_efectivo(cursor.getDouble(cursor.getColumnIndexOrThrow("ventas_efectivo")));
                corteCaja.setVentas_tarjeta(cursor.getDouble(cursor.getColumnIndexOrThrow("ventas_tarjeta")));
                corteCaja.setAbonos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("abonos_totales")));
                corteCaja.setGastos_totales(cursor.getDouble(cursor.getColumnIndexOrThrow("gastos_totales")));
                corteCaja.setGastos_efectivo(cursor.getDouble(cursor.getColumnIndexOrThrow("gastos_efectivo")));
                corteCaja.setGastos_transferencia(cursor.getDouble(cursor.getColumnIndexOrThrow("gastos_transferencia")));
                corteCaja.setDinero_en_caja(cursor.getDouble(cursor.getColumnIndexOrThrow("dinero_en_caja")));
                corteCaja.setDiferencia(cursor.getDouble(cursor.getColumnIndexOrThrow("diferencia")));
                corteCaja.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                return corteCaja;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCorte: " + e.getMessage());
        }
        return null;
    }
}