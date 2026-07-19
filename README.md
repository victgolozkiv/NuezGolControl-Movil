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

**NuezGolControl Móvil** es una aplicación Android nativa para llevar el control completo de una operación agrícola de nuez: desde las cosechas del campo hasta la venta al cliente, el pago a trabajadores y los gastos operativos — todo sin necesidad de internet.

---

## ✨ Características

| Módulo | Descripción |
|---|---|
| 🏪 **Ventas** | Registra ventas con cliente, tipo de nuez (Guicha / Western), kilogramos, precio y total automático |
| 🌿 **Cosechas** | Lleva el control de producción por tipo de nuez y cantidad en kg |
| 👷 **Pagos a Trabajadores** | Pago por kilo cosechado o por raya (día), con autocompletado de nombres guardados |
| 💸 **Gastos / Descuentos** | Registra gastos operativos (fertilizante, agua, etc.) que se **descuentan** del total de egresos |
| 📊 **Resumen Financiero** | Balance automático: Total Pagos − Gastos = Neto de egresos |
| 📤 **Exportación CSV** | Exporta ventas, cosechas y pagos a Excel con un solo toque |
| 🔄 **Navegación fluida** | Desliza entre pantallas con el dedo, sin cortes ni lag |

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
```

---

## 📁 Estructura del proyecto

```
NuezGolControl/
├── app/src/main/java/com/nuezgolcontrol/app/
│   ├── data/
│   │   ├── Venta.kt / VentaDao.kt
│   │   ├── Cosecha.kt / CosechaDao.kt
│   │   ├── PagoTrabajador.kt / PagoTrabajadorDao.kt
│   │   ├── Trabajador.kt / TrabajadorDao.kt
│   │   ├── Gasto.kt / GastoDao.kt
│   │   ├── NuezDatabase.kt
│   │   └── NuezRepository.kt
│   ├── ui/
│   │   ├── ventas/          ← VentasScreen, VentasViewModel, NuevaVentaScreen
│   │   ├── cosechas/        ← CosechasScreen, CosechasViewModel, NuevaCosechaScreen
│   │   ├── trabajadores/    ← TrabajadoresScreen, ViewModel, NuevoPagoScreen
│   │   ├── components/      ← Componentes reutilizables (dialogs, cards, etc.)
│   │   └── theme/           ← Colores, tipografía, tema oscuro
│   ├── util/
│   │   ├── ExcelExporter.kt ← Genera y comparte archivos CSV
│   │   └── Formatters.kt    ← Formateo de fechas, moneda y kilogramos
│   ├── MainActivity.kt      ← Navegación principal con HorizontalPager
│   └── NuezGolApplication.kt
└── app/src/main/res/
    ├── values/              ← strings, colors, themes
    └── xml/file_paths.xml   ← FileProvider para exportar
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

## 📸 Pantallas

| Ventas | Cosechas | Pagos & Gastos |
|:---:|:---:|:---:|
| Historial de ventas con totales | Producción por tipo de nuez | Balance de egresos y trabajadores |

---

## 🗺️ Roadmap

- [ ] Filtros por rango de fechas
- [ ] Gráficas de producción y ventas
- [ ] Respaldo / exportación completa de la base de datos
- [ ] Widget de resumen para la pantalla de inicio

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

</div>
