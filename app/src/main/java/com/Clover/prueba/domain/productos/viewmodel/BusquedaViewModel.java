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

    private List<Productos> listaBaseProductos = new ArrayList<>();
    private boolean filtroRefaccion = false;
    private boolean filtroVenta = false;
    private boolean filtroNoVenta = false;
    private boolean filtroDisponible = true;

    public BusquedaViewModel(GetSeccionesUseCase seccionesUseCase, GetProductBy productosByUseCase, GetProductos productosUseCase) {
        this.seccionesUseCase = seccionesUseCase;
        this.productosByUseCase = productosByUseCase;
        this.productosUseCase = productosUseCase;
    }

    public ArrayList<String> getSecciones() {
        return seccionesUseCase.execute();
    }

    public void setFiltrosChips(boolean refaccion, boolean venta, boolean noVenta, boolean disponible) {
        this.filtroRefaccion = refaccion;
        this.filtroVenta = venta;
        this.filtroNoVenta = noVenta;
        this.filtroDisponible = disponible;
        filtrarYEmitir();
    }

    public void getProductos() {
        try {
            listaBaseProductos = productosUseCase.execute();
            if (listaBaseProductos == null) listaBaseProductos = new ArrayList<>();
            filtrarYEmitir();
        } catch (Exception e) {
            _productos.setValue(new ArrayList<>());
        }
    }

    public void buscarProductoPor(int seccion, String columnaObtencion, String busqueda) {
        try {
            listaBaseProductos = productosByUseCase.execute(seccion, columnaObtencion, busqueda);
            if (listaBaseProductos == null) listaBaseProductos = new ArrayList<>();
            filtrarYEmitir();
        } catch (Exception e) {
            _productos.setValue(new ArrayList<>());
        }
    }

    private void filtrarYEmitir() {
        if (listaBaseProductos == null) {
            _productos.setValue(new ArrayList<>());
            return;
        }

        List<Productos> filtrados = new ArrayList<>();
        for (Productos p : listaBaseProductos) {
            boolean cumpleTipo = true;
            if (filtroRefaccion) {
                cumpleTipo = p.isRefaccion();
            } else if (filtroVenta) {
                cumpleTipo = !p.isRefaccion();
            }

            boolean cumpleVisibilidad = true;
            if (filtroNoVenta) {
                cumpleVisibilidad = p.isVisible();
            }

            boolean cumpleDisponible = true;
            if (filtroDisponible) {
                cumpleDisponible = p.getStock() > 0;
            }

            if (cumpleTipo && cumpleVisibilidad && cumpleDisponible) {
                filtrados.add(p);
            }
        }

        _productos.setValue(filtrados);
    }
}
