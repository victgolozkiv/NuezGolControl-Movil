<div align="center">

# 🌰 NuezGolControl Móvil

**Sistema de gestión de producción y ventas de nuez — 100% offline, directo desde tu bolsillo.**

[![Android](https://img.shields.io/badge/Platform-Android%208.0+-brightgreen?style=for-the-badge&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-latest-blue?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Room%20DB-2.6-orange?style=for-the-badge)](https://developer.android.com/training/data-storage/room)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

> Versión Android del proyecto [NUEZGOLCONTROL](https://github.com/victgolozkiv/NUEZGOLCONTROL).  
> Desarrollado con ❤️ por **Victor** — hecho para agricultores, con la ayuda de IA.

</div>

---

## 📱 ¿Qué es?

**NuezGolControl Móvil** es una aplicación Android nativa para llevar el control completo de una operación agrícola de nuez: desde las cosechas del campo hasta la venta al cliente, el pago a trabajadores, gastos operativos y análisis financiero completo.

**Sin necesidad de internet.** Todo queda guardado localmente en tu teléfono. 📲

---

## ✨ Características Principales

### 📊 Módulo de Resumen Financiero (NUEVO)
| Feature | Descripción |
|---|---|
| 💰 **Dashboard Financiero** | Resumen visual de ingresos, egresos y ganancia neta |
| 📈 **Filtros por Periodo** | Hoy, Esta semana, Este mes, Últimos 30 días, Todo el tiempo |
| 📊 **Estadísticas por Producto** | Análisis detallado de cada tipo de nuez |
| 🏆 **Productos Destacados** | Más vendido y más lucrativo identificados automáticamente |
| 📋 **Reportes Detallados** | Exportación completa a CSV con resumen ejecutivo |
| 🔍 **Búsqueda y Filtros Avanzados** | Filtra por cliente, trabajador, concepto, rango de montos |

### 🏪 Módulo de Ventas
| Módulo | Descripción |
|---|---|
| 💳 **Ventas** | Registra ventas con cliente, tipo de nuez (Guicha / Western), kilogramos, precio y total automático |
| 📤 **Exportación** | Genera CSV compatible con Excel |

### 🌿 Módulo de Cosechas
| Módulo | Descripción |
|---|---|
| 🌱 **Cosechas** | Lleva el control de producción por tipo de nuez y cantidad en kg |

### 👷 Módulo de Trabajadores y Gastos
| Módulo | Descripción |
|---|---|
| 💸 **Pagos a Trabajadores** | Pago por kilo cosechado o por raya (día), con autocompletado de nombres guardados |
| 📊 **Resumen Financiero** | Balance automático: Total Pagos − Gastos = Neto de egresos |
| 💳 **Gastos / Descuentos** | Registra gastos operativos (fertilizante, agua, etc.) que se **descuentan** del total de egresos |
| 🔄 **Navegación fluida** | Desliza entre pantallas con el dedo, sin cortes ni lag |

---

## 🎨 Pantallas Principales

```
📊 Resumen Financiero        Ventas                  Cosechas                Pagos & Gastos
├─ Dashboard                 ├─ Historial             ├─ Producción por tipo  ├─ Balance visual
├─ Filtros por fecha         ├─ Total vendido         ├─ Control de kg        ├─ Empleados pagados
├─ Estadísticas productos    └─ Exportar a Excel      └─ Detalles cosecha     └─ Gastos registrados
├─ Reportes detallados       
└─ Búsqueda avanzada         
```

---

## 🏗️ Tecnologías

```
Kotlin + Jetpack Compose     → UI declarativa moderna
Room Database                → Base de datos local SQLite sin nube
Navigation Compose           → Navegación entre pantallas
HorizontalPager              → Deslizamiento fluido entre pestañas
MVVM + StateFlow             → Arquitectura limpia y reactiva
Material Design 3            → Tema oscuro morado premium
FileProvider + CSV           → Exportación compatible con Excel
Coroutines                   → Manejo asincrónico de datos
```

---

## 📁 Estructura del proyecto

```
NuezGolControl/
├── app/src/main/java/com/nuezgolcontrol/app/
│   ├── data/
│   │   ├── Entidades: Venta, Cosecha, PagoTrabajador, Trabajador, Gasto
│   │   ├── DAOs: VentaDao, CosechaDao, PagoTrabajadorDao, TrabajadorDao, GastoDao
│   │   ├── NuezDatabase.kt (Room + migraciones)
│   │   ├── NuezRepository.kt
│   │   └── ReporteModels.kt
│   ├── ui/
│   │   ├── resumen/         ← Dashboard financiero y estadísticas
│   │   │   ├── ResumenFinancieroScreen
│   │   │   ├── ResumenFinancieroViewModel
│   │   │   ├── ResumenConFiltrosScreen (NEW)
│   │   │   ├── ResumenConFiltrosViewModel (NEW)
│   │   │   ├── EstadisticasScreen (NEW)
│   │   │   └── EstadisticasViewModel (NEW)
│   │   ├── ventas/         ← VentasScreen, VentasViewModel, NuevaVentaScreen
│   │   ├── cosechas/       ← CosechasScreen, CosechasViewModel, NuevaCosechaScreen
│   │   ├── trabajadores/   ← TrabajadoresScreen, ViewModel, NuevoPagoScreen
│   │   ├── components/     ← Componentes reutilizables (dialogs, cards, SearchBar, etc)
│   │   └── theme/          ← Colores, tipografía, tema oscuro
│   ├── util/
│   │   ├── ExcelExporter.kt
│   │   ├── Formatters.kt
│   │   ├── DateRangeFilter.kt (NEW)
│   │   ├── SearchAndFilterUtils.kt (NEW)
│   │   └── AdvancedReportExporter.kt (NEW)
│   ├── MainActivity.kt
│   └── NuezGolApplication.kt
└── app/src/main/res/
    ├── values/
    └── xml/file_paths.xml
```

---

## 🚀 Cómo ejecutar

### Requisitos
- **Android Studio** Ladybug o superior
- **JDK 17**
- Dispositivo o emulador **Android 8.0+ (API 26)**

### Desde Android Studio
1. Abre Android Studio → **Open** → selecciona la carpeta `NuezGolControl`
2. Espera a que Gradle sincronice las dependencias
3. Pulsa **▶ Run** en un emulador o teléfono conectado

### Desde terminal (con ADB)
```bash
# Compilar e instalar directamente en el dispositivo conectado
./gradlew installDebug

# Solo generar el APK
./gradlew assembleDebug
# → APK en: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📖 Guía de Uso Rápida

### Registrar una Venta
1. Abre la app → Tab **"Ventas"**
2. Presiona el botón **"+"** (Nueva Venta)
3. Ingresa: Cliente, Tipo de Nuez, Cantidad (kg), Precio unitario
4. El total se calcula automáticamente
5. ¡Listo! La venta se guardó localmente

### Ver Resumen Financiero
1. Abre la app → Tab **"Resumen"** (primera pestaña)
2. Aplica filtros según el período deseado (Hoy, Semana, Mes, etc.)
3. Observa:
   - Ingresos totales (de ventas)
   - Egresos totales (pagos + gastos)
   - Ganancia neta (ingresos - egresos)
4. Desplázate para ver estadísticas por producto

### Exportar Reportes
1. En la pestaña **"Resumen"** → Presiona **Exportar Reporte Completo**
2. Selecciona una app para compartir (Gmail, Drive, etc.)
3. ¡El CSV se envía automáticamente!

---

## 🎯 Roadmap Futuro

- [ ] Gráficas interactivas (barras, líneas, pastel)
- [ ] Sincronización con Google Drive (backup automático)
- [ ] Autenticación con contraseña
- [ ] Notificaciones de recordatorio de pagos
- [ ] Presupuestos mensuales y alertas
- [ ] Análisis de tendencias (mes a mes)
- [ ] Widget para pantalla de inicio
- [ ] Fotografías de productos/transacciones
- [ ] Multi-usuario (para equipos)
- [ ] Integración con proveedores locales

---

## 🐛 Reporte de Bugs

¿Encontraste un error? Abre un [Issue](https://github.com/victgolozkiv/NuezGolControl-Movil/issues) describiendo:
- Qué intentabas hacer
- Qué sucedió
- Tu modelo de teléfono y versión de Android

---

## 📄 Licencia

Distribuido bajo licencia **MIT**. Puedes usar, modificar y distribuir este proyecto libremente.

---

## ☕ ¿Te fue útil? Apóyame

Si este proyecto te sirvió de inspiración o quieres que siga mejorando, puedes invitarme un café 🙌

<div align="center">

[![Donar con PayPal](https://img.shields.io/badge/Donar%20con-PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.com/paypalme/VictorRicardo162/1)

</div>

---

<div align="center">

Hecho con ☕ y mucho campo en **Ciudad Jiménez, Chihuahua, México** 🇲🇽

**Versión: 2.1.0** - Cosechas & Export Fix Release

</div>

---

### 📝 Notas de la Versión 2.1.0
- **Seguridad de Datos**: Se utiliza un correo electrónico cifrado y anónimo de GitHub para proteger la identidad del autor en los commits. Los tokens de acceso personales (PAT) son de uso temporal y no quedan almacenados en el repositorio público ni en el historial de git.
- **Cosechas Inteligentes**: Selector dinámico de tipo de nuez (`Guicha` y `Western`), ahorrando tiempo y evitando tener que escribirlo manualmente.
- **Corrección de Exportación**: Corregido el proveedor de archivos (`FileProvider`) en Android. Ahora el botón de **"Exportar Reporte Completo"** en formato CSV funciona perfectamente, permitiendo compartir tu reporte de temporada de inmediato hacia Excel, WhatsApp o Gmail sin cierres inesperados.

