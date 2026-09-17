package com.example.ecoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.ecoapp.adapter.TareaMainAdapter;
import com.example.ecoapp.db.DBHelper;
import com.example.ecoapp.model.Planta;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView tvMainTemp, tvMainRainProb, tvMainRegion;
    private View btnSelectRegion, btnMainFavorites;
    private Button btnGoToGarden;
    private TextView tvTotalTareasMain;
    private RecyclerView rvMainTareas;

    private DBHelper dbHelper;
    private List<Planta> listaTareas;
    private TareaMainAdapter tareasAdapter;

    // 4 Pestañas de navegación inferior (Tareas reemplazado por Reportes)
    private LinearLayout navHome, navPlantas, navCalendario, navReportes;
    private ImageView ivHome, ivPlantas, ivCalendario, ivReportes;
    private TextView tvHome, tvPlantas, tvCalendario, tvReportes;

    // Regiones latinoamericanas con sus coordenadas Lat/Lon
    private final String[][] REGIONES = {
            {"Lima, Perú", "-12.05", "-77.04"},
            {"Bogotá, Colombia", "4.71", "-74.07"},
            {"Ciudad de México", "19.43", "-99.13"},
            {"Buenos Aires, Argentina", "-34.60", "-58.38"},
            {"Santiago, Chile", "-33.45", "-70.66"}
    };

    private String currentLat = "-12.05";
    private String currentLon = "-77.04";

    // URLs de alta resolución en Unsplash para las plantas
    private final String URL_MONSTERA = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=600&q=80";
    private final String URL_SANSEVIERIA = "https://images.unsplash.com/photo-1509423350716-97f9360b4e09?auto=format&fit=crop&w=600&q=80";
    private final String URL_ORCHID = "https://images.unsplash.com/photo-1525310072745-f49212b5ac6d?auto=format&fit=crop&w=600&q=80";
    private final String URL_POTHOS = "https://images.unsplash.com/photo-1596547609652-9cf5d8d76921?auto=format&fit=crop&w=600&q=80";
    private final String URL_PEACE_LILY = "https://images.unsplash.com/photo-1593482834024-f513f56d0d5d?auto=format&fit=crop&w=600&q=80";
    private final String URL_FICUS = "https://images.unsplash.com/photo-1525498128493-380d1990a112?auto=format&fit=crop&w=600&q=80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar permisos de notificación para Android 13+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        tvMainTemp = findViewById(R.id.tvMainTemp);
        tvMainRainProb = findViewById(R.id.tvMainRainProb);
        tvMainRegion = findViewById(R.id.tvMainRegion);
        btnSelectRegion = findViewById(R.id.btnSelectRegion);
        btnMainFavorites = findViewById(R.id.btnMainFavorites);
        btnGoToGarden = findViewById(R.id.btnGoToGarden);

        // Inicializar tabs
        navHome = findViewById(R.id.navHome);
        navPlantas = findViewById(R.id.navPlantas);
        navCalendario = findViewById(R.id.navCalendario);
        navReportes = findViewById(R.id.navReportes);

        ivHome = findViewById(R.id.ivHome);
        ivPlantas = findViewById(R.id.ivPlantas);
        ivCalendario = findViewById(R.id.ivCalendario);
        ivReportes = findViewById(R.id.ivReportes);

        tvHome = findViewById(R.id.tvHome);
        tvPlantas = findViewById(R.id.tvPlantas);
        tvCalendario = findViewById(R.id.tvCalendario);
        tvReportes = findViewById(R.id.tvReportes);

        tvTotalTareasMain = findViewById(R.id.tvTotalTareasMain);
        rvMainTareas = findViewById(R.id.rvMainTareas);
        rvMainTareas.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        cargarTareasPendientesHoy();

        // Cargar imágenes por URL con Glide exactamente como en la Librería
        cargarImagenesPlantasURL();

        // Configurar clics de navegación interactivos
        navHome.setOnClickListener(v -> selectTab(navHome, ivHome, tvHome));
        navPlantas.setOnClickListener(v -> {
            selectTab(navPlantas, ivPlantas, tvPlantas);
            Intent intent = new Intent(MainActivity.this, PlantLibraryActivity.class);
            startActivity(intent);
        });
        navCalendario.setOnClickListener(v -> {
            selectTab(navCalendario, ivCalendario, tvCalendario);
            Intent intent = new Intent(MainActivity.this, CalendarioActivity.class);
            startActivity(intent);
        });
        navReportes.setOnClickListener(v -> {
            selectTab(navReportes, ivReportes, tvReportes);
            Intent intent = new Intent(MainActivity.this, ReportesActivity.class);
            startActivity(intent);
        });

        // Corazón de Favoritos abre Mis Plantas Favoritas
        btnMainFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritosActivity.class);
            startActivity(intent);
        });

        // Botón Ir a mi huerto abre la Gestión de Plantas
        btnGoToGarden.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestionPlantasActivity.class);
            startActivity(intent);
        });

        // Desplegable selector de región climática estilo celular
        btnSelectRegion.setOnClickListener(v -> {
            ContextThemeWrapper wrapper = new ContextThemeWrapper(MainActivity.this, R.style.Theme_EcoApp_LightPopup);
            PopupMenu popup = new PopupMenu(wrapper, btnSelectRegion);
            for (String[] region : REGIONES) {
                popup.getMenu().add(region[0]);
            }

            popup.setOnMenuItemClickListener(item -> {
                String regionNombre = item.getTitle() != null ? item.getTitle().toString() : REGIONES[0][0];
                tvMainRegion.setText(regionNombre);
                for (String[] r : REGIONES) {
                    if (r[0].equals(regionNombre)) {
                        currentLat = r[1];
                        currentLon = r[2];
                        break;
                    }
                }
                Toast.makeText(this, "Actualizando clima para " + regionNombre, Toast.LENGTH_SHORT).show();
                fetchOpenMeteoWeather();
                return true;
            });
            popup.show();
        });

        // Funcionalidad para toggle (Hoy / Próximas)
        View btnToggleHoy = findViewById(R.id.btnToggleHoy);
        View btnToggleProximas = findViewById(R.id.btnToggleProximas);
        
        btnToggleHoy.setOnClickListener(v -> {
            btnToggleHoy.setBackgroundResource(R.drawable.bg_filter_btn);
            ((TextView) btnToggleHoy).setTextColor(Color.WHITE);
            btnToggleProximas.setBackground(null);
            ((TextView) btnToggleProximas).setTextColor(Color.parseColor("#2E7D32"));
            cargarTareasPendientesHoy();
        });

        btnToggleProximas.setOnClickListener(v -> {
            btnToggleProximas.setBackgroundResource(R.drawable.bg_filter_btn);
            ((TextView) btnToggleProximas).setTextColor(Color.WHITE);
            btnToggleHoy.setBackground(null);
            ((TextView) btnToggleHoy).setTextColor(Color.parseColor("#2E7D32"));
            cargarTareasProximas();
        });

        // Cargar clima inicial con la API REST especificada
        fetchOpenMeteoWeather();
    }

    private void cargarTareasPendientesHoy() {
        listaTareas = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.query(DBHelper.TABLE_PLANTAS, null, null, null, null, null, null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String hoyStr = sdf.format(new Date());

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID_PLANTA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE));
                String especie = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ESPECIE));
                String fechaSiembra = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA_SIEMBRA));
                int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FRECUENCIA_RIEGO));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_UBICACION));

                try {
                    Date dateSiembra = sdf.parse(fechaSiembra);
                    Date dateObjetivo = sdf.parse(hoyStr);
                    if (dateSiembra != null && dateObjetivo != null) {
                        long diffMillies = dateObjetivo.getTime() - dateSiembra.getTime();
                        long diffDias = diffMillies / (1000 * 60 * 60 * 24);
                        if (diffDias >= 0 && frecuencia > 0 && diffDias % frecuencia == 0) {
                            listaTareas.add(new Planta(id, nombre, especie, fechaSiembra, frecuencia, ubicacion, ""));
                        }
                    }
                } catch (Exception e) {}
            } while (cursor.moveToNext());
            cursor.close();
        }

        tvTotalTareasMain.setText(" " + listaTareas.size() + " tareas ");
        configurarAdapterTareas();
    }

    private void cargarTareasProximas() {
        listaTareas = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.query(DBHelper.TABLE_PLANTAS, null, null, null, null, null, null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date hoy = new Date();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID_PLANTA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE));
                String especie = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ESPECIE));
                String fechaSiembra = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA_SIEMBRA));
                int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FRECUENCIA_RIEGO));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_UBICACION));

                try {
                    Date dateSiembra = sdf.parse(fechaSiembra);
                    if (dateSiembra != null) {
                        for (int i = 1; i <= 7; i++) { // Revisar próximos 7 días
                            Date future = new Date(hoy.getTime() + (i * 24 * 60 * 60 * 1000L));
                            long diffMillies = future.getTime() - dateSiembra.getTime();
                            long diffDias = diffMillies / (1000 * 60 * 60 * 24);
                            if (diffDias >= 0 && frecuencia > 0 && diffDias % frecuencia == 0) {
                                listaTareas.add(new Planta(id, nombre, especie, fechaSiembra, frecuencia, ubicacion, ""));
                                break; // Solo añadirla una vez si tiene riego en los próximos 7 días
                            }
                        }
                    }
                } catch (Exception e) {}
            } while (cursor.moveToNext());
            cursor.close();
        }

        tvTotalTareasMain.setText(" " + listaTareas.size() + " tareas ");
        configurarAdapterTareas();
    }

    private void configurarAdapterTareas() {
        tareasAdapter = new TareaMainAdapter(this, listaTareas, new TareaMainAdapter.OnTareaCompletadaListener() {
            @Override
            public void onTareaCompletada(Planta tarea, int position) {
                // Actualizar base de datos (Bitácora)
                android.database.sqlite.SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                android.content.ContentValues values = new android.content.ContentValues();
                values.put(DBHelper.COLUMN_PLANTA_ID, tarea.getIdPlanta());
                
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                values.put(DBHelper.COLUMN_FECHA_HORA, dateTimeFormat.format(new Date()));
                values.put(DBHelper.COLUMN_TEMPERATURA, 25.0); // Valor dummy
                
                dbWrite.insert(DBHelper.TABLE_BITACORA, null, values);
                dbWrite.close();

                // Eliminar de la lista visible
                if (position >= 0 && position < listaTareas.size()) {
                    listaTareas.remove(position);
                    tareasAdapter.notifyItemRemoved(position);
                    tvTotalTareasMain.setText(" " + listaTareas.size() + " tareas ");
                }
            }

            @Override
            public void onEstablecerAlarma(Planta tarea) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = calendar.get(java.util.Calendar.MINUTE);
                new android.app.TimePickerDialog(MainActivity.this, (view, hourOfDay, minute1) -> {
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int formattedHour = hourOfDay % 12;
                    if (formattedHour == 0) formattedHour = 12;
                    Toast.makeText(MainActivity.this, "✅ Recordatorio de riego establecido para las " + String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, minute1, amPm), Toast.LENGTH_SHORT).show();
                    
                    // Simular notificación en 3 segundos
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
                        intent.putExtra("planta", tarea.getNombre());
                        sendBroadcast(intent);
                    }, 3000);
                }, hour, minute, false).show();
            }
        });
        rvMainTareas.setAdapter(tareasAdapter);
    }

    private void cargarImagenesPlantasURL() {
        // Mi Huerto
        cargarImagenConGlide(findViewById(R.id.ivGardenMonstera), URL_MONSTERA, R.drawable.helecho);
        cargarImagenConGlide(findViewById(R.id.ivGardenSansevieria), URL_SANSEVIERIA, R.drawable.suculenta);
        cargarImagenConGlide(findViewById(R.id.ivGardenOrchid), URL_ORCHID, R.drawable.helecho);
        cargarImagenConGlide(findViewById(R.id.ivGardenPothos), URL_POTHOS, R.drawable.suculenta);
        cargarImagenConGlide(findViewById(R.id.ivGardenPeaceLily), URL_PEACE_LILY, R.drawable.suculenta);
        cargarImagenConGlide(findViewById(R.id.ivGardenFicus), URL_FICUS, R.drawable.helecho);
    }

    private void cargarImagenConGlide(ImageView iv, String url, int placeholderRes) {
        if (iv == null) return;
        Glide.with(this)
                .load(url)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .centerCrop()
                .into(iv);
    }

    private void selectTab(LinearLayout selectedTab, ImageView selectedIv, TextView selectedTv) {
        resetTab(navHome, ivHome, tvHome);
        resetTab(navPlantas, ivPlantas, tvPlantas);
        resetTab(navCalendario, ivCalendario, tvCalendario);
        resetTab(navReportes, ivReportes, tvReportes);

        selectedTab.setBackgroundResource(R.drawable.bg_nav_active);
        selectedIv.setColorFilter(Color.parseColor("#2E7D32"));
        selectedTv.setTextColor(Color.parseColor("#2E7D32"));
        selectedTv.setTypeface(null, Typeface.BOLD);
    }

    private void resetTab(LinearLayout tab, ImageView iv, TextView tv) {
        tab.setBackground(null);
        iv.setColorFilter(Color.parseColor("#222222")); // Todos son vectores ahora
        tv.setTextColor(Color.parseColor("#222222"));
        tv.setTypeface(null, Typeface.NORMAL);
    }

    // Consumo nativo de la API REST pública de Open-Meteo especificada en la indicación
    private void fetchOpenMeteoWeather() {
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + currentLat +
                "&longitude=" + currentLon +
                "&current=temperature_2m,precipitation&daily=precipitation_probability_max&timezone=auto";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(response.toString());
                    JSONObject current = json.getJSONObject("current");
                    double temp = current.getDouble("temperature_2m");

                    int probLluvia = 0;
                    if (json.has("daily")) {
                        JSONObject daily = json.getJSONObject("daily");
                        if (daily.has("precipitation_probability_max")) {
                            JSONArray probArray = daily.getJSONArray("precipitation_probability_max");
                            if (probArray.length() > 0) {
                                probLluvia = probArray.getInt(0);
                            }
                        }
                    }

                    final int finalProbLluvia = probLluvia;
                    runOnUiThread(() -> {
                        tvMainTemp.setText((int) Math.round(temp) + "°");
                        tvMainRainProb.setText("Lluvia: " + finalProbLluvia + "%");
                    });
                } else {
                    runOnUiThread(() -> tvMainRainProb.setText("Lluvia: --%"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> tvMainRainProb.setText("Lluvia: --%"));
            }
        });
        executor.shutdown();
    }
}
