package com.Clover.prueba.data.controller;

import android.content.Context;
import android.util.Log;

import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.utils.ImageUtils;

public class ConfiguracionControl {
    private ConfiguracionDAO configuracionDAO;
    private Context context;

    public ConfiguracionControl(Context context) {
        configuracionDAO = new ConfiguracionDAO(context);
        this.context = context;
    }
    public Configuracion getConfiguracion() {
        return configuracionDAO.getConfiguracion();
    }
    public boolean updateConfiguracionNegocio(Configuracion configuracion) {
        ImageUtils utils = new ImageUtils(context);
        Log.d("Clover_App", "updateConfiguracionNegocio: "+configuracion.toString());
        if (configuracion.getLogoURL() != null){
            String ruta = utils.guardarImagen(configuracion.getLogoURL());
            configuracion.setRutaLogo(ruta);
            utils.eliminarImagen(configuracion.getOldRutaLogo());
        }
        return configuracionDAO.updateConfiguracionNegocio(configuracion);
    }

}
