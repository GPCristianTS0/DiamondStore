package com.Clover.prueba.domain.productos.viewmodel;


import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.GenerarEtiquetaProductoUseCase;
import com.Clover.prueba.domain.productos.usecase.GetProductById;

public class ProductosViewModel extends ViewModel {
    private final MutableLiveData<Productos> _producto = new MutableLiveData<>();
    private final LiveData<Productos> producto = _producto;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final LiveData<String> mensaje = _error;
    private final MutableLiveData<Uri> _etiqueta = new MutableLiveData<>();
    private final LiveData<Uri> etiqueta = _etiqueta;

    private final GenerarEtiquetaProductoUseCase etiquetaProductosUseCase;
    private final GetProductById getProductById;
    public ProductosViewModel(GenerarEtiquetaProductoUseCase etiquetaProductosUseCase, GetProductById getProductById) {
        this.etiquetaProductosUseCase = etiquetaProductosUseCase;
        this.getProductById = getProductById;
    }

    public void generarEtiqueta(Productos producto, Configuracion conf) {
        try{
            _etiqueta.setValue(etiquetaProductosUseCase.ejecutar(producto, conf));
        } catch (Exception e) {
            _error.setValue("No se pudo generar la etiqueta");
        }
    }
    public void cargarProducto(String id) {
        try{
            _producto.setValue(getProductById.execute(id));
        } catch (Exception e) {
            _error.setValue("No se pudo cargar el producto");
        }
    }
    public void clearEtiqueta() {
        _etiqueta.setValue(null);
    }

    public LiveData<Uri> getEtiqueta() {
        return etiqueta;
    }
    public LiveData<String> getMensaje() {
        return mensaje;
    }
    public LiveData<Productos> getProducto() {
        return producto;
    }
}
