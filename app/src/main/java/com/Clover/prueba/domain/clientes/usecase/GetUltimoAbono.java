package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.utils.FormatterFechas;

public class GetUltimoAbono {
    private final IAbonos abonosDAO;
    public GetUltimoAbono(IAbonos abonosDAO) {
        this.abonosDAO = abonosDAO;
    }
    public String execute(String id_cliente) {
        Abonos abono = abonosDAO.getUltimoAbono(id_cliente);
        if (abono==null) throw new RuntimeException("Sin Abonos");
        return FormatterFechas.formatDate(abono.getFecha(), "dd MMMM yyyy", false);
    }
}
