# 🏗️ Arquitectura de NuezGolControl Móvil

## Visión General

NuezGolControl utiliza una arquitectura **MVVM (Model-View-ViewModel)** con **Clean Architecture** principles, optimizada para aplicaciones Android con **Jetpack Compose** y **Room Database**.

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                   │
│              (Composables, Screens, ViewModels)         │
├─────────────────────────────────────────────────────────┤
│                    DOMAIN LAYER                         │
│            (Use Cases, Business Logic Rules)            │
├─────────────────────────────────────────────────────────┤
│                    DATA LAYER                           │
│      (Repository, DAOs, Database, Entities)             │
└─────────────────────────────────────────────────────────┘
```

---

## Capa de Presentación (UI Layer)

### Componentes Principales

#### 1. **Screens** (Composables Principales)
- `VentasScreen` - Lista de ventas
- `CosechasScreen` - Lista de cosechas
- `TrabajadoresScreen` - Pagos a empleados y gastos
- `ResumenFinancieroScreen` - Dashboard financiero
- `ResumenConFiltrosScreen` - Dashboard con filtros
- `EstadisticasScreen` - Análisis por producto
- `NuevaVentaScreen` - Formulario nueva venta
- `NuevaCosechaScreen` - Formulario nueva cosecha
- `NuevoPagoTrabajadorScreen` - Formulario nuevo pago

#### 2. **ViewModels** (Lógica de Presentación)

Cada Screen tiene un ViewModel asociado que:
- Gestiona el estado (`UiState` con StateFlow)
- Proporciona funciones públicas para interacciones del usuario
- Conecta con el Repository para acceder a datos
- Usa Coroutines para operaciones asincrónicas

**Ejemplo:**
```kotlin
class VentasViewModel(private val repository: NuezRepository) : ViewModel() {
    val uiState: StateFlow<VentasUiState> = repository.ventas
        .map { list ->
            VentasUiState(
                ventas = list,
                totalGeneral = list.sumOf { it.total }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VentasUiState())

    fun eliminar(id: Long) {
        viewModelScope.launch { repository.eliminarVenta(id) }
    }
}
```

#### 3. **Componentes Reutilizables**
- `FinancialCard` - Card para mostrar métricas financieras
- `SearchBar` - Barra de búsqueda con auto-limpieza
- `FinancialSummaryWidget` - Widget de resumen rápido

### Flujo de Datos en UI

```
User Interaction
      ↓
  Composable Click/Input
      ↓
  ViewModel Function Call
      ↓
  Repository Operation
      ↓
  Database Change
      ↓
  Flow Emission
      ↓
  ViewModel State Update
      ↓
  Composable Recomposition
```

---

## Capa de Datos (Data Layer)

### Entidades (Room Entities)

Cada entidad representa una tabla en SQLite:

```kotlin
@Entity
data class Venta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cliente: String,
    val tipoNuez: String,  // "Guicha" o "Western"
    val cantidad: Double,  // kg
    val precioUnitario: Double,
    val total: Double = cantidad * precioUnitario,
    val fecha: Long = System.currentTimeMillis()
)
```

**Entidades Principales:**
- `Venta` - Registros de ventas
- `Cosecha` - Registros de producción
- `PagoTrabajador` - Pagos a empleados
- `Trabajador` - Nombres guardados de empleados
- `Gasto` - Gastos operativos

### DAOs (Data Access Objects)

Cada DAO proporciona operaciones CRUD y queries específicas:

```kotlin
@Dao
interface VentaDao {
    @Query("SELECT * FROM venta ORDER BY fecha DESC")
    fun observeAll(): Flow<List<Venta>>
    
    @Insert
    suspend fun insert(venta: Venta)
    
