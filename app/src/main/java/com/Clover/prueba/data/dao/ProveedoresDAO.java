package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.Clover.prueba.data.dao.interfaces.IProveedores;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Proveedor;

import java.util.ArrayList;

public class ProveedoresDAO implements IProveedores {
    private final SQLiteDatabase db;

    public ProveedoresDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public boolean addProveedor(Proveedor proveedor) {
        try {
            ContentValues values = new ContentValues();
            values.put("nombre_proveedor", proveedor.getNombre_proveedor());
            values.put("nombre_vendedor", proveedor.getNombre_vendedor());
            values.put("categoria", proveedor.getCategoria());
            values.put("direccion", proveedor.getDireccion());
            values.put("telefono", proveedor.getTelefono());
            values.put("email", proveedor.getEmail());
            values.put("dias_visita", proveedor.getDias_visita());
            values.put("observaciones", proveedor.getObservaciones());
            values.put("fecha_registro", proveedor.getFecha_registro());
            values.put("diasPago", proveedor.getDiasPago());
            long id = db.insert("proveedores", null, values);
            return id != -1;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en agregar proveedor: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateProveedor(Proveedor proveedorOld, Proveedor proveedor) {
        try {
            ContentValues values = new ContentValues();
            values.put("nombre_proveedor", proveedor.getNombre_proveedor());
            values.put("nombre_vendedor", proveedor.getNombre_vendedor());
            values.put("categoria", proveedor.getCategoria());
            values.put("direccion", proveedor.getDireccion());
            values.put("telefono", proveedor.getTelefono());
            values.put("email", proveedor.getEmail());
            values.put("dias_visita", proveedor.getDias_visita());
            values.put("observaciones", proveedor.getObservaciones());
            values.put("fecha_registro", proveedor.getFecha_registro());
            values.put("diasPago", proveedor.getDiasPago());
            int d = db.update("proveedores", values, "id_proveedor=?", new String[]{String.valueOf(proveedorOld.getId_proveedor())});
            return d > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en actualizar proveedor: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteProveedor(int id_proveedor) {
        try {
            int d = db.delete("proveedores", "id_proveedor=?", new String[]{String.valueOf(id_proveedor)});
            return d > 0;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en eliminar proveedor: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Proveedor getProveedor(int id_proveedor) {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor=?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id_proveedor)})) {
            if (cursor.moveToFirst()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId_proveedor(cursor.getInt(cursor.getColumnIndexOrThrow("id_proveedor")));
                proveedor.setNombre_proveedor(cursor.getString(cursor.getColumnIndexOrThrow("nombre_proveedor")));
                proveedor.setNombre_vendedor(cursor.getString(cursor.getColumnIndexOrThrow("nombre_vendedor")));
                proveedor.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                proveedor.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
                proveedor.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
                proveedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                proveedor.setDias_visita(cursor.getString(cursor.getColumnIndexOrThrow("dias_visita")));
                proveedor.setObservaciones(cursor.getString(cursor.getColumnIndexOrThrow("observaciones")));
                proveedor.setFecha_registro(cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro")));
                proveedor.setDiasPago(cursor.getString(cursor.getColumnIndexOrThrow("diasPago")));
                return proveedor;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en obtener proveedor by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getIdProveedor(String nombre_proveedor) {
        String sql = "SELECT id_proveedor FROM proveedores WHERE nombre_proveedor=?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nombre_proveedor)})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("id_proveedor"));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en obtener proveedor by id: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public ArrayList<Proveedor> getProveedor(String columnBusqueda, String busqueda) {
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        try (Cursor cursor = getRawQueryFilter(columnBusqueda, busqueda)) {
            while (cursor.moveToNext()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId_proveedor(cursor.getInt(cursor.getColumnIndexOrThrow("id_proveedor")));
                proveedor.setNombre_proveedor(cursor.getString(cursor.getColumnIndexOrThrow("nombre_proveedor")));
                proveedor.setNombre_vendedor(cursor.getString(cursor.getColumnIndexOrThrow("nombre_vendedor")));
                proveedor.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                proveedor.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
                proveedor.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
                proveedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                proveedor.setDias_visita(cursor.getString(cursor.getColumnIndexOrThrow("dias_visita")));
                proveedor.setObservaciones(cursor.getString(cursor.getColumnIndexOrThrow("observaciones")));
                proveedor.setFecha_registro(cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro")));
                proveedor.setDiasPago(cursor.getString(cursor.getColumnIndexOrThrow("diasPago")));
                proveedores.add(proveedor);
            }
            return proveedores;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en obtener proveedor by column: " + e.getMessage());
        }

        return proveedores;
    }

    @NonNull
    private Cursor getRawQueryFilter(String columnBusqueda, String busqueda) {
        if (busqueda == null) busqueda = "";
        if (columnBusqueda == null) columnBusqueda = "";

        busqueda = busqueda.trim();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM proveedores");
        ArrayList<String> args = new ArrayList<>();
        if (!busqueda.isEmpty() && !columnBusqueda.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(columnBusqueda).append(" LIKE ?");
            args.add("%" + busqueda + "%");
        }
        sql.append(" ORDER BY nombre_proveedor ASC");
        return db.rawQuery(sql.toString(), args.toArray(new String[0]));
    }
}
