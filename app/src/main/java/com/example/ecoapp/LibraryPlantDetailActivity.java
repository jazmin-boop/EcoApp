package com.example.ecoapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoapp.db.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LibraryPlantDetailActivity extends AppCompatActivity {

    private TextView tvName, tvSpecies, tvDesc;
    private TextView tvTabBasic, tvTabCare, tvTabPests, tvTabOther;
    private ImageView ivBack;
    private Button btnAddToGarden;

    private DBHelper dbHelper;
    private String nombre, especie, descripcion, origen, riego;
    private int imageResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_plant_detail);

        dbHelper = new DBHelper(this);

        nombre = getIntent().getStringExtra("nombre");
        especie = getIntent().getStringExtra("especie");
        descripcion = getIntent().getStringExtra("descripcion");
        origen = getIntent().getStringExtra("origen");
        riego = getIntent().getStringExtra("riego");
        imageResId = getIntent().getIntExtra("imagen", R.drawable.helecho);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        tvName = findViewById(R.id.tvLibDetailName);
        tvSpecies = findViewById(R.id.tvLibDetailSpecies);
        tvDesc = findViewById(R.id.tvLibDetailDesc);
        ImageView ivImage = findViewById(R.id.ivLibDetailImage);
        ImageView ivHeartLike = findViewById(R.id.ivHeartLike);
        ivBack = findViewById(R.id.ivBackLibDetail);
        btnAddToGarden = findViewById(R.id.btnAddPlantToGarden);

        // Lógica de Favoritos persistente y Notificación
        android.content.SharedPreferences prefs = getSharedPreferences("MisFavoritos", MODE_PRIVATE);
        boolean isLiked = prefs.getBoolean(nombre, false);
        
        if (isLiked) {
            ivHeartLike.setImageResource(R.drawable.ic_heart_filled);
        } else {
            ivHeartLike.setImageResource(R.drawable.ic_heart_border);
        }

        ivHeartLike.setOnClickListener(v -> {
            boolean currentState = prefs.getBoolean(nombre, false);
            if (!currentState) {
                prefs.edit().putBoolean(nombre, true).apply();
                ivHeartLike.setImageResource(R.drawable.ic_heart_filled);
                Toast.makeText(this, "¡Me encanta! ❤️ Guardado en tus favoritas", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putBoolean(nombre, false).apply();
                ivHeartLike.setImageResource(R.drawable.ic_heart_border);
                Toast.makeText(this, "💔 Planta favorita eliminada", Toast.LENGTH_SHORT).show();
                
                // Disparar Notificación de "Recordatorio próximo a vencer" en el celular
                android.content.Intent intent = new android.content.Intent(this, NotificationReceiver.class);
                intent.putExtra("planta", nombre);
                intent.putExtra("tipo", "recordatorio_proximo");
                sendBroadcast(intent);
            }
        });

        tvTabBasic = findViewById(R.id.tvTabBasic);
        tvTabCare = findViewById(R.id.tvTabCare);
        tvTabPests = findViewById(R.id.tvTabPests);
        tvTabOther = findViewById(R.id.tvTabOther);

        tvName.setText(nombre);
        tvSpecies.setText(especie);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                    .load(imageUrl)
                    .placeholder(imageResId)
                    .error(imageResId)
                    .centerCrop()
                    .into(ivImage);
        } else {
            ivImage.setImageResource(imageResId);
        }

        // Pestaña por defecto: Información básica
        mostrarInfoBasica();

        // Configurar clics de las pestañas
        tvTabBasic.setOnClickListener(v -> {
            setActiveTab(tvTabBasic);
            mostrarInfoBasica();
        });
        tvTabCare.setOnClickListener(v -> {
            setActiveTab(tvTabCare);
            mostrarCuidados();
        });
        tvTabPests.setOnClickListener(v -> {
            setActiveTab(tvTabPests);
            mostrarProblemasPlagas();
        });
        tvTabOther.setOnClickListener(v -> {
            setActiveTab(tvTabOther);
            mostrarOtros();
        });

        ivBack.setOnClickListener(v -> finish());

        btnAddToGarden.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_NOMBRE, nombre);
            values.put(DBHelper.COLUMN_ESPECIE, especie);
            String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            values.put(DBHelper.COLUMN_FECHA_SIEMBRA, fechaHoy);
            values.put(DBHelper.COLUMN_FRECUENCIA_RIEGO, 7);
            values.put(DBHelper.COLUMN_UBICACION, "Interior");
            values.put(DBHelper.COLUMN_NOTAS, descripcion);

            long newRowId = db.insert(DBHelper.TABLE_PLANTAS, null, values);
            db.close();

            if (newRowId != -1) {
                Toast.makeText(this, "¡" + nombre + " añadida a tu huerto con éxito! 🌱", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error al añadir la planta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActiveTab(TextView activeTab) {
        tvTabBasic.setTextColor(Color.parseColor("#888888"));
        tvTabBasic.setTypeface(null, Typeface.NORMAL);
        tvTabCare.setTextColor(Color.parseColor("#888888"));
        tvTabCare.setTypeface(null, Typeface.NORMAL);
        tvTabPests.setTextColor(Color.parseColor("#888888"));
        tvTabPests.setTypeface(null, Typeface.NORMAL);
        tvTabOther.setTextColor(Color.parseColor("#888888"));
        tvTabOther.setTypeface(null, Typeface.NORMAL);

        activeTab.setTextColor(Color.parseColor("#2E7D32"));
        activeTab.setTypeface(null, Typeface.BOLD);
    }

    private void mostrarInfoBasica() {
        String html = "<b>Descripción general:</b><br>" + descripcion + "<br><br>" +
                "<b>🌍 Origen natural:</b><br>" + origen + "<br><br>" +
                "<b>🌿 Familia botánica:</b><br>Araceae / Especie ornamental de interior.<br><br>" +
                "<b>✨ Característica principal:</b><br>Destaca por su follaje llamativo y su gran adaptabilidad a entornos domésticos en Latinoamérica.";
        tvDesc.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }

    private void mostrarCuidados() {
        String html = "<b>💧 Frecuencia de riego:</b><br>" + riego + "<br><br>" +
                "<b>☀️ Iluminación ideal:</b><br>Prefiere luz indirecta brillante. Evita el sol directo prolongado para no quemar sus hojas.<br><br>" +
                "<b>🌡️ Temperatura:</b><br>Ideal entre 18°C y 28°C. Proteger de corrientes de aire frío.<br><br>" +
                "<b>🌱 Sustrato recomendado:</b><br>Tierra rica en materia orgánica con excelente drenaje para evitar encharcamientos.";
        tvDesc.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }

    private void mostrarProblemasPlagas() {
        String html = "<b>🐛 Plagas comunes:</b><br>Cochinilla algodonosa y ácaros (araña roja) en ambientes muy secos. Limpiar las hojas regularmente con un paño húmedo.<br><br>" +
                "<b>⚠️ Exceso de riego:</b><br>El encharcamiento provoca pudrición de raíces (hojas amarillas o marrones en las puntas).<br><br>" +
                "<b>💡 Consejo experto:</b><br>Deja secar los primeros 3 cm de tierra antes de volver a regar.";
        tvDesc.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }

    private void mostrarOtros() {
        String html = "<b>🐾 Toxicidad y mascotas:</b><br>Contiene cristales de oxalato de calcio. Ligeramente tóxica si es ingerida por mascotas o niños.<br><br>" +
                "<b>✂️ Poda y mantenimiento:</b><br>Retira hojas secas o marchitadas desde la base para estimular nuevo crecimiento.<br><br>" +
                "<b>🌿 Propagación:</b><br>Mediante esquejes de tallo con nudos aéreos en agua o sustrato húmedo.";
        tvDesc.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }
}
