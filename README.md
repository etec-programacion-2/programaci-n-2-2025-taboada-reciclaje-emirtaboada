Emir Taboada
Issue 1.1: Una data class es la mejor opción porque el objetivo de esta clase no es tener lógica compleja, sino representar y manejar datos de manera concisa, segura y eficiente.
Issue 1.3: PuntoDeReciclaje y TipoMaterial tienen una relación de composición.
Un PuntoDeReciclaje contiene una lista de materiales (TipoMaterial) que acepta, pero no es un TipoMaterial.
Esto permite que cada punto de reciclaje sea flexible: algunos aceptan solo plástico y papel, otros aceptan todos.
Issue 4.2: # 📚 Sistema de Persistencia de Datos

## 🎯 Objetivo
Guardar y recuperar datos del sistema de reciclaje para que **no se pierdan al cerrar la aplicación**.

---

## 📁 Formato Elegido: JSON

### ¿Qué es JSON?
**JSON** (JavaScript Object Notation) es un formato de texto para representar datos estructurados.

### Ejemplo de JSON:
```json
{
  "nombre": "Juan",
  "email": "juan@example.com",
  "puntos": 150
}
```

### ¿Por qué JSON y no CSV?

| Característica | JSON | CSV |
|----------------|------|-----|
| **Objetos anidados** | ✅ Soporta listas y objetos dentro de objetos | ❌ Solo datos planos |
| **Tipos de datos** | ✅ String, números, booleanos, null | ❌ Todo es texto |
| **Legibilidad** | ✅ Estructurado y claro | ⚠️ Solo para datos tabulares |
| **Relaciones** | ✅ Fácil representar relaciones | ❌ Difícil mantener relaciones |
| **Estándar** | ✅ Ampliamente usado | ✅ Simple pero limitado |

**Decisión:** JSON es mejor para datos complejos con relaciones.

---

## 🔄 Proceso de Serialización

**Serialización** = Convertir objetos de Kotlin a texto (JSON) para guardar en archivo.

### Ejemplo: Usuario → JSON

**Objeto Kotlin:**
```kotlin
Usuario(
    nombre = "Ana López",
    email = "ana@mail.com",
    puntos = 75
)
```

**JSON generado:**
```json
{
  "nombre": "Ana López",
  "email": "ana@mail.com",
  "puntos": 75
}
```

