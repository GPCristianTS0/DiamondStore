package com.Clover.prueba.ui.reparaciones.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.AddClientReparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.AddReparacionUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.CalcularContadoresUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.FiltrarListaUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetAllClient;
import com.Clover.prueba.domain.reparaciones.usecase.GetAllReparacionesUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetClienteUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetReparacionByIDUseCase;
import com.Clover.prueba.services.generators.TicketUtils;
import com.Clover.prueba.services.sharing.ShareManager;

public class ReparacionesFactory implements ViewModelProvider.Factory {
    private Context context;
    public ReparacionesFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        IReparaciones repaDAO = new ReparacionesDAO(context);
        IClient clientDAO = new ClientesDAO(context);
        ICorteCaja corteCajaDAO = new CorteCajaDAO(context);
        TicketUtils ticketUtils = new TicketUtils(context);
        ShareManager shareManager = new ShareManager(context);
        if (modelClass.isAssignableFrom(ReparacionesViewModel.class)){

            //usecase
            GetClienteUseCase getClienteUseCase = new GetClienteUseCase(clientDAO);
            GetReparacionByIDUseCase getReparacionByIDUseCase = new GetReparacionByIDUseCase(repaDAO);
            CalcularContadoresUseCase calcularContadoresUseCase = new CalcularContadoresUseCase();
            FiltrarListaUseCase filtrarListaUseCase = new FiltrarListaUseCase();
            GetAllReparacionesUseCase getAllReparaciones = new GetAllReparacionesUseCase(repaDAO);

            return (T) new ReparacionesViewModel(calcularContadoresUseCase, filtrarListaUseCase,getAllReparaciones, getClienteUseCase, getReparacionByIDUseCase);
        }
        if (modelClass.isAssignableFrom(FormularioViewModel.class)){
            GetAllClient getAllClient = new GetAllClient(clientDAO);
            AddClientReparaciones addClientReparaciones = new AddClientReparaciones(clientDAO);
            AddReparacionUseCase addReparacionUseCase = new AddReparacionUseCase(repaDAO, corteCajaDAO);
            return (T) new FormularioViewModel(getAllClient, addClientReparaciones, addReparacionUseCase, ticketUtils, shareManager);
        }
        throw new IllegalArgumentException("Clase ViewModel Desconocidad");
    }

}
