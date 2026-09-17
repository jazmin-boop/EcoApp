package com.example.ecoapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.TareaCalendarioAdapter;
import com.example.ecoapp.db.DBHelper;
import com.example.ecoapp.model.Planta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarioActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvFechaSeleccionada, tvEmpty;
    private RecyclerView rvTareas;
    private ImageView ivBack;

    private DBHelper dbHelper;
    private TareaCalendarioAdapter adapter;
    private List<Planta> listaTareas;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        dbHelper = new DBHelper(this);

        ivBack = findViewById(R.id.ivBackCalendario);
        ivBack.setOnClickListener(v -> finish());

        calendarView = findViewById(R.id.calendarView);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        tvEmpty = findViewById(R.id.tvEmptyCalendario);

        rvTareas = findViewById(R.id.rvTareasCalendario);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        String hoyStr = sdf.format(new Date());
        tvFechaSeleccionada.setText("Tareas para hoy (" + hoyStr + ")");
        cargarTareasParaFecha(hoyStr);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            String fechaSelec = sdf.format(cal.getTime());
            tvFechaSeleccionada.setText("Tareas para el " + fechaSelec);
            cargarTareasParaFecha(fechaSelec);
        });
    }

    private void cargarTareasParaFecha(String fechaObjetivo) {
        listaTareas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PLANTAS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID_PLANTA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE));
                String especie = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ESPECIE));
                String fechaSiembra = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA_SIEMBRA));
                int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FRECUENCIA_RIEGO));

                try {
                    Date dateSiembra = sdf.parse(fechaSiembra);
                    Date dateObjetivo = sdf.parse(fechaObjetivo);

                    if (dateSiembra != null && dateObjetivo != null) {
                        long diffMillies = dateObjetivo.getTime() - dateSiembra.getTime();
                        long diffDias = diffMillies / (1000 * 60 * 60 * 24);

                        // Si la diferencia de días es múltiplo exacto de la frecuencia, toca regar ese día
                        if (diffDias >= 0 && frecuencia > 0 && diffDias % frecuencia == 0) {
                            listaTareas.add(new Planta(id, nombre, especie, fechaSiembra, frecuencia, "Interior", "Regar"));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        if (listaTareas.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvTareas.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvTareas.setVisibility(View.VISIBLE);
            adapter = new TareaCalendarioAdapter(this, listaTareas);
            rvTareas.setAdapter(adapter);
        }
    }
}
