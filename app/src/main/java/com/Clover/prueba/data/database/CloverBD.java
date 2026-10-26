package com.Clover.prueba.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CloverBD extends SQLiteOpenHelper{
    private static CloverBD instance;
    private CloverBD(@Nullable Context context) {
        super(context, "Clover.db", null, 25);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tabla productos
        db.execSQL("create table productos (" +
                "rutaImagen TEXT," +
                "id_producto TEXT PRIMARY KEY, " +
                "nombre_producto TEXT," +
                "marca TEXT," +
                "modelo TEXT," +
                "seccion INTEGER," +
                "precioPublico INTEGER," +
                "precioNeto INTEGER," +
                "descripcion TEXT," +
                "vendidos INTEGER," +
                "stock INTEGER," +
                "ventaxpeso INTEGER," +
                "ultimo_pedido TEXT," +
                "isRefaccion INTEGER," +
                "calidad TEXT," +
                "modelo_compatible TEXT," +
                "condicion TEXT," +
                "grado_estetico TEXT," +
                "visible_en_busqueda INTEGER DEFAULT 1, " +
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
        //Tabla secciones de productos
        db.execSQL("create table secciones (" +
                "id_seccion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_seccion TEXT,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
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
                "tipo_tarjeta TEXT,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
        //Tabla detalle_venta
        db.execSQL("create table IF NOT EXISTS detalles_venta (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_venta INTEGER," +
                "id_producto TEXT," +
                "nombre_producto TEXT," +
                "cantidad INTEGER," +
                "precio INTEGER," +
                "precio_neto_historial INTEGER,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
        //Tabla Clientes
        db.execSQL("create table IF NOT EXISTS clientes (" +
                "id_cliente TEXT PRIMARY KEY, " +
                "nombre_cliente TEXT ," +
                " apodo TEXT," +
                " direccion TEXT," +
                "fecha_registro TEXT, " +
                "saldo INTEGER, " +
                "puntos INTEGER,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
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
                "diasPago TEXT,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
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
                "estado TEXT,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
        //Tabla para Gastos
        db.execSQL("create table IF NOT EXISTS gastos (" +
                "id_gasto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descripcion TEXT," +
                "monto REAL," +
                "fecha_hora TEXT," +
                "metodo_pago TEXT," +
                "id_corte INTEGER," +
                "id_proveedor INTEGER,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
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
                "bank_account_name TEXT DEFAULT 'Clover',"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");
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
                "observaciones TEXT,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");

        //Tabla para las reparaciones
        db.execSQL("create table IF NOT EXISTS reparaciones (" +
                "id_reparacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo_servicio TEXT, " +
                "id_cliente TEXT, " +
                "equipo TEXT, " +
                "color TEXT, " +
                "falla_reportada TEXT, " +
                "diagnostico TEXT, " +
                "costo_refacciones REAL, " +
                "monto_total REAL, " +
                "anticipo REAL DEFAULT 0, " +
                "monto_pendiente REAL, " +
                "tipo_pago TEXT, " +
                "estado TEXT, " +
                "fecha_ingreso TEXT, " +
                "fecha_promesa TEXT, " +
                "fecha_entrega TEXT, " +
                "id_corte INTEGER,"+
                "fecha_actualizacion INTEGER DEFAULT 0," +
                "sincronizado INTEGER DEFAULT 0, "+
                "eliminado INTEGER DEFAULT 0)");

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
        db.execSQL("CREATE INDEX idx_reparaciones_id_corte ON reparaciones (id_corte)");
        db.execSQL("CREATE INDEX idx_reparaciones_id_cliente ON reparaciones (id_cliente)");
        db.execSQL("CREATE INDEX idx_reparaciones_estado ON reparaciones (estado)");
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
        if (oldVersion<19){
            db.execSQL("ALTER TABLE productos ADD COLUMN ventaxpeso INTEGER DEFAULT 0");
        }
        if (oldVersion<20){
            db.execSQL("ALTER TABLE productos ADD COLUMN isRefaccion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE productos ADD COLUMN calidad TEXT");
            db.execSQL("ALTER TABLE productos ADD COLUMN modelo_compatible TEXT");
        }
        if (oldVersion<21) db.execSQL("ALTER TABLE productos ADD COLUMN modelo TEXT");
        if (oldVersion<23){
            db.execSQL("DROP TABLE IF EXISTS reparaciones");
            db.execSQL("create table IF NOT EXISTS reparaciones (" +
                    "id_reparacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo_servicio TEXT, " +
                    "id_cliente TEXT, " +
                    "equipo TEXT, " +
                    "color TEXT, " +
                    "falla_reportada TEXT, " +
                    "diagnostico TEXT, " +
                    "costo_refacciones REAL, " +
                    "monto_total REAL, " +
                    "anticipo REAL DEFAULT 0, " +
                    "monto_pendiente REAL, " +
                    "tipo_pago TEXT, " +
                    "estado TEXT, " +
                    "fecha_ingreso TEXT, " +
                    "fecha_promesa TEXT, " +
                    "fecha_entrega TEXT, " +
                    "id_corte INTEGER)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_reparaciones_id_corte ON reparaciones (id_corte)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_reparaciones_id_cliente ON reparaciones (id_cliente)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_reparaciones_estado ON reparaciones (estado)");
        }
        if (oldVersion < 24) {
            db.execSQL("ALTER TABLE productos ADD COLUMN condicion TEXT;");
            db.execSQL("ALTER TABLE productos ADD COLUMN grado_estetico TEXT;");
            db.execSQL("ALTER TABLE productos ADD COLUMN visible_en_busqueda INTEGER DEFAULT 1;");
        }
        if (oldVersion<25){
            // 1. Tabla productos
            db.execSQL("ALTER TABLE productos ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE productos ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE productos ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 2. Tabla secciones
            db.execSQL("ALTER TABLE secciones ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE secciones ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE secciones ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 3. Tabla ventas
            db.execSQL("ALTER TABLE ventas ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE ventas ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE ventas ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 4. Tabla detalles_venta
            db.execSQL("ALTER TABLE detalles_venta ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE detalles_venta ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE detalles_venta ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 5. Tabla clientes
            db.execSQL("ALTER TABLE clientes ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE clientes ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE clientes ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 6. Tabla proveedores
            db.execSQL("ALTER TABLE proveedores ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE proveedores ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE proveedores ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 7. Tabla cortes_cajas
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE cortes_cajas ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 8. Tabla gastos
            db.execSQL("ALTER TABLE gastos ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE gastos ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE gastos ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 9. Tabla configuracion
            db.execSQL("ALTER TABLE configuracion ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE configuracion ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE configuracion ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 10. Tabla abonos
            db.execSQL("ALTER TABLE abonos ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE abonos ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE abonos ADD COLUMN eliminado INTEGER DEFAULT 0");

            // 11. Tabla reparaciones
            db.execSQL("ALTER TABLE reparaciones ADD COLUMN fecha_actualizacion INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE reparaciones ADD COLUMN sincronizado INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE reparaciones ADD COLUMN eliminado INTEGER DEFAULT 0");
        }
    }
    public static synchronized CloverBD getInstance(Context context) {
        if (instance == null) {
            instance = new CloverBD(context.getApplicationContext());
        }
        return instance;
    }

}
