package com.Clover.prueba.data.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Abonos;

import java.util.ArrayList;

public class AbonosDAO implements IAbonos {
    private final SQLiteDatabase db;

    public AbonosDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public long addAbono(Abonos abono) {
        try{
            db.beginTransaction();
            //Registro del abono
            if (abono.getSaldoActual()<0)
                abono.setSaldoActual(0);
            ContentValues values = new ContentValues();
            values.put("fecha_hora", abono.getFecha());
            values.put("monto", abono.getMonto());
            values.put("id_corte", abono.getIdCorte());
            values.put("id_cliente", abono.getIdCliente());
            values.put("saldo_anterior", abono.getSaldoAnterior());
            values.put("saldo_nuevo", abono.getSaldoActual());
            values.put("id_empleado", abono.getIdEmpleado());
            values.put("observaciones", abono.getObservacion());
            values.put("tipo_pago", abono.getTipoPago());
            long newRowId = db.insert("abonos", null, values);
            if (newRowId == -1) {
                db.endTransaction();
                Log.e("Clover_App", "Error al agregar abono");
                return -1;
            }
            //Actualizacion del saldo del cliente
            values = new ContentValues();
            values.put("saldo", abono.getSaldoActual());
            long rowsUpdated = db.update("clientes", values, "id_cliente=?", new String[]{String.valueOf(abono.getIdCliente())});
            if (rowsUpdated == -1) {
                db.endTransaction();
                Log.e("Clover_App", "Error al actualizar el saldo del cliente");
                return -1;
            }
            //Actualizar ventas
            String sql = "SELECT * FROM ventas WHERE id_cliente = ? AND estado = '"+VENTA_PENDIENTE+"' ORDER BY monto_pendiente ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(abono.getIdCliente())});
            double montoTotal = abono.getMonto();
            while (cursor.moveToNext() && montoTotal > 0) {
                int idVenta = cursor.getInt(cursor.getColumnIndexOrThrow("id_venta"));
                double montoPendiente = cursor.getDouble(cursor.getColumnIndexOrThrow("monto_pendiente"));
                double cantidadAPagar;
                if (montoTotal >= montoPendiente){
                    cantidadAPagar = montoPendiente;
                    ContentValues valuesVenta = new ContentValues();
                    valuesVenta.put("monto_pendiente", 0);
                    valuesVenta.put("estado", VENTA_PAGADA);


                    long i = db.update("ventas", valuesVenta, "id_venta=?", new String[]{String.valueOf(idVenta)});
                    if (i == -1){
                        db.endTransaction();
                        Log.e("Clover_App", "Error al actualizar la venta");
                        return -1;
                    }
                }else{
                    cantidadAPagar = montoTotal;
                    ContentValues valuesVenta = new ContentValues();
                    if (montoPendiente-montoTotal<0) {
                        valuesVenta.put("estado", VENTA_PAGADA);
                        montoPendiente =  0;
                    }
                    valuesVenta.put("monto_pendiente", montoPendiente-montoTotal);

                    long i = db.update("ventas", valuesVenta, "id_venta=?", new String[]{String.valueOf(idVenta)});
                    if (i == -1){
                        db.endTransaction();
                        Log.e("Clover_App", "Error al actualizar la venta");
                        return -1;
                    }
                }
                montoTotal -= cantidadAPagar;
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            return newRowId;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al agregar abono: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public ArrayList<Abonos> getAbonos(String idCliente) {
        String sql = "SELECT * FROM abonos WHERE id_cliente = ? ORDER BY fecha_hora DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            ArrayList<Abonos> abonos = new ArrayList<>();
            while (cursor.moveToNext()){
                Abonos abono = new Abonos();
                abono.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_abono")));
                abono.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")));
                abono.setMonto(cursor.getDouble(cursor.getColumnIndexOrThrow("monto")));
                abono.setIdCorte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                abono.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));
                abono.setSaldoAnterior(cursor.getDouble(cursor.getColumnIndexOrThrow("saldo_anterior")));
                abono.setSaldoActual(cursor.getDouble(cursor.getColumnIndexOrThrow("saldo_nuevo")));
                abono.setIdEmpleado(cursor.getInt(cursor.getColumnIndexOrThrow("id_empleado")));
                abono.setObservacion(cursor.getString(cursor.getColumnIndexOrThrow("observaciones")));
                abono.setTipoPago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
                abonos.add(abono);
            }
            return abonos;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener abonos: " + e.getMessage());
        }
        return null;
    }
    @Override
    public Abonos getUltimoAbono(String idCliente) {
        String sql = "SELECT * FROM abonos WHERE id_cliente = ? ORDER BY fecha_hora DESC LIMIT 1";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            if (cursor.moveToFirst()){
                Abonos abono = new Abonos();
                abono.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_abono")));
                abono.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")));
                abono.setMonto(cursor.getDouble(cursor.getColumnIndexOrThrow("monto")));
                abono.setIdCorte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                abono.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));
                abono.setSaldoAnterior(cursor.getDouble(cursor.getColumnIndexOrThrow("saldo_anterior")));
                abono.setSaldoActual(cursor.getDouble(cursor.getColumnIndexOrThrow("saldo_nuevo")));
                abono.setIdEmpleado(cursor.getInt(cursor.getColumnIndexOrThrow("id_empleado")));
                abono.setObservacion(cursor.getString(cursor.getColumnIndexOrThrow("observaciones")));
                abono.setTipoPago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
                return abono;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener abonos: " + e.getMessage());
        }
        return null;
    }


    @Override
    public Abonos getAbono(String idAbono) {
        return null;
    }

    @Override
    public boolean deleteAbono(String idAbono) {
        return false;
    }
}
