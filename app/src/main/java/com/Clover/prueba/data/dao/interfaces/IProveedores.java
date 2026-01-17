package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Proveedor;

import java.util.ArrayList;

public interface IProveedores {
    public boolean addProveedor(Proveedor proveedor);
    public boolean updateProveedor(Proveedor proveedorOld, Proveedor proveedor);
    public boolean deleteProveedor(int id_proveedor);
    public Proveedor getProveedor(int id_proveedor);
    public int getIdProveedor(String nombre_proveedor);
    public ArrayList<Proveedor> getProveedor(String columnBusqueda, String busqueda);

}
