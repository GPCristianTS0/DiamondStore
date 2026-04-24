package com.Clover.prueba.domain.productos.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.usecase.AddProductUseCase;
import com.Clover.prueba.domain.productos.usecase.GetProductById;
import com.Clover.prueba.domain.productos.usecase.GetSecciones;
import com.Clover.prueba.domain.productos.usecase.UpdateProductUseCase;

import java.util.ArrayList;

public class FormularioProductosViewModel extends ViewModel {
    private final MutableLiveData<String> _exito = new MutableLiveData<>();
    private final LiveData<String> exito = _exito;
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final LiveData<String> error = _error;
    private final AddProductUseCase addProductUseCase;
    private final GetProductById getProductById;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetSecciones getSecciones;

    public FormularioProductosViewModel(AddProductUseCase addProductUseCase, GetProductById getProductById, UpdateProductUseCase updateProductUseCase, GetSecciones getSecciones) {
        this.addProductUseCase = addProductUseCase;
        this.getProductById = getProductById;
        this.updateProductUseCase = updateProductUseCase;
        this.getSecciones = getSecciones;
    }
    public boolean isInvalidCodigo(String codigo){
        try{
            return getProductById.execute(codigo) != null;
        } catch (Exception e) {
            _error.setValue(e.getMessage());
            return false;
        }
    }

    public LiveData<String> getError() {
        return _error;
    }
    public LiveData<String> getExito(){
        return _exito;
    }
    public void addProduct(Productos producto, Uri uri){
        try{
            addProductUseCase.addProduct(producto, uri);
            _exito.setValue("Producto agregado");
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public void updateProducto(Productos productoNew, Productos productoOld, Uri uri) {
        try{
            updateProductUseCase.updateProduct(productoOld, productoNew, uri);
            _exito.setValue("Producto actualizado");
        } catch (Exception e) {
            _error.setValue(e.getMessage());
        }
    }
    public ArrayList<String> getSeccion(){
        return getSecciones.execute();
    }
    public int getSeccion(String id){
        return getSecciones.execute().indexOf(id);
    }
}
