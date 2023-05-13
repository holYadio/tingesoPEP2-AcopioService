package tingeso.acopioservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.acopioservice.entity.Acopio;
import tingeso.acopioservice.repository.AcopioRepository;

import java.util.List;

@Service
public class AcopioService {
    @Autowired
    private AcopioRepository acopioRepository;

    public List<Acopio> getAllAcopios() {
        return acopioRepository.findAll();
    }

    public Acopio getAcopioById(int id) {
        return acopioRepository.findById(id).orElse(null);
    }

    public Acopio createAcopio(Acopio acopio) {
        return acopioRepository.save(acopio);
    }

    public List<Acopio> getAcopioByProveedor(String proveedor) {
        return acopioRepository.findByProveedor(proveedor);
    }
}
