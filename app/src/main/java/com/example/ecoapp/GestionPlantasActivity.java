package com.example.ecoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.PlantaAdapter;
import com.example.ecoapp.db.DBHelper;
import com.example.ecoapp.model.Planta;

import java.util.ArrayList;
import java.util.List;

public class GestionPlantasActivity extends AppCompatActivity {

    private RecyclerView rvGestion;
    private TextView tvTotal;
    private EditText etSearch;
    private Button btnNuevaPlanta;
    private ImageView ivBack;

    private DBHelper dbHelper;
    private PlantaAdapter adapter;
    private List<Planta> listaPlantas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_plantas);

        dbHelper = new DBHelper(this);

        ivBack = findViewById(R.id.ivBackGestion);
        ivBack.setOnClickListener(v -> finish());

        tvTotal = findViewById(R.id.tvTotalPlantasGestion);
        etSearch = findViewById(R.id.etSearchGestion);
        btnNuevaPlanta = findViewById(R.id.btnNuevaPlantaGestion);

        rvGestion = findViewById(R.id.rvGestionPlantas);
        rvGestion.setLayoutManager(new LinearLayoutManager(this));

        btnNuevaPlanta.setOnClickListener(v -> {
            Intent intent = new Intent(GestionPlantasActivity.this, PlantaFormActivity.class);
            startActivity(intent);
        });

        // Filtrado en tiempo real
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPlantasDesdeBD();
    }

    private void cargarPlantasDesdeBD() {
        listaPlantas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PLANTAS, null, null, null, null, null, DBHelper.COLUMN_ID_PLANTA + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID_PLANTA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE));
                String especie = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ESPECIE));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA_SIEMBRA));
                int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FRECUENCIA_RIEGO));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_UBICACION));
                String notas = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOTAS));

                listaPlantas.add(new Planta(id, nombre, especie, fecha, frecuencia, ubicacion, notas));
            } while (cursor.moveToNext());
            cursor.close();
        }

        tvTotal.setText(listaPlantas.size() + " Totales");

        adapter = new PlantaAdapter(this, listaPlantas, new PlantaAdapter.OnPlantaListener() {
            @Override
            public void onPlantaClick(Planta planta) {
                Intent intent = new Intent(GestionPlantasActivity.this, PlantaDetalleActivity.class);
                intent.putExtra("planta_id", planta.getIdPlanta());
                startActivity(intent);
            }

            @Override
            public void onEditarClick(Planta planta) {
                Intent intent = new Intent(GestionPlantasActivity.this, PlantaFormActivity.class);
                intent.putExtra("planta_id", planta.getIdPlanta());
                startActivity(intent);
            }

            @Override
            public void onEliminarClick(Planta planta) {
                mostrarDialogoEliminar(planta);
            }
        });

        rvGestion.setAdapter(adapter);
    }

    private void mostrarDialogoEliminar(Planta planta) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(view);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvMessage = view.findViewById(R.id.tvDeleteMessage);
        tvMessage.setText("¿Estás seguro de que deseas eliminar permanentemente a " + planta.getNombre() + "?");

        view.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());
        
        view.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> {
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
            dbWrite.delete(DBHelper.TABLE_PLANTAS, DBHelper.COLUMN_ID_PLANTA + " = ?", new String[]{String.valueOf(planta.getIdPlanta())});
            dbWrite.close();
            Toast.makeText(GestionPlantasActivity.this, "🌱 Planta " + planta.getNombre() + " eliminada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            cargarPlantasDesdeBD();
        });

        dialog.show();
    }

    private void filtrar(String text) {
        List<Planta> filtradas = new ArrayList<>();
        for (Planta p : listaPlantas) {
            if (p.getNombre().toLowerCase().contains(text.toLowerCase()) || p.getEspecie().toLowerCase().contains(text.toLowerCase())) {
                filtradas.add(p);
            }
        }
        if (adapter != null) {
            adapter.actualizarLista(filtradas);
        }
    }
}
