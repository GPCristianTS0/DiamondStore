package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Configuracion;

import java.util.ArrayList;

public interface IConfiguracion {
    public boolean updateConfiguracionNegocio(Configuracion configuracion);
    public Configuracion getConfiguracion();
}
