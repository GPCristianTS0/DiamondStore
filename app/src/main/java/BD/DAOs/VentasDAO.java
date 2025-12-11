package BD.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    private CloverBD dbHelper;

    public VentasDAO(Context context) {
        this.dbHelper = new CloverBD(context);
    }
    @Override
    public void addVenta(Ventas venta, ArrayList<DetalleVenta> detalleventa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id_cliente", venta.getId_cliente());
            values.put("fecha_Hora", venta.getFecha_hora());
            values.put("monto", venta.getMonto());
            values.put("total_piezas", venta.getTotal_piezas());
            values.put("tipo_pago", venta.getTipo_pago());
            long id_venta = db.insert("ventas", null, values);

            ContentValues values2 = new ContentValues();
            for (DetalleVenta detalleventaa : detalleventa) {
                values2.put("id_venta", id_venta);
                values2.put("id_producto", detalleventaa.getId_producto());
                values2.put("cantidad", detalleventaa.getCantidad());
                values2.put("precio", detalleventaa.getPrecio());
                db.insert("detalles_venta", null, values2);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("Clover_App", "Error en agregar venta: "+ e.getMessage());
        }finally {
            db.endTransaction();
            db.close();
        }
        Log.e("Clover_App", "addVenta: "+venta.toString());
        Log.e("Clover_App", "addVenta: "+detalleventa.toString());
    }
    @Override
    public void deleteVenta(Ventas venta) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
        try (SQLiteDatabase db = dbHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(sql, null)){
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
        Log.e("Clover_App", sql.toString());
        Log.e("Clover_App", args.toString());
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
    private String convertirFecha(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime fechaHora = LocalDateTime.parse(fecha, formatter);
        return fechaHora.format(formatterOut);
    }
    @Override
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta) {
        //DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
        String[] args = {String.valueOf(idVenta)};
        String sql = "SELECT dv.*, p.nombre_producto FROM detalle_venta dv INNER JOIN productos p ON dv.id_producto=p.id_producto WHERE dv.id_venta= ?";
        try (SQLiteDatabase db = dbHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(sql, args)){
            while (cursor.moveToNext()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setId_detalle(cursor.getInt(0));
                detalleVenta.setId_venta(cursor.getInt(1));
                detalleVenta.setId_producto(cursor.getString(6));
                detalleVenta.setCantidad(cursor.getInt(4));
                detalleVenta.setPrecio(cursor.getInt(5));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVentas: " + e.getMessage());
        }
        Log.e("Clover_App", "getDetalleVentas: "+detalleVentas.toString());
        return detalleVentas;
    }

    @Override
    public ArrayList<String> getAnios() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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

    //Tools
    private LocalDateTime parseStringLocalDateTime(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        return LocalDateTime.parse(fecha, formatter);
    }

}
