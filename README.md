# Sistema de Reciclaje Interactivo 🌱♻️

Sistema de gestión de reciclaje desarrollado en Kotlin que permite a los usuarios registrar materiales reciclables, acumular puntos y consultar estadísticas.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Ejecución](#ejecución)
- [Arquitectura y Principios](#arquitectura-y-principios)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Funcionalidades](#funcionalidades)
- [Persistencia de Datos](#persistencia-de-datos)

---

## ✨ Características

- ✅ Gestión completa de usuarios con sistema de puntos
- ✅ Registro de materiales reciclables (Plástico, Vidrio, Papel, Metal, Orgánico)
- ✅ Gestión de puntos de reciclaje
- ✅ Cálculo automático de puntos según tipo y cantidad de material
- ✅ Historial y estadísticas de reciclaje
- ✅ Persistencia de datos en formato JSON
- ✅ Interfaz de consola interactiva

---

## 🔧 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

### 1. Java Development Kit (JDK) 21

**Windows:**
```bash
# Verificar instalación
java -version
javac -version

# Si no está instalado, descargar desde:
# https://www.oracle.com/java/technologies/downloads/#java21
# O usar OpenJDK: https://adoptium.net/
```

**Linux (Ubuntu/Debian):**
```bash
# Instalar OpenJDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Verificar instalación
java -version
```

**macOS:**
```bash
# Con Homebrew
brew install openjdk@21

# Agregar al PATH
echo 'export PATH="/usr/local/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verificar
java -version
```

### 2. Gradle (opcional, el proyecto incluye Gradle Wrapper)

El proyecto incluye **Gradle Wrapper** (`gradlew` y `gradlew.bat`), por lo que NO es necesario instalar Gradle manualmente.

---

## 📥 Instalación

### Paso 1: Clonar o Descargar el Proyecto

**Opción A - Clonar con Git:**
```bash
git clone https://github.com/tu-usuario/sistema-reciclaje.git
cd sistema-reciclaje
```

**Opción B - Descargar ZIP:**
1. Descarga el archivo ZIP del proyecto
2. Extrae el contenido en una carpeta de tu preferencia
3. Abre una terminal/consola en esa carpeta

### Paso 2: Verificar la Estructura

Asegúrate de que la estructura del proyecto sea:

```
sistema-reciclaje/
├── src/
│   └── main/
│       └── kotlin/
│           └── org/
│               └── example/
│                   ├── App.kt
│                   ├── Usuario.kt
│                   ├── MaterialReciclable.kt
│                   ├── PuntoDeReciclaje.kt
│                   ├── GestorDeReciclaje.kt
│                   ├── CalculadoraPuntos.kt
│                   ├── RegistroReciclaje.kt
│                   ├── RepositorioRegistro.kt
│                   ├── GestorPersistencia.kt
│                   ├── Interfaz.kt
│                   └── TipoMaterial.kt
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew (Linux/Mac)
├── gradlew.bat (Windows)
└── README.md
```

---

## 🚀 Ejecución

### Método 1: Usar Gradle Wrapper (Recomendado)

**Windows:**
```cmd
# 1. Compilar el proyecto
gradlew.bat build

# 2. Ejecutar la aplicación
gradlew.bat run
```

**Linux/macOS:**
```bash
# 1. Dar permisos de ejecución (solo la primera vez)
chmod +x gradlew

# 2. Compilar el proyecto
./gradlew build

# 3. Ejecutar la aplicación
./gradlew run
```

### Método 2: Compilar y Ejecutar Manualmente

**Compilar:**
```bash
# Windows
gradlew.bat compileKotlin

# Linux/macOS
./gradlew compileKotlin
```

**Ejecutar el JAR generado:**
```bash
# Después de compilar, el JAR estará en build/libs/
java -jar build/libs/sistema-reciclaje.jar
```

### Método 3: Desde IDE (IntelliJ IDEA)

1. Abre IntelliJ IDEA
2. File → Open → Selecciona la carpeta del proyecto
3. Espera a que Gradle sincronice las dependencias
4. Busca el archivo `App.kt`
5. Click derecho → Run 'AppKt'

---

## 🏗️ Arquitectura y Principios

El proyecto está diseñado siguiendo los **principios SOLID** y las mejores prácticas de **Programación Orientada a Objetos**:

### ✅ Principios SOLID Implementados

#### S - Single Responsibility Principle
Cada clase tiene una única responsabilidad:
- `AplicacionReciclaje`: Coordina el flujo principal
- `GestorMenu`: Gestiona la visualización de menús
- `ProcesadorOpciones`: Procesa las opciones del menú
- `RegistradorReciclaje`: Gestiona el registro de reciclaje
- `GestorPersistencia`: Maneja la persistencia de datos

#### O - Open/Closed Principle
El sistema está abierto a extensión, cerrado a modificación:
- `MaterialReciclableBase` es abstracta y extensible
- Nuevos tipos de materiales pueden agregarse sin modificar código existente
- Jerarquía de clases: `MaterialPlastico`, `MaterialVidrio`, etc.

#### L - Liskov Substitution Principle
Las subclases pueden sustituir a sus clases base:
- Todos los `MaterialReciclableBase` pueden usarse polimórficamente
- Los menús heredan de `MenuBase` y son intercambiables

#### I - Interface Segregation Principle
Interfaces específicas y segregadas:
- `ICalculadoraPuntos`: Cálculo de puntos
- `IRepositorioRegistros`: Gestión de registros
- `IGestorReciclaje`: Operaciones de reciclaje
- `IValidadorReciclaje`: Validación
- `ICalculadorEstadisticas`: Estadísticas

#### D - Dependency Inversion Principle
Dependencia de abstracciones, no de concreciones:
- `GestorDeReciclajeImpl` depende de interfaces (`ICalculadoraPuntos`, `IRepositorioRegistros`)
- Inyección de dependencias en constructores

### ✅ Principios POO Implementados

#### Herencia
Jerarquía de clases:
```kotlin
MaterialReciclableBase (abstracta)
├── MaterialPlastico
├── MaterialVidrio
├── MaterialPapel
├── MaterialMetal
└── MaterialOrganico

MenuBase (abstracta)
├── MenuUsuarios
├── MenuMateriales
├── MenuPuntosReciclaje
└── MenuConsultas
```

#### Encapsulamiento
- Propiedades privadas con getters/setters
- Data classes para inmutabilidad
- Modificadores de acceso apropiados

#### Polimorfismo
- Métodos abstractos sobrescritos en subclases
- Comportamiento específico según el tipo de material
- `calcularImpactoAmbiental()` tiene implementaciones diferentes

---

## 📂 Estructura del Proyecto

```
org.example/
├── App.kt                      # Punto de entrada y clases principales
├── Usuario.kt                  # Gestión de usuarios
├── MaterialReciclable.kt       # Jerarquía de materiales (con herencia)
├── PuntoDeReciclaje.kt         # Gestión de puntos de reciclaje
├── GestorDeReciclaje.kt        # Lógica principal de reciclaje
├── CalculadoraPuntos.kt        # Cálculo de puntos
├── RegistroReciclaje.kt        # Registro de transacciones
├── RepositorioRegistro.kt      # Almacenamiento en memoria
├── GestorPersistencia.kt       # Persistencia en JSON
├── Interfaz.kt                 # Interfaces del sistema
└── TipoMaterial.kt             # Enum de tipos de material
```

---

## 🎯 Funcionalidades

### 1. Gestión de Usuarios
- Crear nuevo usuario
- Seleccionar usuario existente
- Ver perfil con nivel y estadísticas
- Sistema de niveles:
    - 🌱 Principiante (< 50 puntos)
    - 🌿 Intermedio (50-149 puntos)
    - 🌳 Avanzado (150-299 puntos)
    - 🏆 Experto (300+ puntos)

### 2. Gestión de Materiales
- Crear materiales reciclables
- Ver lista de materiales registrados
- Información de impacto ambiental
- Categorización automática

### 3. Gestión de Puntos de Reciclaje
- Crear puntos de reciclaje
- Configurar materiales aceptados
- Ver historial de materiales recibidos
- Estadísticas por punto

### 4. Registrar Reciclaje
- Seleccionar material y punto
- Validación automática
- Asignación de puntos
- Registro en historial

### 5. Tabla de Puntos

| Material | Puntos por kg |
|----------|---------------|
| PLASTICO | 5             |
| METAL    | 4             |
| VIDRIO   | 3             |
| PAPEL    | 2             |
| ORGANICO | 1             |

### 6. Consultas e Informes
- Ver puntos acumulados
- Historial personal de reciclajes
- Estadísticas generales del sistema
- Filtrado por usuario o punto

---

## 💾 Persistencia de Datos

El sistema guarda automáticamente los datos en archivos JSON:

- `usuarios.json`: Información de usuarios y puntos
- `materiales.json`: Materiales registrados
- `puntos_reciclaje.json`: Puntos de reciclaje
- `registros.json`: Historial completo de reciclajes

**Ubicación:** En el directorio raíz del proyecto

**Carga automática:** Al iniciar la aplicación, se cargan los datos previos

**Guardar manualmente:** Opción 7 del menú principal

**Guardar al salir:** Se pregunta al salir si desea guardar

---

## 🐛 Solución de Problemas

### Error: "java: command not found"
**Solución:** Instala JDK 21 (ver sección Requisitos Previos)

### Error: "Permission denied: ./gradlew"
**Solución (Linux/Mac):**
```bash
chmod +x gradlew
```

### Error al compilar
**Solución:** Limpia el proyecto y recompila
```bash
# Windows
gradlew.bat clean build

# Linux/macOS
./gradlew clean build
```

### Error: "Could not find or load main class"
**Solución:** Verifica que el archivo `build.gradle.kts` tenga:
```kotlin
application {
    mainClass = "org.example.AppKt"
}
```

---

### **1. Gestión de Usuarios (Opción 1)**

#### **Submenú:**
1. Crear nuevo usuario
2. Seleccionar usuario existente
3. Ver todos los usuarios
4. Ver mi perfil
5. Volver al menú principal

#### **1.1 Crear Nuevo Usuario**

**Proceso:**
1. Ingresa tu nombre
2. Ingresa tu email (debe ser único)
3. El usuario se crea con 0 puntos iniciales
4. Automáticamente queda seleccionado como usuario activo

**Ejemplo:**
```
Nombre: María González
Email: maria@email.com

✅ Usuario creado exitosamente
```

#### **1.2 Seleccionar Usuario Existente**

Lista todos los usuarios registrados con sus puntos acumulados y te permite seleccionar uno como usuario activo.

#### **1.3 Ver Perfil de Usuario**

Muestra información completa del usuario activo:
- Nombre y email
- Puntos acumulados
- Nivel actual (Principiante, Intermedio, Avanzado, Experto)
- Estadísticas personales:
  - Total de reciclajes realizados
  - Kilos totales reciclados
  - Desglose por tipo de material

**Sistema de Niveles:**
- 🌱 **Principiante**: 0-49 puntos
- 🌿 **Intermedio**: 50-149 puntos
- 🌳 **Avanzado**: 150-299 puntos
- 🏆 **Experto**: 300+ puntos

---

### **2. Gestión de Materiales (Opción 2)**

#### **Submenú:**
1. Crear nuevo material
2. Ver materiales registrados
3. Volver al menú principal

#### **2.1 Crear Material Reciclable**

**Pasos:**
1. Ingresa el nombre del material (ej: "Botellas PET")
2. Ingresa una descripción (ej: "Botellas plásticas de bebidas")
3. Selecciona el tipo de material:
   - **PLASTICO** - 5 puntos/kg
   - **METAL** - 4 puntos/kg
   - **VIDRIO** - 3 puntos/kg
   - **PAPEL** - 2 puntos/kg
   - **ORGANICO** - 1 punto/kg
4. Ingresa el peso en kilogramos

**El sistema mostrará:**
- Confirmación de creación
- Puntos potenciales
- Impacto ambiental (tiempo de degradación)
- Categoría según peso (Ligero/Medio/Pesado)

**Ejemplo:**
```
Nombre: Latas de aluminio
Descripción: Latas de bebidas vacías
Tipo: 4 (METAL)
Peso: 0.5 kg

✅ Material creado exitosamente
💰 Puntos potenciales: 2 puntos
🌍 Impacto ambiental: 25.0 (escala de degradación)
📊 Categoría: Metal - Ligero
```

#### **2.2 Ver Materiales Registrados**

Lista todos los materiales con:
- Nombre, tipo y descripción
- Peso y puntos potenciales
- Impacto ambiental
- Categoría

---

### **3. Gestión de Puntos de Reciclaje (Opción 3)**

#### **Submenú:**
1. Crear nuevo punto de reciclaje
2. Ver puntos de reciclaje
3. Ver historial de un punto
4. Ver estadísticas de un punto
5. Volver al menú principal

#### **3.1 Crear Punto de Reciclaje**

**Pasos:**
1. Ingresa el nombre del punto (ej: "EcoPunto Centro")
2. Ingresa la dirección completa
3. Selecciona los materiales aceptados (puedes elegir varios separados por comas)

**Ejemplo:**
```
Nombre: EcoEstación Norte
Dirección: Av. Argentina 456, Neuquén
Selecciona materiales aceptados (ej: 1,2,3):
1. PLASTICO
2. VIDRIO
3. PAPEL
4. METAL
5. ORGANICO

Selección: 1,3,4

✅ Punto creado exitosamente
Acepta: PLASTICO, PAPEL, METAL
```

#### **3.2 Ver Puntos de Reciclaje**

Muestra todos los puntos registrados con sus direcciones y materiales aceptados.

#### **3.3 Ver Historial de un Punto**

Muestra todos los materiales recibidos por el punto seleccionado:
- Lista de materiales con cantidades
- Total de kilogramos recibidos

#### **3.4 Ver Estadísticas de un Punto**

Dashboard completo del punto:
- Dirección y materiales aceptados
- Total de reciclajes recibidos
- Total de kg recibidos
- Número de usuarios únicos que lo utilizaron

---

### **4. Registrar Reciclaje (Opción 4) ⭐ FUNCIÓN PRINCIPAL**

Esta es la operación central del sistema.

#### **Requisitos Previos:**
✅ Tener un usuario seleccionado (opción 1)  
✅ Tener materiales creados (opción 2)  
✅ Tener puntos de reciclaje creados (opción 3)

#### **Proceso de Reciclaje:**

1. **Selección de Material**
   - El sistema muestra todos los materiales disponibles
   - Elige el número del material a reciclar

2. **Selección de Punto de Reciclaje**
   - El sistema muestra todos los puntos disponibles
   - Elige el punto donde realizarás el reciclaje

3. **Validación Automática**
   - El sistema verifica que el punto acepte ese tipo de material
   - Valida que la cantidad sea positiva

4. **Ingreso de Cantidad**
   - Ingresa los kilogramos a reciclar
   - Puedes presionar Enter para usar el peso predeterminado del material

5. **Procesamiento y Resultado**
   - El sistema calcula los puntos ganados
   - Actualiza tu perfil
   - Registra la transacción
   - Actualiza el historial del punto

#### **Cálculo de Puntos:**

```
Puntos Ganados = Cantidad (kg) × Factor del Material

Factores:
- PLASTICO: 5 puntos/kg
- METAL: 4 puntos/kg
- VIDRIO: 3 puntos/kg
- PAPEL: 2 puntos/kg
- ORGANICO: 1 punto/kg
```

#### **Ejemplo Completo:**

```
--- RECICLAR MATERIAL ---

Materiales disponibles:
1. Botellas PET (PLASTICO) - 0.5 kg
2. Latas de aluminio (METAL) - 0.5 kg
3. Diarios viejos (PAPEL) - 2.0 kg

Selecciona el material: 1

Puntos de reciclaje disponibles:
1. EcoPunto Centro - Acepta: [PLASTICO, VIDRIO, PAPEL]
2. EcoEstación Norte - Acepta: [PLASTICO, METAL]

Selecciona el punto de reciclaje: 1

Cantidad a reciclar (kg) [0.5]: 3.5

✅ ¡Reciclaje exitoso!
María González recicló 3.50 kg de 'Botellas PET' en 'EcoPunto Centro'
🌟 Ganaste 17 puntos (3.5 kg × 5 puntos/kg)
📊 Puntos totales: 67
```

#### **Mensajes de Error Comunes:**

| Error | Causa | Solución |
|-------|-------|----------|
| "No hay usuario seleccionado" | Falta activar un usuario | Ve a Opción 1 → Crear/Seleccionar usuario |
| "No hay materiales disponibles" | No se crearon materiales | Ve a Opción 2 → Crear material |
| "No hay puntos de reciclaje" | No se crearon puntos | Ve a Opción 3 → Crear punto |
| "El punto no acepta este material" | Incompatibilidad | Verifica materiales aceptados del punto |
| "La cantidad debe ser mayor a 0" | Cantidad inválida | Ingresa un número positivo |

---

### **5. Consultas e Informes (Opción 5)**

#### **Submenú:**
1. Ver mis puntos
2. Ver mi perfil completo
3. Ver mi historial de reciclajes
4. Ver todos los registros del sistema
5. Ver estadísticas generales
6. Volver al menú principal

#### **5.1 Ver Mis Puntos**

Muestra rápidamente tu puntaje actual.

#### **5.2 Ver Mi Perfil Completo**

Igual que Opción 1.4 - Muestra perfil detallado con estadísticas.

#### **5.3 Ver Mi Historial de Reciclajes**

Muestra cada transacción realizada:
- Material y cantidad reciclada
- Punto de reciclaje utilizado
- Fecha y hora exacta
- Puntos ganados en cada operación

**Ejemplo:**
```
╔═══════════════════════════════════════╗
    HISTORIAL DE María González
╚═══════════════════════════════════════╝

Total de reciclajes: 3

--- Registros ---

#1
┌─────────────────────────────────────────
│ REGISTRO DE RECICLAJE
├─────────────────────────────────────────
│ Usuario: María González
│ Material: Botellas PET (PLASTICO)
│ Cantidad: 3.50 kg
│ Punto: EcoPunto Centro
│ Fecha: 21/11/2025 14:30:15
│ Puntos ganados: 17
└─────────────────────────────────────────
```

#### **5.4 Ver Todos los Registros del Sistema**

Acceso completo al historial global:
- Total de registros en el sistema
- Total de kg reciclados
- Total de puntos otorgados
- Detalle de cada transacción

#### **5.5 Ver Estadísticas Generales**

Dashboard completo del sistema:

```
╔═══════════════════════════════════════════════╗
║         ESTADÍSTICAS GENERALES               ║
╚═══════════════════════════════════════════════╝

📊 Resumen General:
  • Total de reciclajes: 45
  • Total de kg reciclados: 127.50 kg
  • Usuarios activos: 8
  • Puntos de reciclaje activos: 3

📦 Por tipo de material:
  • PLASTICO: 45.00 kg (15 reciclajes)
  • VIDRIO: 32.50 kg (12 reciclajes)
  • PAPEL: 28.00 kg (10 reciclajes)
  • METAL: 18.00 kg (6 reciclajes)
  • ORGANICO: 4.00 kg (2 reciclajes)
```

---

### **6. Ver Tabla de Puntos (Opción 6)**

Muestra la tabla de conversión completa:

```
╔═══════════════════════════════════════╗
║    TABLA DE PUNTOS POR MATERIAL       ║
╚═══════════════════════════════════════╝

Tipo de Material    | Puntos por kg
--------------------|---------------
PLASTICO            | 5
VIDRIO              | 3
PAPEL               | 2
METAL               | 4
ORGANICO            | 1
```

---

### **7. Guardar Datos (Opción 7)**

Guarda manualmente el estado actual del sistema en archivos JSON.

**Nota:** Los datos también se guardan automáticamente al salir del sistema si lo confirmas.

---

### **8. Salir (Opción 8)**

Cierra la aplicación de forma segura:

1. Pregunta si deseas guardar los cambios
2. Si aceptas (S), guarda todo automáticamente
3. Si rechazas (N), los cambios no guardados se pierden
4. Muestra mensaje de despedida

---


¡Gracias por usar el Sistema de Reciclaje Interactivo! 🌍♻️🌱
