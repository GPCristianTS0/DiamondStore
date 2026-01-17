package com.Clover.prueba.data.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.Clover.prueba.data.dao.ProveedoresDAO;
import com.Clover.prueba.data.dao.interfaces.IProveedores;
import com.Clover.prueba.data.models.Proveedor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProveedoresController {
    private IProveedores proveedoresDAO;
    private Context context;
    private Proveedor proveedor;
    public ProveedoresController(Context context) {
        proveedoresDAO = new ProveedoresDAO(context);
        this.context = context;
    }

    public boolean addProveedor(Proveedor proveedor){
        String fecha = SimpleDateFormat.getDateInstance().format(new Date());
        proveedor.setFecha_registro(fecha);
        return proveedoresDAO.addProveedor(proveedor);
    }
    public boolean updateProveedor(Proveedor proveedorOld, Proveedor proveedor){
        Log.i("Clover_App", "Proveedor: " + proveedor.toString());
        return proveedoresDAO.updateProveedor(proveedorOld, proveedor);
    }
    public boolean deleteProveedor(int id_proveedor){
        return proveedoresDAO.deleteProveedor(id_proveedor);
    }
    public ArrayList<Proveedor> getProveedoresBy(String columna, String valor){
        return proveedoresDAO.getProveedor(columna, valor);
    }
}
