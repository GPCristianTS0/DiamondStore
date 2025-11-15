package BD.CRUD;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
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
                "rutaImagen TEXT," +
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
                producto.getRutaImagen()+"','"+
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
    public ArrayList<String> getSecciones() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> secciones = new ArrayList<>();
        String sql ="SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){
                secciones.add(cursor.getString(0));
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
                    producto.setRutaImagen(cursor.getString(0));
                    producto.setId(cursor.getString(1));
                    producto.setNombre(cursor.getString(2));
                    producto.setMarca(cursor.getString(3));
                    producto.setSeccion(seccion);
                    producto.setPrecioPublico(cursor.getInt(4));
                    producto.setPrecioNeto(cursor.getInt(5));
                    producto.setDescripcion(cursor.getString(6));
                    producto.setVendidos(cursor.getInt(7));
                    producto.setStock(cursor.getInt(8));
                    producto.setUltimoPedido(cursor.getString(9));
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
    public ArrayList<Productos> getProductos(String seccion, String columnaObtencion, String busqueda) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Productos> productos = new ArrayList<>();
        String sql;
        if (busqueda.isEmpty()){
            sql = "SELECT * FROM " + seccion;
        }else
            sql = "SELECT * FROM " + seccion + " WHERE "+columnaObtencion+" LIKE '%" + busqueda+"%'";
        Log.e("Clover_App", sql);
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                Productos producto = new Productos();
                producto.setRutaImagen(cursor.getString(0));
                producto.setId(cursor.getString(1));
                producto.setNombre(cursor.getString(2));
                producto.setMarca(cursor.getString(3));
                producto.setSeccion(seccion);
                producto.setPrecioPublico(cursor.getInt(4));
                producto.setPrecioNeto(cursor.getInt(5));
                producto.setDescripcion(cursor.getString(6));
                producto.setVendidos(cursor.getInt(7));
                producto.setStock(cursor.getInt(8));
                producto.setUltimoPedido(cursor.getString(9));
                productos.add(producto);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getProducto: "+e.getMessage());
        }
        return productos;
    }

    @Override
    public ArrayList<Productos> getProductos(String columnaObtencion, String busqueda) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> secciones = getSecciones();
        ArrayList<Productos> productos = new ArrayList<>();
        String sql;
        for (String seccion: secciones) {
            if(busqueda.isEmpty())
                sql = "SELECT * FROM " + seccion.toLowerCase();
            else
                sql = "SELECT * FROM " + seccion.toLowerCase() + " WHERE "+columnaObtencion+" LIKE '%" + busqueda+"%'";
            try (Cursor cursor = db.rawQuery(sql, null)) {
                while(cursor.moveToNext()) {
                    Productos producto = new Productos();
                    producto.setRutaImagen(cursor.getString(0));
                    producto.setId(cursor.getString(1));
                    producto.setNombre(cursor.getString(2));
                    producto.setMarca(cursor.getString(3));
                    producto.setSeccion(seccion);
                    producto.setPrecioPublico(cursor.getInt(4));
                    producto.setPrecioNeto(cursor.getInt(5));
                    producto.setDescripcion(cursor.getString(6));
                    producto.setVendidos(cursor.getInt(7));
                    producto.setStock(cursor.getInt(8));
                    producto.setUltimoPedido(cursor.getString(9));
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
        String sql = "DELETE FROM "+producto.getSeccion().toLowerCase()+" WHERE id='"+producto.getId()+"'";
        Log.e("Clover_App", "deleteProducto: "+sql);
        try(SQLiteDatabase db = getReadableDatabase()) {
            db.execSQL(sql);
        }catch (SQLException e){
            Log.e("Clover_App", "Error en eliminar producto: "+ e.getMessage());
        }
    }

    @Override
    public void updateProducto(Productos old, Productos newProducto) {
        String sql = "UPDATE "+old.getSeccion().toLowerCase()+" SET rutaImagen='"+newProducto.getRutaImagen()+"', " +
                "nombre='"+newProducto.getNombre()+"', " +
                "marca='"+newProducto.getMarca()+"',"+
                "precioPublico="+newProducto.getPrecioPublico()+","+
                "precioNeto="+newProducto.getPrecioNeto()+","+
                "descripcion='"+newProducto.getDescripcion()+"',"+
                "vendidos="+newProducto.getVendidos()+","+
                "stock="+newProducto.getStock()+","+
                "ultimo_pedido='"+newProducto.getUltimoPedido()+"' " +
                " WHERE id='"+old.getId()+"'";
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
        if (!newProducto.getRutaImagen().equals(old.getRutaImagen())){
            File archivo = new File(old.getRutaImagen());
            boolean f = archivo.delete();

        }
    }


    @Override
    public void updateStock(int unidades, Productos producto) {
        String sql = "UPDATE "+producto.getSeccion()+" SET stock="+unidades+" WHERE id='"+producto.getId()+"'";
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
