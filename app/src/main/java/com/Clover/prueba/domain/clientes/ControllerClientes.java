package com.Clover.prueba.domain.clientes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.Clover.prueba.data.dao.AbonosDAO;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.dto.ProductoMasCompradoDTO;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.services.generators.CardClientGenerator;
import com.Clover.prueba.services.generators.GeneradorQR;
import com.Clover.prueba.services.sharing.ShareManager;
import com.Clover.prueba.services.storage.StorageImage;
import com.Clover.prueba.utils.FormatterFechas;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ControllerClientes {
    private Context context;
    private IVentas ventasDAO;
    private IAbonos abonosDAO;
    private IClient clientesDAO;
    private GeneradorQR qr;
    public ControllerClientes(Context context) {
        this.context = context;
        ventasDAO = new VentasDAO(context);
        abonosDAO = new AbonosDAO(context);
        clientesDAO = new ClientesDAO(context);
        qr = new GeneradorQR(context);
    }
    public double getSaldoPendiente(String idCliente){
        return ventasDAO.getSaldoPendiente(idCliente);
    }
    public double getSaldoTotal(String idCliente){
        return ventasDAO.getSaldoTotal(idCliente);
    }
    public ArrayList<Abonos> getAbonos(String id_cliente){
        return abonosDAO.getAbonos(id_cliente);
    }
    /**
     * Obtiene los productos mas comprados de un cliente y los regresa en un arraylist
     *
     * @param idCliente Identificador del cliente (Tabla Clientes)
     * @param noProductos Cantidad de productos a obtener (Tabla Productos)
     *
     */
    public ArrayList<ProductoMasCompradoDTO> getMasComprados(String idCliente, int noProductos){
        return ventasDAO.getMasCompradobyCliente(idCliente, noProductos);
    }
    public int getVentasTotales(String idCliente){
        return ventasDAO.getVentasTotales(idCliente) ;
    }
    public String getTicketPromedio(String idCliente){
        double ticket = ventasDAO.getTicketPromedio(idCliente);
        if (ticket<=0) return "Sin Tickets";
        DecimalFormat formatoDinero = new DecimalFormat("$ #,##0.00");
        return formatoDinero.format(ticket);
    }
    public String getUltimoAbono(String idCliente){
        Abonos abono = abonosDAO.getUltimoAbono(idCliente);
        if (abono==null) return "Sin Abonos";
        return FormatterFechas.formatDate(abono.getFecha(), "dd MMMM yyyy", false);
    }

    public boolean deleteAbono(Abonos abono){
        return false;
    }
    public void compartirTicket(Abonos abono){

    }
    public boolean compartirCard(Clientes cliente) {
        //Genera y guarda el QR
        Bitmap bitmap = qr.generarQR(cliente.getId_cliente());
        //Genera el view con el qr y los datos del cliente
        CardClientGenerator cardClientGenerator = new CardClientGenerator();
        View viewCard = cardClientGenerator.generarCardCliente(context, cliente, bitmap);
        //Genera y guarda la imagen
        StorageImage storageImage = new StorageImage(context);
        String rutaImagen = storageImage.guardarImgaenTemporal(context, )
        //Compartir imagen
        ShareManager shareManager = new ShareManager();
        shareManager.compartirImagen(context, "Gracias por ser parte de nosotros", null);
        return false;
    }

    public Clientes getClientes(String idCliente) {
        return clientesDAO.getClient(idCliente);
    }
}
