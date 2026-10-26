package com.Clover.prueba.data.database;

public interface SyncCallback {
    void onInicio();
    void onProgreso(String mensaje);
    void onExito(String mensaje);
    void onError(String mensaje);
}
