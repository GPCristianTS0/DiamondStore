package com.Clover.prueba.ui.reparaciones.viewModel;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.AddClientReparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.AddReparacionUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetAllClient;
import com.Clover.prueba.services.generators.TicketUtils;
import com.Clover.prueba.services.sharing.ShareManager;

import java.util.ArrayList;

public class FormularioViewModel extends ViewModel{
    private Reparaciones rep;
    private Clientes clienteSeleccionado;
    //LiveDatas
    private final MutableLiveData<ArrayList<Clientes>> _listaClientesF = new MutableLiveData<>();
    public final LiveData<ArrayList<Clientes>> listaClientesLiv = _listaClientesF;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;
    private final MutableLiveData<Boolean> _exito = new MutableLiveData<>();
    public final LiveData<Boolean> exito = _exito;
    private final MutableLiveData<Intent> _intetTicket = new MutableLiveData<>();
    public final LiveData<Intent> intentTicket = _intetTicket;
    //UseCase
    private final GetAllClient getAllClient ;
    private final AddClientReparaciones addClientUseCase;
    private final AddReparacionUseCase addReparacionUseCase;
    //Services
    private final TicketUtils ticketUtils;
    private final ShareManager shareManager;

    public FormularioViewModel(GetAllClient getAllClient, AddClientReparaciones addClientUseCase, AddReparacionUseCase addReparacionUseCase, TicketUtils ticketUtils, ShareManager shareManager) {
        this.getAllClient = getAllClient;
        this.addClientUseCase = addClientUseCase;
        this.addReparacionUseCase = addReparacionUseCase;
        this.ticketUtils = ticketUtils;
        this.shareManager = shareManager;
    }

    public void getClient(){
        try{
            ArrayList<Clientes> listaClientes = getAllClient.execute();
            if (listaClientes == null || listaClientes.isEmpty()) {
                listaClientes = new ArrayList<>();
                listaClientes.add(new Clientes("-1", "Sin Clientes Registrados"));
            }
            _listaClientesF.setValue(listaClientes);
        }catch (Exception e){
            _error.setValue(e.getMessage());
        }
    }

    public void addClient(Clientes cliente){
        try{
            addClientUseCase.execute(cliente);
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void addReparacion(Reparaciones reparacion){
        try{
            addReparacionUseCase.execute(reparacion);
            rep = reparacion;
            _exito.setValue(true);
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void setClient(Clientes cliente){
        clienteSeleccionado = cliente;
    }
    public void generarTicket(){
        /*
        * Refactorizar ticketutils*/
        try{
            _intetTicket.setValue(shareManager.compartirImagen("Compartir Via...",ticketUtils.generarTicketReparacion(rep, clienteSeleccionado, true)));
        } catch (Exception e){
            _error.setValue(e.getMessage());
        }
    }
    public void resetExito(){
        _exito.setValue(false);
    }
    public void resetIntent(){
        _intetTicket.setValue(null);
    }
}
