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

## 📊 Ejemplo de Uso

```
SISTEMA DE RECICLAJE INTERACTIVO 🌱

👤 Usuario: Juan (15 pts)

MENÚ PRINCIPAL
1. 👤 Gestión de Usuarios
2. 📦 Gestión de Materiales
3. 📍 Gestión de Puntos de Reciclaje
4. ♻️  Registrar Reciclaje
5. 📊 Consultas e Informes
6. 💰 Ver Tabla de Puntos
7. 💾 Guardar Datos
8. 🚪 Salir

➤ Opción: 4

--- REGISTRAR RECICLAJE ---
Material: Botella PET (PLASTICO) - 1.0 kg
Punto: EcoPunto Centro

✅ ¡Reciclaje exitoso!
🌟 Ganaste 5 puntos
📊 Puntos totales: 20
```

---

## 👥 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 📧 Contacto

Para preguntas o sugerencias, por favor abre un Issue en el repositorio.

---

## 🎓 Créditos

Desarrollado como proyecto académico para demostrar:
- Principios SOLID
- Programación Orientada a Objetos
- Herencia y Polimorfismo
- Patrones de Diseño
- Persistencia de Datos

---

¡Gracias por usar el Sistema de Reciclaje Interactivo! 🌍♻️🌱