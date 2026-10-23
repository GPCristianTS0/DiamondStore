package com.Clover.prueba.ui.reparaciones.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.DTO.ResultadoContadores;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.CalcularContadoresUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.FiltrarListaUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.GetAllReparacionesUseCase;

import java.util.ArrayList;

public class ReparacionesViewModel extends ViewModel {
    //Reparaciones View
    private final MutableLiveData<ArrayList<Reparaciones>> _reparcionesAMostrar = new MutableLiveData<>();
    public final LiveData<ArrayList<Reparaciones>> reparacionesAMostrar = _reparcionesAMostrar;
    private final MutableLiveData<ResultadoContadores> _contadoresResult = new MutableLiveData<>();
    public final LiveData<ResultadoContadores> contadoresResult = _contadoresResult;
    private ArrayList<Reparaciones> listaCompleta = new ArrayList<>();
        //UseCase
    private final CalcularContadoresUseCase calcularContadoresUseCase;
    private final FiltrarListaUseCase filtrarListaUseCase;
    private final GetAllReparacionesUseCase getAllReparaciones;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public ReparacionesViewModel(CalcularContadoresUseCase calcularContadoresUseCase, FiltrarListaUseCase filtrarListaUseCase, GetAllReparacionesUseCase getAllReparaciones) {
        this.calcularContadoresUseCase = calcularContadoresUseCase;
        this.filtrarListaUseCase = filtrarListaUseCase;
        this.getAllReparaciones = getAllReparaciones;

    }
    /*
    *   Metodos para ReparacionView
    *  */
    public void aplicarFiltros(String query, String estado, String servicio){
        try{
            listaCompleta = getAllReparaciones.execute();
            if (listaCompleta!=null) {
                calcularContadores();
                _reparcionesAMostrar.setValue(filtrarListaUseCase.execute(listaCompleta, query, estado, servicio));
            }
        } catch (Exception e) {
            _error.setValue("Lista no se pudo cargar");
        }
    }
    public void calcularContadores(){
        try{
            _contadoresResult.setValue(calcularContadoresUseCase.execute(listaCompleta));
        } catch (Exception e) {
            _error.setValue("Error al obtener datos");
        }
    }
}
