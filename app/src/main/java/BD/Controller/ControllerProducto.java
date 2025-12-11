package BD.Controller;

import java.util.ArrayList;

import Entidades.Productos;

public interface ControllerProducto {

    public void createTabl(String name);
    public void addProducto(Productos producto);
    public Productos getProductoCode(String id);
    public String getSeccion(int id);

    public ArrayList<String> getSeccione();
    public ArrayList<Productos> buscarProductosPor(String seccion, String columnaObtencion, String busqueda);
    public ArrayList<Productos> getProductos();
    public void deleteProducto(Productos producto);
    public void updateProducto(Productos old, Productos newProducto);
    public void updateStock(int unidades, Productos producto);
}
