package com.Clover.prueba.domain.productos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.usecase.GetProductBy;
import com.Clover.prueba.domain.productos.usecase.GetProductos;
import com.Clover.prueba.domain.productos.usecase.GetSeccionesUseCase;

import java.util.ArrayList;
import java.util.List;

public class BusquedaViewModel extends ViewModel {

    private final MutableLiveData<List<Productos>> _productos = new MutableLiveData<>();
    public LiveData<List<Productos>> productos = _productos;
    private final GetSeccionesUseCase seccionesUseCase;
    private final GetProductBy productosByUseCase;
    private final GetProductos productosUseCase;


    public BusquedaViewModel(GetSeccionesUseCase seccionesUseCase, GetProductBy productosByUseCase, GetProductos productosUseCase) {
        this.seccionesUseCase = seccionesUseCase;
        this.productosByUseCase = productosByUseCase;
        this.productosUseCase = productosUseCase;
    }

    public ArrayList<String> getSecciones(){
        return seccionesUseCase.execute();
    }
    public void getProductos(){
        try {
            _productos.setValue(productosUseCase.execute());
        } catch (Exception e) {

        }
    }
    public void buscarProductoPor(int seccion, String columnaObtencion, String busqueda){
        try {
            _productos.setValue(productosByUseCase.execute(seccion, columnaObtencion, busqueda));
        } catch (Exception e) {

        }
    }
}
