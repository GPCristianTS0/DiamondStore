package com.Clover.prueba.ui.reparaciones.viewModel;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.ActualizarEstadoUSECASE;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.NotaReparacionUC;
import com.Clover.prueba.domain.reparaciones.usecase.Formulario.GetClienteUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.DeleteReparacionUSECASE;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.GetReparacionByIDUseCase;
import com.Clover.prueba.ui.reparaciones.DTO.DetalleReparacionStateUI;

import java.util.Objects;

public class DetallesReparacionVM extends ViewModel {
    private final MutableLiveData<DetalleReparacionStateUI> _detallesReparacion = new MutableLiveData<>();
    public final LiveData<DetalleReparacionStateUI> detallesReparacion = _detallesReparacion;
    private final MutableLiveData<Boolean> _eliminado = new MutableLiveData<>();
    public final LiveData<Boolean> eliminado = _eliminado;
    private final MutableLiveData<Intent> _nota = new MutableLiveData<>();
    public final LiveData<Intent> nota = _nota;
    private final MutableLiveData<Boolean> _exito = new MutableLiveData<>();
    public final LiveData<Boolean> exito = _exito;
    //USECASE
    private final GetClienteUseCase getClienteUseCase;
    private final GetReparacionByIDUseCase getReparacionByIDUseCase;
    private final ActualizarEstadoUSECASE updateEstate;
    private final DeleteReparacionUSECASE deleteReparacion;
    private final NotaReparacionUC notaReparacionUC;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public DetallesReparacionVM(GetClienteUseCase getClienteUseCase, GetReparacionByIDUseCase getReparacionByIDUseCase, ActualizarEstadoUSECASE updateEstate, DeleteReparacionUSECASE deleteReparacion, NotaReparacionUC notaReparacionUC) {
        this.getClienteUseCase = getClienteUseCase;
        this.getReparacionByIDUseCase = getReparacionByIDUseCase;
        this.updateEstate = updateEstate;
        this.deleteReparacion = deleteReparacion;
        this.notaReparacionUC = notaReparacionUC;
    }

    public void setReparacion(int idReparacion) {
        try{
            Reparaciones rep = getReparacionByIDUseCase.execute(idReparacion);
            Clientes c = getClienteUseCase.execute(rep.getId_cliente());
            DetalleReparacionStateUI stateUI = new DetalleReparacionStateUI();
            stateUI.setReparacion(rep);
            stateUI.setCliente(c);
            _detallesReparacion.setValue(stateUI);
        } catch (Exception e) {
            Log.e("Clover_App", Objects.requireNonNull(e.getMessage()));
            _error.setValue(e.getMessage());
        }
    }
    public DetalleReparacionStateUI getReparacion(){
        return _detallesReparacion.getValue();
    }
    public void updateEstado(EstadoReparcion estado){
        try {
            DetalleReparacionStateUI stateUI = _detallesReparacion.getValue();
            if (stateUI!=null) {
                updateEstate.execute(stateUI.getFolio(), estado);
                _exito.setValue(true);
            }
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void estadoListo(String diagnostico, String costoRefacciones){
        try {
            DetalleReparacionStateUI detalles = _detallesReparacion.getValue();
            if (detalles!=null) {
                updateEstate.executeListo(_detallesReparacion.getValue().getFolio(), diagnostico, costoRefacciones);
                _exito.setValue(true);
            }
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void eliminarReparacion(){
        try{
            deleteReparacion.execute(Objects.requireNonNull(_detallesReparacion.getValue()).getFolio());
            _exito.setValue(true);
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void compartirTicket(boolean whatsapp){
        try{
            _nota.setValue(notaReparacionUC.execute(_detallesReparacion.getValue().getReparacion(), _detallesReparacion.getValue().getCliente(), whatsapp));
        } catch (Exception e) {
            _error.setValue(e.getMessage());
            //Log.e("Clover_App", e);
        }
    }
    public void resetExito(){
        _exito.setValue(false);
    }
    public void resetEliminado(){
        _eliminado.setValue(false);
    }
    public void resetIntent(){
        _nota.setValue(null);
    }
}
