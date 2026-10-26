package com.Clover.prueba.domain.corteCaja.usecase;

import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.models.CorteCaja;
import com.Clover.prueba.domain.corteCaja.EstadoCaja;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IniciarTurnoUseCase {
    private ICorteCaja repository;

    public IniciarTurnoUseCase(ICorteCaja repository) {
        this.repository = repository;
    }

    public boolean execute(String monto) throws Exception {
        CorteCaja corteCaja = new CorteCaja();
        if (monto.isEmpty()) throw new Exception("Ingrese un valor");
        int montoInicial = Integer.parseInt(monto);
        if (montoInicial<0){
            throw new Exception("Ingrese un monto valido");
        }
        corteCaja.setMonto_inicial(montoInicial);
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        corteCaja.setFecha_apertura(fechaHora);
        corteCaja.setEstado(EstadoCaja.ABIERTO.getValor());
        return repository.iniciarCorte(corteCaja);
    }
}
