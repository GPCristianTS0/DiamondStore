package com.Clover.prueba.data.controller;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import com.Clover.prueba.data.dao.AbonosDAO;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Ventas;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ControllerCredito {
    private final Context context;
    private IClient clientesDAO;
    private IVentas ventasDAO;
    private IAbonos abonosDAO;
    private ICorteCaja corteCajaDAO;
    public ControllerCredito(Context context) {
        this.context = context;
        clientesDAO = new ClientesDAO(context);
        ventasDAO = new VentasDAO(context);
        abonosDAO = new AbonosDAO(context);
        corteCajaDAO = new CorteCajaDAO(context);
    }

    public boolean darAbono(double abono, Clientes cliente, String tipoPago) {
        Abonos abonos = new Abonos();
        SimpleDateFormat fechaHoy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        abonos.setIdCliente(cliente.getId_cliente());
        abonos.setFecha(fechaHoy.format(new Date()));
        abonos.setMonto(abono);
        abonos.setSaldoAnterior(cliente.getSaldo());
        abonos.setSaldoActual(cliente.getSaldo()-abono);
        abonos.setTipoPago(tipoPago);
        int id = corteCajaDAO.getCorteActual().getId_corte();
        abonos.setIdCorte(id);
        return abonosDAO.addAbono(abonos);
    }
    public Clientes getCliente(String id){
        return clientesDAO.getClient(id);
    }
    public ArrayList<Ventas> getVentas(String idCliente){
        return ventasDAO.getVentas(idCliente);
    }
    public double getSaldoTotal(){
        return clientesDAO.getSaldoTotal();
    }
}
