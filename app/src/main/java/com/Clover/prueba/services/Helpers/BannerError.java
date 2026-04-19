package com.Clover.prueba.services.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Clover.prueba.R;

public class BannerError {
    public static void mostrarError(View view, String mensaje) {
        ViewGroup rootView = (ViewGroup) view.getRootView();

        View banner = rootView.findViewById(R.id.cvErrorBanner);
        if (banner == null) {
            banner = LayoutInflater.from(view.getContext()).inflate(R.layout.error_layout, rootView, false);
            rootView.addView(banner);
        }

        banner.setVisibility(View.VISIBLE);
        TextView errorMessage = banner.findViewById(R.id.tvErrorMessage);
        errorMessage.setText(mensaje);

        banner.setTranslationY(-300f);
        banner.setVisibility(View.VISIBLE);

        View finalBanner = banner;
        banner.animate()
                .translationY(0f)
                .setDuration(200)
                .withEndAction(() -> {
                    finalBanner.postDelayed(() -> {
                        finalBanner.animate()
                            .translationY(-300f)
                            .setDuration(200)
                            .withEndAction(() -> finalBanner.setVisibility(View.GONE)).start();
                    }, 2000);
                });

    }
}
