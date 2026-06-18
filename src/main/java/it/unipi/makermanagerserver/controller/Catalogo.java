/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.makermanagerserver.controller;

import it.unipi.makermanagerserver.model.ComponenteInventario;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giuse27
 */

@RestController
@RequestMapping(path = "/catalogo")
public class Catalogo {
    
    @GetMapping(path = "/all")
    public List<ComponenteInventario> getTuttiComponenti() {
        return List.of(
            new ComponenteInventario(1L, "Arduino Uno", 3),
            new ComponenteInventario(2L, "Sensore ultrasuoni HC-SR04", 5),
            new ComponenteInventario(3L, "Filamento PLA 1kg", 2)
        );
    }
    
}
