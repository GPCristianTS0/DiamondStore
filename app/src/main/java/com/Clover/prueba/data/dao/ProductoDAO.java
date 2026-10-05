package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Productos;

public class ProductoDAO implements IProducto {

    private SQLiteDatabase db;

    public ProductoDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public void createTabl(String name) {
    }

    @Override
    public int addSeccion(String nombre) {
        ContentValues values = new ContentValues();
        values.put("nombre_seccion", nombre);
        values.put("fecha_actualizacion", System.currentTimeMillis());
        values.put("sincronizado", 0);
        values.put("eliminado", 0);
        return (int) db.insert("secciones", null, values);
    }

    @Override
    public void addProducto(Productos producto) {
        String sql = "SELECT id_seccion FROM secciones WHERE nombre_seccion = ? AND eliminado = 0";
        long idSeccion = -1;
        db.beginTransaction();
        try {
            try (Cursor cursor = db.rawQuery(sql, new String[]{producto.getSeccion()})) {
                if (cursor.moveToFirst()) {
                    idSeccion = cursor.getInt(0);
                }
            }

            ContentValues valuesd = new ContentValues();
            valuesd.put("rutaImagen", producto.getRutaImagen());
            valuesd.put("id_producto", producto.getId().toUpperCase());
            valuesd.put("nombre_producto", producto.getNombre());
            valuesd.put("marca", producto.getMarca());
            valuesd.put("modelo", producto.getModelo());
            valuesd.put("seccion", idSeccion);
            valuesd.put("precioPublico", producto.getPrecioPublico());
            valuesd.put("precioNeto", producto.getPrecioNeto());
            valuesd.put("descripcion", producto.getDescripcion());
            valuesd.put("vendidos", producto.getVendidos());
            valuesd.put("stock", producto.getStock());
            valuesd.put("ventaxpeso", producto.getVentaxpeso());
            valuesd.put("ultimo_pedido", producto.getUltimoPedido());
            valuesd.put("isRefaccion", producto.isRefaccion() ? 1 : 0);
            valuesd.put("calidad", producto.getCalidad());
            valuesd.put("modelo_compatible", producto.getModelosCompatible());
            valuesd.put("condicion", producto.getCondicion());
            valuesd.put("grado_estetico", producto.getEstado());
            valuesd.put("visible_en_busqueda", producto.isVisible() ? 1 : 0);
            valuesd.put("fecha_actualizacion", System.currentTimeMillis());
            valuesd.put("sincronizado", 0);
            valuesd.put("eliminado", 0);

            long idProducto = db.insertWithOnConflict("productos", null, valuesd, SQLiteDatabase.CONFLICT_REPLACE);
            if (idProducto == -1){
                throw new Exception("Error al agregar producto");
            }
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e("Clover_App", "Error en agregar producto: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public String getSeccion(int id) {
        String nombre = "";
        String sql = "SELECT nombre_seccion FROM secciones WHERE id_seccion = ? AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                nombre = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: " + e.getMessage());
        }
        return nombre;
    }

