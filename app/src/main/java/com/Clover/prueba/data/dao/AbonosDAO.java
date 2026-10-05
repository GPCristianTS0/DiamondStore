package com.Clover.prueba.data.dao;

import static com.Clover.prueba.domain.ventas.VentasConstantes.VENTA_PAGADA;
import static com.Clover.prueba.domain.ventas.VentasConstantes.VENTA_PENDIENTE;

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
        long ahora = System.currentTimeMillis();
        db.beginTransaction();
        try {
            if (abono.getSaldoActual() < 0) {
                abono.setSaldoActual(0);
            }
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
            values.put("fecha_actualizacion", ahora);
            values.put("sincronizado", 0);
            values.put("eliminado", 0);

            long newRowId = db.insert("abonos", null, values);
            if (newRowId == -1) {
                Log.e("Clover_App", "Error al agregar abono");
                return -1;
            }
            values = new ContentValues();
            values.put("saldo", abono.getSaldoActual());
            values.put("fecha_actualizacion", ahora);
            values.put("sincronizado", 0);

            long rowsUpdated = db.update("clientes", values, "id_cliente=?", new String[]{String.valueOf(abono.getIdCliente())});
            if (rowsUpdated == -1) {
                Log.e("Clover_App", "Error al actualizar el saldo del cliente");
                return -1;
            }

            String sql = "SELECT * FROM ventas WHERE id_cliente = ? AND estado = '" + VENTA_PENDIENTE + "' AND eliminado = 0 ORDER BY monto_pendiente ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(abono.getIdCliente())});
            double montoTotal = abono.getMonto();

            while (cursor.moveToNext() && montoTotal > 0) {
                int idVenta = cursor.getInt(cursor.getColumnIndexOrThrow("id_venta"));
                double montoPendiente = cursor.getDouble(cursor.getColumnIndexOrThrow("monto_pendiente"));
                double cantidadAPagar;

                ContentValues valuesVenta = new ContentValues();
                if (montoTotal >= montoPendiente) {
                    cantidadAPagar = montoPendiente;
                    valuesVenta.put("monto_pendiente", 0);
                    valuesVenta.put("estado", VENTA_PAGADA);
                } else {
                    cantidadAPagar = montoTotal;
                    double nuevoPendiente = montoPendiente - montoTotal;
                    if (nuevoPendiente <= 0) {
                        valuesVenta.put("estado", VENTA_PAGADA);
                        nuevoPendiente = 0;
                    }
                    valuesVenta.put("monto_pendiente", nuevoPendiente);
                }

                valuesVenta.put("fecha_actualizacion", ahora);
                valuesVenta.put("sincronizado", 0);

                long i = db.update("ventas", valuesVenta, "id_venta=?", new String[]{String.valueOf(idVenta)});
                if (i == -1) {
                    cursor.close();
                    Log.e("Clover_App", "Error al actualizar la venta");
                    return -1;
                }

                montoTotal -= cantidadAPagar;
            }
            cursor.close();

            db.setTransactionSuccessful();
            return newRowId;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en addAbono: " + e.getMessage());
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public ArrayList<Abonos> getAbonos(String idCliente) {
        String sql = "SELECT * FROM abonos WHERE id_cliente = ? AND eliminado = 0 ORDER BY fecha_hora DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})) {
            ArrayList<Abonos> abonos = new ArrayList<>();
            while (cursor.moveToNext()) {
                abonos.add(mapearAbono(cursor));
            }
            return abonos;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener abonos: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Abonos getUltimoAbono(String idCliente) {
        String sql = "SELECT * FROM abonos WHERE id_cliente = ? AND eliminado = 0 ORDER BY fecha_hora DESC LIMIT 1";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})) {
            if (cursor.moveToFirst()) {
                return mapearAbono(cursor);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener último abono: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Abonos getAbono(String idAbono) {
        String sql = "SELECT * FROM abonos WHERE id_abono = ? AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idAbono})) {
            if (cursor.moveToFirst()) {
                return mapearAbono(cursor);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener abono: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteAbono(String idAbono) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("eliminado", 1);
            cv.put("fecha_actualizacion", System.currentTimeMillis());
            cv.put("sincronizado", 0);

            int filas = db.update("abonos", cv, "id_abono=?", new String[]{idAbono});
            return filas > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al eliminar abono: " + e.getMessage());
            return false;
        }
    }

    private Abonos mapearAbono(Cursor cursor) {
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
}