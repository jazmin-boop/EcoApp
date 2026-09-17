# EcoHuerta - Aplicación Nativa Android para Gestión de Huertos Urbanos

[![Android Min SDK](https://img.shields.io/badge/Min%20SDK-26%20%28Android%208.0%2B%29-brightgreen?logo=android)](https://developer.android.com/)
[![Language Java](https://img.shields.io/badge/Language-Java%2011-orange?logo=java)](https://www.oracle.com/java/)
[![Database SQLite](https://img.shields.io/badge/Database-SQLite%203-blue?logo=sqlite)](https://www.sqlite.org/)
[![API REST Open--Meteo](https://img.shields.io/badge/API-Open--Meteo%20REST-009688?logo=openapi-initiative)](https://open-meteo.com/)
[![License MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**EcoHuerta** es una aplicación móvil nativa para Android diseñada como asistente técnico para la gestión, cuidado y seguimiento de huertos urbanos y plantas de interior. Integra persistencia de datos relacional con **SQLite**, consumo asíncrono de la **API REST pública de Open-Meteo**, un sistema de recordatorios y notificaciones push, un catálogo botánico con soporte de carga de imágenes mediante **Glide**, y consultas analíticas estructuradas en SQL.

---

## Características Principales

### 1. Dashboard Principal e Integración Meteorológica
- **API REST Open-Meteo**: Consulta asíncrona mediante `HttpURLConnection` y `ExecutorService` para la obtención de datos climáticos en tiempo real (temperatura y probabilidad de precipitación).
- **Selector Regional**: Componente interactivo para alternar entre diferentes ciudades de la región (*Lima, Bogotá, Ciudad de México, Buenos Aires, Santiago*) y actualizar los parámetros meteorológicos.
- **Gestión de Tareas Diarias**: Listado de tareas programadas con integración al `TimePickerDialog` nativo del sistema para la programación de recordatorios push.
- **Sincronización de Tareas**: Actualización de estados mediante casillas de verificación que registran automáticamente los eventos en la bitácora de la base de datos.
- **Visualización de Huerto**: Cuadrícula de elementos con avatares gestionados a través de **Glide**.

### 2. Catálogo y Librería Botánica
- **Búsqueda y Filtrado**: Funcionalidad de filtrado en tiempo real por categorías de especies (*Tropicales, Suculentas, Orquídeas, Trepadoras, Helechos*).
- **Detalle Estructurado**: Interfaz dividida en cuatro secciones (*Información básica, Cuidados, Problemas y Plagas, Información adicional*).
- **Sistema de Calificación**: Indicadores visuales normalizados para nivel de cuidado, requerimientos lumínicos, frecuencia de riego y toxicidad.
- **Sistema de Favoritos**: Registro persistente en `SharedPreferences` vinculado a notificaciones del sistema.

### 3. Gestión y Operaciones CRUD de Plantas
- **Panel de Administración**: Vista general con resumen de ejemplares activos y listado completo.
- **Confirmación de Eliminación**: Diálogos modulares para la supresión de registros con borrado en cascada en la base de datos.
- **Formulario de Registro**: Interfaz de alta y edición con un componente desplegable tipo `BottomSheetDialog` para la selección y autocompletado desde el catálogo.

### 4. Calendario de Cuidados
- Vista mensual interactiva orientada al cálculo algorítmico de fechas de riego y mantenimiento preventivo según los parámetros de cada especie.

### 5. Módulo de Reportes y Analítica SQL
- **Ranking Analítico**: Consultas SQL avanzadas utilizando uniones (`JOIN`), agrupamientos (`GROUP BY`), ordenamiento (`ORDER BY`) y límites (`LIMIT`) para identificar las especies con mayor regularidad de mantenimiento.
- **Historial de Riegos**: Tarjetas detalladas de eventos que registran la fecha, hora, estado del sustrato, condiciones climáticas y estado de poda.

---

## Arquitectura y Tecnologías Utilizadas

- **Lenguaje**: Java 11 (Compatible con Android SDK).
- **Interfaz de Usuario**: Android Views (XML Layouts, `CoordinatorLayout`, `NestedScrollView`, `MaterialCardView`, `ConstraintLayout`, `RecyclerView`).
- **Base de Datos Local**: SQLite (`ecohuerta.db`) con esquema normalizado en 7 tablas relacionales.
- **Carga de Imágenes**: [Glide v4.16.0](https://github.com/bumptech/glide) para el renderizado y caché de recursos gráficos.
- **Conectividad**: `HttpURLConnection` y `org.json` ejecutados en hilos secundarios mediante `ExecutorService`.
- **Notificaciones**: `BroadcastReceiver` (`NotificationReceiver`) y `NotificationManager`.

---

## Modelo Entidad-Relación de la Base de Datos (SQLite)

```plantuml
@startuml
skinparam linetype ortho
skinparam packageStyle rectangle
skinparam shadowing false

entity "catalogo_especies" as catalogo {
  * id_especie : INTEGER <<PK>>
  --
  * nombre_comun : TEXT
  * nombre_cientifico : TEXT
  * categoria : TEXT
  nivel_cuidado : TEXT
  tipo_luz : TEXT
  es_toxica : BOOLEAN
  frecuencia_riego_dias : INTEGER
  imagen_url : TEXT
  descripcion : TEXT
}

entity "plantas" as plantas {
  * id_planta : INTEGER <<PK>>
  --
  * especie_id : INTEGER <<FK>>
  * nombre : TEXT
  * fecha_siembra : TEXT
  * ubicacion : TEXT
  racha_dias : INTEGER
  notas : TEXT
}

entity "tipos_tarea" as tipos_tarea {
  * id_tipo_tarea : INTEGER <<PK>>
  --
  * nombre_tarea : TEXT
  icono : TEXT
}

entity "tareas_programadas" as tareas {
  * id_tarea : INTEGER <<PK>>
  --
  * planta_id : INTEGER <<FK>>
  * tipo_tarea_id : INTEGER <<FK>>
  * fecha_programada : TEXT
  * estado : TEXT
  fecha_completada : TEXT
}

entity "bitacora_riego" as bitacora {
  * id_riego : INTEGER <<PK>>
  --
  * planta_id : INTEGER <<FK>>
  * fecha_hora : TEXT
  temperatura_momento : REAL
  humedad_tierra : TEXT
  clima_dia : TEXT
  podada : TEXT
}

entity "favoritos" as favoritos {
  * id_favorito : INTEGER <<PK>>
  --
  * especie_id : INTEGER <<FK>>
  fecha_agregado : TEXT
}

entity "cache_clima" as cache_clima {
  * id_cache : INTEGER <<PK>>
  --
  * latitud : REAL
  * longitud : REAL
  temperatura : REAL
  probabilidad_lluvia : INTEGER
  fecha_registro : TEXT
}

catalogo ||--o{ plantas
catalogo ||--o| favoritos
plantas ||--o{ bitacora
plantas ||--o{ tareas
tipos_tarea ||--o{ tareas
@enduml
