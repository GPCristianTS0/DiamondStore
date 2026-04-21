package com.Clover.prueba.domain.clientes;

import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.dto.ProductoMasCompradoDTO;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.domain.clientes.usecase.GetAbonosClientes;
import com.Clover.prueba.domain.clientes.usecase.GetClientes;
import com.Clover.prueba.domain.clientes.usecase.GetMasCompradosCliente;
import com.Clover.prueba.domain.clientes.usecase.GetSaldoTotalCliente;
import com.Clover.prueba.domain.clientes.usecase.GetUltimoAbono;
import com.Clover.prueba.domain.clientes.usecase.GetVentasTotales;
import com.Clover.prueba.domain.clientes.usecase.CompartirCardUseCase;
import com.Clover.prueba.domain.clientes.usecase.getTicketPromedio;

import java.util.ArrayList;

public class ViewModelClientes extends ViewModel {
    private final GetSaldoTotalCliente saldoTotalCliente;
    private final GetAbonosClientes getAbonosUseCase;
    private final GetMasCompradosCliente masCompradosCliente;
    private final GetVentasTotales getVentasTotales;
    private final getTicketPromedio getTicketPromedio;
    private final CompartirCardUseCase compartirCardUseCase;
    private final GetUltimoAbono getUltimoAbono;
    private final GetClientes getCliente;

    public ViewModelClientes(GetSaldoTotalCliente saldoTotalCliente, GetAbonosClientes getAbonosUseCase, GetMasCompradosCliente masCompradosCliente, GetVentasTotales getVentasTotales, getTicketPromedio getTicketPromedio, CompartirCardUseCase compartirCardUseCase, GetUltimoAbono getUltimoAbono, GetClientes getCliente) {
        this.saldoTotalCliente = saldoTotalCliente;
        this.getAbonosUseCase = getAbonosUseCase;
        this.masCompradosCliente = masCompradosCliente;
        this.getVentasTotales = getVentasTotales;
        this.getTicketPromedio = getTicketPromedio;
        this.compartirCardUseCase = compartirCardUseCase;
        this.getUltimoAbono = getUltimoAbono;
        this.getCliente = getCliente;
    }
    /*public double getSaldoPendiente(String idCliente){

    }*/
    public double getSaldoTotal(String idCliente){
        return saldoTotalCliente.execute(idCliente);
    }
    public ArrayList<Abonos> getAbonos(String id_cliente){
        return getAbonosUseCase.execute(id_cliente);
    }
    /**
     * Obtiene los productos mas comprados de un cliente y los regresa en un arraylist
     *
     * @param idCliente Identificador del cliente (Tabla Clientes)
     * @param noProductos Cantidad de productos a obtener (Tabla Productos)
     *
     */
    public ArrayList<ProductoMasCompradoDTO> getMasComprados(String idCliente, int noProductos){
        return masCompradosCliente.execute(idCliente, noProductos);
    }
    public int getVentasTotales(String idCliente){
        return getVentasTotales.execute(idCliente);
    }
    public String getTicketPromedio(String idCliente){
        try{
            return getTicketPromedio.execute(idCliente);
        } catch (Exception e){
            return e.getMessage();
        }
    }
    public String getUltimoAbono(String idCliente){
        try{
            return getUltimoAbono.execute(idCliente);
        } catch (Exception e){
            return e.getMessage();
        }
    }
    public boolean deleteAbono(Abonos abono){
        return false;
    }
    public void compartirTicket(Abonos abono){

    }
    public void compartirCard(Clientes cliente) {
        compartirCardUseCase.execute(cliente);
    }

    public Clientes getClientes(String idCliente) {
        return getCliente.execute(idCliente);
    }
}
