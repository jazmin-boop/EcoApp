package com.example.ecoapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.LibraryPlantAdapter;
import com.example.ecoapp.db.DBHelper;
import com.example.ecoapp.model.LibraryPlant;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlantaFormActivity extends AppCompatActivity {

    private EditText etNombre, etEspecie, etFechaSiembra, etFrecuencia, etNotas;
    private RadioButton rbInterior;
    private Button btnChooseLib, btnSave;
    private ImageView ivBack;

    private DBHelper dbHelper;
    private int plantaIdEditar = -1; // -1 significa que es nueva planta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planta_form);

        dbHelper = new DBHelper(this);

        TextView tvFormTitle = findViewById(R.id.tvFormTitle);
        etNombre = findViewById(R.id.etNombre);
        etEspecie = findViewById(R.id.etEspecie);
        etFechaSiembra = findViewById(R.id.etFechaSiembra);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etNotas = findViewById(R.id.etNotas);
        rbInterior = findViewById(R.id.rbInterior);
        RadioButton rbExterior = findViewById(R.id.rbExterior);
        btnChooseLib = findViewById(R.id.btnChooseFromLibrary);
        btnSave = findViewById(R.id.btnSavePlant);
        ivBack = findViewById(R.id.ivBackForm);

        // Verificar si venimos a "Editar" pasándole un planta_id
        if (getIntent().hasExtra("planta_id")) {
            plantaIdEditar = getIntent().getIntExtra("planta_id", -1);
            if (plantaIdEditar != -1) {
                tvFormTitle.setText("Editar Planta");
                btnSave.setText("Actualizar Planta");
                cargarDatosPlanta(plantaIdEditar, rbExterior);
            }
        } else {
            // Autocompletar fecha de siembra con la fecha de hoy si es nueva planta
            String hoyStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            etFechaSiembra.setText(hoyStr);
        }

        ivBack.setOnClickListener(v -> finish());

        // Modal desplegable para seleccionar de la librería de plantas
        btnChooseLib.setOnClickListener(v -> mostrarModalLibreria());

        // Guardar o Actualizar planta en la base de datos
        btnSave.setOnClickListener(v -> guardarPlanta());
    }

    private void cargarDatosPlanta(int id, RadioButton rbExterior) {
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.query(DBHelper.TABLE_PLANTAS, null, DBHelper.COLUMN_ID_PLANTA + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE)));
            etEspecie.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ESPECIE)));
            etFechaSiembra.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA_SIEMBRA)));
            etFrecuencia.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FRECUENCIA_RIEGO))));
            
            String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_UBICACION));
            if ("Exterior".equals(ubicacion)) {
                rbExterior.setChecked(true);
            } else {
                rbInterior.setChecked(true);
            }
            etNotas.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOTAS)));
            cursor.close();
        }
    }

    private void mostrarModalLibreria() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_library_plant, null);
        bottomSheetDialog.setContentView(dialogView);

        RecyclerView rvDialog = dialogView.findViewById(R.id.rvDialogLibrary);
        rvDialog.setLayoutManager(new LinearLayoutManager(this));

        List<LibraryPlant> lista = new ArrayList<>();
        lista.add(new LibraryPlant("Monstera", "Monstera deliciosa", "Tropicales", "Hojas grandes decorativas para interior", "México", "Regar cada 7-10 días", "Fácil", "Luz difusa", "Sombra", R.drawable.helecho, ""));
        lista.add(new LibraryPlant("Lirio de la Paz", "Spathiphyllum wallisii", "Tropicales", "Elegantes flores blancas purificadoras de aire", "América del Sur", "Regar cada 5-7 días", "Media", "Luz difusa", "Sombra", R.drawable.suculenta, ""));
        lista.add(new LibraryPlant("Sansevieria", "Sansevieria trifasciata", "Suculentas", "Lengua de suegra muy resistente", "África", "Regar cada 15-20 días", "Fácil", "Luz difusa", "Sol directo", R.drawable.helecho, ""));
        lista.add(new LibraryPlant("Ficus Variegata", "Ficus elastica", "Tropicales", "Hojas carnosas bicolor para decoración", "Asia", "Regar cada 10 días", "Fácil", "Luz difusa", "Sombra", R.drawable.suculenta, ""));
        lista.add(new LibraryPlant("Orquídea Mariposa", "Phalaenopsis orchid", "Orquídeas", "Flores sofisticadas duraderas para centro de mesa", "Sudeste Asiático", "Regar por inmersión cada 8-12 días", "Fácil", "Luz difusa", "", R.drawable.helecho, ""));
        lista.add(new LibraryPlant("Potus Dorado", "Epipremnum aureum", "Trepadoras", "Planta colgante muy resistente con matices dorados", "Oceanía", "Regar cada 7 días", "Fácil", "Luz difusa", "Sombra", R.drawable.suculenta, ""));

        // Adaptador con callback para autocompletar el formulario al seleccionar
        LibraryPlantAdapter adapter = new LibraryPlantAdapter(this, lista) {
            @Override
            public void onBindViewHolder(@androidx.annotation.NonNull LibraryPlantAdapter.LibViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                LibraryPlant plant = lista.get(position);
                holder.itemView.setOnClickListener(v -> {
                    etNombre.setText(plant.getNombre());
                    etEspecie.setText(plant.getEspecie());
                    etFrecuencia.setText("7");
                    etNotas.setText(plant.getDescripcion());
                    bottomSheetDialog.dismiss();
                    Toast.makeText(PlantaFormActivity.this, "🌱 Datos de " + plant.getNombre() + " cargados al formulario", Toast.LENGTH_SHORT).show();
                });
            }
        };

        rvDialog.setAdapter(adapter);
        bottomSheetDialog.show();
    }

    private void guardarPlanta() {
        String nombre = etNombre.getText().toString().trim();
        String especie = etEspecie.getText().toString().trim();
        String fecha = etFechaSiembra.getText().toString().trim();
        String frecuenciaStr = etFrecuencia.getText().toString().trim();
        String notas = etNotas.getText().toString().trim();

        if (nombre.isEmpty() || especie.isEmpty() || fecha.isEmpty() || frecuenciaStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        int frecuencia = Integer.parseInt(frecuenciaStr);
        String ubicacion = rbInterior.isChecked() ? "Interior" : "Exterior";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NOMBRE, nombre);
        values.put(DBHelper.COLUMN_ESPECIE, especie);
        values.put(DBHelper.COLUMN_FECHA_SIEMBRA, fecha);
        values.put(DBHelper.COLUMN_FRECUENCIA_RIEGO, frecuencia);
        values.put(DBHelper.COLUMN_UBICACION, ubicacion);
        values.put(DBHelper.COLUMN_NOTAS, notas);

        long id;
        if (plantaIdEditar == -1) {
            id = db.insert(DBHelper.TABLE_PLANTAS, null, values);
            if (id != -1) {
                Toast.makeText(this, "¡Planta registrada con éxito en tu huerto! 🌱", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar la planta", Toast.LENGTH_SHORT).show();
            }
        } else {
            id = db.update(DBHelper.TABLE_PLANTAS, values, DBHelper.COLUMN_ID_PLANTA + "=?", new String[]{String.valueOf(plantaIdEditar)});
            if (id > 0) {
                Toast.makeText(this, "¡Planta actualizada con éxito! ✏️", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la planta", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }
}
