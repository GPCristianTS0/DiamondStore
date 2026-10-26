package com.Clover.prueba.ui.reparaciones.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.IConfiguracion;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.ActualizarEstadoUSECASE;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.NotaReparacionUC;
import com.Clover.prueba.domain.reparaciones.usecase.Formulario.AddClientReparaciones;
import com.Clover.prueba.domain.reparaciones.usecase.Formulario.AddReparacionUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.CalcularContadoresUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion.DeleteReparacionUSECASE;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.FiltrarListaUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.Formulario.GetAllClient;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.GetAllReparacionesUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.Formulario.GetClienteUseCase;
import com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal.GetReparacionByIDUseCase;
import com.Clover.prueba.domain.reparaciones.validators.TicketsValidator;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.generators.TicketReparacionGenerator;
import com.Clover.prueba.services.generators.TicketUtils;
import com.Clover.prueba.services.sharing.ShareManager;
import com.Clover.prueba.services.storage.StorageImage;

public class ReparacionesFactory implements ViewModelProvider.Factory {
    private Context context;
    public ReparacionesFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        IReparaciones repaDAO = new ReparacionesDAO(context);
        IClient clientDAO = new ClientesDAO(context);
        ICorteCaja corteCajaDAO = new CorteCajaDAO(context);
        IConfiguracion configuracionDao = new ConfiguracionDAO(context);
        TicketUtils ticketUtils = new TicketUtils(context);
        ShareManager shareManager = new ShareManager(context);
        StorageImage storageImage = new StorageImage(context);
        ImageGenerator imageGenerator = new ImageGenerator();
        TicketsValidator ticketsValidator = new TicketsValidator();
        TicketReparacionGenerator ticketReparacionGenerator = new TicketReparacionGenerator(context);
        NotaReparacionUC notaReparacionUC = new NotaReparacionUC(configuracionDao, storageImage, imageGenerator, ticketReparacionGenerator, shareManager, ticketsValidator);

        if (modelClass.isAssignableFrom(ReparacionesViewModel.class)){

            //usecase
            CalcularContadoresUseCase calcularContadoresUseCase = new CalcularContadoresUseCase();
            FiltrarListaUseCase filtrarListaUseCase = new FiltrarListaUseCase();
            GetAllReparacionesUseCase getAllReparaciones = new GetAllReparacionesUseCase(repaDAO);

            return (T) new ReparacionesViewModel(calcularContadoresUseCase, filtrarListaUseCase,getAllReparaciones);
        }
        if (modelClass.isAssignableFrom(FormularioViewModel.class)){
            GetAllClient getAllClient = new GetAllClient(clientDAO);
            AddClientReparaciones addClientReparaciones = new AddClientReparaciones(clientDAO);
            AddReparacionUseCase addReparacionUseCase = new AddReparacionUseCase(repaDAO, corteCajaDAO);
            return (T) new FormularioViewModel(getAllClient, addClientReparaciones, addReparacionUseCase, notaReparacionUC,ticketUtils, shareManager);
        }
        if (modelClass.isAssignableFrom(DetallesReparacionVM.class)){
            //UseCase
            GetClienteUseCase getClienteUseCase = new GetClienteUseCase(clientDAO);
            GetReparacionByIDUseCase getReparacionByIDUseCase = new GetReparacionByIDUseCase(repaDAO);
            ActualizarEstadoUSECASE actualizarEstadoUSECASE = new ActualizarEstadoUSECASE(repaDAO);
            DeleteReparacionUSECASE deleteReparacionUSECASE = new DeleteReparacionUSECASE(repaDAO);


            return (T) new DetallesReparacionVM(getClienteUseCase,getReparacionByIDUseCase, actualizarEstadoUSECASE, deleteReparacionUSECASE, notaReparacionUC);
        }
        throw new IllegalArgumentException("Clase ViewModel Desconocidad");
    }

}
