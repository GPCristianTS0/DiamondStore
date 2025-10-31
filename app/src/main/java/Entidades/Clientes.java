package Entidades;

public class Clientes {
     int id;
    private String nombre;
    private String apodo;
    private int id_compras;
    private String direccion;
    private String telefono;

    public Clientes() {

    }
    public Clientes(int id, String nombre, String apodo, int id_compras, String telefono){
        this.id = id;
        this.nombre = nombre;
        this.apodo = apodo;
        this.id_compras = id_compras;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_compras() {
        return id_compras;
    }

    public void setId_compras(int id_compras) {
        this.id_compras = id_compras;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apodo='" + apodo + '\'' +
                ", id_compras=" + id_compras +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}
