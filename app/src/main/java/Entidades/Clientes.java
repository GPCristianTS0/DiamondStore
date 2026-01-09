package Entidades;

public class Clientes {
     int id_cliente;
    private String nombre_cliente;
    private String apodo;
    private String direccion;
    private String telefono;
    private int puntos;
    private int saldo;

    public Clientes() {

    }
    public Clientes(int id, String nombre, String apodo, int saldo, String telefono){
        this.id_cliente = id;
        this.nombre_cliente = nombre;
        this.apodo = apodo;
        this.saldo = saldo;
        this.telefono = telefono;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
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

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String[] getColumn(){
        return new String[]{"ID", "Nombre", "Apodo", "Saldo", "Puntos"};
    }

    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id_cliente +
                ", nombre='" + nombre_cliente + '\'' +
                ", apodo='" + apodo + '\'' +
                ", saldo=" + saldo +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}
