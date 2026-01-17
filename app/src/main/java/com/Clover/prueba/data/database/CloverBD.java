package com.Clover.prueba.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CloverBD extends SQLiteOpenHelper{
    private static CloverBD instance;
    private CloverBD(@Nullable Context context) {
        super(context, "Clover.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tabla productos
        db.execSQL("create table productos (" +
                "rutaImagen TEXT," +
                "id_producto TEXT PRIMARY KEY, " +
                "nombre_producto TEXT," +
                "marca TEXT," +
                "seccion INTEGER," +
                "precioPublico INTEGER," +
                "precioNeto INTEGER," +
                "descripcion TEXT," +
                "vendidos INTEGER," +
                "stock INTEGER," +
                "ultimo_pedido TEXT)");
        //Tabla secciones de productos
        db.execSQL("create table secciones (" +
                "id_seccion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_seccion TEXT)");
        //Tabla Ventas
        db.execSQL("create table IF NOT EXISTS ventas (" +
                "id_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_cliente TEXT," +
                "fecha_Hora TEXT,"+
                "monto INTEGER," +
                "total_piezas INTEGER," +
                "tipo_pago TEXT)");
        //Tabla detalle_venta
        db.execSQL("create table IF NOT EXISTS detalles_venta (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_venta INTEGER," +
                "id_producto TEXT," +
                "nombre_producto TEXT," +
                "cantidad INTEGER," +
                "precio INTEGER," +
                "precio_neto_historial INTEGER)");
        //Tabla Clientes
        db.execSQL("create table IF NOT EXISTS clientes (" +
                "id_cliente TEXT PRIMARY KEY, " +
                "nombre_cliente TEXT ," +
                " apodo TEXT," +
                " direccion TEXT," +
                "fecha_registro TEXT, " +
                "saldo INTEGER, " +
                "puntos INTEGER)");
        //Tabla Proveedores
        db.execSQL("create table IF NOT EXISTS proveedores (" +
                "id_proveedor INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_proveedor TEXT, " +
                "nombre_vendedor TEXT, " +
                "categoria TEXT, " +
                "direccion TEXT, " +
                "telefono TEXT, " +
                "email TEXT, " +
                "dias_visita TEXT, " +
                "observaciones TEXT, " +
                "fecha_registro TEXT, " +
                "diasPago TEXT)");
        //Tabla para el corte de caja
        db.execSQL("create table IF NOT EXISTS cortes_cajas (" +
                "id_corte INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha_apertura TEXT, " +
                "fecha_cierre TEXT, " +
                "monto_inicial REAL, " +
                "ventas_totales REAL, " +
                "abonos_totales REAL, " +
                "gastos_totales REAL, "  +
                "dinero_en_caja REAL, " +
                "diferencia REAL, " +
                "estado TEXT)");
        //Tabla para Gastos
        db.execSQL("create table IF NOT EXISTS gastos (" +
                "id_gasto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descripcion TEXT," +
                "monto REAL," +
                "fecha_hora TEXT," +
                "metodo_pago TEXT," +
                "id_corte INTEGER," +
                "id_proveedor INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2){
            db.execSQL("ALTER TABLE detalles_venta ADD COLUMN precio_neto_historial INTEGER DEFAULT 0");
        }
        if (oldVersion<3){
            db.execSQL("create table IF NOT EXISTS cortes_cajas (" +
                    "id_corte INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fecha_apertura TEXT, " +
                    "fecha_cierre TEXT, " +
                    "monto_inicial REAL, " +
                    "ventas_totales REAL, " +
                    "abonos_totales REAL, " +
                    "gastos_totales REAL, "  +
                    "dinero_en_caja REAL, " +
                    "diferencia REAL, " +
                    "estado TEXT)");
        }
        if (oldVersion<4){
            db.execSQL("create table IF NOT EXISTS proveedores (" +
                    "id_proveedor INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_proveedor TEXT, " +
                    "nombre_vendedor TEXT, " +
                    "categoria TEXT, " +
                    "direccion TEXT, " +
                    "telefono TEXT, " +
                    "email TEXT, " +
                    "dias_visita TEXT, " +
                    "observaciones TEXT, " +
                    "fecha_registro TEXT, " +
                    "diasPago TEXT)");
        }
        if (oldVersion<6){
            db.execSQL("create table IF NOT EXISTS gastos (" +
                    "id_gasto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descripcion TEXT," +
                    "monto REAL," +
                    "fecha_hora TEXT," +
                    "metodo_pago TEXT," +
                    "id_corte INTEGER," +
                    "id_proveedor INTEGER)");
        }
    }
    public static synchronized CloverBD getInstance(Context context) {
        if (instance == null) {
            instance = new CloverBD(context.getApplicationContext());
        }
        return instance;
    }

}
