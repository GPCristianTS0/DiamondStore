package com.Clover.prueba.data.dao.interfaces;

import java.util.ArrayList;

import com.Clover.prueba.data.models.Clientes;

public interface IClient {
    public boolean addClient(Clientes cliente);
    public ArrayList<Clientes> getClient(String filtro, String valor, boolean deudores);
    public Clientes getClient(String idCliente);
    public ArrayList<Clientes> getClients();
    public boolean deleteClient(Clientes cliente);
    public boolean updateClient(Clientes oldClient, Clientes newClient);
}
