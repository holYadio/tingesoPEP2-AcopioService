package tingeso.acopioservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tingeso.acopioservice.entity.Acopio;
import tingeso.acopioservice.repository.AcopioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcopioService {
    @Autowired
    AcopioRepository acopioRepository;

    @Autowired
    RestTemplate restTemplate;

    private final Logger logg = LoggerFactory.getLogger(AcopioService.class);

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

    public List<Acopio> getAcopioByQuincenaAndProveedor(String quincena, String proveedor) {
        List<Acopio> acopios = acopioRepository.findByProveedor(proveedor);
        List<Acopio> acopiosByQuincena = new ArrayList<>();
        int yearA = Integer.parseInt(quincena.split("/")[0]);
        int monthA = Integer.parseInt(quincena.split("/")[1]);
        String dayA = quincena.split("/")[2];
        for (Acopio acopio : acopios) {
            int year = Integer.parseInt(acopio.getFecha().split("/")[0]);
            int month = Integer.parseInt(acopio.getFecha().split("/")[1]);
            int day = Integer.parseInt(acopio.getFecha().split("/")[2]);
            if (dayA.equals("Q1")) {
                if (yearA == year && monthA == month && day <= 15 && day > 0) {
                    acopiosByQuincena.add(acopio);
                }
            } else if (dayA.equals("Q2")) {
                if (year == yearA && month == monthA && day > 15 && day <= 31) {
                    acopiosByQuincena.add(acopio);
                }
            }
        }
        return acopiosByQuincena;
    }


}
