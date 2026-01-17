package com.Clover.prueba.data.dao.interfaces;

import java.util.ArrayList;

import com.Clover.prueba.data.models.Productos;

public interface IProducto {

    public void createTabl(String name);
    public void addProducto(Productos producto);
    public Productos getProductoCode(String id);
    public String getSeccion(int id);
    public int addSeccion(String nombre);
    public ArrayList<String> getSeccione();
    public ArrayList<Productos> buscarProductosPor(String seccion, String columnaObtencion, String busqueda);
    public ArrayList<Productos> getProductos();
    public void deleteProducto(Productos producto);
    public void updateProducto(Productos old, Productos newProducto);
    public boolean updateStock(int unidades, String producto);
    public int getStockBajo();
}
