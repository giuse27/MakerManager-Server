/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.makermanagerserver.model;

/**
 *
 * @author giuse27
 */
public class ComponenteInventario {
    
    // caratteristiche di ogni componente dell'inventario
    private long id;
    private String nome;
    private int quantita;
    
    // Costruttore vuoto necessario per la (de)serializzazione JSON (vedi lez 5)
    public ComponenteInventario() {
    }

    public ComponenteInventario(long id, String nome, int quantita) {
        this.id = id;
        this.nome = nome;
        this.quantita = quantita;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
    
}
