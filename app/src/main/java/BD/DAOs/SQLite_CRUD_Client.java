package BD.DAOs;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import BD.Controller.ControllerClient;
import Entidades.Clientes;

public class SQLite_CRUD_Client extends SQLiteOpenHelper implements ControllerClient {
    Context context;

    public SQLite_CRUD_Client(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }
    private void creatBD(SQLiteDatabase BD){
        String sql = "create table clientes("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT,"+
                "apodo TEXT,"+
                "id_compras INTEGER,"+
                "direccion TEXT,"+
                "telefono TEXT)";
        BD.execSQL(sql);
    }
    @Override
    public void addClient(int id, String nombre, String apodo, int id_compras, String direccion, String telefono) {
        String sql = "insert into clientes (id,nombre, apodo, id_compras, direccion, telefono) VALUES ("+id+",";

    }

    @Override
    public Clientes getClient(int id) {
        String sql = "SELECT * FROM clientes WHERE id="+id;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToNext()) {
                Clientes cliente = new Clientes();
                cliente.setId(cursor.getInt(0));
                cliente.setNombre(cursor.getString(1));
                cliente.setApodo(cursor.getString(2));
                cliente.setId_compras(cursor.getInt(3));
                cliente.setDireccion(cursor.getString(4));
                cliente.setTelefono(cursor.getString(5));
                return cliente;
            } else
                return null;
        } catch (SQLException e) {
            Log.d("TAG", "Error en getClient by id: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Clientes getClient(String nombre) {
        String sql = "SELECT * FROM clientes WHERE nombre='"+nombre+"''";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                Clientes cliente = new Clientes();
                cliente.setId(cursor.getInt(0));
                cliente.setNombre(cursor.getString(1));
                cliente.setApodo(cursor.getString(2));
                cliente.setId_compras(cursor.getInt(3));
                cliente.setDireccion(cursor.getString(4));
                cliente.setTelefono(cursor.getString(5));
                return cliente;
            } else
                return null;
        }catch (SQLException e) {
            Log.d("TAG","Error en getClient by nombre: "+e.getMessage());
            throw e;
        }finally {
            cursor.close();
        }
    }

    @Override
    public ArrayList getClients() {
        String sql = "SELECT * FROM clientes";
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            ArrayList<Clientes> clientes = new ArrayList<>();
            while (cursor.moveToNext()) {
                Clientes cliente = new Clientes();
                cliente.setId(cursor.getInt(0));
                cliente.setNombre(cursor.getString(1));
                cliente.setApodo(cursor.getString(2));
                cliente.setId_compras(cursor.getInt(3));
                cliente.setDireccion(cursor.getString(4));
                cliente.setTelefono(cursor.getString(5));
                clientes.add(cliente);
            }
            return clientes;
        } catch (SQLException e) {
            Log.d("TAG", "Error en getClients Array: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteClient(int id) {
        String sql = "DELETE * FROM clientes WHERE id="+id;
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
    }

    @Override
    public void updateClient(Clientes oldClient, Clientes newClient) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
