package com.Clover.prueba.data.models;

import android.net.Uri;

import java.io.Serializable;

public class Configuracion implements Serializable {
    private String nombreNegocio;
    private String eslogan;
    private String direccion;
    private String telefono;
    private String rfc;
    private Uri logoURL;
    private String rutaLogo;
    private String oldRutaLogo;
    private int stockMinimo;
    private double iva;
    private String notaGarantia;
    private String mensajeShare;
    private boolean skipLogin;
    private String pin;
    private String printerMac;
    private String printerName;
    private int printerWidth;

    public Configuracion() {
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getEslogan() {
        return eslogan;
    }

    public void setEslogan(String eslogan) {
        this.eslogan = eslogan;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Uri getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(Uri logoURL) {
        this.logoURL = logoURL;
    }

    public String getRutaLogo() {
        return rutaLogo;
    }

    public String getOldRutaLogo() {
        return oldRutaLogo;
    }

    public void setOldRutaLogo(String oldRutaLogo) {
        this.oldRutaLogo = oldRutaLogo;
    }

    public void setRutaLogo(String rutaLogo) {
        this.rutaLogo = rutaLogo;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getNotaGarantia() {
        return notaGarantia;
    }

    public void setNotaGarantia(String notaGarantia) {
        this.notaGarantia = notaGarantia;
    }

    public String getMensajeShare() {
        return mensajeShare;
    }

    public void setMensajeShare(String mensajeShare) {
        this.mensajeShare = mensajeShare;
    }

    public boolean isSkipLogin() {
        return skipLogin;
    }

    public void setSkipLogin(boolean skipLogin) {
        this.skipLogin = skipLogin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPrinterMac() {
        return printerMac;
    }

    public void setPrinterMac(String printerMac) {
        this.printerMac = printerMac;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public int getPrinterWidth() {
        return printerWidth;
    }

    public void setPrinterWidth(int printerWidth) {
        this.printerWidth = printerWidth;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "nombreNegocio='" + nombreNegocio + '\'' +
                ", eslogan='" + eslogan + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rfc='" + rfc + '\'' +
                ", logoURL=" + logoURL +
                ", rutaLogo='" + rutaLogo + '\'' +
                ", oldRutaLogo='" + oldRutaLogo + '\'' +
                ", stockMinimo=" + stockMinimo +
                ", iva=" + iva +
                ", notaGarantia='" + notaGarantia + '\'' +
                ", mensajeShare='" + mensajeShare + '\'' +
                ", skipLogin=" + skipLogin +
                ", pin='" + pin + '\'' +
                ", printerMac='" + printerMac + '\'' +
                ", printerName='" + printerName + '\'' +
                ", printerWidth=" + printerWidth +
                '}';
    }
}
