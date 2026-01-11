package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import com.Clover.prueba.data.controller.ControllerVentas;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

public class VentasDAO implements ControllerVentas {

    private SQLiteDatabase db;
    public VentasDAO(Context context) {
         db = CloverBD.getInstance(context).getWritableDatabase();
    }
    @Override
    public void addVenta(Ventas venta, ArrayList<DetalleVenta> detalleventa) {

        db.beginTransaction();
        try {
            ContentValues valuesVentas = new ContentValues();
            valuesVentas.put("id_cliente", venta.getId_cliente());
            valuesVentas.put("fecha_Hora", venta.getFecha_hora());
            valuesVentas.put("monto", venta.getMonto());
            valuesVentas.put("total_piezas", venta.getTotal_piezas());
            valuesVentas.put("tipo_pago", venta.getTipo_pago());
            long id_venta = db.insert("ventas", null, valuesVentas);

            String sqlVendidos = "UPDATE productos SET vendidos = vendidos + ? WHERE id_producto = ?";

            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";


            //Detalle venta insersion
            SQLiteStatement stmtStock = db.compileStatement(sqlStock);
            SQLiteStatement stmtVendidos = db.compileStatement(sqlVendidos);
            for (DetalleVenta detalleventaa : detalleventa) {
                ContentValues valuesDetalleVenta = new ContentValues();
                valuesDetalleVenta.put("id_venta", id_venta);
                valuesDetalleVenta.put("id_producto", detalleventaa.getId_producto());
                valuesDetalleVenta.put("nombre_producto", detalleventaa.getNombre_producto());
                valuesDetalleVenta.put("cantidad", detalleventaa.getCantidad());
                valuesDetalleVenta.put("precio", detalleventaa.getPrecio());
                db.insert("detalles_venta", null, valuesDetalleVenta);
                //Actualizar stock
                stmtStock.bindLong(1, detalleventaa.getCantidad());
                stmtStock.bindString(2, detalleventaa.getId_producto());
                stmtStock.bindLong(3, detalleventaa.getCantidad());
                if (stmtStock.executeUpdateDelete()<=0) throw new Exception("Error al actualizar stock");

                //Actualizar Vendidos
                stmtVendidos.bindLong(1, detalleventaa.getCantidad());
                stmtVendidos.bindString(2, detalleventaa.getId_producto());
                if(stmtVendidos.executeUpdateDelete()<=0) throw new Exception("Error al actualizar vendidos");
            }
            db.setTransactionSuccessful();
            stmtStock.close();
            stmtVendidos.close();
        }catch (Exception e){
            Log.e("Clover_App", "Error en agregar venta: "+ e.getMessage());
            throw new RuntimeException(e);
        }finally {
            db.endTransaction();
        }
    }
    @Override
    public void deleteVenta(Ventas venta) {
        db.beginTransaction();
        try {
            String[] args = {String.valueOf(venta.getId_venta())};
            db.delete("detalles_venta", "id_venta = ?", args);

            db.delete("ventas", "id_venta = ?", args);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("Clover_App", "Error en eliminar venta: "+ e.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public ArrayList<Ventas> getVentas() {
        ArrayList<Ventas> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";
        try (Cursor cursor = db.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                Ventas venta = new Ventas();
                venta.setId_venta(cursor.getInt(0));
                venta.setId_cliente(cursor.getInt(1));
                venta.setFecha_hora(cursor.getString(2));
                venta.setMonto(cursor.getInt(3));
                venta.setTotal_piezas(cursor.getInt(4));
                venta.setTipo_pago(cursor.getString(5));
                ventas.add(venta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentas: " + e.getMessage());
        }
        return ventas;
    }
    private Cursor rawQueryGetVentas(String mes, String year, String busqueda){
        if (busqueda==null) busqueda="";
        ArrayList<String> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ventas WHERE 1=1 ");
        sql.append("AND strftime('%Y', fecha_Hora) = ? ");
        args.add(year);

        if (!mes.isEmpty()) {
            sql.append("AND strftime('%m', fecha_Hora) = ? ");
            args.add(mes);
        }

        if (!busqueda.isEmpty()) {
            sql.append("AND id_venta = ? ");
            args.add(busqueda);
        }
        return db.rawQuery(sql.toString(), args.toArray(new String[0]));
    }
    @Override
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda) {
        ArrayList<Ventas> ventas = new ArrayList<>();
        try (Cursor cursor = rawQueryGetVentas(mes, year, busqueda)){
            while (cursor.moveToNext()){
                Ventas venta = new Ventas();
                venta.setId_venta(cursor.getInt(0));
                venta.setId_cliente(cursor.getInt(1));
                venta.setFecha_hora(cursor.getString(2));
                venta.setMonto(cursor.getInt(3));
                venta.setTotal_piezas(cursor.getInt(4));
                venta.setTipo_pago(cursor.getString(5));
                ventas.add(venta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentas Array: " + e.getMessage());
        }
        return ventas;
    }
    @Override
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta) {
        //DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
        String[] args = {String.valueOf(idVenta)};
        String sql = "SELECT * FROM detalles_venta WHERE id_venta= ?";
        try ( Cursor cursor = db.rawQuery(sql, args)){
            while (cursor.moveToNext()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setId_detalle(cursor.getInt(0));
                detalleVenta.setId_venta(cursor.getInt(1));
                detalleVenta.setId_producto(cursor.getString(2));
                detalleVenta.setNombre_producto(cursor.getString(3));
                detalleVenta.setCantidad(cursor.getInt(4));
                detalleVenta.setPrecio(cursor.getInt(5));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVentas: " + e.getMessage());
        }
        getDetalleVenta();
        return detalleVentas;
    }

    @Override
    public ArrayList<String> getAnios() {
        ArrayList<String> anios = new ArrayList<>();
        String sql = "SELECT DISTINCT strftime('%Y', fecha_Hora) FROM ventas ORDER BY strftime('%Y', fecha_Hora) DESC";
        try (Cursor cursor = db.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                anios.add(cursor.getString(0));
            }
        } catch (Exception e){
            Log.e("Clover_App", "Error en getAnios: "+e.getMessage());
        }
        return anios;
    }
    private void getDetalleVenta(){
        String sql = "SELECT * FROM detalles_venta";
        ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setId_detalle(cursor.getInt(0));
                detalleVenta.setId_venta(cursor.getInt(1));
                detalleVenta.setId_producto(cursor.getString(2));
                detalleVenta.setNombre_producto(cursor.getString(3));
                detalleVenta.setCantidad(cursor.getInt(4));
                detalleVenta.setPrecio(cursor.getInt(5));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVenta: " + e.getMessage());
        }
    }

    //Tools
    private LocalDateTime parseStringLocalDateTime(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        return LocalDateTime.parse(fecha, formatter);
    }

}
