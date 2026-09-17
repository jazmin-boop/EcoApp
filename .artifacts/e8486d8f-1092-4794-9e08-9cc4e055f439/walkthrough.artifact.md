# Walkthrough - EcoHuerta App

Hemos completado el desarrollo de la aplicación **EcoHuerta** siguiendo exactamente todas las directrices, plantillas visuales y requisitos técnicos obligatorios.

## 📱 Resumen de Pantallas y Componentes Implementados

### 1. Portada (Splash Screen) y Pantalla de Bienvenida (Login)
- **`activity_splash.xml` & `SplashActivity.java`**:
  - Cuenta con la imagen de fondo (`fondo.png`) y una capa oscura translúcida.
  - Muestra el título **EcoHuerta**, una **imagen circular perfecta** con el `icono.png` de extremo a extremo, una frase de reflexión sobre la naturaleza y un botón de ingreso.
- **`activity_login.xml` & `LoginActivity.java`**:
  - Recrea el diseño moderno de la plantilla solicitada, con el icono circular central, título, subtítulo en español y los botones de **"Registrarse"** e **"Iniciar sesión"**.

### 2. Panel Principal (Dashboard / MainActivity)
- **`activity_main.xml` & `MainActivity.java`**:
  - **Cabecera**: Saludo dinámico *"Buenas tardes"* y subtítulo de tareas pendientes.
  - **Tarjeta de Clima Interactiva (Open-Meteo)**: Consumo en tiempo real de la API pública gratuita de `Open-Meteo` mediante `HttpURLConnection` en un hilo secundario (`ExecutorService`), parseando la temperatura real (`°C`) y el estado del clima (Soleado, Nublado, Lluvia, etc.) sin requerir API Key.
  - **Estadísticas (4 tarjetas)**: Resumen de tareas (Por hacer, Hecho hoy, Atrasadas, Destacados).
  - **Sección de Tareas (Today's To-Do)**: Tarjetas horizontales de Fertilizar, Podar y Trasplantar.
  - **Plantas Recientes**: Muestra las imágenes que subiste (`helecho.png` y `suculenta.png`) con sus respectivos nombres y estados de riego.
  - **Barra de Navegación Inferior y Botón Flotante (FAB)**: Diseño flotante con iconos y el botón verde de acción `+` en la parte inferior derecha.

### 3. Capa de Persistencia (Base de Datos SQLite)
- **`DBHelper.java`**:
  - Extiende de `SQLiteOpenHelper` configurando la base de datos `ecohuerta.db`.
  - Implementa las dos tablas relacionales obligatorias:
    - **`plantas`**: `id_planta` (PK), `nombre`, `especie`, `fecha_siembra`, `frecuencia_riego_dias`, `ubicacion` (interior/exterior), `notas`.
    - **`bitacora_riego`**: `id_riego` (PK), `planta_id` (FK relacionada a `plantas` con borrado en cascada), `fecha_hora`, `temperatura_momento`, `llovio`.
- **Modelos**:
  - Clases de dominio **`Planta.java`** y **`BitacoraRiego.java`**.

## ✅ Verificación de Compilación
- El proyecto compila **exitosamente** (`BUILD SUCCESSFUL`) en Android Studio sin errores de sintaxis ni conflictos de recursos.
