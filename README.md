# EcoHuerta - Aplicación Nativa Android para Gestión de Huertos Urbanos

**EcoHuerta es una aplicación móvil nativa para Android orientada a la gestión de huertos urbanos y plantas en Latinoamérica. Integra persistencia en SQLite, consumo asíncrono de la API Open-Meteo, notificaciones push, catálogo con Glide y analítica SQL.
---

## Características Principales

### 1. Dashboard Principal e Integración Meteorológica en Vivo
- **API REST Open-Meteo en Vivo**: Consulta asíncrona a la API REST de Open-Meteo (`https://api.open-meteo.com/v1/forecast?latitude=-12.05&longitude=-77.04&current=temperature_2m,precipitation&daily=precipitation_probability_max&timezone=auto`) mediante `HttpURLConnection` y `ExecutorService` para obtener temperatura real y probabilidad de lluvia diaria sin requerir claves de API.
- **Selector de Región Móvil**: Interfaz desplegable para alternar entre diversas ciudades de América Latina (*Lima, Bogotá, Ciudad de México, Buenos Aires, Santiago*) y actualizar automáticamente la información climática.
- **Listado Dinámico de Tareas Diarias**: Tareas programadas con botones de alarma que abren el componente nativo `TimePickerDialog` para agendar recordatorios PUSH.
- **Tachado y Sincronización Automática**: Al marcar el casillero de verificación de una tarea, se aplica un formato visual de completado y se registra el evento en la bitácora de SQLite.
- **Mi Huerto**: Cuadrícula interactiva con avatares circulares optimizados mediante Glide.

### 2. Catálogo y Librería de Plantas
- **Buscador en Tiempo Real y Filtro por Categorías**: Filtra especies botánicas por categoría (*Tropicales, Suculentas, Orquídeas, Trepadoras, Helechos*) y búsqueda por nombre común o científico.
- **Detalle de Planta en 4 Pestañas**: Información estructurada en cuatro pestañas (*Información básica, Cuidados, Problemas y Plagas, Otros*) con viñetas temáticas de hojas.
- **Calificación por Estrellas Estilo Michelin**: Evaluación cuantitativa de cuatro atributos clave (*nivel de cuidado, iluminación, riego y toxicidad*) mediante un sistema de evaluación de cuatro estrellas (`★★★★☆`).
- **Sistema de Favoritos**: Botón de interacción con almacenamiento persistente en `SharedPreferences` y emisión de notificaciones PUSH del sistema fuera de la aplicación.

### 3. Gestión y CRUD de Plantas
- **Administración del Huerto**: Vista de administración con resumen de plantas activas y listado completo.
- **Modal de Confirmación de Eliminación**: Diálogo personalizado para confirmar la eliminación permanente de una planta con borrado en cascada en la base de datos.
- **Formulario Inteligente con Modal de Autocompletado**: Formulario de alta y edición de alto contraste que incluye un componente `BottomSheetDialog` desplegable para elegir plantas del catálogo y autocompletar sus datos.

### 4. Calendario de Cuidados
- Vista de calendario mensual interactiva que calcula algorítmicamente las fechas futuras de riego y mantenimiento en función de los parámetros de cada especie.

### 5. Módulo de Reportes y Analítica SQL
- **Ranking Top 5+ Plantas Más Regadas**: Consultas SQL avanzadas utilizando las cláusulas `JOIN`, `GROUP BY`, `ORDER BY` y `LIMIT` para determinar las plantas con mayor racha de cumplimiento.
- **Historial de Riegos**: Módulo de auditoría que muestra fecha, hora, estado del sustrato (*Húmeda/Seca*), clima ambiental y mantenimiento de poda (*Podada / Sin poda*).

---

## Arquitectura y Tecnologías Utilizadas

- **Lenguaje**: Java 11 (SDK Nativo de Android).
- **Interfaz de Usuario**: Vistas nativas de Android en XML (`CoordinatorLayout`, `NestedScrollView`, `MaterialCardView`, `ConstraintLayout`, `RecyclerView`).
- **Base de Datos Local**: SQLite v4 (`ecohuerta.db`) con arquitectura normalizada de 7 tablas relacionales.
- **Carga de Imágenes**: [Glide v4.16.0](https://github.com/bumptech/glide) para almacenamiento en caché y renderizado eficiente de fotografía Unsplash en HD.
- **Redes y API**: Consumo de la API REST de Open-Meteo (`https://api.open-meteo.com/v1/forecast`) mediante `HttpURLConnection` y `org.json` ejecutados en hilos secundarios desacoplados con `ExecutorService`.
- **Notificaciones del Sistema**: Implementación de `BroadcastReceiver` (`NotificationReceiver`) y `NotificationManager` para notificaciones PUSH fuera de la aplicación.

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
