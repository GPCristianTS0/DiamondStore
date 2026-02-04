package com.Clover.prueba.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CloverBD extends SQLiteOpenHelper{
    private static CloverBD instance;
    private CloverBD(@Nullable Context context) {
        super(context, "Clover.db", null, 18);
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
                "monto_pendiente REAL," +
                "total_piezas INTEGER," +
                "tipo_pago TEXT, " +
                "id_corte TEXT, " +
                "pago_con REAL," +
                "estado TEXT, " +
                "dias_plazo INTEGER, " +
                "frecuencia_pago TEXT, " +
                "fecha_limite TEXT, " +
                "banco_tarjeta TEXT, " +
                "numero_tarjeta TEXT, " +
                "numero_aprobacion TEXT, " +
                "tipo_tarjeta TEXT)");//Credito o debito
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
                "ventas_totales REAL," +
                "ventas_efectivo REAL," +
                "ventas_tarjeta REAL, " +
                "abonos_totales REAL, " +
                "gastos_totales REAL," +
                "gastos_efectivo REAL," +
                "gastos_transferencia REAL, "  +
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
        //Configuraciones
        db.execSQL("CREATE TABLE IF NOT EXISTS configuracion (" +
                "id_config INTEGER PRIMARY KEY CHECK (id_config = 1), " + // Asegura que solo exista la fila 1
                "negocio_nombre TEXT DEFAULT 'Taller Clover', " +
                "negocio_eslogan TEXT DEFAULT 'Venta y Reparación', " +
                "negocio_direccion TEXT DEFAULT '', " +
                "negocio_telefono TEXT DEFAULT '', " +
                "negocio_rfc TEXT DEFAULT '', " +
                "negocio_logo_uri TEXT DEFAULT '', " +
                "sys_stock_min INTEGER DEFAULT 5, " +
                "sys_impuesto_iva REAL DEFAULT 0.0, " +
                "sys_msg_garantia TEXT DEFAULT 'Garantía de 30 días.', " +
                "sys_msg_share TEXT DEFAULT 'Gracias por tu compra en Clover.', " +
                "printer_mac TEXT DEFAULT '', " +
                "printer_name TEXT DEFAULT '', " +
                "printer_width INTEGER DEFAULT 58, " +
                "security_pin TEXT DEFAULT '1234', " +
                "security_skip_login INTEGER DEFAULT 0 ," +
                "bank_name TEXT DEFAULT 'BBVA', " +
                "bank_account TEXT DEFAULT '1234567890', " +
                "bank_account_name TEXT DEFAULT 'Clover')");
        String INIT_CONFIG = "INSERT OR IGNORE INTO configuracion (id_config) VALUES (1)";
        db.execSQL(INIT_CONFIG);
        //Tabla para los abonos
        db.execSQL("create table IF NOT EXISTS abonos (" +
                "id_abono INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha_hora TEXT, " +
                "monto REAL, " +
                "id_corte INTEGER, " +
                "id_cliente TEXT, " +
                "saldo_anterior REAL, " +
                "saldo_nuevo REAL, " +
                "id_empleado TEXT, " +
                "tipo_pago TEXT," +
                "observaciones TEXT)");

        //Index
        db.execSQL("CREATE INDEX idx_ventas_id_cliente ON ventas (id_cliente)");
        db.execSQL("CREATE INDEX idx_ventas_id_corte ON ventas (id_corte)");
        db.execSQL("CREATE INDEX idx_det_venta_id_venta ON detalles_venta (id_venta)");
        db.execSQL("CREATE INDEX idx_cortes_fecha_apertura ON cortes_cajas (fecha_apertura)");
        db.execSQL("CREATE INDEX idx_cortes_fecha_cierre ON cortes_cajas (fecha_cierre)");
        db.execSQL("CREATE INDEX idx_gastos_fecha_hora ON gastos (fecha_hora)");
        db.execSQL("CREATE INDEX idx_abonos_fecha_hora ON abonos (fecha_hora)");
        db.execSQL("CREATE INDEX idx_ventas_fecha_Hora ON ventas (fecha_Hora)");
        db.execSQL("CREATE INDEX idx_productos_nombre_producto ON productos (nombre_producto)");
        db.execSQL("CREATE INDEX idx_ventas_estado ON ventas (estado)");
        db.execSQL("CREATE INDEX idx_abonos_id_cliente ON abonos (id_cliente)");
        db.execSQL("CREATE INDEX idx_abonos_id_corte ON abonos (id_corte)");
        db.execSQL("CREATE INDEX idx_clientes_saldo ON clientes (saldo)");
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
        if (oldVersion<7){
            db.execSQL("CREATE TABLE IF NOT EXISTS configuracion (" +
                    "id_config INTEGER PRIMARY KEY CHECK (id_config = 1), " + //Asegura que solo exista la fila 1
                    "negocio_nombre TEXT DEFAULT 'Taller Clover', " +
                    "negocio_eslogan TEXT DEFAULT 'Venta y Reparación', " +
                    "negocio_direccion TEXT DEFAULT '', " +
                    "negocio_telefono TEXT DEFAULT '', " +
                    "negocio_rfc TEXT DEFAULT '', " +
                    "negocio_logo_uri TEXT DEFAULT '', " +
                    "sys_stock_min INTEGER DEFAULT 5, " +
                    "sys_impuesto_iva REAL DEFAULT 0.0, " +
                    "sys_msg_garantia TEXT DEFAULT 'Garantía de 30 días.', " +
                    "sys_msg_share TEXT DEFAULT 'Gracias por tu compra en Clover.', " +
                    "printer_mac TEXT DEFAULT '', " +
                    "printer_name TEXT DEFAULT '', " +
                    "printer_width INTEGER DEFAULT 58, " +
                    "security_pin TEXT DEFAULT '1234', " +
                    "security_skip_login INTEGER DEFAULT 0 " +
                    ");");
            String INIT_CONFIG = "INSERT OR IGNORE INTO configuracion (id_config) VALUES (1)";
            db.execSQL(INIT_CONFIG);
        }
        if (oldVersion<8){
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN gastos_transferencia REAL DEFAULT 0.0");
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN gastos_efectivo REAL DEFAULT 0.0");
        }
        if (oldVersion<11) {
            db.execSQL("ALTER TABLE gastos DROP COLUMN ventas_tarjeta");
            db.execSQL("ALTER TABLE gastos DROP COLUMN ventas_efectivo");
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN ventas_efectivo REAL DEFAULT 0.0");
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN ventas_tarjeta REAL DEFAULT 0.0");
        }
        if (oldVersion<12){
            db.execSQL("ALTER TABLE ventas ADD COLUMN id_corte TEXT");
        }
        if (oldVersion<13){
            db.execSQL("ALTER TABLE configuracion ADD COLUMN bank_name TEXT");
            db.execSQL("ALTER TABLE configuracion ADD COLUMN bank_account TEXT");
            db.execSQL("ALTER TABLE configuracion ADD COLUMN bank_account_name TEXT");
        }
        if (oldVersion<14){
            //Modificacion de la tabla ventas para el sistema de credito
            db.execSQL("ALTER TABLE ventas ADD COLUMN banco_tarjeta TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN numero_tarjeta TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN numero_aprobacion TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN estado TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN dias_plazo INTEGER");
            db.execSQL("ALTER TABLE ventas ADD COLUMN frecuencia_pago TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN fecha_limite TEXT");
            db.execSQL("ALTER TABLE ventas ADD COLUMN tipo_tarjeta TEXT");
            //Creacion de la tabla abonos
            db.execSQL("create table IF NOT EXISTS abonos (" +
                    "id_abono INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fecha_hora TEXT, " +
                    "monto REAL, " +
                    "id_corte INTEGER, " +
                    "id_cliente TEXT, " +
                    "saldo_anterior REAL, " +
                    "saldo_nuevo REAL, " +
                    "id_empleado TEXT, " +
                    "observaciones TEXT)");
        }
        if (oldVersion<15){
            db.execSQL("ALTER TABLE ventas ADD COLUMN pago_con REAL");
        }
        if (oldVersion<16){
            db.execSQL("ALTER TABLE abonos ADD COLUMN tipo_pago TEXT");
        }
        if (oldVersion<17) {
            db.execSQL("ALTER TABLE ventas ADD COLUMN monto_pendiente REAL");
        }
        if (oldVersion<18){
            db.execSQL("CREATE INDEX idx_ventas_id_cliente ON ventas (id_cliente)");
            db.execSQL("CREATE INDEX idx_ventas_id_corte ON ventas (id_corte)");
            db.execSQL("CREATE INDEX idx_det_venta_id_venta ON detalles_venta (id_venta)");
            db.execSQL("CREATE INDEX idx_cortes_fecha_apertura ON cortes_cajas (fecha_apertura)");
            db.execSQL("CREATE INDEX idx_cortes_fecha_cierre ON cortes_cajas (fecha_cierre)");
            db.execSQL("CREATE INDEX idx_gastos_fecha_hora ON gastos (fecha_hora)");
            db.execSQL("CREATE INDEX idx_abonos_fecha_hora ON abonos (fecha_hora)");
            db.execSQL("CREATE INDEX idx_ventas_fecha_Hora ON ventas (fecha_Hora)");
            db.execSQL("CREATE INDEX idx_productos_nombre_producto ON productos (nombre_producto)");
            db.execSQL("CREATE INDEX idx_ventas_estado ON ventas (estado)");
            db.execSQL("CREATE INDEX idx_abonos_id_cliente ON abonos (id_cliente)");
            db.execSQL("CREATE INDEX idx_abonos_id_corte ON abonos (id_corte)");
            db.execSQL("CREATE INDEX idx_clientes_saldo ON clientes (saldo)");
        }
    }
    public static synchronized CloverBD getInstance(Context context) {
        if (instance == null) {
            instance = new CloverBD(context.getApplicationContext());
        }
        return instance;
    }

}
