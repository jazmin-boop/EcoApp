package com.example.ecoapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.adapter.LibraryPlantAdapter;
import com.example.ecoapp.model.LibraryPlant;

import java.util.ArrayList;
import java.util.List;

public class PlantLibraryActivity extends AppCompatActivity {

    private RecyclerView rvLibrary;
    private ImageView ivBack;
    private EditText etSearch;
    private ImageButton btnFilter;

    private List<LibraryPlant> listaCompleta;
    private LibraryPlantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_library);

        ivBack = findViewById(R.id.ivBackLibrary);
        ivBack.setOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearchLibrary);
        btnFilter = findViewById(R.id.btnFilterSpecies);

        rvLibrary = findViewById(R.id.rvPlantLibrary);
        rvLibrary.setLayoutManager(new LinearLayoutManager(this));

        // Cargar las 6 plantas exactas de la plantilla con imágenes de alta calidad
        cargarLibreriaDesdeBD();

        // Filtrado en tiempo real
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarPlantas(s.toString(), "Todas las categorías");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Menú desplegable claro de filtro por Categoría
        btnFilter.setOnClickListener(v -> {
            ContextThemeWrapper wrapper = new ContextThemeWrapper(PlantLibraryActivity.this, R.style.Theme_EcoApp_LightPopup);
            PopupMenu popup = new PopupMenu(wrapper, btnFilter);
            popup.getMenu().add("Todas las categorías");
            popup.getMenu().add("Tropicales");
            popup.getMenu().add("Suculentas");
            popup.getMenu().add("Orquídeas");
            popup.getMenu().add("Trepadoras");
            popup.getMenu().add("Helechos");

            popup.setOnMenuItemClickListener(item -> {
                String categoriaSeleccionada = item.getTitle() != null ? item.getTitle().toString() : "Todas las categorías";
                Toast.makeText(this, "Filtrando por: " + categoriaSeleccionada, Toast.LENGTH_SHORT).show();
                filtrarPlantas(etSearch.getText().toString(), categoriaSeleccionada);
                return true;
            });
            popup.show();
        });
    }

    private void cargarLibreriaDesdeBD() {
        listaCompleta = new ArrayList<>();

        // 1. Monstera
        listaCompleta.add(new LibraryPlant(
                "Monstera",
                "Monstera deliciosa",
                "Tropicales",
                "Impresionante planta de grandes hojas nativa de los bosques tropicales de México. Sus hojas brillantes con aberturas naturales le dan un toque selvático y elegante al hogar.",
                "Selvas tropicales de México y Centroamérica",
                "Regar cada 7-10 días, permitiendo que la tierra se seque.",
                "Fácil", "Luz difusa", "Sombra",
                R.drawable.helecho,
                "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=600&q=80"
        ));

        // 2. Peace Lily
        listaCompleta.add(new LibraryPlant(
                "Lirio de la Paz",
                "Spathiphyllum wallisii",
                "Tropicales",
                "Destaca por sus elegantes flores blancas y su capacidad para purificar el aire de interiores. Muy agradecida y fácil de mantener.",
                "Selvas tropicales de América del Sur",
                "Regar cada 5-7 días. Mantener el sustrato ligeramente húmedo.",
                "Medio", "Luz difusa", "Sombra",
                R.drawable.suculenta,
                "https://images.unsplash.com/photo-1593482834024-f513f56d0d5d?auto=format&fit=crop&w=600&q=80"
        ));

        // 3. Snake Plant
        listaCompleta.add(new LibraryPlant(
                "Sansevieria",
                "Sansevieria trifasciata",
                "Suculentas",
                "Conocida también como Lengua de Suegra o Espada de San Jorge. Una de las plantas más resistentes que existen.",
                "África occidental y tropical",
                "Regar cada 15-20 días. Tolera muy bien la sequía.",
                "Medio", "Luz difusa", "Sol directo",
                R.drawable.helecho,
                "https://images.unsplash.com/photo-1509423350716-97f9360b4e09?auto=format&fit=crop&w=600&q=80"
        ));

        // 4. Rubber Plant 'Variegata'
        listaCompleta.add(new LibraryPlant(
                "Ficus Variegata",
                "Ficus elastica variegata",
                "Tropicales",
                "Planta de interior muy decorativa con hojas carnosas de tono verde y bordes crema. Aporta un toque moderno.",
                "Asia tropical (India y Malasia)",
                "Regar cada 10 días cuando la capa superior esté seca.",
                "Fácil", "Luz difusa", "Sombra",
                R.drawable.suculenta,
                "https://images.unsplash.com/photo-1525498128493-380d1990a112?auto=format&fit=crop&w=600&q=80"
        ));

        // 5. Moth Orchid
        listaCompleta.add(new LibraryPlant(
                "Orquídea Mariposa",
                "Phalaenopsis orchid",
                "Orquídeas",
                "Elegante orquídea mariposa con flores duraderas en tonos blancos y violeta. Perfecta para mesas de centro.",
                "Sudeste asiático y Australia",
                "Regar por inmersión cada 8-12 días cuando las raíces se tornen grises.",
                "Fácil", "Luz difusa", "",
                R.drawable.helecho,
                "https://images.unsplash.com/photo-1525310072745-f49212b5ac6d?auto=format&fit=crop&w=600&q=80"
        ));

        // 6. Golden Pothos
        listaCompleta.add(new LibraryPlant(
                "Potus Dorado",
                "Epipremnum aureum",
                "Trepadoras",
                "Planta colgante muy popular por sus hojas con matices dorados. Muy versátil y casi indestructible.",
                "Islas Salomón y Oceanía",
                "Regar cada 7 días. Muy tolerante a olvidos de riego.",
                "Fácil", "Luz difusa", "Sombra",
                R.drawable.suculenta,
                "https://images.unsplash.com/photo-1596547609652-9cf5d8d76921?auto=format&fit=crop&w=600&q=80"
        ));

        adapter = new LibraryPlantAdapter(this, listaCompleta);
        rvLibrary.setAdapter(adapter);
    }

    private void filtrarPlantas(String query, String categoriaFiltro) {
        List<LibraryPlant> listaFiltrada = new ArrayList<>();
        for (LibraryPlant plant : listaCompleta) {
            boolean coincideTexto = plant.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    plant.getEspecie().toLowerCase().contains(query.toLowerCase());
            boolean coincideCategoria = categoriaFiltro.equals("Todas las categorías") ||
                    plant.getCategoria().equalsIgnoreCase(categoriaFiltro);

            if (coincideTexto && coincideCategoria) {
                listaFiltrada.add(plant);
            }
        }
        adapter = new LibraryPlantAdapter(this, listaFiltrada);
        rvLibrary.setAdapter(adapter);
    }
}
