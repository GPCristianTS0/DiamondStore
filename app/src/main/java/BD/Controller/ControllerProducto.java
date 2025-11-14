package BD.Controller;

import java.util.ArrayList;

import Entidades.Productos;

public interface ControllerProducto {

    public void createTable(String name);
    public void addProducto(Productos producto);
    public Productos getProductoCode(String id);
    public Productos getProducto(String nombre);
    public ArrayList<String> getSecciones();
    public ArrayList<Productos> getProductos();
    public void deleteProducto(Productos producto);
    public void updateProducto(Productos old, Productos newProducto);
    public void updateStock(int unidades, Productos producto);
}
