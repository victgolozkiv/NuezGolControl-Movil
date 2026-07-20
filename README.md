````markdown
1| <div align="center">
2| 
3| # 🌰 NuezGolControl Móvil
4| 
5| **Sistema de gestión de producción y ventas de nuez — 100% offline, directo desde tu bolsillo.**
6| 
7| [![Android](https://img.shields.io/badge/Platform-Android%208.0+-brightgreen?style=for-the-badge&logo=android)](https://developer.android.com)
8| [![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
9| [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-latest-blue?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
10| [![Room](https://img.shields.io/badge/Room%20DB-2.6-orange?style=for-the-badge)](https://developer.android.com/training/data-storage/room)
11| [![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)
12| 
13| > Versión Android del proyecto [NUEZGOLCONTROL](https://github.com/victgolozkiv/NUEZGOLCONTROL).  
14| > Desarrollado con ❤️ por **Victor** — hecho para agricultores, con la ayuda de IA.
15| 
16| </div>
17| 
18| ---
19| 
20| ## 📱 ¿Qué es?
21| 
22| **NuezGolControl Móvil** es una aplicación Android nativa para llevar el control completo de una operación agrícola de nuez: desde las cosechas del campo hasta la venta al cliente, el pago a trabajadores, gastos operativos y análisis financiero completo.
23| 
24| **Sin necesidad de internet.** Todo queda guardado localmente en tu teléfono. 📲
25| 
26| ---
27| 
28| ## ✨ Características Principales
29| 
30| ### 📊 Módulo de Resumen Financiero (NUEVO)
31| | Feature | Descripción |
32| |---|---|
33| | 💰 **Dashboard Financiero** | Resumen visual de ingresos, egresos y ganancia neta |
34| | 📈 **Filtros por Periodo** | Hoy, Esta semana, Este mes, Últimos 30 días, Todo el tiempo |
35| | 📊 **Estadísticas por Producto** | Análisis detallado de cada tipo de nuez |
36| | 🏆 **Productos Destacados** | Más vendido y más lucrativo identificados automáticamente |
37| | 📋 **Reportes Detallados** | Exportación completa a CSV con resumen ejecutivo |
38| | 🔍 **Búsqueda y Filtros Avanzados** | Filtra por cliente, trabajador, concepto, rango de montos |
39| 
40| ### 🏪 Módulo de Ventas
41| | Módulo | Descripción |
42| |---|---|
43| | 💳 **Ventas** | Registra ventas con cliente, tipo de nuez (Guicha / Western), kilogramos, precio y total automático |
44| | 📤 **Exportación** | Genera CSV compatible con Excel |
45| 
46| ### 🌿 Módulo de Cosechas
47| | Módulo | Descripción |
48| |---|---|
49| | 🌱 **Cosechas** | Lleva el control de producción por tipo de nuez y cantidad en kg |
50| 
51| ### 👷 Módulo de Trabajadores y Gastos
52| | Módulo | Descripción |
53| |---|---|
54| | 💸 **Pagos a Trabajadores** | Pago por kilo cosechado o por raya (día), con autocompletado de nombres guardados |
55| | 📊 **Resumen Financiero** | Balance automático: Total Pagos − Gastos = Neto de egresos |
56| | 💳 **Gastos / Descuentos** | Registra gastos operativos (fertilizante, agua, etc.) que se **descuentan** del total de egresos |
57| | 🔄 **Navegación fluida** | Desliza entre pantallas con el dedo, sin cortes ni lag |
58| 
59| ---
60| 
61| ## 🎨 Pantallas Principales
62| 
63| ```
64| 📊 Resumen Financiero        Ventas                  Cosechas                Pagos & Gastos
65| ├─ Dashboard                 ├─ Historial             ├─ Producción por tipo  ├─ Balance visual
66| ├─ Filtros por fecha         ├─ Total vendido         ├─ Control de kg        ├─ Empleados pagados
67| ├─ Estadísticas productos    └─ Exportar a Excel      └─ Detalles cosecha     └─ Gastos registrados
68| ├─ Reportes detallados       
69| └─ Búsqueda avanzada         
70| ```
71| 
72| ---
73| 
74| ## 🏗️ Tecnologías
75| 
76| ```
77| Kotlin + Jetpack Compose     → UI declarativa moderna
78| Room Database                → Base de datos local SQLite sin nube
79| Navigation Compose           → Navegación entre pantallas
80| HorizontalPager              → Deslizamiento fluido entre pestañas
81| MVVM + StateFlow             → Arquitectura limpia y reactiva
82| Material Design 3            → Tema oscuro morado premium
83| FileProvider + CSV           → Exportación compatible con Excel
84| Coroutines                   → Manejo asincrónico de datos
85| ```
86| 
87| ---
88| 
89| ## 📁 Estructura del proyecto
90| 
91| ```
92| NuezGolControl/
93| ├── app/src/main/java/com/nuezgolcontrol/app/
94| │   ├── data/
95| │   │   ├── Entidades: Venta, Cosecha, PagoTrabajador, Trabajador, Gasto
96| │   │   ├── DAOs: VentaDao, CosechaDao, PagoTrabajadorDao, TrabajadorDao, GastoDao
97| │   │   ├── NuezDatabase.kt (Room + migraciones)
98| │   │   ├── NuezRepository.kt
99| │   │   └── ReporteModels.kt
100| │   ├── ui/
101| │   │   ├── resumen/         ← Dashboard financiero y estadísticas
102| │   │   │   ├── ResumenFinancieroScreen
103| │   │   │   ├── ResumenFinancieroViewModel
104| │   │   │   ├── ResumenConFiltrosScreen (NEW)
105| │   │   │   ├── ResumenConFiltrosViewModel (NEW)
106| │   │   │   ├── EstadisticasScreen (NEW)
107| │   │   │   └── EstadisticasViewModel (NEW)
108| │   │   ├── ventas/         ← VentasScreen, VentasViewModel, NuevaVentaScreen
109| │   │   ├── cosechas/       ← CosechasScreen, CosechasViewModel, NuevaCosechaScreen
110| │   │   ├── trabajadores/   ← TrabajadoresScreen, ViewModel, NuevoPagoScreen
111| │   │   ├── components/     ← Componentes reutilizables (dialogs, cards, SearchBar, etc)
112| │   │   └── theme/          ← Colores, tipografía, tema oscuro
113| │   ├── util/
114| │   │   ├── ExcelExporter.kt
115| │   │   ├── Formatters.kt
116| │   │   ├── DateRangeFilter.kt (NEW)
117| │   │   ├── SearchAndFilterUtils.kt (NEW)
118| │   │   └── AdvancedReportExporter.kt (NEW)
119| │   ├── MainActivity.kt
120| │   └── NuezGolApplication.kt
121| └── app/src/main/res/
122|     ├── values/
123|     └── xml/file_paths.xml
124| ```
125| 
126| ---
127| 
128| ## 🚀 Cómo ejecutar
129| 
130| ### Requisitos
131| - **Android Studio** Ladybug o superior
132| - **JDK 17**
133| - Dispositivo o emulador **Android 8.0+ (API 26)**
134| 
135| ### Desde Android Studio
136| 1. Abre Android Studio → **Open** → selecciona la carpeta `NuezGolControl`
137| 2. Espera a que Gradle sincronice las dependencias
138| 3. Pulsa **▶ Run** en un emulador o teléfono conectado
139| 
140| ### Desde terminal (con ADB)
141| ```bash
142| # Compilar e instalar directamente en el dispositivo conectado
143| ./gradlew installDebug
144| 
145| # Solo generar el APK
146| ./gradlew assembleDebug
147| # → APK en: app/build/outputs/apk/debug/app-debug.apk
148| ```
149| 
150| ---
151| 
152| ## 📖 Guía de Uso Rápida
153| 
154| ### Registrar una Venta
155| 1. Abre la app → Tab **"Ventas"**
156| 2. Presiona el botón **"+"** (Nueva Venta)
157| 3. Ingresa: Cliente, Tipo de Nuez, Cantidad (kg), Precio unitario
158| 4. El total se calcula automáticamente
159| 5. ¡Listo! La venta se guardó localmente
160| 
161| ### Ver Resumen Financiero
162| 1. Abre la app → Tab **"Resumen"** (primera pestaña)
163| 2. Aplica filtros según el período deseado (Hoy, Semana, Mes, etc.)
164| 3. Observa:
165|    - Ingresos totales (de ventas)
166|    - Egresos totales (pagos + gastos)
167|    - Ganancia neta (ingresos - egresos)
168| 4. Desplázate para ver estadísticas por producto
169| 
170| ### Exportar Reportes
171| 1. En la pestaña **"Resumen"** → Presiona **Exportar Reporte Completo**
172| 2. Selecciona una app para compartir (Gmail, Drive, etc.)
173| 3. ¡El CSV se envía automáticamente!
174| 
175| ---
176| 
177| ## 🎯 Roadmap Futuro
178| 
179| - [ ] Gráficas interactivas (barras, líneas, pastel)
180| - [ ] Sincronización con Google Drive (backup automático)
181| - [ ] Autenticación con contraseña
182| - [ ] Notificaciones de recordatorio de pagos
183| - [ ] Presupuestos mensuales y alertas
184| - [ ] Análisis de tendencias (mes a mes)
185| - [ ] Widget para pantalla de inicio
186| - [ ] Fotografías de productos/transacciones
187| - [ ] Multi-usuario (para equipos)
188| - [ ] Integración con proveedores locales
189| 
190| ---
191| 
192| ## 🐛 Reporte de Bugs
193| 
194| ¿Encontraste un error? Abre un [Issue](https://github.com/victgolozkiv/NuezGolControl-Movil/issues) describiendo:
195| - Qué intentabas hacer
196| - Qué sucedió
197| - Tu modelo de teléfono y versión de Android
198| 
199| ---
200| 
201| ## 📄 Licencia
202| 
203| Distribuido bajo licencia **MIT**. Puedes usar, modificar y distribuir este proyecto libremente.
204| 
205| ---
206| 
207| ## ☕ ¿Te fue útil? Apóyame
208| 
209| Si este proyecto te sirvió de inspiración o quieres que siga mejorando, puedes invitarme un café 🙌
210| 
211| <div align="center">
212| 
213| [![Donar con PayPal](https://img.shields.io/badge/Donar%20con-PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.com/paypalme/VictorRicardo162/1)
214| 
215| </div>
216| 
217| ---
218| 
219| <div align="center">
220| 
220| Hecho con ☕ y mucho campo en **Ciudad Jiménez, Chihuahua, México** 🇲🇽
221| 
222| **Versión: 2.0.0** - Financial Dashboard Release
223| 
224| </div>
````
