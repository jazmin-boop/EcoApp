package com.example.ecoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ecoapp.model.BitacoraRiego;
import com.example.ecoapp.model.Planta;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecohuerta.db";
    private static final int DATABASE_VERSION = 4; // Versión 4 con Arquitectura Normalizada Completa

    // 1. Tabla Catalogo Especies
    public static final String TABLE_CATALOGO = "catalogo_especies";
    public static final String COLUMN_ID_ESPECIE = "id_especie";
    public static final String COLUMN_NOMBRE_COMUN = "nombre_comun";
    public static final String COLUMN_NOMBRE_CIENTIFICO = "nombre_cientifico";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_NIVEL_CUIDADO = "nivel_cuidado";
    public static final String COLUMN_TIPO_LUZ = "tipo_luz";
    public static final String COLUMN_ES_TOXICA = "es_toxica";
    public static final String COLUMN_FRECUENCIA_SUGERIDA = "frecuencia_riego_dias";
    public static final String COLUMN_IMAGEN_URL = "imagen_url";
    public static final String COLUMN_DESCRIPCION = "descripcion";

    // 2. Tabla Plantas (Huerto del Usuario)
    public static final String TABLE_PLANTAS = "plantas";
    public static final String COLUMN_ID_PLANTA = "id_planta";
    public static final String COLUMN_ESPECIE_ID = "especie_id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_ESPECIE = "especie";
    public static final String COLUMN_FECHA_SIEMBRA = "fecha_siembra";
    public static final String COLUMN_FRECUENCIA_RIEGO = "frecuencia_riego_dias";
    public static final String COLUMN_UBICACION = "ubicacion";
    public static final String COLUMN_NOTAS = "notas";
    public static final String COLUMN_RACHA = "racha_dias";

    // 3. Tabla Tipos de Tarea
    public static final String TABLE_TIPOS_TAREA = "tipos_tarea";
    public static final String COLUMN_ID_TIPO_TAREA = "id_tipo_tarea";
    public static final String COLUMN_NOMBRE_TAREA = "nombre_tarea";
    public static final String COLUMN_ICONO = "icono";

    // 4. Tabla Tareas Programadas
    public static final String TABLE_TAREAS = "tareas_programadas";
    public static final String COLUMN_ID_TAREA = "id_tarea";
    public static final String COLUMN_TAREA_PLANTA_ID = "planta_id";
    public static final String COLUMN_TIPO_TAREA_ID = "tipo_tarea_id";
    public static final String COLUMN_FECHA_PROGRAMADA = "fecha_programada";
    public static final String COLUMN_ESTADO_TAREA = "estado";
    public static final String COLUMN_FECHA_COMPLETADA = "fecha_completada";

    // 5. Tabla Bitácora de Riego
    public static final String TABLE_BITACORA = "bitacora_riego";
    public static final String COLUMN_ID_RIEGO = "id_riego";
    public static final String COLUMN_PLANTA_ID = "planta_id";
    public static final String COLUMN_FECHA_HORA = "fecha_hora";
    public static final String COLUMN_TEMPERATURA = "temperatura_momento";
    public static final String COLUMN_LLOvio = "llovio";
    public static final String COLUMN_HUMEDAD_TIERRA = "humedad_tierra";
    public static final String COLUMN_CLIMA_DIA = "clima_dia";
    public static final String COLUMN_PODADA = "podada";

    // 6. Tabla Favoritos
    public static final String TABLE_FAVORITOS = "favoritos";
    public static final String COLUMN_ID_FAVORITO = "id_favorito";
    public static final String COLUMN_FAV_ESPECIE_ID = "especie_id";
    public static final String COLUMN_FECHA_AGREGADO = "fecha_agregado";

    // 7. Tabla Cache Clima
    public static final String TABLE_CACHE_CLIMA = "cache_clima";
    public static final String COLUMN_ID_CACHE = "id_cache";
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LONGITUD = "longitud";
    public static final String COLUMN_CACHE_TEMP = "temperatura";
    public static final String COLUMN_PROB_LLUVIA = "probabilidad_lluvia";
    public static final String COLUMN_FECHA_REGISTRO = "fecha_registro";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Catalogo Especies
        db.execSQL("CREATE TABLE " + TABLE_CATALOGO + " (" +
                COLUMN_ID_ESPECIE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE_COMUN + " TEXT NOT NULL, " +
                COLUMN_NOMBRE_CIENTIFICO + " TEXT NOT NULL, " +
                COLUMN_CATEGORIA + " TEXT NOT NULL, " +
                COLUMN_NIVEL_CUIDADO + " TEXT, " +
                COLUMN_TIPO_LUZ + " TEXT, " +
                COLUMN_ES_TOXICA + " INTEGER DEFAULT 0, " +
                COLUMN_FRECUENCIA_SUGERIDA + " INTEGER, " +
                COLUMN_IMAGEN_URL + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT)");

        // 2. Plantas
        db.execSQL("CREATE TABLE " + TABLE_PLANTAS + " (" +
                COLUMN_ID_PLANTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ESPECIE_ID + " INTEGER, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_ESPECIE + " TEXT NOT NULL, " +
                COLUMN_FECHA_SIEMBRA + " TEXT NOT NULL, " +
                COLUMN_FRECUENCIA_RIEGO + " INTEGER NOT NULL, " +
                COLUMN_UBICACION + " TEXT NOT NULL, " +
                COLUMN_NOTAS + " TEXT, " +
                COLUMN_RACHA + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COLUMN_ESPECIE_ID + ") REFERENCES " + TABLE_CATALOGO + "(" + COLUMN_ID_ESPECIE + "))");

        // 3. Tipos Tarea
        db.execSQL("CREATE TABLE " + TABLE_TIPOS_TAREA + " (" +
                COLUMN_ID_TIPO_TAREA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE_TAREA + " TEXT NOT NULL, " +
                COLUMN_ICONO + " TEXT)");

        // 4. Tareas Programadas
        db.execSQL("CREATE TABLE " + TABLE_TAREAS + " (" +
                COLUMN_ID_TAREA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TAREA_PLANTA_ID + " INTEGER NOT NULL, " +
                COLUMN_TIPO_TAREA_ID + " INTEGER NOT NULL, " +
                COLUMN_FECHA_PROGRAMADA + " TEXT NOT NULL, " +
                COLUMN_ESTADO_TAREA + " TEXT NOT NULL, " +
                COLUMN_FECHA_COMPLETADA + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TAREA_PLANTA_ID + ") REFERENCES " + TABLE_PLANTAS + "(" + COLUMN_ID_PLANTA + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_TIPO_TAREA_ID + ") REFERENCES " + TABLE_TIPOS_TAREA + "(" + COLUMN_ID_TIPO_TAREA + "))");

        // 5. Bitácora Riego
        db.execSQL("CREATE TABLE " + TABLE_BITACORA + " (" +
                COLUMN_ID_RIEGO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLANTA_ID + " INTEGER NOT NULL, " +
                COLUMN_FECHA_HORA + " TEXT NOT NULL, " +
                COLUMN_TEMPERATURA + " REAL, " +
                COLUMN_LLOvio + " INTEGER, " +
                COLUMN_HUMEDAD_TIERRA + " TEXT, " +
                COLUMN_CLIMA_DIA + " TEXT, " +
                COLUMN_PODADA + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PLANTA_ID + ") REFERENCES " + TABLE_PLANTAS + "(" + COLUMN_ID_PLANTA + ") ON DELETE CASCADE)");

        // 6. Favoritos
        db.execSQL("CREATE TABLE " + TABLE_FAVORITOS + " (" +
                COLUMN_ID_FAVORITO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FAV_ESPECIE_ID + " INTEGER NOT NULL, " +
                COLUMN_FECHA_AGREGADO + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_FAV_ESPECIE_ID + ") REFERENCES " + TABLE_CATALOGO + "(" + COLUMN_ID_ESPECIE + "))");

        // 7. Cache Clima
        db.execSQL("CREATE TABLE " + TABLE_CACHE_CLIMA + " (" +
                COLUMN_ID_CACHE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUD + " REAL, " +
                COLUMN_LONGITUD + " REAL, " +
                COLUMN_CACHE_TEMP + " REAL, " +
                COLUMN_PROB_LLUVIA + " INTEGER, " +
                COLUMN_FECHA_REGISTRO + " TEXT)");

        // Poblado Inicial
        db.execSQL("INSERT INTO " + TABLE_CATALOGO + " (" + COLUMN_NOMBRE_COMUN + ", " + COLUMN_NOMBRE_CIENTIFICO + ", " + COLUMN_CATEGORIA + ", " + COLUMN_NIVEL_CUIDADO + ", " + COLUMN_TIPO_LUZ + ", " + COLUMN_ES_TOXICA + ", " + COLUMN_FRECUENCIA_SUGERIDA + ", " + COLUMN_IMAGEN_URL + ", " + COLUMN_DESCRIPCION + ") VALUES ('Monstera', 'Monstera deliciosa', 'Tropicales', 'Fácil', 'Luz difusa', 1, 7, 'https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=600&q=80', 'Hojas grandes decorativas para interior')");
        db.execSQL("INSERT INTO " + TABLE_CATALOGO + " (" + COLUMN_NOMBRE_COMUN + ", " + COLUMN_NOMBRE_CIENTIFICO + ", " + COLUMN_CATEGORIA + ", " + COLUMN_NIVEL_CUIDADO + ", " + COLUMN_TIPO_LUZ + ", " + COLUMN_ES_TOXICA + ", " + COLUMN_FRECUENCIA_SUGERIDA + ", " + COLUMN_IMAGEN_URL + ", " + COLUMN_DESCRIPCION + ") VALUES ('Lirio de la Paz', 'Spathiphyllum wallisii', 'Tropicales', 'Media', 'Luz difusa', 0, 5, 'https://images.unsplash.com/photo-1593482834024-f513f56d0d5d?auto=format&fit=crop&w=600&q=80', 'Purificadora de aire con flores blancas')");
        db.execSQL("INSERT INTO " + TABLE_CATALOGO + " (" + COLUMN_NOMBRE_COMUN + ", " + COLUMN_NOMBRE_CIENTIFICO + ", " + COLUMN_CATEGORIA + ", " + COLUMN_NIVEL_CUIDADO + ", " + COLUMN_TIPO_LUZ + ", " + COLUMN_ES_TOXICA + ", " + COLUMN_FRECUENCIA_SUGERIDA + ", " + COLUMN_IMAGEN_URL + ", " + COLUMN_DESCRIPCION + ") VALUES ('Sansevieria', 'Sansevieria trifasciata', 'Suculentas', 'Fácil', 'Sol directo', 0, 14, 'https://images.unsplash.com/photo-1509423350716-97f9360b4e09?auto=format&fit=crop&w=600&q=80', 'Lengua de suegra muy resistente')");

        // Plantas iniciales
        db.execSQL("INSERT INTO " + TABLE_PLANTAS + " (" + COLUMN_ESPECIE_ID + ", " + COLUMN_NOMBRE + ", " + COLUMN_ESPECIE + ", " + COLUMN_FECHA_SIEMBRA + ", " + COLUMN_FRECUENCIA_RIEGO + ", " + COLUMN_UBICACION + ", " + COLUMN_NOTAS + ", " + COLUMN_RACHA + ") VALUES (1, 'Monstera de la Sala', 'Monstera deliciosa', '01/06/2026', 7, 'Interior', 'Excelente salud', 12)");
        db.execSQL("INSERT INTO " + TABLE_PLANTAS + " (" + COLUMN_ESPECIE_ID + ", " + COLUMN_NOMBRE + ", " + COLUMN_ESPECIE + ", " + COLUMN_FECHA_SIEMBRA + ", " + COLUMN_FRECUENCIA_RIEGO + ", " + COLUMN_UBICACION + ", " + COLUMN_NOTAS + ", " + COLUMN_RACHA + ") VALUES (2, 'Lirio de la Paz', 'Spathiphyllum wallisii', '05/06/2026', 5, 'Interior', 'Muy bonita', 9)");
        db.execSQL("INSERT INTO " + TABLE_PLANTAS + " (" + COLUMN_ESPECIE_ID + ", " + COLUMN_NOMBRE + ", " + COLUMN_ESPECIE + ", " + COLUMN_FECHA_SIEMBRA + ", " + COLUMN_FRECUENCIA_RIEGO + ", " + COLUMN_UBICACION + ", " + COLUMN_NOTAS + ", " + COLUMN_RACHA + ") VALUES (3, 'Sansevieria', 'Sansevieria trifasciata', '10/06/2026', 14, 'Exterior', 'Poco riego', 5)");

        // Bitácora inicial
        db.execSQL("INSERT INTO " + TABLE_BITACORA + " (" + COLUMN_PLANTA_ID + ", " + COLUMN_FECHA_HORA + ", " + COLUMN_TEMPERATURA + ", " + COLUMN_LLOvio + ", " + COLUMN_HUMEDAD_TIERRA + ", " + COLUMN_CLIMA_DIA + ", " + COLUMN_PODADA + ") VALUES (1, '15/09/2026 14:30', 25.0, 0, 'Húmeda', 'Soleado 25°C', 'Podada ✅')");
        db.execSQL("INSERT INTO " + TABLE_BITACORA + " (" + COLUMN_PLANTA_ID + ", " + COLUMN_FECHA_HORA + ", " + COLUMN_TEMPERATURA + ", " + COLUMN_LLOvio + ", " + COLUMN_HUMEDAD_TIERRA + ", " + COLUMN_CLIMA_DIA + ", " + COLUMN_PODADA + ") VALUES (2, '14/09/2026 10:15', 22.5, 0, 'Seca', 'Nublado 22°C', 'Sin poda')");
        db.execSQL("INSERT INTO " + TABLE_BITACORA + " (" + COLUMN_PLANTA_ID + ", " + COLUMN_FECHA_HORA + ", " + COLUMN_TEMPERATURA + ", " + COLUMN_LLOvio + ", " + COLUMN_HUMEDAD_TIERRA + ", " + COLUMN_CLIMA_DIA + ", " + COLUMN_PODADA + ") VALUES (3, '13/09/2026 18:45', 27.2, 1, 'Húmeda', 'Lluvia 20°C', 'Sin poda')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE_CLIMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BITACORA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_TAREA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGO);
        onCreate(db);
    }

    // Métodos auxiliares para la App
    public List<Planta> obtenerTodasLasPlantas() {
        List<Planta> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANTAS, null, null, null, null, null, COLUMN_ID_PLANTA + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Planta planta = new Planta(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_PLANTA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESPECIE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_SIEMBRA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRECUENCIA_RIEGO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RACHA))
                );
                lista.add(planta);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return lista;
    }

    public Planta obtenerPlantaPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANTAS, null, COLUMN_ID_PLANTA + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Planta planta = new Planta(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_PLANTA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESPECIE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_SIEMBRA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRECUENCIA_RIEGO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTAS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RACHA))
            );
            cursor.close();
            return planta;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public void incrementarRacha(int plantaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PLANTAS + " SET " + COLUMN_RACHA + " = " + COLUMN_RACHA + " + 1 WHERE " + COLUMN_ID_PLANTA + " = " + plantaId);
    }

    public void insertarRiego(int plantaId, String fechaHora, double temperatura, int llovio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLANTA_ID, plantaId);
        values.put(COLUMN_FECHA_HORA, fechaHora);
        values.put(COLUMN_TEMPERATURA, temperatura);
        values.put(COLUMN_LLOvio, llovio);
        values.put(COLUMN_HUMEDAD_TIERRA, "Húmeda");
        values.put(COLUMN_CLIMA_DIA, "Soleado " + temperatura + "°C");
        values.put(COLUMN_PODADA, "Sin poda");
        db.insert(TABLE_BITACORA, null, values);
        db.close();
    }

    public List<BitacoraRiego> obtenerBitacoraPorPlanta(int plantaId) {
        List<BitacoraRiego> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BITACORA, null, COLUMN_PLANTA_ID + " = ?", new String[]{String.valueOf(plantaId)}, null, null, COLUMN_ID_RIEGO + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                BitacoraRiego riego = new BitacoraRiego(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_RIEGO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLANTA_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_HORA)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TEMPERATURA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LLOvio)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HUMEDAD_TIERRA))
                );
                lista.add(riego);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return lista;
    }

    public void eliminarPlanta(int plantaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BITACORA, COLUMN_PLANTA_ID + " = ?", new String[]{String.valueOf(plantaId)});
        db.delete(TABLE_TAREAS, COLUMN_TAREA_PLANTA_ID + " = ?", new String[]{String.valueOf(plantaId)});
        db.delete(TABLE_PLANTAS, COLUMN_ID_PLANTA + " = ?", new String[]{String.valueOf(plantaId)});
        db.close();
    }
}
