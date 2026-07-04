package it.unipi.makermanagerserver.service.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unipi.makermanagerserver.dto.progetto.ProgettoRequestDTO;
import it.unipi.makermanagerserver.dto.progetto.ProgettoResponseDTO;

@Service
public class ProgettoService {

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
