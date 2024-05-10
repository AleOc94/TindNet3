package com.example.tindnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

    }

    public void abrirLoginEmpresa(View view) {
    // Crear un Intent para abrir la actividad de la página de login
        Intent intent = new Intent(this, LoginEmpresa.class);
        startActivity(intent); // Iniciar la actividad
    }

    public void abrirLoginCliente(View view) {
    // Crear un Intent para abrir la actividad de la página de login
        Intent intent = new Intent(this, LoginCliente.class);
        startActivity(intent); // Iniciar la actividad
    }
}