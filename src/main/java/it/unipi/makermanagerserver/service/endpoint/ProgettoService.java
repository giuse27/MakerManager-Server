package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;
import it.unipi.makermanagerserver.mapper.ProgettoMapper;
import it.unipi.makermanagerserver.repository.ProgettoMakerRepository;

@Service
public class ProgettoService {

    private final ProgettoMakerRepository progettoRepo;
    private final ProgettoMapper progettoMapper;

    public ProgettoService(
        ProgettoMakerRepository progettoRepo,
        ProgettoMapper progettoMapper
    ) {
 
        this.progettoRepo = progettoRepo;
        this.progettoMapper = progettoMapper;
 
    }
    
    public List<ProgettoResponseDTO> trovaTutti() {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trovaTutti'");
        
    }

    public ProgettoResponseDTO trovaPerId(Long idProgetto) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trovaPerId'");

    }

    public List<ProgettoResponseDTO> trovaPerTipologia(String tipologia) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trovaPerTipologia'");
    
    }

    public ProgettoResponseDTO crea(ProgettoRequestDTO dtoRichiesta) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crea'");

    }

    public void elimina(Long idProgetto) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'elimina'");

    }
    
}
