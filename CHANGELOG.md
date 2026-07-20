# Changelog

Todos los cambios notables en este proyecto se documentarán en este archivo.

## [2.0.0] - 2026-07-20

### 🎉 Added (Características Nuevas)

#### Dashboard Financiero Completo
- ✨ **ResumenFinancieroScreen**: Panel principal mostrando ingresos, egresos y ganancia neta
- 📊 **ResumenConFiltrosScreen**: Dashboard con filtros interactivos por período (Hoy, Semana, Mes, 30 días, Todo el tiempo)
- 📈 **EstadisticasScreen**: Análisis detallado por producto con identificación de:
  - Producto más vendido (por cantidad)
  - Producto más lucrativo (por ingresos)
  - Desglose por tipo de nuez con precios promedio

#### Utilidades de Filtrado y Búsqueda
- 🔍 **SearchAndFilterUtils**: Funciones para filtrado avanzado de:
  - Ventas (por cliente, tipo de nuez, rango de monto)
  - Pagos a empleados (por trabajador, tipo de pago, monto)
  - Gastos (por concepto, rango de monto)
- 📅 **DateRangeFilter**: Utilidades para filtrar por períodos de tiempo
- 🔎 **SearchBar**: Componente visual reutilizable para búsqueda

#### Exportación Avanzada
- 📋 **AdvancedReportExporter**: Generador de reportes completos en CSV con:
  - Resumen ejecutivo (totales, ganancias)
  - Detalle completo de ventas
  - Detalle completo de pagos a empleados
  - Detalle completo de gastos
  - Funcionalidad de compartir automáticamente

#### Modelos de Datos
- 💾 **ReporteModels**: Nuevas clases de datos para reportes (Transaccion, ReporteResumen)

### 🔄 Modified (Cambios)

#### MainActivity.kt
- Agregada cuarta pestaña para "Resumen" financiero
- Integración del ResumenFinancieroViewModel en la navegación
- Actualización del HorizontalPager para 4 tabs

#### Navegación
- La pestaña de "Resumen" ahora es el primer tab (índice 0)
- Las otras pestañas se desplazan: Ventas (1), Cosechas (2), Pagos (3)

### 📚 Documentation
- README.md actualizado con todas las nuevas características
- Adición de sección "Módulo de Resumen Financiero"
- Actualización de roadmap futuro
- Versión incrementada a 2.0.0

### 🏗️ Architecture
- Nuevo paquete `ui/resumen/` para todos los componentes financieros
- Separación de responsabilidades:
  - ViewModels para lógica de negocio
  - Screens para UI
  - Utilities para operaciones comunes

## [1.0.0] - 2026-07-19

### 🎉 Initial Release

#### Features Originales
- ✅ Registro de ventas
- ✅ Control de cosechas
- ✅ Pagos a trabajadores
- ✅ Registro de gastos
- ✅ Exportación a CSV
- ✅ Base de datos local (Room)
- ✅ UI con Jetpack Compose
- ✅ Tema oscuro morado
- ✅ Navegación fluida entre tabs

---

## Notas de Desarrollo

### Cambios de Base de Datos
- La versión 2.0.0 mantiene compatibilidad con la versión anterior
- Las migraciones existentes se preservan
- No se requieren cambios en la estructura de tablas

### Performance
- Todos los cálculos de resumen se realizan en ViewModels con Flows
- Los filtros se aplican en memoria (recomendado para <10k registros)
- Para aplicaciones más grandes, considerar migrar a queries SQL en el DAO

### Próximas Mejoras (v2.1.0)
- Gráficas interactivas
- Sincronización con Google Drive
- Autenticación de usuario
- Notificaciones locales