    @Query("DELETE FROM venta WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT * FROM venta")
    suspend fun getAll(): List<Venta>
}
```

### Repository (Data Access Layer)

Actúa como intermediario entre ViewModel y DAOs:

```kotlin
class NuezRepository(
    private val ventaDao: VentaDao,
    private val cosechaDao: CosechaDao,
    private val pagoTrabajadorDao: PagoTrabajadorDao,
    private val trabajadorDao: TrabajadorDao,
    private val gastoDao: GastoDao
) {
    // Flows observables para reactive updates
    val ventas = ventaDao.observeAll()
    val cosechas = cosechaDao.observeAll()
    val pagosTrabajadores = pagoTrabajadorDao.observeAll()
    val trabajadores = trabajadorDao.observeAll()
    val gastos = gastoDao.observeAll()
    
    // Suspend functions para operaciones
    suspend fun agregarVenta(cliente: String, tipoNuez: String, cantidad: Double, precioUnitario: Double) {
        ventaDao.insert(Venta(cliente = cliente, tipoNuez = tipoNuez, cantidad = cantidad, precioUnitario = precioUnitario))
    }
}
```

### Base de Datos (Room)

```kotlin
@Database(
    entities = [Venta::class, Cosecha::class, PagoTrabajador::class, Trabajador::class, Gasto::class],
    version = 4,
    exportSchema = false
)
abstract class NuezDatabase : RoomDatabase() {
    abstract fun ventaDao(): VentaDao
    abstract fun cosechaDao(): CosechaDao
    // ... más DAOs
    
    companion object {
        fun getInstance(context: Context): NuezDatabase {
            return Room.databaseBuilder(context, NuezDatabase::class.java, "nuez.db")
                .addMigrations(MIGRATION_1_3, MIGRATION_2_3, MIGRATION_3_4)
                .build()
        }
    }
}
```

---

## Patrones y Técnicas Clave

### 1. **StateFlow para Reactive Updates**

```kotlin
// El ViewModel expone StateFlow que UI observa
val uiState: StateFlow<VentasUiState> = repository.ventas
    .map { ventasList -> VentasUiState(ventas = ventasList) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VentasUiState())
```

### 2. **Coroutines para Operaciones Asincrónicas**

```kotlin
fun agregar(cliente: String, onDone: () -> Unit) {
    viewModelScope.launch {
        repository.agregarVenta(cliente)
        onDone()
    }
}
```

### 3. **Combine para Múltiples Flows**

```kotlin
val uiState: StateFlow<ResumenFinancieroUiState> = combine(
    repository.ventas,
    repository.pagosTrabajadores,
    repository.gastos
) { ventas, pagos, gastos ->
    ResumenFinancieroUiState(
        totalIngresos = ventas.sumOf { it.total },
        totalEgresos = pagos.sumOf { ... } + gastos.sumOf { it.monto }
    )
}.stateIn(...)
```

### 4. **Dependency Injection Manual**

```kotlin
val app = application as NuezGolApplication
val repository = app.repository
val viewModel: VentasViewModel = viewModel(factory = VentasViewModel.factory(repository))
```

### 5. **Utility Objects para Lógica Compartida**

```kotlin
object Formatters {
    fun dinero(valor: Double): String = currencyFormat.format(valor)
    fun fecha(millis: Long): String = dateTimeFormat.format(Date(millis))
}

object DateRangeFilter {
    fun getTodayRange(): DateRange = ...
    fun getThisMonthRange(): DateRange = ...
}

object SearchAndFilterUtils {
    fun filtrarVentas(ventas: List<Venta>, query: String): List<Venta> = ...
}
```

---

## Gestión de Estado (State Management)

### UiState Pattern

Cada Screen tiene un `data class` que representa su estado:

```kotlin
data class VentasUiState(
    val ventas: List<Venta> = emptyList(),
    val totalGeneral: Double = 0.0
)

