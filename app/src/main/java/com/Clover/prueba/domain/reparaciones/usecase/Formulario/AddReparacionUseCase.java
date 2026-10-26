package com.Clover.prueba.domain.reparaciones.usecase.Formulario;

import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;
import com.Clover.prueba.domain.reparaciones.constantes.TipoReparacion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddReparacionUseCase {
    private final IReparaciones dao;
    private final ICorteCaja daoCorte;

    public AddReparacionUseCase(IReparaciones dao, ICorteCaja daoCorte) {
        this.dao = dao;
        this.daoCorte = daoCorte;
    }
    public int execute(Reparaciones reparacion) throws Exception {
        //Validar vacios
        validarCampos(reparacion.getColor());
        validarCampos(reparacion.getEquipo());
        validarCampos(reparacion.getFalla_reportada());
        validarCampos(reparacion.getId_cliente());
        //Validacion monto
        if (reparacion.getAnticipo()>reparacion.getMonto_total()) throw new Exception("Ingresa un anticipo menor o igual al monto");
        if (reparacion.getMonto_total()<=0 || reparacion.getAnticipo()<=-1) throw new Exception("Ingresa montos validos");
        //Valida si hay corte abierto
        if (!daoCorte.getEstadoCorte()) throw new Exception("No hay corte abierto");
        //Termina de Armar el objeto
        reparacion.setFecha_ingreso(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        reparacion.setMonto_pendiente(reparacion.getMonto_total()- reparacion.getAnticipo());
        if (reparacion.getTipo_servicio().equals(TipoReparacion.DOMICILIO.toString())) {
            reparacion.setEstado(EstadoReparcion.ENTREGADO.toString());
            reparacion.setFecha_entrega(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        }else {
            validarCampos(reparacion.getFecha_promesa());
        }
        reparacion.setId_corte(daoCorte.getCorteActual().getId_corte());
        //Agrega la reparacion a la base de datos
        return 2;// dao.addReparacion(reparacion);

    }
    private void validarCampos(String campos) throws Exception {
        if (campos==null||campos.isEmpty()) throw new Exception("Rellena los campos necesarios");
    }
}
