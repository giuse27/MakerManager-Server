package it.unipi.makermanagerserver.model.project;

import java.time.LocalDate;

import it.unipi.makermanagerserver.enums.TipologiaProgetto;
import it.unipi.makermanagerserver.model.common.Progresso;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "progetti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "tipo_progetto", 
    discriminatorType = DiscriminatorType.STRING
)
public abstract class ProgettoMaker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "tipologia")
    @Enumerated(EnumType.STRING)
    private TipologiaProgetto tipologia;

    // Nuovo campo: JPA creerà una colonna "data_caricamento" di tipo DATE nel DB
    @Column(name = "data_caricamento")
    private LocalDate dataCaricamento;

    @Embedded
    private BOM distintaBase;

    @Embedded
    private Progresso progresso;

    // COSTRUTTORI

    // costruttore vuoto per JPA
    public ProgettoMaker() {
        dataCaricamento = LocalDate.now();
    }

    public ProgettoMaker(String nome, String descrizione, TipologiaProgetto tipologia, BOM distintaBase, Progresso progresso, LocalDate dataCaricamento) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.distintaBase = distintaBase;
        this.progresso = progresso;
        // Se passiamo null, si imposta automaticamente ad oggi
        this.dataCaricamento = (dataCaricamento != null) ? dataCaricamento : LocalDate.now();
    }

    // GETTER E SETTER

    public Long getId() {
        return id;
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

    public TipologiaProgetto getTipologia() {
        return tipologia;
    }

    public void setTipologia(TipologiaProgetto tipologia) {
        this.tipologia = tipologia;
    }   

    public BOM getDistintaBase() {
        return distintaBase;
    }

    public void setDistintaBase(BOM distintaBase) {
        this.distintaBase = distintaBase;
    }

    public LocalDate getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(LocalDate dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
    }

    public Progresso getProgresso() {
        return progresso;
    }

    public void setProgresso(Progresso progresso) {
        this.progresso = progresso;
    } 

}
