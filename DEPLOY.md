# 📋 Checklist de Deploy

## Antes de Publicar una Nueva Versión

### 1. Code Review
- [ ] Todos los tests pasan (`./gradlew test`)
- [ ] Lint check sin errores (`./gradlew lint`)
- [ ] Build release sin warnings (`./gradlew assembleRelease`)
- [ ] Pruebas en múltiples dispositivos/APIs

### 2. Documentación
- [ ] README.md actualizado
- [ ] CHANGELOG.md con cambios nuevos
- [ ] Comentarios de código claros
- [ ] Docstrings para funciones públicas

### 3. Versioning
- [ ] Versión incrementada en `build.gradle.kts`
- [ ] CHANGELOG.md con fecha de release
- [ ] Tag creado en Git

### 4. Testing
- [ ] Pruebas manuales completadas
- [ ] Casos límite testeados
- [ ] Compatibilidad con Android 8.0+ verificada

### 5. Release
- [ ] APK generado sin errores
- [ ] Tamaño razonable (<50MB)
- [ ] ProGuard/R8 configurado correctamente
- [ ] Release notes redactadas

---

## Versionado Semántico

Usamos MAJOR.MINOR.PATCH:

- **MAJOR**: Cambios incompatibles (v1.0.0 → v2.0.0)
- **MINOR**: Nuevas características compatibles (v2.0.0 → v2.1.0)
- **PATCH**: Correcciones de bugs (v2.1.0 → v2.1.1)

Ejemplos:
- `feat: nuevo módulo` → incrementa MINOR
- `fix: corrige crash` → incrementa PATCH
- `refactor: reescribe arquitectura` → incrementa MAJOR
