package BD.CRUD;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import BD.Controller.ControllerProducto;
import Entidades.Productos;

public class ProductoDB extends SQLiteOpenHelper implements ControllerProducto {
    Context context;

    public ProductoDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void createTable(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "create table IF NOT EXISTS "+name.toLowerCase()+" (" +
                "id TEXT PRIMARY KEY, " +
                "nombre TEXT," +
                "marca TEXT," +
                "precioPublico INTEGER," +
                "precioNeto INTEGER," +
                "descripcion TEXT," +
                "vendidos INTEGER," +
                "stock INTEGER," +
                "ultimo_pedido TEXT)";
        try {
            db.execSQL(sql);
        }catch (SQLException e){
            Log.e("Clover_App", "Error en Creacion de tabla "+name+": "+e.getMessage());
        }
    }

    @Override
    public void addProducto(Productos producto) {
        SQLiteDatabase db = getReadableDatabase();
        createTable(producto.getSeccion());
        String sql = "insert into "+producto.getSeccion().toLowerCase()+" ( "+producto.toColumns()+") VALUES ('" +
                producto.getId()+"','"+
                producto.getNombre()+"','"+
                producto.getMarca()+"',"+
                producto.getPrecioPublico()+","+
                producto.getPrecioNeto()+",'"+
                producto.getDescripcion()+"',"+
                producto.getVendidos()+","+
                producto.getStock()+",'"+
                producto.getUltimoPedido()+
                "')";
        SQLiteDatabase sb = getReadableDatabase();
        try {
            sb.execSQL(sql);
        }catch (SQLException e){
            Log.e("Clover_App", "Error en agregar producto: "+ e.getMessage());
        }
    }
    private ArrayList<String> getSecciones() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> secciones = new ArrayList<>();
        String sql ="SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){
                secciones.add(cursor.getString(0));
                Log.e("Clover_App", "Seccion: "+cursor.getString(0)+"\n");
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: "+e.getMessage());
        }
        return secciones;
    }
    @Override
    public Productos getProductoCode(String id) {
        Productos producto = new Productos();
        ArrayList<String> secciones = getSecciones();
        SQLiteDatabase db = getReadableDatabase();
        getSecciones();
        for (String seccion: secciones) {
            String sql = "SELECT * FROM " + seccion.toLowerCase() + " WHERE id='" + id+"'";
            try (Cursor cursor = db.rawQuery(sql, null)) {
                if (cursor.moveToFirst()) {
                    producto.setId(cursor.getString(0));
                    producto.setNombre(cursor.getString(1));
                    producto.setMarca(cursor.getString(2));
                    producto.setSeccion(seccion);
                    producto.setPrecioPublico(cursor.getInt(3));
                    producto.setPrecioNeto(cursor.getInt(4));
                    producto.setDescripcion(cursor.getString(5));
                    producto.setVendidos(cursor.getInt(6));
                    producto.setStock(cursor.getInt(7));
                    producto.setUltimoPedido(cursor.getString(8));
                    return producto;
                }
            } catch (Exception e) {
                Log.e("Clover_App", "En getProductoCode: "+e.getMessage());
            }
        }
        producto.setId("0");
        return producto;
    }

    @Override
    public Productos getProducto(String nombre) {
        ArrayList<String> secciones = getSecciones();
        SQLiteDatabase db = getReadableDatabase();
        for (String seccion: secciones) {
            String sql = "SELECT * FROM " + seccion + " WHERE nombre=" + nombre;
            try (Cursor cursor = db.rawQuery(sql, null)) {
                if (cursor.moveToNext()) {
                    Productos producto = new Productos();
                    producto.setId(cursor.getString(0));
                    producto.setNombre(cursor.getString(1));
                    producto.setMarca(cursor.getString(2));
                    producto.setSeccion(seccion);
                    producto.setPrecioPublico(cursor.getInt(4));
                    producto.setPrecioNeto(cursor.getInt(5));
                    producto.setStock(cursor.getInt(7));
                    producto.setUltimoPedido(cursor.getString(8));
                    return producto;
                }
            } catch (Exception e) {
                Log.e("Clover_App", "En getProducto: "+e.getMessage());
            }
        }
        return null;
    }

    @Override
    public ArrayList<Productos> getProductos() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> secciones = getSecciones();
        ArrayList<Productos> productos = new ArrayList<>();
        for (String seccion: secciones) {
            Log.e("Clover_App", seccion+" Prueba");
            String sql = "SELECT * FROM " + seccion.toLowerCase();
            try (Cursor cursor = db.rawQuery(sql, null)) {
                while(cursor.moveToNext()) {
                    Productos producto = new Productos();
                    producto.setId(cursor.getString(0));
                    producto.setNombre(cursor.getString(1));
                    producto.setMarca(cursor.getString(2));
                    producto.setSeccion(seccion);
                    producto.setPrecioPublico(cursor.getInt(3));
                    producto.setPrecioNeto(cursor.getInt(4));
                    producto.setDescripcion(cursor.getString(5));
                    producto.setVendidos(cursor.getInt(6));
                    producto.setStock(cursor.getInt(7));
                    producto.setUltimoPedido(cursor.getString(8));
                    Log.e("Clover_App", producto.toString());
                    productos.add(producto);
                }
            } catch (Exception e) {
                Log.e("Clover_App", "getProductos: "+e.getMessage());
            }
        }
        return productos;
    }

    @Override
    public void deleteProducto(Productos producto) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "DELETE * FROM "+producto.getSeccion().toLowerCase()+" WHERE id="+producto.getId();
        db.execSQL(sql);
    }

    @Override
    public void updateProducto(Productos old, Productos newProducto) {

    }

    @Override
    public void updateStock(int unidades, Productos producto) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
