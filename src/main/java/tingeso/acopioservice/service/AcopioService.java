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

    public double klsTotalLeche(List<Acopio> acopios) {
        double kls = 0;
        for (Acopio acopio : acopios) {
            kls += Integer.parseInt(acopio.getKlsLeche());
        }
        return kls;
    }

    public double diasEnvioLeche(List<Acopio> acopios) {
        double dias = 0;
        int i = 0;
        if (acopios.size() > 1) {
            dias++;
        } else {
            while (i < (acopios.size())) {
                if (i < acopios.size() - 1) {
                    if ((acopios.get(i).getFecha().equals(acopios.get(i + 1).getFecha())) &&
                            !acopios.get(i).getTurno().equals(acopios.get(i + 1).getTurno())) {
                        i++;
                        dias++;
                    } else if (!acopios.get(i).getFecha().equals(acopios.get(i + 1).getFecha())) {
                        dias++;
                    }
                } else {
                    try {
                        if (!acopios.get(i).getFecha().equals(acopios.get(i - 1).getFecha()) ||
                                !acopios.get(i).getTurno().equals(acopios.get(i - 1).getTurno())) {
                            dias++;
                        }
                    } catch (Exception e) {
                        logg.error("Error: ", e);
                    }
                }
                i++;
            }
        }
        return dias;
    }

    public double getVariacionLeche(String quincena, String codigoProveedor, double klsTotalLeche) {
        double klsLecheAnterior;
        String quincenaAnterior = restTemplate.getForObject("http://localhost:8002/laboratorio/lastquincena/" + quincena, String.class);
        if (quincenaAnterior == null) {
            klsLecheAnterior = klsTotalLeche;
        }else{
            List<Acopio> datosAcopioQuincena = getAcopioByQuincenaAndProveedor(
                    quincenaAnterior, codigoProveedor);
            klsLecheAnterior = klsTotalLeche(datosAcopioQuincena);

        }
        double variacion = Math.round((((klsLecheAnterior - klsTotalLeche)*100)/klsLecheAnterior)*10000)/10000.0;
        if (variacion <= 0) {
            variacion = 0;
        }
        return variacion;
    }
}
