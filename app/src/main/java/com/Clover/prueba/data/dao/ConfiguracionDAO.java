package com.Clover.prueba.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Clover.prueba.data.dao.interfaces.IConfiguracion;
import com.Clover.prueba.data.database.CloverBD;
import com.Clover.prueba.data.models.Configuracion;

import java.util.ArrayList;

public class ConfiguracionDAO implements IConfiguracion {
    private SQLiteDatabase db;

    public ConfiguracionDAO(Context context) {
        db = CloverBD.getInstance(context).getWritableDatabase();
    }

    @Override
    public boolean updateConfiguracionNegocio(Configuracion configuracion) {
        ContentValues values = new ContentValues();
        values.put("negocio_nombre", configuracion.getNombreNegocio());
        values.put("negocio_eslogan", configuracion.getEslogan());
        values.put("negocio_direccion", configuracion.getDireccion());
        values.put("negocio_telefono", configuracion.getTelefono());
        values.put("negocio_rfc", configuracion.getRfc());
        values.put("negocio_logo_uri", configuracion.getRutaLogo());
        values.put("sys_stock_min", configuracion.getStockMinimo());
        values.put("sys_impuesto_iva", configuracion.getIva());
        values.put("sys_msg_garantia", configuracion.getNotaGarantia());
        values.put("sys_msg_share", configuracion.getMensajeShare());
        values.put("printer_mac", configuracion.getPrinterMac());
        values.put("printer_name", configuracion.getPrinterName());
        values.put("printer_width", configuracion.getPrinterWidth());
        values.put("security_pin", configuracion.getPin());
        values.put("security_skip_login", configuracion.isSkipLogin() ? 1 : 0);
        return db.update("configuracion", values, "id_config = ?", new String[]{"1"}) > 0;
    }

    @Override
    public Configuracion getConfiguracion() {
        String sql = "SELECT * FROM configuracion WHERE id_config = 1";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()){
                Configuracion configuracion = new Configuracion();
                configuracion.setNombreNegocio(cursor.getString(cursor.getColumnIndexOrThrow("negocio_nombre")));
                configuracion.setEslogan(cursor.getString(cursor.getColumnIndexOrThrow("negocio_eslogan")));
                configuracion.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow("negocio_direccion")));
                configuracion.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow("negocio_telefono")));
                configuracion.setRfc(cursor.getString(cursor.getColumnIndexOrThrow("negocio_rfc")));
                configuracion.setRutaLogo(cursor.getString(cursor.getColumnIndexOrThrow("negocio_logo_uri")));
                configuracion.setStockMinimo(cursor.getInt(cursor.getColumnIndexOrThrow("sys_stock_min")));
                configuracion.setIva(cursor.getDouble(cursor.getColumnIndexOrThrow("sys_impuesto_iva")));
                configuracion.setNotaGarantia(cursor.getString(cursor.getColumnIndexOrThrow("sys_msg_garantia")));
                configuracion.setMensajeShare(cursor.getString(cursor.getColumnIndexOrThrow("sys_msg_share")));
                configuracion.setPrinterMac(cursor.getString(cursor.getColumnIndexOrThrow("printer_mac")));
                configuracion.setPrinterName(cursor.getString(cursor.getColumnIndexOrThrow("printer_name")));
                configuracion.setPrinterWidth(cursor.getInt(cursor.getColumnIndexOrThrow("printer_width")));
                configuracion.setPin(cursor.getString(cursor.getColumnIndexOrThrow("security_pin")));
                configuracion.setSkipLogin(cursor.getInt(cursor.getColumnIndexOrThrow("security_skip_login")) == 1);
                return configuracion;
            }

        } catch (Exception e) {
            Log.e("ConfiguracionDAO", "Error al obtener la configuración: " + e.getMessage());
        }
        return null;
    }
}
