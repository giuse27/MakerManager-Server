package it.unipi.makermanagerserver.service.endpoint;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.mapper.ArticoloInventarioMapper;
import it.unipi.makermanagerserver.mapper.InventarioMapper;
import it.unipi.makermanagerserver.repository.ArticoloInventarioRepository;
import it.unipi.makermanagerserver.repository.InventarioRepository;

/**
 * Questa classe fornisce metodi per l'endpoint /api/inventario/*
 * 
 * Il controller usa questa classe come intermediaria con il repository o mapper
 */
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepo;
    private final ArticoloInventarioRepository articoloRepo;
    private final InventarioMapper inventarioMapper;
    private final ArticoloInventarioMapper articoloMapper;

    public InventarioService(
        InventarioRepository inventarioRepo,
        ArticoloInventarioRepository articoloRepo,
        InventarioMapper inventarioMapper,
        ArticoloInventarioMapper articoloMapper
    ) {

        this.inventarioRepo = inventarioRepo;
        this.articoloRepo = articoloRepo;
        this.inventarioMapper = inventarioMapper;
        this.articoloMapper = articoloMapper;

    }

}
