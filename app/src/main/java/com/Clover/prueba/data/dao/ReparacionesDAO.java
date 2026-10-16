package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Reparaciones;

import java.util.ArrayList;

public class ReparacionesDAO implements IReparaciones {
    private final SQLiteDatabase db;

    public ReparacionesDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public boolean addReparacion(Reparaciones reparacion) {
        try {
            ContentValues values = new ContentValues();
            values.put("tipo_servicio", reparacion.getTipo_servicio());
            values.put("id_cliente", reparacion.getId_cliente());
            values.put("equipo", reparacion.getEquipo());
            values.put("color", reparacion.getColor());
            values.put("falla_reportada", reparacion.getFalla_reportada());
            values.put("diagnostico", reparacion.getDiagnostico());
            values.put("costo_refacciones", reparacion.getCosto_refacciones());
            values.put("monto_total", reparacion.getMonto_total());
            values.put("anticipo", reparacion.getAnticipo());
            values.put("monto_pendiente", reparacion.getMonto_pendiente());
            values.put("tipo_pago", reparacion.getTipo_pago());
            values.put("estado", reparacion.getEstado());
            values.put("fecha_ingreso", reparacion.getFecha_ingreso());
            values.put("fecha_promesa", reparacion.getFecha_promesa());
            values.put("fecha_entrega", reparacion.getFecha_entrega());
            values.put("id_corte", reparacion.getId_corte());
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);
            values.put("eliminado", 0);

            long result = db.insert("reparaciones", null, values);
            if (result != -1) {
                reparacion.setId_reparacion((int) result);
                return true;
            }
            return true;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al agregar reparacion: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean updateReparacion(Reparaciones reparacion) {
        try {
            ContentValues values = new ContentValues();
            values.put("tipo_servicio", reparacion.getTipo_servicio());
            values.put("id_cliente", reparacion.getId_cliente());
            values.put("equipo", reparacion.getEquipo());
            values.put("color", reparacion.getColor());
            values.put("falla_reportada", reparacion.getFalla_reportada());
            values.put("diagnostico", reparacion.getDiagnostico());
            values.put("costo_refacciones", reparacion.getCosto_refacciones());
            values.put("monto_total", reparacion.getMonto_total());
            values.put("anticipo", reparacion.getAnticipo());
            values.put("monto_pendiente", reparacion.getMonto_pendiente());
            values.put("tipo_pago", reparacion.getTipo_pago());
            values.put("estado", reparacion.getEstado());
            values.put("fecha_promesa", reparacion.getFecha_promesa());
            values.put("fecha_entrega", reparacion.getFecha_entrega());
            values.put("id_corte", reparacion.getId_corte());
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);

            int result = db.update("reparaciones", values, "id_reparacion = ?", new String[]{String.valueOf(reparacion.getId_reparacion())});
            return result > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al actualizar reparacion: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateEstadoReparacion(int idReparacion, String estado, String fechaEntrega, int idCorte) {
        try {
            ContentValues values = new ContentValues();
            values.put("estado", estado);
            if (fechaEntrega != null && !fechaEntrega.isEmpty()) {
                values.put("fecha_entrega", fechaEntrega);
            }
            if (idCorte > 0) {
                values.put("id_corte", idCorte);
            }
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);

            int result = db.update("reparaciones", values, "id_reparacion = ?", new String[]{String.valueOf(idReparacion)});
            return result > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al actualizar estado de reparacion: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteReparacion(int idReparacion) {
        try {
            ContentValues values = new ContentValues();
            values.put("eliminado", 1);
            values.put("fecha_actualizacion", System.currentTimeMillis());
            values.put("sincronizado", 0);

            int result = db.update("reparaciones", values, "id_reparacion = ?", new String[]{String.valueOf(idReparacion)});
            return result > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al eliminar reparacion: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Reparaciones getReparacionById(int idReparacion) {
        String sql = "SELECT * FROM reparaciones WHERE id_reparacion = ? AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idReparacion)})) {
            if (cursor.moveToFirst()) {
                return mapCursorToReparacion(cursor);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener reparacion por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Reparaciones> getReparacionesByEstado(String estado) {
        ArrayList<Reparaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM reparaciones WHERE estado = ? AND eliminado = 0 ORDER BY id_reparacion DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{estado})) {
            while (cursor.moveToNext()) {
                lista.add(mapCursorToReparacion(cursor));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener reparaciones por estado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public ArrayList<Reparaciones> getReparacionesByTipoServicio(String tipoServicio) {
        ArrayList<Reparaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM reparaciones WHERE tipo_servicio = ? AND eliminado = 0 ORDER BY id_reparacion DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{tipoServicio})) {
            while (cursor.moveToNext()) {
                lista.add(mapCursorToReparacion(cursor));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener reparaciones por tipo servicio: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public ArrayList<Reparaciones> getReparacionesByCorte(int idCorte) {
        ArrayList<Reparaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM reparaciones WHERE id_corte = ? AND eliminado = 0 ORDER BY id_reparacion DESC";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idCorte)})) {
            while (cursor.moveToNext()) {
                lista.add(mapCursorToReparacion(cursor));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener reparaciones por corte: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public ArrayList<Reparaciones> getAllReparaciones() {
        ArrayList<Reparaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM reparaciones WHERE eliminado = 0 ORDER BY id_reparacion DESC";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                lista.add(mapCursorToReparacion(cursor));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener todas las reparaciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public double getIngresosReparacionesByCorte(int idCorte, String tipoPago) {
        String sql;
        String[] args;
        if (tipoPago == null || tipoPago.isEmpty()) {
            sql = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE id_corte = ? AND estado = 'ENTREGADO' AND eliminado = 0";
            args = new String[]{String.valueOf(idCorte)};
        } else {
            sql = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE id_corte = ? AND tipo_pago = ? AND estado = 'ENTREGADO' AND eliminado = 0";
            args = new String[]{String.valueOf(idCorte), tipoPago};
        }
        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al sumar ingresos de reparaciones por corte: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public double getIngresosTotalesReparaciones(String fechaInicio, String fechaFin) {
        String sql = "SELECT IFNULL(SUM(monto_total), 0) FROM reparaciones WHERE estado = 'ENTREGADO' AND eliminado = 0 AND fecha_entrega BETWEEN ? AND ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaInicio, fechaFin})) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al obtener ingresos totales de reparaciones: " + e.getMessage());
        }
        return 0;
    }

    private Reparaciones mapCursorToReparacion(Cursor cursor) {
        Reparaciones r = new Reparaciones();
        r.setId_reparacion(cursor.getInt(cursor.getColumnIndexOrThrow("id_reparacion")));
        r.setTipo_servicio(cursor.getString(cursor.getColumnIndexOrThrow("tipo_servicio")));
        r.setId_cliente(cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));
        r.setEquipo(cursor.getString(cursor.getColumnIndexOrThrow("equipo")));
        r.setColor(cursor.getString(cursor.getColumnIndexOrThrow("color")));
        r.setFalla_reportada(cursor.getString(cursor.getColumnIndexOrThrow("falla_reportada")));
        r.setDiagnostico(cursor.getString(cursor.getColumnIndexOrThrow("diagnostico")));
        r.setCosto_refacciones(cursor.getInt(cursor.getColumnIndexOrThrow("costo_refacciones")));
        r.setMonto_total(cursor.getInt(cursor.getColumnIndexOrThrow("monto_total")));
        r.setAnticipo(cursor.getInt(cursor.getColumnIndexOrThrow("anticipo")));
        r.setMonto_pendiente(cursor.getInt(cursor.getColumnIndexOrThrow("monto_pendiente")));
        r.setTipo_pago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
        r.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
        r.setFecha_ingreso(cursor.getString(cursor.getColumnIndexOrThrow("fecha_ingreso")));
        r.setFecha_promesa(cursor.getString(cursor.getColumnIndexOrThrow("fecha_promesa")));
        r.setFecha_entrega(cursor.getString(cursor.getColumnIndexOrThrow("fecha_entrega")));
        r.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
        return r;
    }
}