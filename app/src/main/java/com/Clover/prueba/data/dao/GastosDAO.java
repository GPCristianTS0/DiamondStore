package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IGastos;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Gastos;

import java.util.ArrayList;

public class GastosDAO implements IGastos {
        private SQLiteDatabase db;
        public GastosDAO(Context context){
            db = CloverBD.getInstance(context).getWritableDatabase();
        }
        @Override
        public boolean addGasto(Gastos gasto) {
            try {
                ContentValues values = new ContentValues();
                values.put("descripcion", gasto.getDescripcion());
                values.put("monto", gasto.getMonto());
                values.put("fecha_hora", gasto.getFecha_hora());
                values.put("metodo_pago", gasto.getMetodo_pago());
                values.put("id_corte", gasto.getId_corte());
                values.put("id_proveedor", gasto.getId_proveedor());
                long result = db.insert("gastos", null, values);
                return result != -1;
            } catch (Exception e) {
                Log.e("Clover_App", "Error al agregar gasto: " + e.getMessage());
            }
            return false;
        }

        @Override
        public boolean deleteGasto(int gasto) {
            try {
                int result = db.delete("gastos", "id_gasto = ?", new String[]{String.valueOf(gasto)});
                return result > 0;
            } catch (Exception e) {
                Log.e("Clover_App", "Error al eliminar gasto: " + e.getMessage());
            }
            return false;
        }

        @Override
        public ArrayList<Gastos> getGastosBy(String columna, String valor) {
            ArrayList<Gastos> gastos = new ArrayList<>();
            try (Cursor cursor = rawQuery(columna, valor)){
                while (cursor.moveToNext()){
                    Gastos gasto = new Gastos();
                    gasto.setId_gasto(cursor.getInt(cursor.getColumnIndexOrThrow("id_gasto")));
                    gasto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                    gasto.setMonto(cursor.getDouble(cursor.getColumnIndexOrThrow("monto")));
                    gasto.setFecha_hora(cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")));
                    gasto.setMetodo_pago(cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")));
                    gasto.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                    gasto.setId_proveedor(cursor.getInt(cursor.getColumnIndexOrThrow("id_proveedor")));
                    gastos.add(gasto);
                }
                return gastos;
            } catch (Exception e) {
                Log.e("Clover_App", "Error al obtener gastos: " + e.getMessage());
            }
            return gastos;
        }
        private Cursor rawQuery(String columna, String valor){
            if (columna == null) columna ="";
            if (valor == null) valor ="";
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM gastos");
            ArrayList<String> args = new ArrayList<String>();
            if (!columna.isEmpty()){
                sql.append(" WHERE ");
                sql.append(columna);
                sql.append(" = ?");
                args.add(valor);
            }
            sql.append(" ORDER BY id_gasto DESC");
            return db.rawQuery(sql.toString(), args.toArray(new String[0]));
        }
        @Override
        public ArrayList<Gastos> getGastosByCorte(int id_corte) {
            ArrayList<Gastos> gastos = new ArrayList<>();
            String sql = "SELECT * FROM gastos WHERE id_corte = ?";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id_corte)})){
                while (cursor.moveToNext()){
                    Gastos gasto = new Gastos();
                    gasto.setId_gasto(cursor.getInt(cursor.getColumnIndexOrThrow("id_gasto")));
                    gasto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                    gasto.setMonto(cursor.getDouble(cursor.getColumnIndexOrThrow("monto")));
                    gasto.setFecha_hora(cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")));
                    gasto.setMetodo_pago(cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")));
                    gasto.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
                    gasto.setId_proveedor(cursor.getInt(cursor.getColumnIndexOrThrow("id_proveedor")));
                    gastos.add(gasto);
                }
                return gastos;
            } catch (Exception e) {
                Log.e("Clover_App", "Error al obtener gastos: " + e.getMessage());
            }
            return gastos;
        }

        @Override
        public double sumarTotalGastosByCorte(int id_corte) {
            String sql = "SELECT SUM(monto) FROM gastos WHERE id_corte = ?";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id_corte)})){
                if (cursor.moveToNext()){
                    return cursor.getDouble(0);
                }
            } catch (Exception e) {
                Log.e("Clover_App", "Error al obtener gastos: " + e.getMessage());
            }
            return 0;
        }
}