data class ResumenConFiltrosUiState(
    val totalIngresos: Double = 0.0,
    val totalPagosEmpleados: Double = 0.0,
    val totalGastos: Double = 0.0,
    val totalEgresos: Double = 0.0,
    val gananciaNet: Double = 0.0,
    val filtroActual: String = "Todo el tiempo",
    val ventasCount: Int = 0,
    val pagosCount: Int = 0,
    val gastosCount: Int = 0
)
```

### Ventajas
- ✅ Todo el estado en un lugar
- ✅ Fácil de testear
- ✅ Composables puras (sin side effects)
- ✅ Recomposición solo cuando cambia el estado

---

## Navegación

### Navigation Graph

```
main_tabs (HorizontalPager)
├── Resumen (Tab 0)
│   └── 0 -> Detalle
├── Ventas (Tab 1)
│   └── 1 -> nueva_venta
├─�� Cosechas (Tab 2)
│   └── 2 -> nueva_cosecha
└── Trabajadores (Tab 3)
    └── 3 -> nuevo_pago_trabajador
```

**Implementación:**
```kotlin
NavHost(navController = navController, startDestination = "main_tabs") {
    composable("main_tabs") {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> ResumenScreen()
                1 -> VentasScreen()
                // ...
            }
        }
    }
    composable("nueva_venta") {
        NuevaVentaScreen(onBack = { navController.popBackStack() })
    }
}
```

---

## Testing

### Estrategia de Testing

```
┌─────────────────┐
│  Unit Tests     │  ViewModels, Utilities, Formatters
│  (JUnit)        │
├─────────────────┤
│ Integration     │  Repository + DAO + Database
│ Tests           │  (usando test database)
│ (Room Testing)  │
├─────────────────┤
│ UI Tests        │  Composables, Navigation
│ (Espresso/      │  (en emulador/dispositivo)
│  Compose)       │
└─────────────────┘
```

**Ejemplo ViewModel Test:**
```kotlin
@Test
fun testVentasTotal() = runTest {
    val mockRepository = mockk<NuezRepository>()
    every { mockRepository.ventas } returns flowOf(listOf(
        Venta(cliente = "Cliente A", total = 1000.0),
        Venta(cliente = "Cliente B", total = 500.0)
    ))
    
    val viewModel = VentasViewModel(mockRepository)
    val state = viewModel.uiState.first()
    
    assertEquals(1500.0, state.totalGeneral)
}
```

---

## Performance Considerations

### 1. **Database Queries**
- ✅ Usar indexes en columnas frecuentemente filtradas
- ✅ Limitar resultados con LIMIT en queries grandes
- ✅ Usar pagination para listas largas

### 2. **Memory Management**
- ✅ Flows se cancelan cuando el scope se destruye
- ✅ StateFlow usa `WhileSubscribed` para limpiar automáticamente
- ✅ Composables se recomponem solo cuando el estado cambia

### 3. **Rendering**
- ✅ LazyColumn para listas grandes
- ✅ Compose optimiza recomposiciones automáticamente
- ✅ Evitar cálculos costosos en Composables puros

---

## Seguridad

### Consideraciones de Seguridad
- 🔒 Datos almacenados localmente (sin transmisión por red)
- 🔒 Room encripta con encryption keys (opcional)
- 🔒 No almacenamos contraseñas en v1.0
- 🔒 FileProvider para compartir archivos de forma segura

---

## Futuras Mejoras Arquitectónicas

1. **Inyección de Dependencias (Hilt)**
   - Reemplazar manual DI con Hilt
   - Proporcionar automáticamente dependencias

2. **Room Database Queries Avanzadas**
   - Migrar filtros en memoria a SQL queries
   - Mejorar performance con índices

3. **Testing Mejorado**
   - Unit tests para todos los ViewModels
   - Integration tests con test database
   - UI tests con Compose testing

4. **Error Handling**
   - Capa de error handling centralizada
   - Retry logic para operaciones
   - User-friendly error messages

5. **Caching**
   - Cache de resultados de queries
   - Invalidación automática de cache

---

## Referencias

- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
