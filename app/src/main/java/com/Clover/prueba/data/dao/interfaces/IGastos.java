package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Gastos;

import java.util.ArrayList;

public interface IGastos {
    public boolean addGasto(Gastos gasto);
    public boolean deleteGasto(int gasto);
    public ArrayList<Gastos> getGastosBy(String columna, String valor);
    public ArrayList<Gastos> getGastosByCorte(int id_corte);
    public double sumarTotalGastosByCorte(int id_corte);

}
