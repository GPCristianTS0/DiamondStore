package com.Clover.prueba.data.dao.interfaces;

import com.Clover.prueba.data.models.Abonos;

import java.util.ArrayList;

public interface IAbonos {
    public long addAbono(Abonos abono);
    public ArrayList<Abonos> getAbonos(String idCliente);
    public Abonos getAbono(String idAbono);
    public boolean deleteAbono(String idAbono);


}
