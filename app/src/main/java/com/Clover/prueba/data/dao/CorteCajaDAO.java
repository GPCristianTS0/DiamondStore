package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.CorteCaja;

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
            long filas = db.insert("cortes_cajas", null, values);
            if (filas!=-1)return true;
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
            values.put("abonos_totales", corteCaja.getAbonos_totales());
            values.put("gastos_totales", corteCaja.getGastos_totales());
            values.put("dinero_en_caja", corteCaja.getDinero_en_caja());
            values.put("diferencia", corteCaja.getDiferencia());
            values.put("estado", corteCaja.getEstado());
            int filas = db.update("cortes_cajas", values, "id_corte = ?", new String[]{String.valueOf(corteCaja.getId_corte())});
            if (filas!=0)return true;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en cerrarCorte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public double getVentasTotalesCorte(String fechaHora) {
        String sql = "SELECT SUM(monto) FROM ventas WHERE fecha_Hora >= ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaHora})){
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean getEstadoCorte() {
        String sql = "SELECT id_corte FROM cortes_cajas WHERE estado = 'Abierto'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getEstadoCorte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public CorteCaja getCorteActual() {
        String sql = "SELECT * FROM cortes_cajas WHERE estado = 'Abierto'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                CorteCaja corteCaja = new CorteCaja();
                corteCaja.setId_corte(cursor.getInt(0));
                corteCaja.setFecha_apertura(cursor.getString(1));
                corteCaja.setFecha_cierre(cursor.getString(2));
                corteCaja.setMonto_inicial(cursor.getDouble(3));
                corteCaja.setVentas_totales(cursor.getDouble(4));
                corteCaja.setAbonos_totales(cursor.getDouble(5));
                corteCaja.setGastos_totales(cursor.getDouble(6));
                corteCaja.setDinero_en_caja(cursor.getDouble(7));
                corteCaja.setDiferencia(cursor.getDouble(8));
                corteCaja.setEstado(cursor.getString(9));
                return corteCaja;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getCorteActual: " + e.getMessage());
        }
        return null;
    }
    @Override
    public void vaciarTabla() {
        db.execSQL("DELETE FROM cortes_cajas");
    }
}
