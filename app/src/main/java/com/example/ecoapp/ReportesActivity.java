package com.example.ecoapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.HistorialAdapter;
import com.example.ecoapp.adapter.ReporteAdapter;
import com.example.ecoapp.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportesActivity extends AppCompatActivity {

    private RecyclerView rvTopRegadas, rvHistorialRiegos;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        dbHelper = new DBHelper(this);

        rvTopRegadas = findViewById(R.id.rvTopRegadas);
        rvTopRegadas.setLayoutManager(new LinearLayoutManager(this));

        rvHistorialRiegos = findViewById(R.id.rvHistorialRiegos);
        rvHistorialRiegos.setLayoutManager(new LinearLayoutManager(this));

        ImageView ivBack = findViewById(R.id.ivBackReports);
        ivBack.setOnClickListener(v -> finish());

        cargarReportePlantasMasRegadas();
        cargarReporteHistorialGeneral();
    }

    private void cargarReportePlantasMasRegadas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT p." + DBHelper.COLUMN_NOMBRE + ", p." + DBHelper.COLUMN_ESPECIE + ", p." + DBHelper.COLUMN_RACHA + " " +
                "FROM " + DBHelper.TABLE_PLANTAS + " p " +
                "ORDER BY p." + DBHelper.COLUMN_RACHA + " DESC " +
                "LIMIT 10";

        Cursor cursor = db.rawQuery(sql, null);
        List<ReporteAdapter.TopPlanta> listaTop = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String especie = cursor.getString(1);
                int racha = cursor.getInt(2);
                listaTop.add(new ReporteAdapter.TopPlanta(nombre, especie, racha));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Si hay menos de 5, rellenar con datos de prueba ricos (mínimo 6 para probar)
        if (listaTop.size() < 5) {
            listaTop.clear();
            listaTop.add(new ReporteAdapter.TopPlanta("Monstera de la Sala", "Monstera deliciosa", 12));
            listaTop.add(new ReporteAdapter.TopPlanta("Sansevieria", "Sansevieria trifasciata", 9));
            listaTop.add(new ReporteAdapter.TopPlanta("Orquídea Mariposa", "Phalaenopsis orchid", 7));
            listaTop.add(new ReporteAdapter.TopPlanta("Potus Dorado", "Epipremnum aureum", 6));
            listaTop.add(new ReporteAdapter.TopPlanta("Lirio de la Paz", "Spathiphyllum wallisii", 5));
            listaTop.add(new ReporteAdapter.TopPlanta("Ficus Variegata", "Ficus elastica", 4));
        }

        ReporteAdapter adapter = new ReporteAdapter(this, listaTop);
        rvTopRegadas.setAdapter(adapter);
    }

    private void cargarReporteHistorialGeneral() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT p." + DBHelper.COLUMN_NOMBRE + ", r." + DBHelper.COLUMN_FECHA_HORA + ", r." + DBHelper.COLUMN_HUMEDAD_TIERRA + ", r." + DBHelper.COLUMN_CLIMA_DIA + ", r." + DBHelper.COLUMN_PODADA + " " +
                "FROM " + DBHelper.TABLE_PLANTAS + " p " +
                "INNER JOIN " + DBHelper.TABLE_BITACORA + " r ON p." + DBHelper.COLUMN_ID_PLANTA + " = r." + DBHelper.COLUMN_PLANTA_ID + " " +
                "ORDER BY r." + DBHelper.COLUMN_ID_RIEGO + " DESC " +
                "LIMIT 10";

        Cursor cursor = db.rawQuery(sql, null);
        List<HistorialAdapter.HistorialRiego> listaHistorial = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String fecha = cursor.getString(1);
                String humedad = cursor.getString(2);
                String clima = cursor.getString(3);
                String podada = cursor.getString(4);
                listaHistorial.add(new HistorialAdapter.HistorialRiego(nombre, fecha, humedad, clima, podada));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Si está vacía, rellenar con datos de prueba
        if (listaHistorial.isEmpty()) {
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Monstera de la Sala", "15/09/2026 14:30", "Húmeda", "Soleado 25°C", "Podada ✅"));
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Sansevieria", "14/09/2026 10:15", "Seca", "Nublado 22°C", "Sin poda"));
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Orquídea Mariposa", "13/09/2026 18:45", "Húmeda", "Lluvia 20°C", "Sin poda"));
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Potus Dorado", "12/09/2026 09:00", "Seca", "Soleado 24°C", "Podada ✅"));
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Lirio de la Paz", "11/09/2026 16:20", "Húmeda", "Parcialmente Nublado 21°C", "Sin poda"));
            listaHistorial.add(new HistorialAdapter.HistorialRiego("Ficus Variegata", "10/09/2026 11:10", "Seca", "Soleado 26°C", "Podada ✅"));
        }

        HistorialAdapter adapter = new HistorialAdapter(this, listaHistorial);
        rvHistorialRiegos.setAdapter(adapter);
    }
}
