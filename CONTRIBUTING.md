# Contribuyendo a NuezGolControl

¡Gracias por tu interés en contribuir! Este documento te guía a través del proceso.

## 📋 Antes de Empezar

1. **Fork** el repositorio
2. **Clone** tu fork: `git clone https://github.com/tu-usuario/NuezGolControl-Movil.git`
3. **Crea una rama** para tu feature: `git checkout -b feature/mi-feature`
4. **Sigue las convenciones** de código del proyecto

## 🎯 Tipos de Contribución

### 🐛 Reportar Bugs

Abre un [Issue](https://github.com/victgolozkiv/NuezGolControl-Movil/issues) con:

```
Título: [BUG] Breve descripción del problema

Descripción:
- ¿Qué intentabas hacer?
- ¿Qué sucedió?
- ¿Qué debería haber pasado?
- Pasos para reproducir

Detalles:
- Teléfono/Emulador: [ej: Pixel 6, API 33]
- Versión de Android: [ej: 13]
- Versión de la app: [ej: 2.0.0]
```

### ✨ Solicitar Nuevas Características

Abre un [Issue](https://github.com/victgolozkiv/NuezGolControl-Movil/issues) con:

```
Título: [FEATURE] Nombre de la característica

Descripción:
- ¿Por qué la necesitas?
- ¿Cómo debería funcionar?
- ¿Hay ejemplos o casos de uso?

Alternativas consideradas:
- Otra forma de resolver esto
```

### 💻 Contribuir Código

#### Configuración del Entorno

1. **Instala Android Studio** (Ladybug o superior)
2. **JDK 17** instalado
3. Sincroniza Gradle: `./gradlew sync`

#### Convenciones de Código

```kotlin
// 1. Nombrado de paquetes (snake_case en archivos, camelCase en clases)
package com.nuezgolcontrol.app.ui.resumen

// 2. Clases ViewModel (sufijo "ViewModel")
class MiFeatureViewModel(private val repository: NuezRepository) : ViewModel()

// 3. Clases Screen (sufijo "Screen", parámetro @Composable)
@Composable
fun MiFeatureScreen(viewModel: MiFeatureViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    // UI Code
}

// 4. Data Classes para UI State
data class MiFeatureUiState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// 5. Manejo de errores
viewModelScope.launch {
    try {
        // Tu código
    } catch (e: Exception) {
        e.printStackTrace()
        // Maneja el error apropiadamente
    }
}
```

#### Pasos para Enviar un PR

1. **Haz cambios** siguiendo las convenciones
2. **Prueba localmente**: `./gradlew build`
3. **Crea un commit** con mensaje descriptivo:
   ```bash
   git commit -m "feat: agregar búsqueda de ventas por cliente"
   ```
   Usa prefijos:
   - `feat:` - Nueva característica
   - `fix:` - Corrección de bug
   - `refactor:` - Cambio de código sin nueva funcionalidad
   - `docs:` - Cambios en documentación
   - `style:` - Cambios de formato (sin lógica)

4. **Push a tu rama**:
   ```bash
   git push origin feature/mi-feature
   ```

5. **Abre un Pull Request** en el repositorio original con:
   - Descripción clara del cambio
   - Referencia a issue relacionado (si existe)
   - Screenshots si es un cambio visual
   - Checklist de pruebas

#### Template de PR

```markdown
## Descripción
Breve descripción de los cambios.

## Tipo de Cambio
- [ ] Bug fix (corrección que no rompe funcionalidad)
- [ ] New feature (nueva funcionalidad)
- [ ] Breaking change (cambio que rompe funcionalidad existente)
- [ ] Documentation update

## ¿Cómo ha sido testeado?
- [ ] Probado en emulador (API XX)
- [ ] Probado en dispositivo físico (Modelo, API XX)
- [ ] Pruebas unitarias agregadas

## Checklist
- [ ] Mi código sigue las convenciones del proyecto
- [ ] He actualizado la documentación si es necesario
- [ ] No hay errores de lint
- [ ] He probado la feature en al menos 2 dispositivos/emuladores

## Screenshots (si aplica)
[Pegue capturas de pantalla aquí]
```

## 📦 Estructura de Cambios Esperados

Para nuevas características:

```
feat: nombre-de-feature
├── ViewModel (new)
├── Screen (new)
├── Componentes (if needed)
└── Tests (recommended)
```

## 🧪 Testing

```bash
# Ejecutar tests
./gradlew test

# Ejecutar tests instrumentados (en dispositivo/emulador)
./gradlew connectedAndroidTest

# Build release
./gradlew assembleRelease
```

## 📖 Documentación

Para cada nueva feature, actualiza:

1. **README.md** - Agrega descripción
2. **CHANGELOG.md** - Documenta el cambio
3. **Code comments** - Explica lógica compleja
4. **Docstrings** - Para funciones públicas

Ejemplo:

```kotlin
/**
 * Filtra ventas por rango de fechas.
 *
 * @param ventas Lista de ventas a filtrar
 * @param dateRange Rango de fechas para el filtro
 * @return Lista de ventas que coinciden con el rango
 */
fun filtrarPorFecha(ventas: List<Venta>, dateRange: DateRange): List<Venta> {
    // Implementation
}
```

## 🤝 Código de Conducta

Este proyecto respeta a todos. Por favor:

- Sé respetuoso en comentarios y discusiones
- Acepta crítica constructiva
- Enfócate en lo que es mejor para el proyecto
- Reporta comportamiento inapropiado

## ❓ Preguntas?

- Abre una [Discusión](https://github.com/victgolozkiv/NuezGolControl-Movil/discussions)
- Contacta al maintainer
- Lee la documentación existente

---

**¡Gracias por tu contribución!** 🎉
