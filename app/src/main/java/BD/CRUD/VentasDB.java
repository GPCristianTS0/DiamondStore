package BD.CRUD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import BD.Controller.ControllerVentas;
import Entidades.DetalleVenta;
import Entidades.Ventas;

public class VentasDB extends SQLiteOpenHelper implements ControllerVentas {
    @Override
    public void createTable() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String sql = "create table IF NOT EXISTS ventas_"+year+" (" +
                "id_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_cliente INTEGER," +
                "fecha_Hora TEXT,"+
                "monto INTEGER," +
                "total_piezas INTEGER," +
                "tipo_pago TEXT)";
        try (SQLiteDatabase db = getReadableDatabase()){
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Clover_App", "Error en Creacion de tabla Ventas: "+e.getMessage());
        }

        sql = "create table IF NOT EXISTS detalle_venta_"+year+" (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_venta INTEGER," +
                "id_producto INTEGER," +
                "cantidad INTEGER," +
                "precio INTEGER)";
        try (SQLiteDatabase db = getReadableDatabase()){
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Clover_App", "Error en Creacion de tabla detalle_venta: "+e.getMessage());
        }
    }

    Context context;
    public VentasDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void addVenta(Ventas venta) {
        String sql = "insert into ventas_"+Calendar.getInstance().get(Calendar.YEAR)+" ( "+venta.toColumns()+") VALUES (" +
                venta.getId_cliente()+",'"+
                venta.getFecha_hora()+"',"+
                venta.getMonto()+","+
                venta.getTotal_piezas()+",'"+
                venta.getTipo_pago()+"')";
        try (SQLiteDatabase db = getReadableDatabase()){
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Clover_App", "Error en agregar venta: "+ e.getMessage());
        }
    }

    @Override
    public void addDetalleVenta(ArrayList<DetalleVenta> detallesVentas) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            for (DetalleVenta detalleVenta : detallesVentas){
                String sql = "insert into detalle_venta_" + Calendar.getInstance().get(Calendar.YEAR) + " ( " + detalleVenta.toColumns() + ") VALUES (" +
                        detalleVenta.getId_venta() + ",'" +
                        detalleVenta.getId_producto() + "'," +
                        detalleVenta.getCantidad() + "," +
                        detalleVenta.getPrecio() + ")";
                db.execSQL(sql);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en agregar detalle venta: " + e.getMessage());
        }
    }

    @Override
    public void deleteVenta(Ventas venta) {
        String sql = "delete from ventas_"+Calendar.getInstance().get(Calendar.YEAR)+" where id_venta="+venta.getId_venta();
        String sql2 = "delete from detalle_venta_"+Calendar.getInstance().get(Calendar.YEAR)+" where id_venta="+venta.getId_venta();
        try (SQLiteDatabase db = getReadableDatabase()){
            db.execSQL(sql);
            db.execSQL(sql2);
        }catch (Exception e){
            Log.e("Clover_App", "Error en eliminar venta: "+ e.getMessage());
        }
    }

    @Override
    public void deleteDetalleVenta(DetalleVenta detalleVenta) {

    }

    @Override
    public ArrayList<Ventas> getVentas() {
        ArrayList<Ventas> ventas = new ArrayList<>();
        String sql = "select * from ventas_"+Calendar.getInstance().get(Calendar.YEAR);
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                Ventas venta = new Ventas();
                venta.setId_venta(cursor.getInt(0));
                venta.setId_cliente(cursor.getInt(1));
                venta.setFecha_hora(convertirFecha(cursor.getString(2)));
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

    @Override
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda) {
        ArrayList<Ventas> ventas = new ArrayList<>();
        String sql;
        if (busqueda!=null && !busqueda.isEmpty()){
            sql = "select * from ventas_"+year+" where id_venta="+busqueda+" AND fecha_Hora LIKE '%"+mes+"%'";
        }else{
            sql = "select * from ventas_"+year+" where fecha_Hora LIKE '%"+mes+"%'";
        }
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                Ventas venta = new Ventas();
                venta.setId_venta(cursor.getInt(0));
                venta.setId_cliente(cursor.getInt(1));
                venta.setFecha_hora(convertirFecha(cursor.getString(2)));
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
    private String convertirFecha(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime fechaHora = LocalDateTime.parse(fecha, formatter);
        return fechaHora.format(formatterOut);
    }
    @Override
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta, LocalDateTime fecha) {

        ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta_"+fecha.getYear()+" WHERE id_venta="+idVenta;
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery(sql, null)){
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
            Log.e("Clover_App", "Error en getDetalleVentas: " + e.getMessage());
        }
        return null;
    }
    public ArrayList<String> getAnios() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> secciones = new ArrayList<>();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'ventas_%'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                String nombreTabla = cursor.getString(0);
                String anioSolo = nombreTabla.replace("ventas_", "");

                secciones.add(anioSolo);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: " + e.getMessage());
        }
        return secciones;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
