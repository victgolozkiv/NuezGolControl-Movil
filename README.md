# NuezGol Control (Android)

Versión Android en **Kotlin** del sistema [NUEZGOLCONTROL](https://github.com/victgolozkiv/NUEZGOLCONTROL): control de ventas y cosechas de nuez.

## Características

- Registro y listado de **ventas** (cliente, tipo Guicha/Western, kg, precio, total)
- Registro y listado de **cosechas** (tipo de nuez, kg)
- Eliminación con confirmación
- Totales generales (dinero / kilogramos)
- Exportación a CSV compatible con Excel
- Fechas en zona horaria `America/Chihuahua`
- Base de datos local con **Room** (`nuez.db`)
- UI con **Jetpack Compose** (tema oscuro morado, como la web)

## Requisitos

- Android Studio Ladybug o superior (recomendado)
- JDK 17
- Dispositivo o emulador Android 8.0+ (API 26)

## Cómo abrir y ejecutar

1. Abre Android Studio → **Open** → selecciona la carpeta `NuezGolControl`
2. Espera a que Gradle sincronice
3. Pulsa **Run** (▶) en un emulador o teléfono

Desde terminal (con Android SDK configurado):

```bash
cd NuezGolControl
./gradlew assembleDebug
```

El APK queda en:

`app/build/outputs/apk/debug/app-debug.apk`

## Uso

| Pantalla | Acción |
|----------|--------|
| Ventas | Ver historial, exportar, eliminar, ir a nueva venta |
| Nueva venta | Cliente, tipo (Guicha/Western), cantidad kg, precio $/kg |
| Cosechas | Ver historial, exportar, eliminar, ir a nueva cosecha |
| Nueva cosecha | Tipo de nuez + cantidad kg |

## Estructura

```
NuezGolControl/
├── app/src/main/java/com/nuezgolcontrol/app/
│   ├── data/          # Room: Venta, Cosecha, DAOs, DB, Repository
│   ├── ui/            # Compose: pantallas y tema
│   ├── util/          # Formato fechas/moneda + exportación
│   ├── MainActivity.kt
│   └── NuezGolApplication.kt
└── app/src/main/res/
```

## Licencia

MIT — mismo espíritu que el proyecto web original.
