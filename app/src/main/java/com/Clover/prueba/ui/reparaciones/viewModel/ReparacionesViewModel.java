package com.Clover.prueba.ui.reparaciones.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.DTO.ResultadoContadores;
import com.Clover.prueba.domain.reparaciones.usecase.CalcularContadoresUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.FiltrarListaUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetAllReparacionesUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetClienteUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.GetReparacionByIDUseCase;

import java.util.ArrayList;

public class ReparacionesViewModel extends ViewModel {
    //Reparaciones View
    private final MutableLiveData<ArrayList<Reparaciones>> _reparcionesAMostrar = new MutableLiveData<>();
    public final LiveData<ArrayList<Reparaciones>> reparacionesAMostrar = _reparcionesAMostrar;
    private final MutableLiveData<ResultadoContadores> _contadoresResult = new MutableLiveData<>();
    public final LiveData<ResultadoContadores> contadoresResult = _contadoresResult;
    private ArrayList<Reparaciones> listaCompleta;
        //UseCase
    private final CalcularContadoresUseCase calcularContadoresUseCase;
    private final FiltrarListaUseCase filtrarListaUseCase;
    private final GetAllReparacionesUseCase getAllReparaciones;

    private final MutableLiveData<Reparaciones> _reparacion = new MutableLiveData<>();
    public final LiveData<Reparaciones> reparacion = _reparacion;

    private final MutableLiveData<Clientes> _cliente = new MutableLiveData<>();
    public final LiveData<Clientes> cliente = _cliente;

    private final MutableLiveData<String> _mensaje = new MutableLiveData<>();
    public final LiveData<String> mensaje = _mensaje;

    private final MutableLiveData<Boolean> _eliminado = new MutableLiveData<>();
    public final LiveData<Boolean> eliminado = _eliminado;

    //USECASE
    private final GetClienteUseCase getClienteUseCase;
    private final GetReparacionByIDUseCase getReparacionByIDUseCase;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public ReparacionesViewModel(CalcularContadoresUseCase calcularContadoresUseCase, FiltrarListaUseCase filtrarListaUseCase, GetAllReparacionesUseCase getAllReparaciones, GetClienteUseCase getClienteUseCase, GetReparacionByIDUseCase getReparacionByIDUseCase) {
        this.calcularContadoresUseCase = calcularContadoresUseCase;
        this.filtrarListaUseCase = filtrarListaUseCase;
        this.getAllReparaciones = getAllReparaciones;
        this.getClienteUseCase = getClienteUseCase;
        this.getReparacionByIDUseCase = getReparacionByIDUseCase;
        listaCompleta = getAllReparaciones.execute();
    }
    /*
    *   Metodos para ReparacionView
    *  */
    public void aplicarFiltros(String query, String estado, String servicio){
        try{
            _reparcionesAMostrar.setValue(filtrarListaUseCase.execute(listaCompleta, query,estado,servicio));
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

    public void cargarReparacion(int idReparacion) {
        try {
            Reparaciones rep = getReparacionByIDUseCase.execute(idReparacion);
            _reparacion.setValue(rep);
            cargarCliente(rep.getId_cliente());
        } catch (Exception e) {
            _mensaje.setValue(e.getMessage());
        }
    }

    public void setReparacion(Reparaciones rep) {
        if (rep != null) {
            _reparacion.setValue(rep);
            cargarCliente(rep.getId_cliente());
        }
    }

    private void cargarCliente(String idCliente) {
        try {
            Clientes c = getClienteUseCase.execute(idCliente);
            _cliente.setValue(c);
        } catch(Exception e) {
            _mensaje.setValue(e.getMessage());
        }
    }
}
