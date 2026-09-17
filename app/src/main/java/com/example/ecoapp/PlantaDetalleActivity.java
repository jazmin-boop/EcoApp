package com.example.ecoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.BitacoraAdapter;
import com.example.ecoapp.db.DBHelper;
import com.example.ecoapp.model.BitacoraRiego;
import com.example.ecoapp.model.Planta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlantaDetalleActivity extends AppCompatActivity {

    private TextView tvName, tvSpecies, tvLocation, tvFrequency, tvNotes;
    private ImageView ivBack, btnDelete, ivPlantImage;
    private Button btnRegarHoy;
    private RecyclerView rvBitacora;

    private DBHelper dbHelper;
    private int plantaId = -1;
    private Planta planta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planta_detalle);

        dbHelper = new DBHelper(this);

        // Obtener ID de la planta enviada por Intent
        plantaId = getIntent().getIntExtra("planta_id", -1);
        if (plantaId == -1) {
            Toast.makeText(this, "Error: Planta no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName = findViewById(R.id.tvDetailName);
        tvSpecies = findViewById(R.id.tvDetailSpecies);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvFrequency = findViewById(R.id.tvDetailFrequency);
        tvNotes = findViewById(R.id.tvDetailNotes);
        ivBack = findViewById(R.id.ivBack);
        btnDelete = findViewById(R.id.btnDeletePlant);
        ivPlantImage = findViewById(R.id.ivPlantDetailImage);
        btnRegarHoy = findViewById(R.id.btnRegarHoy);
        rvBitacora = findViewById(R.id.rvBitacora);
        rvBitacora.setLayoutManager(new LinearLayoutManager(this));

        cargarDatosPlanta();
        cargarBitacora();

        ivBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            dbHelper.eliminarPlanta(plantaId);
            Toast.makeText(this, "Planta eliminada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnRegarHoy.setOnClickListener(v -> {
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());
            // Temperatura simulada o de API (22.5°C por defecto, sin lluvia)
            dbHelper.insertarRiego(plantaId, fechaHora, 22.5, 0);
            Toast.makeText(this, "¡Riego registrado para hoy! 💧", Toast.LENGTH_SHORT).show();
            cargarBitacora();
        });
    }

    private void cargarDatosPlanta() {
        planta = dbHelper.obtenerPlantaPorId(plantaId);
        if (planta != null) {
            tvName.setText(planta.getNombre());
            tvSpecies.setText(planta.getEspecie());
            tvLocation.setText(planta.getUbicacion());
            tvFrequency.setText("Cada " + planta.getFrecuenciaRiegoDias() + " días");
            tvNotes.setText("Notas: " + (planta.getNotas() != null && !planta.getNotas().isEmpty() ? planta.getNotas() : "Sin notas adicionales."));
            
            // Asignar imagen según el nombre o ID
            if (planta.getNombre().toLowerCase().contains("helecho")) {
                ivPlantImage.setImageResource(R.drawable.helecho);
            } else {
                ivPlantImage.setImageResource(R.drawable.suculenta);
            }
        }
    }

    private void cargarBitacora() {
        List<BitacoraRiego> listaRiegos = dbHelper.obtenerBitacoraPorPlanta(plantaId);
        BitacoraAdapter adapter = new BitacoraAdapter(listaRiegos);
        rvBitacora.setAdapter(adapter);
    }
}
