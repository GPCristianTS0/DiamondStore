package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IVentas;

import java.text.DecimalFormat;

public class getTicketPromedio {
    private final IVentas ventasDAO;
    public getTicketPromedio(IVentas ventasDAO) {
        this.ventasDAO = ventasDAO;
    }
    public String execute(String idCliente) {
        double ticket = ventasDAO.getTicketPromedio(idCliente);
        if (ticket<=0) throw new RuntimeException("Sin Tickets");
        DecimalFormat formatoDinero = new DecimalFormat("$ #,##0.00");
        return formatoDinero.format(ticket);
    }
}
