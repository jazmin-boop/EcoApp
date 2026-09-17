package com.example.ecoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.LibraryPlantAdapter;
import com.example.ecoapp.model.LibraryPlant;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView rvFavoritos;
    private List<LibraryPlant> todasLasPlantas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        ImageView ivBack = findViewById(R.id.ivBackFavoritos);
        ivBack.setOnClickListener(v -> finish());

        rvFavoritos = findViewById(R.id.rvFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));

        // Poblado maestro igual a la Librería para filtrar de ahí
        todasLasPlantas = new ArrayList<>();
        todasLasPlantas.add(new LibraryPlant("Monstera", "Monstera deliciosa", "Tropicales", "Impresionante planta de grandes hojas nativa de los bosques tropicales de México.", "México y Centroamérica", "Regar cada 7-10 días", "Fácil", "Luz difusa", "Sombra", R.drawable.helecho, "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=600&q=80"));
        todasLasPlantas.add(new LibraryPlant("Lirio de la Paz", "Spathiphyllum wallisii", "Tropicales", "Conocida como Lirio de la Paz, destaca por sus elegantes flores blancas.", "América del Sur", "Regar cada 5-7 días", "Medio", "Luz difusa", "Sombra", R.drawable.suculenta, "https://images.unsplash.com/photo-1593482834024-f513f56d0d5d?auto=format&fit=crop&w=600&q=80"));
        todasLasPlantas.add(new LibraryPlant("Sansevieria", "Sansevieria trifasciata", "Suculentas", "Conocida también como Lengua de Suegra o Espada de San Jorge.", "África", "Regar cada 15-20 días", "Medio", "Luz difusa", "Sol directo", R.drawable.helecho, "https://images.unsplash.com/photo-1509423350716-97f9360b4e09?auto=format&fit=crop&w=600&q=80"));
        todasLasPlantas.add(new LibraryPlant("Ficus Variegata", "Ficus elastica variegata", "Tropicales", "Planta de interior decorativa con hojas carnosas de tono verde y bordes crema.", "Asia tropical", "Regar cada 10 días", "Fácil", "Luz difusa", "Sombra", R.drawable.suculenta, "https://images.unsplash.com/photo-1525498128493-380d1990a112?auto=format&fit=crop&w=600&q=80"));
        todasLasPlantas.add(new LibraryPlant("Orquídea Mariposa", "Phalaenopsis orchid", "Orquídeas", "Elegante orquídea mariposa con flores duraderas en tonos blancos y violeta.", "Sudeste asiático", "Regar por inmersión cada 8-12 días", "Fácil", "Luz difusa", "", R.drawable.helecho, "https://images.unsplash.com/photo-1525310072745-f49212b5ac6d?auto=format&fit=crop&w=600&q=80"));
        todasLasPlantas.add(new LibraryPlant("Potus Dorado", "Epipremnum aureum", "Trepadoras", "Planta colgante muy popular por sus hojas con matices dorados.", "Oceanía", "Regar cada 7 días", "Fácil", "Luz difusa", "Sombra", R.drawable.suculenta, "https://images.unsplash.com/photo-1596547609652-9cf5d8d76921?auto=format&fit=crop&w=600&q=80"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarFavoritos();
    }

    private void cargarFavoritos() {
        SharedPreferences prefs = getSharedPreferences("MisFavoritos", MODE_PRIVATE);
        List<LibraryPlant> favoritas = new ArrayList<>();
        
        for (LibraryPlant planta : todasLasPlantas) {
            if (prefs.getBoolean(planta.getNombre(), false)) {
                favoritas.add(planta);
            }
        }

        LibraryPlantAdapter adapter = new LibraryPlantAdapter(this, favoritas);
        rvFavoritos.setAdapter(adapter);
    }
}
