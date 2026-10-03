package com.Clover.prueba.ui.corte;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.CorteCajaDAO;
import com.Clover.prueba.data.dao.interfaces.ICorteCaja;
import com.Clover.prueba.domain.corteCaja.CorteCajaController;
import com.Clover.prueba.domain.corteCaja.CorteViewModel;
import com.Clover.prueba.domain.corteCaja.usecase.IniciarTurnoUseCase;
import com.Clover.prueba.services.Helpers.BannerError;
import com.google.android.material.textfield.TextInputEditText;

public class DialogAbrirCorte extends DialogFragment {
    private CorteViewModel viewModel;
    public interface OnMontoConfirmado {

        void onMontoConfirmado();
    }
    private OnMontoConfirmado onMontoConfirmado;
    public void setOnMontoConfirmado(OnMontoConfirmado onMontoConfirmado) {
        this.onMontoConfirmado = onMontoConfirmado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ventas_inicio_corte, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View rootView = requireActivity().findViewById(android.R.id.content);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null){
                    getActivity().finish();
                }
            }
        };
        //viewModel
        ICorteCaja repositoryCorte = new CorteCajaDAO(view.getContext());
        IniciarTurnoUseCase usecase = new IniciarTurnoUseCase(repositoryCorte);

        viewModel = new CorteViewModel(usecase);
        viewModel.getEstadoCorteActual().observe(this, estado -> {
            if (estado){
                if (onMontoConfirmado != null) {
                    onMontoConfirmado.onMontoConfirmado();
                }
                callback.setEnabled(false);
                dismiss();
            }
        });
        viewModel.getError().observe(this, mensaje ->{
            BannerError.mostrarError(view, mensaje);
        });
        Button b = view.findViewById(R.id.VCorte_boton);
        TextInputEditText t = view.findViewById(R.id.VCorte_montotxt);

        b.setOnClickListener(v -> viewModel.iniciarTurno(t.getText().toString()));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }
}
