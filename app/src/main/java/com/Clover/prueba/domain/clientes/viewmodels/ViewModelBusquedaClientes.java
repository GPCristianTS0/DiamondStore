package com.Clover.prueba.domain.clientes.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.domain.clientes.usecase.GetClientsSearch;

import java.util.ArrayList;

public class ViewModelBusquedaClientes extends ViewModel {
    private final MutableLiveData<ArrayList<Clientes>> _clientes = new MutableLiveData<>();
    private LiveData<ArrayList<Clientes>> clientes = _clientes;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<String> error = _error;
    private final GetClientsSearch clientesSearchUseCase;

    public ViewModelBusquedaClientes(GetClientsSearch clientesSearchUseCase) {
        this.clientesSearchUseCase = clientesSearchUseCase;
    }

    public void cargarClientes(String filtro, String valor, boolean deudores){
        try {
            _clientes.setValue(clientesSearchUseCase.execute(filtro, valor, deudores));
        } catch (Exception e) {
            Log.e("Clover_App","Error al cargar los clientes: ", e);
            _error.setValue(e.getMessage());
        }
    }
    public void llamar(){

    }
    public LiveData<ArrayList<Clientes>> getClientes() {
        return clientes;
    }
    public LiveData<String> getError() {
        return error;
    }
}
