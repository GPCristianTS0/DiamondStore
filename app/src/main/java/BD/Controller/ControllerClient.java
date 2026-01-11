package BD.Controller;

import java.util.ArrayList;

import Entidades.Clientes;

public interface ControllerClient {
    public boolean addClient(Clientes cliente);
    public ArrayList<Clientes> getClient(String filtro, String valor, boolean deudores);
    public Clientes getClient(String nombre);
    public ArrayList<Clientes> getClients();
    public boolean deleteClient(Clientes cliente);
    public boolean updateClient(Clientes oldClient, Clientes newClient);
}
