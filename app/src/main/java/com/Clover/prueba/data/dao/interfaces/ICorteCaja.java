package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.CorteCaja;

public interface ICorteCaja {
    public boolean iniciarCorte(CorteCaja corteCaja);
    public boolean cerrarCorte(CorteCaja corteCaja);

    double getVentasTotalesCorte(String fechaHora);

    public boolean getEstadoCorte();
    public CorteCaja getCorteActual();
    public void vaciarTabla();
}
