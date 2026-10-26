package com.Clover.prueba.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

public class SyncManager {
    private static final String TAG = "SYNC_MANAGER";
    private static final String PREFS_NAME = "CloverSyncPrefs";

    private final Context context;
    private final SQLiteDatabase db;
    private final FirebaseFirestore firestore;
    private final SharedPreferences prefs;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private final String[][] TABLAS_SYNC = {
            {"configuracion", "id_config"},
            {"secciones", "id_seccion"},
            {"productos", "id_producto"},
            {"clientes", "id_cliente"},
            {"proveedores", "id_proveedor"},
            {"cortes_cajas", "id_corte"},
            {"ventas", "id_venta"},
            {"detalles_venta", "id_detalle"},
            {"reparaciones", "id_reparacion"},
            {"gastos", "id_gasto"},
            {"abonos", "id_abono"}
    };
    public SyncManager(Context context) {
        this.context = context;
        this.db = CloverBD.getInstance(context).getWritableDatabase();
        this.firestore = FirebaseFirestore.getInstance();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    public void sincronizarTodo(SyncCallback callback){
        if (callback != null) callback.onInicio();

        executor.execute(()->{
            try{
                for (String[] par: TABLAS_SYNC){
                    String tabla  = par[0];
                    String pk = par[1];
                    notificarProgreso(callback, "");
                    subirPendientes(tabla, pk);
                }
                for (String[] par: TABLAS_SYNC){
                    String tabla = par[0];
                    String pk = par[1];
                    notificarProgreso(callback, "");
                    descargarNovedades(tabla, pk);
                }

                mainHandler.post(()->{
                    if (callback!=null) callback.onExito("Sincronizacion completada con exito!");
                });

            } catch (Exception e) {
                Log.e("Clover_App", "Error durante la sincronización", e);
                mainHandler.post(() -> {
                    if (callback != null) callback.onError("Error de conexión: " + e.getMessage());
                });
            }
        });
    }
    private void subirPendientes(String tabla, String pk) throws Exception{
        Cursor cursor = db.rawQuery("SELECT * FROM " + tabla + " WHERE sincronizado = 0", null);
        if (cursor == null) return;

        if (cursor.getCount() == 0) {
            cursor.close();
            return;
        }

        String[] columnas = cursor.getColumnNames();
        WriteBatch batch = firestore.batch();
        int batchCount = 0;

        while (cursor.moveToNext()){
            Map<String, Object> data  = new HashMap<>();
            String idDoc = null;

            for (int i = 0; i< columnas.length; i++){
                String col = columnas[i];
                if (col.equalsIgnoreCase("sincronizado")) continue;

                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        long valInt = cursor.getLong(i);
                        data.put(col,valInt);
                        if (col.equalsIgnoreCase(pk)) idDoc = String.valueOf(valInt);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        data.put(col, cursor.getDouble(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        String valStr = cursor.getString(i);
                        data.put(col, valStr);
                        if (col.equalsIgnoreCase(pk)) idDoc = valStr;
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        data.put(col, null);
                        break;
                }
            }
            if (idDoc != null && !idDoc.trim().isEmpty()){
                batch.set(firestore.collection(tabla).document(idDoc), data);
                batchCount++;
            }

            if (batchCount >= 400){
                Tasks.await(batch.commit());
                batch = firestore.batch();
                batchCount = 0;
            }
        }
        if (batchCount > 0){
            Tasks.await(batch.commit());
        }
        cursor.close();

        //Actualizacion de sincronizado
        ContentValues values = new ContentValues();
        values.put("sincronizado", 1);
        db.update(tabla,values, "sincronizado = 0", null);
    }
    private void descargarNovedades(String tabla, String pk) throws Exception{
        String keyPref = "sync_ts_"+tabla;
        long ultimaSync = prefs.getLong(keyPref, 0L);

        QuerySnapshot snapshots = Tasks.await(
                firestore.collection(tabla).whereGreaterThan("fecha_actualizacion", ultimaSync).get()
        );

        if (snapshots == null || snapshots.isEmpty()){
            return;
        }

        long nuevoTimestamp = ultimaSync;
        db.beginTransaction();
        try{
            for (DocumentSnapshot doc: snapshots.getDocuments()) {
                Map<String, Object> data = doc.getData();
                if (data == null) continue;

                ContentValues values = new ContentValues();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String col = entry.getKey();
                    Object val = entry.getValue();

                    if (val instanceof Long || val instanceof Integer) {
                        values.put(col, ((Number) val).longValue());
                    } else if (val instanceof Double || val instanceof Float) {
                        values.put(col, ((Number) val).doubleValue());
                    } else if (val instanceof String) {
                        values.put(col, (String) val);
                    } else if (val instanceof Boolean) {
                        values.put(col, (Boolean) val ? 1 : 0);
                    }
                }

                values.put("sincronizado", 1);

                db.insertWithOnConflict(tabla, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                if (data.containsKey("fecha_actualizacion")) {
                    Object fObj = data.get("fecha_actualizacion");
                    if (fObj instanceof Number) {
                        long f = ((Number) fObj).longValue();
                        if (f > nuevoTimestamp) {
                            nuevoTimestamp = f;
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
            prefs.edit().putLong(keyPref, nuevoTimestamp).apply();
        } finally {
            db.endTransaction();
        }
    }
    private void notificarProgreso(SyncCallback callback, String mensaje) {
        if (callback != null) {
            mainHandler.post(() -> callback.onProgreso(mensaje));
        }
    }
}
