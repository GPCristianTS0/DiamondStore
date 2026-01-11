package com.Clover.prueba.data.controller;

import java.util.ArrayList;

import com.Clover.prueba.data.models.Deuda;

public interface ControllerDeuda {

    public void addDeuda(int id, int id_deuda, int saldo_total, String plazo, String fecha_pago, int pagos_restantes, int pagos_totales, int monto_pago, String id_productos, int saldo_restante);
    public Deuda getDeuda(int id);
    public Deuda getDeuda(String nombre);
    public ArrayList getDeuda();
    public void deleteDeuda(int id);
    public void realizarPago(int monto_pago, int id_deuda);

}
