package com.Clover.prueba.data.dao;

import static com.Clover.prueba.utils.Constantes.VENTA_PENDIENTE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Clientes;

public class ClientesDAO implements IClient {
    private SQLiteDatabase db;
    public ClientesDAO(Context context){
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public boolean addClient(Clientes cliente){
        try {
            ContentValues values = new ContentValues();
            values.put("id_cliente", cliente.getId_cliente());
            values.put("nombre_cliente", cliente.getNombre_cliente());
            values.put("apodo", cliente.getApodo());
            values.put("saldo", cliente.getSaldo());
            values.put("direccion", cliente.getDireccion());
            values.put("puntos", cliente.getPuntos());
            values.put("fecha_registro", cliente.getFecha_registro());
            long a = db.insert("clientes", null, values);
            return a != -1;
        }catch (Exception e){
            Log.e("Clover_App", "addClient: "+e.getMessage());
            return false;
        }

    }
    @Override
    public ArrayList<Clientes> getClient(String filtro, String valor, boolean deudores) {
        ArrayList<Clientes> clientes = new ArrayList<>();
        try {
            Cursor cursor = rawQueryGetClientes(filtro, valor, deudores);
            while (cursor.moveToNext()) {
                Clientes cliente = new Clientes();
                cliente.setId_cliente(cursor.getString(0));
                cliente.setNombre_cliente(cursor.getString(1));
                cliente.setApodo(cursor.getString(2));
                cliente.setDireccion(cursor.getString(3));
                cliente.setFecha_registro(cursor.getString(4));
                cliente.setSaldo(cursor.getInt(5));
                cliente.setPuntos(cursor.getInt(6));
                clientes.add(cliente);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Clover_App", "getClient: "+e.getMessage());
        }
        return clientes;
    }
    private Cursor rawQueryGetClientes(String filtro, String valor, boolean deudores){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM clientes ");
        ArrayList<String> arrgs = new ArrayList<>();
        boolean all = false;
        if (deudores) {
            sql.append("WHERE saldo > 0 ");
            all = true;
        }
        if (!valor.isEmpty()){
            if (all)
                sql.append("AND ");
            else
                sql.append("WHERE ");
            sql.append(filtro);
            if (filtro.equals("saldo")||filtro.equals("puntos")) {
                sql.append(" = ? ");
                arrgs.add(valor);
            }else {
                sql.append(" LIKE ? ");
                arrgs.add("%"+valor+"%");
            }
        }
        return db.rawQuery(sql.toString(), arrgs.toArray(new String[0]));
    }
    @Override
    public Clientes getClient(String idCliente) {
        Clientes clientee = new Clientes();
        String query = "SELECT * FROM clientes WHERE id_cliente = ?";
        String[] args = {String.valueOf(idCliente)};
        try (Cursor cursor = db.rawQuery(query, args)){
            if (cursor.moveToFirst()) {
                clientee.setId_cliente(cursor.getString(0));
                clientee.setNombre_cliente(cursor.getString(1));
                clientee.setApodo(cursor.getString(2));
                clientee.setDireccion(cursor.getString(3));
                clientee.setFecha_registro(cursor.getString(4));
                clientee.setSaldo(cursor.getInt(5));
                clientee.setPuntos(cursor.getInt(6));
                return clientee;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "getClient: "+e.getMessage());
        }
        return null;
    }
    @Override
    public ArrayList<Clientes> getDeudores(){
        ArrayList<Clientes> clientes = new ArrayList<>();
        String query = "SELECT cl.*, " +
                "(SELECT MIN(fecha_limite) FROM ventas WHERE id_cliente = cl.id_cliente AND estado = '"+VENTA_PENDIENTE+"') AS fecha_limiteCl, " +
                "(SELECT MAX(fecha_hora) FROM abonos WHERE id_cliente = cl.id_cliente) AS ultimo_abono FROM clientes cl " +
                "WHERE cl.saldo > 0";
        try (Cursor cursor = db.rawQuery(query, null)){
            while (cursor.moveToNext()){
                Clientes cliente = new Clientes();
                cliente.setId_cliente(cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));
                cliente.setNombre_cliente(cursor.getString(cursor.getColumnIndexOrThrow("nombre_cliente")));
                cliente.setApodo(cursor.getString(cursor.getColumnIndexOrThrow("apodo")));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
                cliente.setFecha_registro(cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro")));
                cliente.setSaldo(cursor.getInt(cursor.getColumnIndexOrThrow("saldo")));
                cliente.setPuntos(cursor.getInt(cursor.getColumnIndexOrThrow("puntos")));
                cliente.setFechaLimite(cursor.getString(cursor.getColumnIndexOrThrow("fecha_limiteCl")));
                cliente.setUltimoAbono(cursor.getString(cursor.getColumnIndexOrThrow("ultimo_abono")));
                clientes.add(cliente);
            }
            return clientes;
        } catch (Exception e) {
            Log.e("Clover_App", "getDeudores: "+e.getMessage());
        }
        return clientes;
    }
    @Override
    public ArrayList<Clientes> getClients() {
        ArrayList<Clientes> clientes = new ArrayList<>();
        try {
            String query = "SELECT * FROM clientes";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){
                Clientes cliente = new Clientes();
                cliente.setId_cliente(cursor.getString(0));
                cliente.setNombre_cliente(cursor.getString(1));
                cliente.setApodo(cursor.getString(2));
                cliente.setDireccion(cursor.getString(3));
                cliente.setFecha_registro(cursor.getString(4));
                cliente.setSaldo(cursor.getInt(5));
                cliente.setPuntos(cursor.getInt(6));
                clientes.add(cliente);
            }
            return clientes;
        } catch (Exception e) {
            Log.e("Clover_App", "getClients: "+e.getMessage());
        }
        return clientes;
    }

    @Override
    public double getSaldoTotal() {
        String sql = "SELECT SUM(saldo) FROM clientes";
        try (Cursor cursor = db.rawQuery(sql, null)){
            if (cursor.moveToFirst()){
                return cursor.getDouble(0);

            }
        } catch (Exception e) {
            Log.e("Clover_App", "getSaldoTotal: "+e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean deleteClient(Clientes cliente) {
        try {
            int filas = db.delete("clientes", "id_cliente=?", new String[]{cliente.getId_cliente()});
            if (filas!=0) return true;
        }catch (Exception e){
            Log.e("Clover_App", "deleteClient: "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateClient(Clientes oldClient, Clientes newClient) {
        try{
            ContentValues values = new ContentValues();
            values.put("id_cliente", newClient.getId_cliente());
            values.put("nombre_cliente", newClient.getNombre_cliente());
            values.put("apodo", newClient.getApodo());
            values.put("saldo", newClient.getSaldo());
            values.put("direccion", newClient.getDireccion());
            values.put("puntos", newClient.getPuntos());
            int filas = db.update("clientes", values, "id_cliente=?", new String[]{oldClient.getId_cliente()});
            if (filas!=0) return true;
        } catch (Exception e){
            Log.e("Clover_App", "updateClient: "+e.getMessage());
        }
        return false;
    }
}
