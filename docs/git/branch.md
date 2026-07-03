# Utilizzo dei branch

> [!note]
> Questo file offre un breve riepilogo sull'utilizzo dei branch della repository

## `main`

Nel branch `main` è riportata l'ultima versione stabile del progetto

## `v.M.m.p/docs` 

Nel branch `*/docs` viene portata avanti la documentazione fino al raggiungimento di una forma ottimale. Solo allora verrà spostata su `main`

## `v.M.m.p/feature/deployment`

In questo branch vengono sviluppate nuove funzionalità per il progetto (generalmente riguardano il package `controller`, `service` e vari package ausiliari). Solo quando le funzioni implementate saranno in una forma ottimale potranno essere testate nel branch `v.M.m.p/feature/testbench` e poi potranno essere dichiarati stabili (a quel punto potranno passare direttamente su `main`).

## `v.M.m.p/feature/testbench`

Come detto prima è il banco di prova per il deployment e generalmente potrebbe essere "sporcato" con test temporanei o codice di debug.

## Altri branch

Sono presenti altri branch come `v.M.m.p/feature/modelli`, `v.M.m.p/feature/repository` o `v.M.m.p/feature/dto` ma sono tutti branch temporanei che potrebbero già essere stati eliminati al momento della lettura. Qui ho sviluppato in modo indipendete lo scheletro del server. Tutti questi branch anche se in momenti diversi sono stati riuniti in deployment.