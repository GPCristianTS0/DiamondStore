package BD.DAOs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import BD.Controller.ControllerClient;
import Entidades.Clientes;

public class ClientesDAO implements ControllerClient {
    private SQLiteDatabase db;
    public ClientesDAO(Context context){
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public void addClient(int id, String nombre, String apodo, int id_compras, String direccion, String telefono){
        String sql = "INSERT INTO ";
    }

    @Override
    public Clientes getClient(int id) {
        return null;
    }

    @Override
    public Clientes getClient(String nombre) {
        return null;
    }

    @Override
    public ArrayList getClients() {
        return null;
    }

    @Override
    public void deleteClient(int id) {

    }

    @Override
    public void updateClient(Clientes oldClient, Clientes newClient) {

    }
}
