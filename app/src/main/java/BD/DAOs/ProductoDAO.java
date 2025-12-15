package BD.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import BD.Controller.ControllerProducto;
import Entidades.Productos;

public class ProductoDAO implements ControllerProducto{

    private SQLiteDatabase db;


    public ProductoDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public void createTabl(String name) {
    }

    @Override
    public void addProducto(Productos producto) {
        //Se comprubea si existe la seccion para tomar el id
        String sql = "SELECT id_seccion FROM secciones WHERE nombre_seccion='"+producto.getSeccion()+"'";
        long idSeccion = -1;
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                idSeccion = cursor.getInt(0);
            }
            cursor.close();
            if (idSeccion==-1){
                ContentValues values = new ContentValues();
                values.put("nombre_seccion", producto.getSeccion());
                idSeccion = db.insert("secciones", null, values);
                if (idSeccion==-1){
                    throw new Exception("Error al agregar seccion");
                }
            }

            ContentValues valuesd = new ContentValues();
            valuesd.put("rutaImagen", producto.getRutaImagen());
            valuesd.put("id_producto", producto.getId());
            valuesd.put("nombre_producto", producto.getNombre());
            valuesd.put("marca", producto.getMarca());
            valuesd.put("seccion", idSeccion);
            valuesd.put("precioPublico", producto.getPrecioPublico());
            valuesd.put("precioNeto", producto.getPrecioNeto());
            valuesd.put("descripcion", producto.getDescripcion());
            valuesd.put("vendidos", producto.getVendidos());
            valuesd.put("stock", producto.getStock());
            valuesd.put("ultimo_pedido", producto.getUltimoPedido());
            long idProducto = db.insert("productos", null, valuesd);
            if (idProducto==-1){
                throw new Exception("Error al agregar producto");
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("Clover_App", "Error en agregar producto: "+ e.getMessage());
        }finally {
            db.endTransaction();
            db.close();
        }
    }
    public String getSeccion(int id) {
        String nombre = "";
        String sql ="SELECT nombre FROM secciones WHERE id_seccion="+id;
        try (Cursor cursor = db.rawQuery(sql, null)) {
            nombre = cursor.getString(0);
            return nombre;
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: "+e.getMessage());
        }
        return nombre;
    }
    public ArrayList<String> getSeccione() {
        ArrayList<String> secciones = new ArrayList<>();
        String sql = "SELECT nombre_seccion FROM secciones";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                secciones.add(cursor.getString(0));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: " + e.getMessage());
        }
        return secciones;
    }
    @Override
    public Productos getProductoCode(String id) {
        Productos producto = new Productos();
        String sql = "SELECT p.*, s.nombre_seccion FROM productos p INNER JOIN secciones s ON p.seccion=s.id_seccion WHERE p.id_producto='" + id.toLowerCase().trim()+"'";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                producto.setRutaImagen(cursor.getString(0));
                producto.setId(cursor.getString(1));
                producto.setNombre(cursor.getString(2));
                producto.setMarca(cursor.getString(3));
                producto.setSeccion(cursor.getString(4));
                producto.setPrecioPublico(cursor.getInt(5));
                producto.setPrecioNeto(cursor.getInt(6));
                producto.setDescripcion(cursor.getString(7));
                producto.setVendidos(cursor.getInt(8));
                producto.setStock(cursor.getInt(9));
                producto.setUltimoPedido(cursor.getString(10));
                //return producto;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getProductoCode: "+e.getMessage());
        }
        Log.e("Clover_App", "getProductoCode: "+producto.toString());
        return producto;
    }
    private Cursor rawQueryGetProductos(String seccion, String columnaObtencion, String busqueda){
        if (busqueda==null) busqueda="";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.*, s.nombre_seccion ");
        sql.append("FROM productos p ");
        sql.append("INNER JOIN secciones s ON p.seccion=s.id_seccion ");
        ArrayList<String> arrgs = new ArrayList<>();
        boolean all = false;

        if (!seccion.equals("Todas")) {
            sql.append("WHERE ");
            sql.append("s.nombre_seccion= ? ");
            arrgs.add(seccion);
            all = true;
        }
        //Busca en la seccion seleccionada
        if (!busqueda.isEmpty()) {
            if (all)
                sql.append("AND ");
            else
                sql.append("WHERE ");
            sql.append(columnaObtencion).append(" LIKE ?");
            arrgs.add("%"+busqueda+"%");
        }
        sql.append(" ORDER BY p.nombre_producto ASC");
        return db.rawQuery(sql.toString(), arrgs.toArray(new String[0]));
    }
    @Override
    public ArrayList<Productos> buscarProductosPor(String seccion, String columnaObtencion, String busqueda) {
        ArrayList<Productos> productos = new ArrayList<>();
        try (Cursor cursor = rawQueryGetProductos(seccion, columnaObtencion, busqueda)) {
            while (cursor.moveToNext()) {
                Productos producto = new Productos();
                producto.setRutaImagen(cursor.getString(0));
                producto.setId(cursor.getString(1));
                producto.setNombre(cursor.getString(2));
                producto.setMarca(cursor.getString(3));
                producto.setSeccion(cursor.getString(11));
                producto.setPrecioPublico(cursor.getInt(5));
                producto.setPrecioNeto(cursor.getInt(6));
                producto.setDescripcion(cursor.getString(7));
                producto.setVendidos(cursor.getInt(8));
                producto.setStock(cursor.getInt(9));
                producto.setUltimoPedido(cursor.getString(10));
                productos.add(producto);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getProducto por: "+e.getMessage());
        }
        return productos;
    }

    @Override
    public ArrayList<Productos> getProductos() {
        ArrayList<Productos> productos = new ArrayList<>();
        String sql = "SELECT p.*, s.nombre_seccion FROM productos p INNER JOIN secciones s ON p.seccion=s.id_seccion ORDER BY p.nombre_producto ASC";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while(cursor.moveToNext()) {
                Productos producto = new Productos();
                producto.setRutaImagen(cursor.getString(0));
                producto.setId(cursor.getString(1));
                producto.setNombre(cursor.getString(2));
                producto.setMarca(cursor.getString(3));
                producto.setSeccion(cursor.getString(11));
                producto.setPrecioPublico(cursor.getInt(5));
                producto.setPrecioNeto(cursor.getInt(6));
                producto.setDescripcion(cursor.getString(7));
                producto.setVendidos(cursor.getInt(8));
                producto.setStock(cursor.getInt(9));
                producto.setUltimoPedido(cursor.getString(10));
                productos.add(producto);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "getProductos: "+e.getMessage());
        }
        return productos;
    }

    @Override
    public void deleteProducto(Productos producto) {
        String sql = "DELETE FROM productos WHERE id_producto='"+producto.getId()+"'";
        try {
            db.execSQL(sql);
            File archivo = new File(producto.getRutaImagen());
            if (archivo.exists()) archivo.delete();
        }catch (SQLException e){
            Log.e("Clover_App", "Error en eliminar producto: "+ e.getMessage());
        }finally {
            db.close();
        }
    }

    @Override
    public void updateProducto(Productos old, Productos newProducto) {
        db.beginTransaction();
        try {
            long idSeccion = -1;
            String sql = "SELECT id_seccion FROM secciones WHERE nombre_seccion=?";
            Cursor cursor = db.rawQuery(sql, new String[]{newProducto.getSeccion()});
            if (cursor.moveToFirst()) {
                idSeccion = cursor.getInt(0);
            }
            cursor.close();
            if (idSeccion==-1){
                ContentValues values = new ContentValues();
                values.put("nombre_seccion", newProducto.getSeccion().toLowerCase().trim());
                idSeccion = db.insert("secciones", null, values);
            }
            ContentValues valuesd = new ContentValues();
            if (newProducto.getRutaImagen()!=null)
                valuesd.put("rutaImagen",newProducto.getRutaImagen());
            valuesd.put("id_producto",newProducto.getId());
            valuesd.put("nombre_producto",newProducto.getNombre());
            valuesd.put("marca",newProducto.getMarca());
            valuesd.put("seccion",idSeccion);
            valuesd.put("precioPublico",newProducto.getPrecioPublico());
            valuesd.put("precioNeto",newProducto.getPrecioNeto());
            valuesd.put("descripcion",newProducto.getDescripcion());
            valuesd.put("vendidos",newProducto.getVendidos());
            valuesd.put("stock",newProducto.getStock());
            valuesd.put("ultimo_pedido",newProducto.getUltimoPedido());
            int filas = db.update("productos", valuesd, "id_producto=?", new String[]{old.getId()});
            db.setTransactionSuccessful();
            if (filas>0){
                if (!newProducto.getRutaImagen().equals(old.getRutaImagen())){
                    try{
                        File archivo = new File(old.getRutaImagen());
                        if (archivo.exists()) {
                            archivo.delete();
                        }
                    }catch (Exception e){
                        Log.e("Clover_App", "Error en eliminar imagen: "+e.getMessage());
                    }
                }
            }
        }catch (Exception e){
            Log.e("Clover_App", "Error en actualizar producto: "+e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }

    }


    @Override
    public boolean updateStock(int unidades, String id) {
        String sql = "UPDATE productos SET stock= stock + ? WHERE id_producto=?";
        try {
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, unidades);
            statement.bindString(2, id);
            int files = statement.executeUpdateDelete();
            if (files==0) return false;
        } catch (SQLException e) {
            Log.e("Clover_App", "Error en actualizar stock: "+e.getMessage());
            return false;
        }
        return true;
    }
    //Cerrar la base de datos
    public void cerrarConexion() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
