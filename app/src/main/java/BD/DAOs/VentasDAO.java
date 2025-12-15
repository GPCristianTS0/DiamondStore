package BD.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import BD.Controller.ControllerVentas;
import Entidades.DetalleVenta;
import Entidades.Ventas;

public class VentasDAO implements ControllerVentas {

    private SQLiteDatabase db;
    public VentasDAO(Context context) {
         db = CloverBD.getInstance(context).getWritableDatabase();
    }
    @Override
    public void addVenta(Ventas venta, ArrayList<DetalleVenta> detalleventa) {

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id_cliente", venta.getId_cliente());
            values.put("fecha_Hora", venta.getFecha_hora());
            values.put("monto", venta.getMonto());
            values.put("total_piezas", venta.getTotal_piezas());
            values.put("tipo_pago", venta.getTipo_pago());
            long id_venta = db.insert("ventas", null, values);

            String sqlVendidos = "UPDATE productos SET vendidos = vendidos + ? WHERE id_producto = ?";

            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";


            //Detalle venta insersion
            ContentValues values2 = new ContentValues();
            for (DetalleVenta detalleventaa : detalleventa) {
                values2.put("id_venta", id_venta);
                values2.put("id_producto", detalleventaa.getId_producto());
                values2.put("cantidad", detalleventaa.getCantidad());
                values2.put("precio", detalleventaa.getPrecio());
                db.insert("detalles_venta", null, values2);
                //Actualizar stock
                SQLiteStatement statement = db.compileStatement(sqlStock);
                statement.bindLong(1, detalleventaa.getCantidad());
                statement.bindString(2, detalleventaa.getId_producto());
                statement.bindLong(3, detalleventaa.getCantidad());
                if (statement.executeUpdateDelete()<=0) return;

                //Actualizar Vendidos
                statement = db.compileStatement(sqlVendidos);
                statement.bindLong(1, detalleventaa.getCantidad());
                statement.bindString(2, detalleventaa.getId_producto());
                if(statement.executeUpdateDelete()<=0) return;
            }
            Log.e("Clover_App", "Venta agregada");
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("Clover_App", "Error en agregar venta: "+ e.getMessage());
        }finally {
            db.endTransaction();
            db.close();
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
            db.close();
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
        Log.e("Clover_App", "getVentas: "+ventas.toString());
        return ventas;
    }
    @Override
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta) {
        //DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
        String[] args = {String.valueOf(idVenta)};
        String sql = "SELECT dv.*, p.nombre_producto FROM detalles_venta dv INNER JOIN productos p ON dv.id_producto=p.id_producto WHERE dv.id_venta= ?";
        try ( Cursor cursor = db.rawQuery(sql, args)){
            while (cursor.moveToNext()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setId_detalle(cursor.getInt(0));
                detalleVenta.setId_venta(cursor.getInt(1));
                detalleVenta.setId_producto(cursor.getString(5));
                detalleVenta.setCantidad(cursor.getInt(3));
                detalleVenta.setPrecio(cursor.getInt(4));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVentas: " + e.getMessage());
        }
        Log.e("Clover_App", "getDetalleVentas: "+detalleVentas.toString());
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
                detalleVenta.setCantidad(cursor.getInt(3));
                detalleVenta.setPrecio(cursor.getInt(4));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVenta: " + e.getMessage());
        }
        for (DetalleVenta detalleVenta : detalleVentas) {
            Log.e("Clover_App", "getDetalleVenta: " + detalleVenta.toValues());
        }
    }
    //Tools
    private LocalDateTime parseStringLocalDateTime(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        return LocalDateTime.parse(fecha, formatter);
    }

}
