package com.Clover.prueba.domain.clientes;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.domain.clientes.usecase.CompartirCardUseCase;
import com.Clover.prueba.domain.clientes.usecase.GetAbonosClientes;
import com.Clover.prueba.domain.clientes.usecase.GetClientes;
import com.Clover.prueba.domain.clientes.usecase.GetMasCompradosCliente;
import com.Clover.prueba.domain.clientes.usecase.GetSaldoTotalCliente;
import com.Clover.prueba.domain.clientes.usecase.GetUltimoAbono;
import com.Clover.prueba.domain.clientes.usecase.GetVentasTotales;
import com.Clover.prueba.domain.clientes.usecase.getTicketPromedio;

public class ViewModelFactoryClientes implements ViewModelProvider.Factory {

    private final GetSaldoTotalCliente saldoTotalCliente;
    private final GetAbonosClientes getAbonosUseCase;
    private final GetMasCompradosCliente masCompradosCliente;
    private final GetVentasTotales getVentasTotales;
    private final getTicketPromedio getTicketPromedio;
    private final CompartirCardUseCase compartirCardUseCase;
    private final GetUltimoAbono getUltimoAbono;
    private final GetClientes getCliente;

    public ViewModelFactoryClientes(GetSaldoTotalCliente saldoTotalCliente, GetAbonosClientes getAbonosUseCase, GetMasCompradosCliente masCompradosCliente, GetVentasTotales getVentasTotales, getTicketPromedio getTicketPromedio, CompartirCardUseCase compartirCardUseCase, GetUltimoAbono getUltimoAbono, GetClientes getCliente) {
        this.saldoTotalCliente = saldoTotalCliente;
        this.getAbonosUseCase = getAbonosUseCase;
        this.masCompradosCliente = masCompradosCliente;
        this.getVentasTotales = getVentasTotales;
        this.getTicketPromedio = getTicketPromedio;
        this.compartirCardUseCase = compartirCardUseCase;
        this.getUltimoAbono = getUltimoAbono;
        this.getCliente = getCliente;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelClientes.class)) {
            return (T) new ViewModelClientes(saldoTotalCliente, getAbonosUseCase, masCompradosCliente, getVentasTotales, getTicketPromedio, compartirCardUseCase, getUltimoAbono, getCliente);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
