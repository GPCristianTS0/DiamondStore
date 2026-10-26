package com.Clover.prueba.domain.reparaciones.usecase.Formulario;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddClientReparaciones {
    private final IClient dao;

    public AddClientReparaciones(IClient dao) {
        this.dao = dao;
    }
    public void execute(Clientes cliente) throws Exception {
        if (cliente==null) throw new Exception("Error al registrar nuevo cliente");
        if (cliente.getId_cliente().isEmpty()||cliente.getId_cliente()==null || cliente.getId_cliente().length()!=10) throw new Exception("Ingrese el numero de telefono ");
        if (cliente.getNombre_cliente().isEmpty()||cliente.getNombre_cliente()==null) throw new Exception("Ingrese el nombre del cliente");
        cliente.setSaldo(0);
        cliente.setPuntos(0);
        cliente.setFecha_registro(new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date()));

        if (!dao.addClient(cliente)) throw new Exception("Error al registrar nuevo cliente");
    }
}
