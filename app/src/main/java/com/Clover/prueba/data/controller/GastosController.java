package com.Clover.prueba.data.controller;

import android.content.Context;

import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.GastosDAO;
import com.Clover.prueba.data.dao.ProveedoresDAO;
import com.Clover.prueba.data.dao.interfaces.IGastos;
import com.Clover.prueba.data.dao.interfaces.IProveedores;
import com.Clover.prueba.data.models.Gastos;
import com.Clover.prueba.data.models.Proveedor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GastosController {
    private final IGastos gastosDAO;
    private ArrayList<String> nombresProveedores;
    private ArrayList<Proveedor> listaProveedores;
    private final Context context;
    public GastosController(Context context){
        gastosDAO = new GastosDAO(context);
        this.context = context;

    }
    public boolean addGasto(Gastos gasto) {
        CorteCajaDAO corteCajaDAO = new CorteCajaDAO(context);
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        gasto.setFecha_hora(fechaHoy);
        gasto.setId_corte(corteCajaDAO.getCorteActual().getId_corte());
        return gastosDAO.addGasto(gasto);
    }
    public boolean deleteGasto(int id_gasto){
        return gastosDAO.deleteGasto(id_gasto);
    }
    public void prepararDatosSpinner(){
        IProveedores proveedoresDAO = new ProveedoresDAO(context);
        listaProveedores = proveedoresDAO.getProveedor("", "");
        nombresProveedores = new ArrayList<>();
        nombresProveedores.add("Gasto General");

        for (Proveedor proveedor : listaProveedores) {
            nombresProveedores.add(proveedor.getNombre_proveedor());
        }

    }
    public ArrayList<String> getNombresProveedores(){
        return nombresProveedores;
    }

    public int getIdProveedor(int position){
        if (position == 0) return 0;
        return listaProveedores.get(position-1).getId_proveedor();
    }
    public String getNombresProveedor(int position) {
        if (position == 0) return "Gasto General";
        return listaProveedores.get(position-1).getNombre_proveedor();
    }
    public ArrayList<Gastos> getGastos(String columna, String valor){
        return gastosDAO.getGastosBy(columna, valor);
    }
    public String getNombresProveedorById(int id_proveedor){
        if (id_proveedor == 0) {
            return "Gasto General";
        }

        if (listaProveedores == null || listaProveedores.isEmpty()) {
            prepararDatosSpinner();
        }
        if (listaProveedores != null) {
            for (Proveedor p : listaProveedores) {
                if (p.getId_proveedor() == id_proveedor) {
                    return p.getNombre_proveedor(); // ¡Encontrado!
                }
            }
        }
        return "Prov. Desconocido (ID " + id_proveedor + ")";
    }
    public double getMontoTotalGastos(){
        return gastosDAO.getMontoTotalGastos();
    }
}
