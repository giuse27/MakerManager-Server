package it.unipi.makermanagerserver.dto.raccomandazione;

import java.util.List;

/**
 * DTO di RISPOSTA per GET /api/progetti/consigliati
 * 
 * un progetto viene suggerito all'utente in base ai suoi inventari
 * presenta oltre al progetto consigliato anche l'indice di fattibilità
 * e il dettaglio degli elementi che eventualmente mancano.
 */
public class ProgettoConsigliatoResponseDTO {

    private Long id;
    private String tipo;
    private String nome;
    private String descrizione;
    private Long idAutore;
    private String nicknameAutore;

    // Metriche di raccomandazione
    private double indiceFattibilita;
    private boolean realizzabile;
    private int righeTotali;
    private int righeMancanti;
    private List<ElementoMancanteDTO> dettaglioMancanti;

    public ProgettoConsigliatoResponseDTO() {
    }

    public ProgettoConsigliatoResponseDTO(
        Long id, 
        String tipo, 
        String nome, 
        String descrizione,
        Long idAutore, 
        String nicknameAutore,
        double indiceFattibilita, 
        boolean realizzabile,
        int righeTotali, 
        int righeMancanti,
        List<ElementoMancanteDTO> dettaglioMancanti
    ) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.descrizione = descrizione;
        this.idAutore = idAutore;
        this.nicknameAutore = nicknameAutore;
        this.indiceFattibilita = indiceFattibilita;
        this.realizzabile = realizzabile;
        this.righeTotali = righeTotali;
        this.righeMancanti = righeMancanti;
        this.dettaglioMancanti = dettaglioMancanti;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getIdAutore() {
        return idAutore;
    }

    public void setIdAutore(Long idAutore) {
        this.idAutore = idAutore;
    }

    public String getNicknameAutore() {
        return nicknameAutore;
    }

    public void setNicknameAutore(String nicknameAutore) {
        this.nicknameAutore = nicknameAutore;
    }

    public double getIndiceFattibilita() {
        return indiceFattibilita;
    }

    public void setIndiceFattibilita(double indiceFattibilita) {
        this.indiceFattibilita = indiceFattibilita;
    }

    public boolean isRealizzabile() {
        return realizzabile;
    }

    public void setRealizzabile(boolean realizzabile) {
        this.realizzabile = realizzabile;
    }

    public int getRigheTotali() {
        return righeTotali;
    }

    public void setRigheTotali(int righeTotali) {
        this.righeTotali = righeTotali;
    }

    public int getRigheMancanti() {
        return righeMancanti;
    }

    public void setRigheMancanti(int righeMancanti) {
        this.righeMancanti = righeMancanti;
    }

    public List<ElementoMancanteDTO> getDettaglioMancanti() {
        return dettaglioMancanti;
    }

    public void setDettaglioMancanti(List<ElementoMancanteDTO> dettaglioMancanti) {
        this.dettaglioMancanti = dettaglioMancanti;
    }

}