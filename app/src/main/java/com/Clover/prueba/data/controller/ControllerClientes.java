package com.Clover.prueba.data.controller;

import android.content.Context;

import com.Clover.prueba.data.dao.AbonosDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Abonos;

import java.util.ArrayList;

public class ControllerClientes {
    private Context context;
    private IVentas ventasDAO;
    private IAbonos abonosDAO;
    public ControllerClientes(Context context) {
        this.context = context;
        ventasDAO = new VentasDAO(context);
        abonosDAO = new AbonosDAO(context);
    }
    public double getSaldoPendiente(String idCliente){
        return ventasDAO.getSaldoPendiente(idCliente);
    }
    public double getSaldoTotal(String idCliente){
        return ventasDAO.getSaldoTotal(idCliente);
    }
    public ArrayList<Abonos> getAbonos(String id_cliente){
        return abonosDAO.getAbonos(id_cliente);
    }
    public boolean deleteAbono(Abonos abono){
        return false;
    }
    public void compartirTicket(Abonos abono){

    }
}
