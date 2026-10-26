# Clover - Sistema POS y Gestión de Taller de Reparaciones

Clover (DiamondStore) es una solución integral nativa para Android diseñada para la administración completa de negocios de tecnología, venta al por menor y talleres de reparación técnica. Combina un Punto de Venta (POS) de alta velocidad, Control de Inventarios, Gestión de Cuentas por Cobrar (Crédito) y un Módulo Especializado de Taller y Reparaciones con generación dinámica de comprobantes para WhatsApp.

---

## Capturas de Pantalla

| Módulo de Taller | Ticket de Reparación | Punto de Venta |
| :---: | :---: | :---: |
| ![Taller](docs/images/taller.png) | ![Ticket](docs/images/ticket.png) | ![POS](docs/images/pos.png) |

| Inventario y Productos | Dashboard Financiero | Corte de Caja |
| :---: | :---: | :---: |
| ![Inventario](docs/images/inventario.png) | ![Dashboard](docs/images/dashboard.png) | ![Corte](docs/images/corte.png) |

---

## Módulos Principales

### 1. Punto de Venta (Ventas y Carrito)
* **Caja Interactiva:** Búsqueda rápida de productos por nombre, sección o código de barras.
* **Escáner Integrado:** Lectura acelerada mediante cámara (ZXing / MLKit).
* **Ventas a Granel:** Calculadora interactiva de precio por peso (DialogCalculadoraPeso).
* **Múltiples Métodos de Pago:** Efectivo, Tarjeta, Transferencia bancaria y Venta a Crédito / Fiado.
* **Tickets Virtuales:** Generación e impresión de comprobantes de venta.

### 2. Módulo de Taller y Servicio Técnico
* **Tablero Kanban e Indicadores:** Clasificación en tiempo real (Pendientes, En Proceso, Listos).
* **Búsqueda y Filtros:** Filtrado por modelo, cliente, falla, folio (#REP-xxx), estado y tipo de servicio (GENERAL, EXPRESS, TALLER, GARANTÍA, DOMICILIO).
* **Diagnóstico y Refacciones:** Registro de costo de refacciones, anticipos y presupuesto dinámico.
* **Envío de Tickets por WhatsApp:** Motor de renderizado en memoria para generar comprobantes digitales PNG/HD y compartirlos directamente con el cliente.

### 3. Control de Inventario y Productos
* **Catálogo Multimedia:** Gestión local de imágenes de productos renderizadas con Glide.
* **Stock Bajo y Alertas:** Notificaciones visuales de reabastecimiento crítico.
* **Generador de Etiquetas:** Creación e impresión automática de códigos de barras/QR por producto (GenerarEtiquetaProductoUseCase).

### 4. Clientes y Cuentas por Cobrar
* **Historial y Fidelización:** Registro de clientes con historial de compras, saldo pendiente y puntos acumulados.
* **Tarjetas Digitales QR:** Generación de tarjetas de identificación de cliente en código QR (CardClientGenerator).
* **Módulo de Crédito:** Administración de deudores, abonos parciales y recalculado automático de saldo.

### 5. Proveedores
* Directorio completo con contacto de vendedores, días de visita/pago y accesos directos a llamadas y mensajes de WhatsApp.

### 6. Control de Caja y Cortes de Turno
* Apertura y cierre de caja con cálculo de faltantes/sobrantes.
* Desglose financiero detallado por tipo de ingreso (efectivo, tarjeta, transferencias, abonos y reparaciones).

### 7. Registro de Gastos
* Control de egresos clasificados por categoría y método de pago impactando directamente el corte de caja activo.

### 8. Dashboard Financiero y Reportes
* Métricas en tiempo real de ganancias netas, margen de utilidad, gastos acumulados e ingresos filtrables por rango de fechas.

### 9. Configuración y Respaldo Cloud
* Datos del negocio, cuentas bancarias para transferencias, notas de garantía e impresoras térmicas Bluetooth (dirección MAC / ancho de papel).
* Sincronización y respaldo en segundo plano con Firebase.

---

## Arquitectura del Proyecto

El proyecto está construido siguiendo los principios de Clean Architecture y el patrón MVVM (Model-View-ViewModel) para garantizar mantenibilidad, testabilidad y un bajo acoplamiento:

```text
com.Clover.prueba
│
├── data                 // Capa de Datos
│   ├── dao              // Interfaces y Contratos DAO (IProducto, IReparaciones, etc.)
│   ├── models           // Modelos de datos SQLite (Reparaciones, Clientes, Ventas)
│   └── repositories     // Implementación de acceso a base de datos
│
├── domain               // Capa de Dominio (Reglas de Negocio Puras)
│   ├── dtos             // Transferencia de datos (NotaReparacionDTO, CarritoDTO)
│   ├── mappers          // Transformadores de modelos a DTOs de interfaz
│   ├── validators       // Validaciones de negocio (TicketsValidator, StockValidator)
│   └── usecases         // Casos de Uso (NotaReparacionUC, GenerarEtiquetaProductoUseCase)
│
├── services             // Capa de Servicios e Infraestructura
│   ├── generators       // ImageGenerator, TicketReparacionGenerator, GeneradorQR
│   ├── sharing          // ShareManager (Manejo de Intents y WhatsApp)
│   └── sync             // SyncManager (Sincronización Cloud con Firebase)
│
└── ui                   // Capa de Presentación
    ├── viewmodels       // Coordinadores de UI orientados a Lifecycle / LiveData
    └── views            // Fragments, Activities y BottomSheetDialogs (Material Design 3)
```

---

## Modelo de Base de Datos (SQLite - CloverBD)

El sistema administra 11 tablas relacionales optimizadas mediante índices:

| Tabla | Descripción |
| :--- | :--- |
| `ventas` | Encabezado de ventas, folios, totales y estado de cobro. |
| `detalles_venta` | Desglose ítem por ítem de cada transacción realizada. |
| `productos` | Catálogo, precios público/neto, stock, código de barras y categoría. |
| `clientes` | Información personal, saldo a crédito activo y puntos acumulados. |
| `proveedores` | Directorio comercial y días de atención/visita. |
| `reparaciones` | Folios, equipo, falla reportada, diagnóstico, costos y estado del servicio. |
| `abonos` | Historial de pagos parciales aplicados a deudas de clientes. |
| `deudas` | Registro consolidado de saldos pendientes por cliente. |
| `gastos` | Salidas operativas de efectivo o transferencia. |
| `cortes_cajas` | Arqueos, saldos iniciales/finales y diferencias de turno. |
| `configuracion` | Parámetros globales del taller, datos fiscales e impresoras Bluetooth. |

---

## Tecnologías y Librerías

* **Lenguaje:** Java 11+
* **Framework:** Android SDK (API 21+)
* **UI Components:** Material Design 3, ConstraintLayout, NestedScrollView, CardView
* **Async & Lifecycle:** LiveData, ViewModel, Custom Event Wrappers
* **Imágenes & Rendering:** Glide, Canvas API, Android PdfDocument
* **Escáner:** ZXing / Google MLKit Barcode Scanning
* **Backend Cloud:** Firebase BOM (Authentication, Firestore/Realtime Database)

---

## Autor

Desarrollado por **Cristian Uriel Tobar Santillan** - Técnico en Programación y Especialista en Reparación Hardware/Software Mobile.