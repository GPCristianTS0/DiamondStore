package BD.Controller;

import java.util.ArrayList;

import Entidades.Clientes;

public interface ControllerClient {
    public void addClient(int id, String nombre, String apodo, int id_compras, String direccion, String telefono);
    public Clientes getClient(int id);
    public Clientes getClient(String nombre);
    public ArrayList getClients();
    public void deleteClient(int id);
    public void updateClient(Clientes oldClient, Clientes newClient);
}
