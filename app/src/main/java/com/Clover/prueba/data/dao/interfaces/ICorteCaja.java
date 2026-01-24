package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.CorteCaja;

import java.util.ArrayList;

public interface ICorteCaja {
    public boolean iniciarCorte(CorteCaja corteCaja);
    public boolean cerrarCorte(CorteCaja corteCaja);

    double getVentasTotalesCorte(String fechaHora);

    public boolean getEstadoCorte();
    public CorteCaja getCorteActual();
    public void vaciarTabla();

    ArrayList<CorteCaja> getCortes(String estado);

    CorteCaja getCorte(int id_corte);
}
