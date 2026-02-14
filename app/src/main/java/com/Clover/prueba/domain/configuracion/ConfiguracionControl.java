package com.Clover.prueba.domain.configuracion;

import android.content.Context;

import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.services.storage.StorageImage;

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
        StorageImage utils = new StorageImage(context);
        if (configuracion.getLogoURL() != null){
            String ruta = utils.guardarImagenInterno(configuracion.getLogoURL());
            configuracion.setRutaLogo(ruta);
            utils.eliminarImagen(configuracion.getOldRutaLogo());
        }else {
            configuracion.setRutaLogo(configuracion.getOldRutaLogo());
        }
        return configuracionDAO.updateConfiguracionNegocio(configuracion);
    }

}