    public ArrayList<String> getSeccione() {
        ArrayList<String> secciones = new ArrayList<>();
        String sql = "SELECT nombre_seccion FROM secciones WHERE eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                secciones.add(cursor.getString(cursor.getColumnIndexOrThrow("nombre_seccion")));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getSecciones: " + e.getMessage());
        }
        return secciones;
    }

    @Override
    public Productos getProductoCode(String id) {
        Productos producto = new Productos();
        String sql = "SELECT p.*, s.nombre_seccion AS seccionNombre, s.id_seccion " +
                "FROM productos p INNER JOIN secciones s ON p.seccion = s.id_seccion " +
                "WHERE p.id_producto = ? AND p.eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{id.toUpperCase().trim()})) {
            if (cursor.moveToFirst()) {
                producto = getProductBD(cursor);
                producto.setId_seccion(cursor.getInt(cursor.getColumnIndexOrThrow("id_seccion")));
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getProductoCode: " + e.getMessage());
        }
        return producto;
    }

    private Productos getProductBD(Cursor cursor) {
        Productos producto = new Productos();
        producto.setRutaImagen(cursor.getString(cursor.getColumnIndexOrThrow("rutaImagen")));
        producto.setId(cursor.getString(cursor.getColumnIndexOrThrow("id_producto")));
        producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
        producto.setMarca(cursor.getString(cursor.getColumnIndexOrThrow("marca")));
        producto.setSeccion(cursor.getString(cursor.getColumnIndexOrThrow("seccionNombre")));
        producto.setPrecioPublico(cursor.getInt(cursor.getColumnIndexOrThrow("precioPublico")));
        producto.setPrecioNeto(cursor.getInt(cursor.getColumnIndexOrThrow("precioNeto")));
        producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
        producto.setVendidos(cursor.getInt(cursor.getColumnIndexOrThrow("vendidos")));
        producto.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("stock")));
        producto.setUltimoPedido(cursor.getString(cursor.getColumnIndexOrThrow("ultimo_pedido")));
        producto.setVentaxpeso(cursor.getInt(cursor.getColumnIndexOrThrow("ventaxpeso")));
        producto.setRefaccion(cursor.getInt(cursor.getColumnIndexOrThrow("isRefaccion")) == 1);
        producto.setModelosCompatible(cursor.getString(cursor.getColumnIndexOrThrow("modelo_compatible")));
        producto.setCalidad(cursor.getString(cursor.getColumnIndexOrThrow("calidad")));
        producto.setModelo(cursor.getString(cursor.getColumnIndexOrThrow("modelo")));
        producto.setVisible(cursor.getInt(cursor.getColumnIndexOrThrow("visible_en_busqueda")) == 1);
        producto.setCondicion(cursor.getString(cursor.getColumnIndexOrThrow("condicion")));
        producto.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("grado_estetico")));
        return producto;
    }

    private Cursor rawQueryGetProductos(int seccion, String columnaObtencion, String busqueda){
        if (busqueda == null) busqueda = "";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.*, s.nombre_seccion AS seccionNombre, s.id_seccion FROM productos p " +
                "LEFT JOIN secciones s ON p.seccion = s.id_seccion WHERE p.eliminado = 0 ");
        ArrayList<String> arrgs = new ArrayList<>();

        if (seccion != 0) {
            sql.append("AND p.seccion = ? ");
            arrgs.add(String.valueOf(seccion));
        }

        if (!busqueda.isEmpty()) {
            sql.append("AND (").append(columnaObtencion).append(" LIKE ?");
            arrgs.add("%" + busqueda + "%");
            if (columnaObtencion.equals("modelo")) {
                sql.append(" OR p.modelo_compatible LIKE ?");
                arrgs.add("%" + busqueda + "%");
            }
            sql.append(") ");
        }
        sql.append("ORDER BY seccionNombre ASC");
        return db.rawQuery(sql.toString(), arrgs.toArray(new String[0]));
    }

    @Override
    public ArrayList<Productos> buscarProductosPor(int seccion, String columnaObtencion, String busqueda) {
        ArrayList<Productos> productos = new ArrayList<>();
        try (Cursor cursor = rawQueryGetProductos(seccion, columnaObtencion, busqueda)) {
            while (cursor.moveToNext()) {
                Productos producto = getProductBD(cursor);
                producto.setId_seccion(cursor.getInt(cursor.getColumnIndexOrThrow("id_seccion")));
                productos.add(producto);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "En getProducto por: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public ArrayList<Productos> getProductos() {
        ArrayList<Productos> productos = new ArrayList<>();
        String sql = "SELECT *, (SELECT nombre_seccion FROM secciones WHERE id_seccion = seccion) AS seccionNombre " +
                "FROM productos WHERE eliminado = 0 ORDER BY seccionNombre ASC";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            return getByCursor(cursor);
        } catch (Exception e) {
            Log.e("Clover_App", "getProductos: " + e.getMessage());
        }
        return productos;
    }

    private ArrayList<Productos> getByCursor(Cursor cursor) {
        try {
            ArrayList<Productos> productos = new ArrayList<>();
            while (cursor.moveToNext()) {
                productos.add(getProductBD(cursor));
            }
            return productos;
        } catch (Exception e) {
            Log.e("Clover_App", "En getByCursor: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteProducto(Productos producto) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("eliminado", 1);
            cv.put("visible_en_busqueda", 0);
            cv.put("fecha_actualizacion", System.currentTimeMillis());
            cv.put("sincronizado", 0);

            int filasAfectadas = db.update("productos", cv, "id_producto = ?", new String[]{producto.getId()});

            if (filasAfectadas > 0 && producto.getRutaImagen() != null && !producto.getRutaImagen().isEmpty()) {
                File archivo = new File(producto.getRutaImagen());
                if (archivo.exists()) {
                    boolean borrado = archivo.delete();
                    if (!borrado) {
                        Log.w("Clover_App", "No se pudo borrar el archivo de imagen: " + producto.getRutaImagen());
                    }
                }
            }
        } catch (SQLException e){
            Log.e("Clover_App", "Error en eliminar producto: " + e.getMessage());
        }
    }

    @Override
    public void updateProducto(Productos old, Productos newProducto) {
        db.beginTransaction();
        try {
            long idSeccion = -1;
            String sql = "SELECT id_seccion FROM secciones WHERE nombre_seccion = ? AND eliminado = 0";
            try (Cursor cursor = db.rawQuery(sql, new String[]{newProducto.getSeccion()})) {
                if (cursor.moveToFirst()) {
                    idSeccion = cursor.getInt(0);
                }
            }

            if (idSeccion == -1){
                ContentValues values = new ContentValues();
                values.put("nombre_seccion", newProducto.getSeccion().toLowerCase().trim());
                values.put("fecha_actualizacion", System.currentTimeMillis());
                values.put("sincronizado", 0);
                values.put("eliminado", 0);
                idSeccion = db.insert("secciones", null, values);
            }

            ContentValues valuesd = new ContentValues();
            if (newProducto.getRutaImagen() != null)
                valuesd.put("rutaImagen", newProducto.getRutaImagen());
            valuesd.put("id_producto", newProducto.getId());
            valuesd.put("nombre_producto", newProducto.getNombre());
            valuesd.put("marca", newProducto.getMarca());
            valuesd.put("seccion", idSeccion);
            valuesd.put("precioPublico", newProducto.getPrecioPublico());
            valuesd.put("precioNeto", newProducto.getPrecioNeto());
            valuesd.put("descripcion", newProducto.getDescripcion());
            valuesd.put("vendidos", newProducto.getVendidos());
            valuesd.put("stock", newProducto.getStock());
            valuesd.put("ventaxpeso", newProducto.getVentaxpeso());
            valuesd.put("ultimo_pedido", newProducto.getUltimoPedido());
            valuesd.put("modelo", newProducto.getModelo());
            valuesd.put("modelo_compatible", newProducto.getModelosCompatible());
            valuesd.put("calidad", newProducto.getCalidad());
            valuesd.put("isRefaccion", newProducto.isRefaccion() ? 1 : 0);
            valuesd.put("condicion", newProducto.getCondicion());
            valuesd.put("grado_estetico", newProducto.getEstado());
            valuesd.put("visible_en_busqueda", newProducto.isVisible() ? 1 : 0);
            valuesd.put("fecha_actualizacion", System.currentTimeMillis());
            valuesd.put("sincronizado", 0);

            int filas = db.update("productos", valuesd, "id_producto = ?", new String[]{old.getId()});
            db.setTransactionSuccessful();

            if (filas > 0 && newProducto.getRutaImagen() != null){
                if (!newProducto.getRutaImagen().equals(old.getRutaImagen())){
                    try {
                        File archivo = new File(old.getRutaImagen());
                        if (archivo.exists()) {
                            archivo.delete();
                        }
                    } catch (Exception e){
                        Log.e("Clover_App", "Error en eliminar imagen: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e){
            Log.e("Clover_App", "Error en actualizar producto: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean updateStock(int unidades, String id) {
        String sql = "UPDATE productos SET stock = stock + ?, fecha_actualizacion = ?, sincronizado = 0 WHERE id_producto = ?";
        try {
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, unidades);
            statement.bindLong(2, System.currentTimeMillis());
            statement.bindString(3, id);
            int files = statement.executeUpdateDelete();
            if (files == 0) return false;
        } catch (SQLException e) {
            Log.e("Clover_App", "Error en actualizar stock: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int getStockBajo(int stockMinimo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE stock < ? AND eliminado = 0";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(stockMinimo)})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getStockBajo: " + e.getMessage());
        }
        return 0;
    }
}