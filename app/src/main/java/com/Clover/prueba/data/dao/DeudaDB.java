package com.Clover.prueba.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.Clover.prueba.data.dao.interfaces.IDeuda;
import com.Clover.prueba.data.models.Deuda;

public class DeudaDB extends SQLiteOpenHelper implements IDeuda {
    Context context;
    public DeudaDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void addDeuda(int id, int id_deuda, int saldo_total, String plazo, String fecha_pago, int pagos_restantes, int pagos_totales, int monto_pago, String id_productos, int saldo_restante) {
        String sql = "insert into deudas ( "+new Deuda().toColumns()+") VALUES (" +
                id+","+
                id_deuda+","+
                saldo_restante+","+
                saldo_total+",'"+
                plazo+"','"+
                fecha_pago+"',"+
                pagos_restantes+","+
                pagos_totales+","+
                monto_pago+","+
                id_productos+
                ")";
        SQLiteDatabase sb = getReadableDatabase();
        sb.execSQL(sql);
    }

    @Override
    public Deuda getDeuda(int id) {
        String sql = "SELECT * FROM deudas WHERE id="+id;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)){
            if (cursor.moveToNext()){
                Deuda deuda = new Deuda();
                deuda.setId_cliente(cursor.getInt(0));
                deuda.setId_deuda(cursor.getInt(1));
                deuda.setSaldo_pendiente(cursor.getInt(2));
                deuda.setSaldo_total(cursor.getInt(3));
                deuda.setPlazo(cursor.getString(4));
                deuda.setFecha_pago(cursor.getString(5));
                deuda.setPagos_restantes(cursor.getInt(6));
                deuda.setPagos_totales(cursor.getInt(7));
                deuda.setMonto_pagos(cursor.getInt(8));
                deuda.setId_productos(cursor.getString(9));
                return deuda;
            }else
                return null;
        }catch (SQLException e) {
            Log.d("TAG", "Error en getDeuda by id: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Deuda getDeuda(String nombre) {
        String sql = "SELECT * FROM deudas WHERE nombre='"+nombre+"''";
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)){
            if (cursor.moveToNext()){
                Deuda deuda = new Deuda();
                deuda.setId_cliente(cursor.getInt(0));
                deuda.setId_deuda(cursor.getInt(1));
                deuda.setSaldo_pendiente(cursor.getInt(2));
                deuda.setSaldo_total(cursor.getInt(3));
                deuda.setPlazo(cursor.getString(4));
                deuda.setFecha_pago(cursor.getString(5));
                deuda.setPagos_restantes(cursor.getInt(6));
                deuda.setPagos_totales(cursor.getInt(7));
                deuda.setMonto_pagos(cursor.getInt(8));
                deuda.setId_productos(cursor.getString(9));
                return deuda;
            }else
                return null;
        }catch (SQLException e) {
            Log.d("TAG", "Error en getDeuda by nombre: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ArrayList getDeuda() {
        String sql = "SELECT * FROM deudas";
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            ArrayList<Deuda> deudas = new ArrayList<>();
            while (cursor.moveToNext()) {
                Deuda deuda = new Deuda();
                deuda.setId_cliente(cursor.getInt(0));
                deuda.setId_deuda(cursor.getInt(1));
                deuda.setSaldo_pendiente(cursor.getInt(2));
                deuda.setSaldo_total(cursor.getInt(3));
                deuda.setPlazo(cursor.getString(4));
                deuda.setFecha_pago(cursor.getString(5));
                deuda.setPagos_restantes(cursor.getInt(6));
                deuda.setPagos_totales(cursor.getInt(7));
                deuda.setMonto_pagos(cursor.getInt(8));
                deuda.setId_productos(cursor.getString(9));
                deudas.add(deuda);
            }
            return deudas;
        } catch (SQLException e) {
            Log.d("TAG", "Error en getClients Array: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteDeuda(int id) {
        String sql = "DELETE * FROM deudas WHERE id="+id;
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
    }

    @Override
    public void realizarPago(int monto_pago, int id_deuda) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table deudas (" +
                "id_deuda INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "saldo_pendiente INTEGER," +
                "saldo_total INTEGER," +
                "plazo TEXT," +
                "fecha_pago TEXT," +
                "pagos_restantes INTEGER," +
                "pagos_Totales INTEGER," +
                "monto_pago INTEGER," +
                "id_producto)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
