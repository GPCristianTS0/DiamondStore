package com.Clover.prueba.domain.corteCaja;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.CorteCaja;
import com.Clover.prueba.domain.corteCaja.usecase.IniciarTurnoUseCase;

public class CorteViewModel extends ViewModel {
    private final MutableLiveData<CorteCaja> corte = new MutableLiveData<>();
    public LiveData<CorteCaja> getCorteActual() {return corte;}
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<String> getError(){return error;}
    private final MutableLiveData<Boolean> estadoCorte = new MutableLiveData<>();
    public LiveData<Boolean> getEstadoCorteActual(){return estadoCorte;}
    //UseCase
    private final IniciarTurnoUseCase iniciarTurnoCorte;

    public CorteViewModel(IniciarTurnoUseCase iniciarTurnoCorte) {
        this.iniciarTurnoCorte = iniciarTurnoCorte;
    }

    public void iniciarTurno(String monto){
        try {
            iniciarTurnoCorte.execute(monto);
            estadoCorte.setValue(true);
        }catch (Exception e){
            error.setValue("Ingrese un valor valido");
        }
    }
}
