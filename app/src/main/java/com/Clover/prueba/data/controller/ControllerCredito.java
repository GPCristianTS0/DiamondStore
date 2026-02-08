package com.Clover.prueba.data.controller;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

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
import com.Clover.prueba.utils.TicketUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ControllerCredito {
    private final Context context;
    private IClient clientesDAO;
    private IVentas ventasDAO;
    private IAbonos abonosDAO;
    private ICorteCaja corteCajaDAO;
    private Abonos abono;
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
        long idRow = abonosDAO.addAbono(abonos);
        if(idRow==-1){
            Log.e("Clover_App", "Error al registrar abono");
            return false;
        }
        abonos.setId((int)idRow);
        this.abono = abonos;
        return true;
    }
    public void generarTicketAbono(Context context, String nombreCliente, boolean reimprimir, Abonos abonoLocal){
        TicketUtils ticketUtils = new TicketUtils(context);
        String nombre = nombreCliente;
        if (abonoLocal!=null){
            abono = abonoLocal;
            nombre = clientesDAO.getNombreCliente(abonoLocal.getIdCliente());
        }
        ticketUtils.generarTicketAbono(context, abono, nombre, reimprimir);
    }
    public Abonos getAbono(String idAbono){
        return abonosDAO.getAbono(idAbono);
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
    public boolean deleteAbono(int idAbono){
        return abonosDAO.deleteAbono(String.valueOf(idAbono));
    }
}
