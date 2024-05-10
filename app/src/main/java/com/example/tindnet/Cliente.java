package com.example.tindnet;

public class Cliente {
    private String nombre;
    private String email;
    private String perfil;

    // Constructor vacío requerido por Firebase
    public Cliente() {
    }

    // Constructor para inicializar los atributos
    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.perfil = "cliente";
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}

