package com.example.mibiblioteca;

public class Libro {
    private int libroId;
    private String titulo;
    private String autor;
    private String genero;

    public Libro(int libroId, String titulo, String autor, String genero) {
        this.libroId = libroId;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
    // Getters y setters
}
