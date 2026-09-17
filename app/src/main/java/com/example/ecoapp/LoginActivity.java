package com.example.ecoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ocultar la barra de acción para diseño a pantalla completa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Por ahora, ambos botones llevarán a la pantalla principal 
        // ya que la funcionalidad profunda de login no es el objetivo principal del huerto
        View.OnClickListener goToMain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        btnRegistrarse.setOnClickListener(goToMain);
        btnIniciarSesion.setOnClickListener(goToMain);
    }
}