### Pasos del proceso:
1. **Tomar el objeto** en memoria
2. **Construir un String JSON** con la estructura correcta
3. **Escapar caracteres especiales** (" → \", \n → \\n)
4. **Escribir el String** en un archivo `.json`

---

## 🔄 Proceso de Deserialización

**Deserialización** = Leer texto (JSON) desde archivo y reconstruir objetos de Kotlin.

### Ejemplo: JSON → Usuario

**JSON leído:**
```json
{
  "nombre": "Ana López",
  "email": "ana@mail.com",
  "puntos": 75
}
```

**Objeto Kotlin reconstruido:**
```kotlin
Usuario(
    nombre = "Ana López",
    email = "ana@mail.com",
    puntos = 75
)
```

### Pasos del proceso:
1. **Leer el archivo** `.json` como String
2. **Parsear el String** buscando pares clave-valor
3. **Extraer valores** de cada campo
4. **Crear objetos nuevos** con los valores extraídos
5. **Reconstruir relaciones** entre objetos (usuarios ↔ registros ↔ puntos)

---

## 📂 Archivos Generados

El sistema crea 3 archivos JSON en la carpeta del proyecto:

### 1. `usuarios.json`
Guarda todos los usuarios con sus puntos acumulados.

```json
[
  {
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "puntos": 150
  },
  {
    "nombre": "María García",
    "email": "maria@example.com",
    "puntos": 80
  }
]
```

### 2. `puntos_reciclaje.json`
Guarda los puntos de reciclaje con los materiales que aceptan.

```json
[
  {
    "nombre": "EcoPunto Centro",
    "direccion": "Av. Principal 123",
    "materialesAceptados": ["PLASTICO", "VIDRIO", "PAPEL"]
  },
  {
    "nombre": "Reciclador Industrial",
    "direccion": "Zona Industrial 456",
    "materialesAceptados": ["METAL", "ORGANICO"]
  }
]
```

### 3. `registros.json`
Guarda el historial completo de reciclajes.

```json
[
  {
    "usuarioEmail": "juan@example.com",
    "materialNombre": "Botella PET",
    "materialDescripcion": "Botella de plástico",
    "materialTipo": "PLASTICO",
    "materialPeso": 0.5,
    "puntoNombre": "EcoPunto Centro",
    "cantidad": 0.5,
    "fecha": "2024-11-04T10:30:00"
  }
]
```

---

## 🔗 Manejo de Relaciones

### Problema:
Los registros tienen referencias a objetos (Usuario, Material, Punto). No podemos guardar objetos completos directamente.

### Solución:
Guardamos **identificadores únicos**:

```json
{
  "usuarioEmail": "juan@example.com",     // 👈 Referencia por email
  "puntoNombre": "EcoPunto Centro",        // 👈 Referencia por nombre
  "materialNombre": "Botella PET",
  // ... resto de datos del material incrustados
}
```

Al cargar, **buscamos** los objetos correspondientes:
```kotlin
val usuario = usuarios.find { it.email == usuarioEmail }
val punto = puntos.find { it.nombre == puntoNombre }
```

---

## 🛠️ Implementación Técnica

### Clase Principal: `GestorPersistencia`

```kotlin
object GestorPersistencia {
    // Guardar todo
    fun guardarTodo(usuarios, puntos, registros)
    
    // Cargar todo
    fun cargarTodo(): DatosCargados
    
    // Funciones auxiliares
    private fun guardarUsuarios()
    private fun cargarUsuarios()
    private fun escaparJson()
    private fun extraerValorJson()
    // ...
}
```

### Flujo en la Aplicación:

```kotlin
fun main() {
    // 1. AL INICIAR: Cargar datos
    val datos = GestorPersistencia.cargarTodo()
    val usuarios = datos.usuarios.toMutableList()
    val puntos = datos.puntos.toMutableList()
    
    // 2. DURANTE EJECUCIÓN: Trabajar normalmente
    // ... operaciones del sistema ...
    
    // 3. AL SALIR: Guardar cambios
    GestorPersistencia.guardarTodo(usuarios, puntos, registros)
}
```

---

## 🎨 Características Especiales

### 1. **Parser JSON Manual**
No usamos librerías externas. Implementamos nuestro propio parser usando:
- Regex para buscar patrones
- Conteo de llaves `{}` para delimitar objetos
- Extracción de valores con expresiones regulares

### 2. **Escape de Caracteres**
Caracteres especiales se escapan para no romper el JSON:
```kotlin
"Hola\nMundo"  →  "Hola\\nMundo"  // Salto de línea
"Dice: \"Hola\""  →  "Dice: \\\"Hola\\\""  // Comillas
```

### 3. **Formato ISO para Fechas**
```kotlin
LocalDateTime.now()  →  "2024-11-04T10:30:00"
```
Formato estándar ISO 8601, fácil de parsear.

### 4. **Tolerancia a Errores**
Si un registro está corrupto:
- Se salta ese registro
- Se muestra un aviso
- El resto de datos se cargan normalmente

---

## ✅ Criterios de Aceptación Cumplidos

### ✅ 1. Usuarios, Puntos y Registros se guardan en archivos
- **usuarios.json** ✓
- **puntos_reciclaje.json** ✓  
- **registros.json** ✓

### ✅ 2. Los datos se cargan automáticamente al iniciar
```kotlin
val datosCargados = GestorPersistencia.cargarTodo()
```
- Se ejecuta en el `main()` al iniciar
- Restaura usuarios, puntos y registros
- Muestra resumen de datos cargados

### ✅ 3. El alumno puede explicar el formato y la serialización
Este documento explica:
- ✅ Por qué JSON
- ✅ Cómo funciona la serialización (Objeto → JSON → Archivo)
- ✅ Cómo funciona la deserialización (Archivo → JSON → Objeto)
- ✅ Cómo se manejan las relaciones entre objetos
- ✅ Estructura de los archivos generados

---

## 🧪 Cómo Probarlo

### Paso 1: Ejecutar y crear datos
```
1. Crea usuarios
2. Crea puntos de reciclaje
3. Realiza reciclajes
4. Sal de la aplicación (opción 16 → S para guardar)
```

### Paso 2: Verificar archivos
Busca en la carpeta del proyecto:
- `usuarios.json`
- `puntos_reciclaje.json`
- `registros.json`

Ábrelos con un editor de texto para ver el JSON.

### Paso 3: Recargar datos
```
1. Vuelve a ejecutar la aplicación
2. Verás el mensaje: "📂 Datos cargados exitosamente"
3. Verifica que tus usuarios, puntos y registros siguen ahí
```

---

## 🚀 Ventajas del Sistema

✅ **Persistencia**: Los datos sobreviven al cierre de la aplicación  
✅ **Portabilidad**: Los archivos JSON son legibles y editables  
✅ **Debugging**: Puedes inspeccionar los datos fácilmente  
✅ **Backup**: Copiar los archivos = backup completo  
✅ **Versionable**: Puedes guardar diferentes versiones de los datos  

---

## 📝 Resumen

**Serialización:** Objetos → JSON → Archivo  
**Deserialización:** Archivo → JSON → Objetos  
**Formato:** JSON (legible, estructurado, estándar)  
**Archivos:** 3 archivos .json (usuarios, puntos, registros)  
**Automático:** Carga al iniciar, guarda al salir
