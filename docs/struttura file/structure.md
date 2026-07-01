```text
src/main/java
└── it.unipi.makermanagerserver
    │
    ├── MakerManagerServerApplication.java  <-- La classe Main con @SpringBootApplication
    │
    ├── 📁 config
    │   └── ConfigurazioneApp.java          <-- Configurazione dinamica da file .properties
    │
    ├── 📁 model         <-- Classi modello (@Entity)
    │   │
    │   ├── 📁 common    <-- Cose condivise
    │   │   ├── Calcolabile.java            <-- Interfaccia per i costi
    │   │   ├── Tracciabile.java            <-- Interfaccia per il progresso
    │   │   └── Progresso.java              <-- Classe @Embeddable
    │   │
    │   ├── 📁 catalog   <-- L'astrazione teorica del'elemento
    │   │   └── ElementoCatalogo.java       <-- Classe @Entity
    │   │
    │   ├── 📁 inventory <-- Il possesso fisico
    │   │   ├── ArticoloInventario.java     <-- Classe @Entity astratta
    │   │   ├── ComponenteElettronico.java
    │   │   ├── MaterialeConsumabile.java
    │   │   └── StrumentoDiLavoro.java
    │   │
    │   └── 📁 project   <-- Le idee e realizzazioni
    │       ├── ProgettoMaker.java          <-- Classe @Entity astratta
    │       ├── ProgettoStampa3D.java
    │       ├── ProgettoElettronica.java
    │       ├── ProgettoRobotica.java
    │       ├── ProgettoSoftware.java
    │       └── BOM.java                <-- Classe @Entity
    │
    ├── 📁 repository    <-- collegamenti verso MySQL
    │   ├── ArticoloRepository.java         <-- Interfaccia JpaRepository
    │   ├── ProgettoRepository.java         <-- Interfaccia JpaRepository
    │   └── CatalogoRepository.java         <-- Interfaccia JpaRepository
    │
    ├── 📁 service       <-- La Logica di Business
    │   ├── InventarioService.java          <-- Esegue controlli di disponibilità
    │   ├── ProgettoService.java            <-- Calcola costi, salva BOM
    │   └── InizializzazioneService.java    <-- Gestisce il popolamento iniziale /inizializza dal JSON
    │
    └── 📁 controller    <-- Gli endpoint HTTP per il Client
        ├── InventarioController.java       <-- Ascolta su /api/inventario
        ├── ProgettoController.java         <-- Ascolta su /api/progetti
        └── InizializzazioneController.java <-- Ascolta su /api/inizializza
```