package com.Clover.prueba.data.dao;

import static com.Clover.prueba.utils.Constantes.VENTA_PENDIENTE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.dto.ProductoMasCompradoDTO;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

public class VentasDAO implements IVentas {

    private SQLiteDatabase db;
    public VentasDAO(Context context) {
         db = CloverBD.getInstance(context).getWritableDatabase();
    }
    @Override
    public long addVenta(Ventas venta, ArrayList<DetalleVenta> detalleventa) {
        db.beginTransaction();
        try {
            ContentValues valuesVentas = new ContentValues();
            valuesVentas.put("id_cliente", venta.getId_cliente());
            valuesVentas.put("fecha_Hora", venta.getFecha_hora());
            valuesVentas.put("monto", venta.getMonto());
            valuesVentas.put("monto_pendiente", venta.getMonto_pendiente());
            valuesVentas.put("total_piezas", venta.getTotal_piezas());
            valuesVentas.put("tipo_pago", venta.getTipo_pago());
            valuesVentas.put("pago_con", venta.getPago_con());
            valuesVentas.put("id_corte", venta.getId_corte());
            valuesVentas.put("banco_tarjeta", venta.getBanco_tarjeta());
            valuesVentas.put("numero_tarjeta", venta.getNumero_tarjeta());
            valuesVentas.put("numero_aprobacion", venta.getNumero_aprobacion());
            valuesVentas.put("tipo_tarjeta", venta.getTipo_tarjeta());
            valuesVentas.put("dias_plazo", venta.getDias_plazo());
            valuesVentas.put("frecuencia_pago", venta.getFrecuencia_pago());
            valuesVentas.put("estado", venta.getEstado());
            valuesVentas.put("fecha_limite", venta.getFecha_limite());
            //Ventas insersion
            long id_venta = db.insert("ventas", null, valuesVentas);

            String sqlVendidos = "UPDATE productos SET vendidos = vendidos + ? WHERE id_producto = ?";

            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";

            String sqlCliente = "UPDATE clientes SET saldo = saldo + ? WHERE id_cliente = ?";


            //Detalle venta insersion
            SQLiteStatement stmtStock = db.compileStatement(sqlStock);
            SQLiteStatement stmtVendidos = db.compileStatement(sqlVendidos);
            SQLiteStatement stmtCliente = db.compileStatement(sqlCliente);
            stmtCliente.bindDouble(1, venta.getMonto_pendiente());
            stmtCliente.bindString(2, venta.getId_cliente());
            stmtCliente.executeUpdateDelete();

            for (DetalleVenta detalleventaa : detalleventa) {
                ContentValues valuesDetalleVenta = new ContentValues();
                valuesDetalleVenta.put("id_venta", id_venta);
                valuesDetalleVenta.put("id_producto", detalleventaa.getId_producto());
                valuesDetalleVenta.put("nombre_producto", detalleventaa.getNombre_producto());
                valuesDetalleVenta.put("cantidad", detalleventaa.getCantidad());
                valuesDetalleVenta.put("precio", detalleventaa.getPrecio());
                valuesDetalleVenta.put("precio_neto_historial", detalleventaa.getPrecio_neto());
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
            stmtCliente.close();
            stmtStock.close();
            stmtVendidos.close();
            return id_venta;
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
        String sql = "SELECT * FROM ventas";
        try (Cursor cursor = db.rawQuery(sql, null)){
            return getVentasCursor(cursor);
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentas: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    private Cursor rawQueryGetVentas(String mes, String year, String busqueda){
        if (busqueda==null) busqueda="";
        ArrayList<String> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ventas WHERE 1=1 ");

        if (!busqueda.isEmpty()) {
            sql.append("AND id_venta = ? ");
            args.add(busqueda);
        }else{
            String fecha = year+"-"+mes+"%";
            sql.append(" AND fecha_Hora LIKE ? ");
            args.add(fecha);
        }
        sql.append(" ORDER BY id_venta DESC");
        return db.rawQuery(sql.toString(), args.toArray(new String[0]));
    }
    @Override
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda) {
        try (Cursor cursor = rawQueryGetVentas(mes, year, busqueda)){
            return getVentasCursor(cursor);
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentas Array: " + e.getMessage());
        }
        return null;
    }
    //Ventas que el cliente debe pagar
    @Override
    public ArrayList<Ventas> getVentas(String idCliente) {
        String sql = "SELECT * FROM ventas WHERE id_cliente = ? AND estado = '"+VENTA_PENDIENTE+"'";
        ArrayList<Ventas> ventas = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            return getVentasCursor(cursor);
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentas: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta) {
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
                detalleVenta.setPrecio_neto(cursor.getInt(6));
                detalleVentas.add(detalleVenta);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getDetalleVentas: " + e.getMessage());
        }
        return detalleVentas;
    }

    @Override
    public ArrayList<String> getAnios() {
        ArrayList<String> anios = new ArrayList<>();
        String sql = "SELECT MIN(fecha_Hora) AS fecha_min, MAX(fecha_Hora) AS fecha_max FROM ventas";
        try (Cursor cursor = db.rawQuery(sql, null)){
            if (cursor.moveToFirst()){
                String fechaMinStr = cursor.getString(0);
                String fechaMaxStr = cursor.getString(1);

                if (fechaMinStr != null && fechaMaxStr != null) {
                    int min = Integer.parseInt(fechaMinStr.substring(0, 4));
                    int max = Integer.parseInt(fechaMaxStr.substring(0, 4));

                    for (int i = max; i >= min; i--) {
                        anios.add(String.valueOf(i));
                    }
                }
            }
        } catch (Exception e){
            Log.e("Clover_App", "Error en getAnios: "+e.getMessage());
        }
        return anios;
    }

    @Override
    public int getGanancias() {
        String sql = "SELECT SUM((d.precio - d.precio_neto_historial) * d.cantidad) FROM detalles_venta d" +
                " INNER JOIN ventas v ON d.id_venta = v.id_venta WHERE v.fecha_Hora LIKE ? AND d.precio_neto_historial > 0";
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaHoy+"%"})){
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }catch ( Exception e){
            Log.e( "Clover_App", "Error en getGanancias: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getSaldoTotal() {
        String sql = "SELECT SUM(monto) FROM ventas WHERE fecha_Hora LIKE ?";
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try (Cursor cursor = db.rawQuery(sql, new String[]{fechaHoy+"%"})){
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        return 0;
    }
    @Override
    public double getSaldoTotal(String idCliente) {
        String sql = "SELECT SUM(monto) FROM ventas WHERE id_cliente = ? AND estado = '"+VENTA_PENDIENTE+"'";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: ");
        }
        return -1;
    }
    @Override
    public double getVentasMetodoPago(String metodoPago, int id_corte) {
        String sql = "SELECT SUM(monto) FROM ventas WHERE tipo_pago = ? AND id_corte = ?";
        ArrayList<String> args = new ArrayList<>();
        args.add(metodoPago);
        args.add(String.valueOf(id_corte));
        Log.d("Clover_App", "id_corte: " + id_corte);
        try (Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]))){
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasMetodoPago: " + e.getMessage());
        }
        return 0;
    }
    @Override
    public String getProductoMasVendido() {
        String sql = "SELECT nombre_producto FROM detalles_venta GROUP BY nombre_producto ORDER BY SUM(cantidad) DESC LIMIT 1";
        try (Cursor cursor = db.rawQuery(sql, null)){
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getProductoMasVendido: " + e.getMessage());
        }
        return "";
    }
    @Override
    public ArrayList<ProductoMasCompradoDTO> getMasCompradobyCliente(String idCliente, int noProductos) {
        String sql = "SELECT p.nombre_producto, p.rutaImagen AS ruta, SUM(dv.cantidad) AS cantidad FROM ventas v" +
                " INNER JOIN detalles_venta dv ON v.id_venta = dv.id_venta " +
                " INNER JOIN productos p ON dv.id_producto = p.id_producto " +
                " WHERE v.id_cliente = ? GROUP BY p.nombre_producto, p.rutaImagen, p.id_producto " +
                " ORDER BY cantidad DESC LIMIT ?";
        ArrayList<String> args = new ArrayList<>();
        args.add(idCliente);
        args.add(String.valueOf(noProductos));
        ArrayList<ProductoMasCompradoDTO> masComprados = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]))){
            while (cursor.moveToNext()) {
                ProductoMasCompradoDTO productoMasComprado = new ProductoMasCompradoDTO();
                productoMasComprado.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                productoMasComprado.setRuta(cursor.getString(cursor.getColumnIndexOrThrow("ruta")));
                productoMasComprado.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")));
                masComprados.add(productoMasComprado);
            }
            return masComprados;
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getMasCompradobyCliente: " + e.getMessage());
        }
        return null;
    }
    //Clientes y ventas
    @Override
    public int getVentasTotales(String idCliente) {
        String sql = "SELECT COUNT(*) FROM ventas WHERE id_cliente = ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getVentasTotales: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public double getSaldoPendiente(String idCliente){
        String sql = "SELECT SUM(monto_pendiente) FROM ventas WHERE id_cliente = ? AND estado = '"+VENTA_PENDIENTE+"'";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getSaldoPendiente: " + e.getMessage());
        }
        return -1;
    }
    @Override
    public double getTicketPromedio(String idCliente) {
        String sql = "SELECT AVG(monto) FROM ventas WHERE id_cliente = ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{idCliente})){
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error en getTicketPromedio: " + e.getMessage());
        }
        return -1;
    }


    //Tools
    private LocalDateTime parseStringLocalDateTime(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", new Locale("es", "ES"));
        return LocalDateTime.parse(fecha, formatter);
    }
    private ArrayList<Ventas> getVentasCursor(Cursor cursor) {
        ArrayList<Ventas> ventas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Ventas venta = new Ventas();
            venta.setId_venta(cursor.getInt(cursor.getColumnIndexOrThrow("id_venta")));
            venta.setId_cliente(cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));
            venta.setFecha_hora(cursor.getString(cursor.getColumnIndexOrThrow("fecha_Hora")));
            venta.setMonto(cursor.getInt(cursor.getColumnIndexOrThrow("monto")));
            venta.setMonto_pendiente(cursor.getDouble(cursor.getColumnIndexOrThrow("monto_pendiente")));
            venta.setTotal_piezas(cursor.getInt(cursor.getColumnIndexOrThrow("total_piezas")));
            venta.setTipo_pago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
            venta.setId_corte(cursor.getInt(cursor.getColumnIndexOrThrow("id_corte")));
            venta.setPago_con(cursor.getDouble(cursor.getColumnIndexOrThrow("pago_con")));
            venta.setBanco_tarjeta(cursor.getString(cursor.getColumnIndexOrThrow("banco_tarjeta")));
            venta.setNumero_tarjeta(cursor.getString(cursor.getColumnIndexOrThrow("numero_tarjeta")));
            venta.setNumero_aprobacion(cursor.getString(cursor.getColumnIndexOrThrow("numero_aprobacion")));
            venta.setTipo_tarjeta(cursor.getString(cursor.getColumnIndexOrThrow("tipo_tarjeta")));
            venta.setDias_plazo(cursor.getInt(cursor.getColumnIndexOrThrow("dias_plazo")));
            venta.setFrecuencia_pago(cursor.getString(cursor.getColumnIndexOrThrow("frecuencia_pago")));
            venta.setFecha_limite(cursor.getString(cursor.getColumnIndexOrThrow("fecha_limite")));
            venta.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
            venta.setMonto_pendiente(cursor.getDouble(cursor.getColumnIndexOrThrow("monto_pendiente")));
            ventas.add(venta);
        }
        return ventas;
    }
}
