package tingeso.acopioservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tingeso.acopioservice.entity.Acopio;
import tingeso.acopioservice.repository.AcopioRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class AcopioService {
    @Autowired
    AcopioRepository acopioRepository;

    @Autowired
    RestTemplate restTemplate;

    private final Logger logg = LoggerFactory.getLogger(AcopioService.class);

    /**
     * Funcion para obtener todos los acopios
     * @return List<DatosAcopioEntity> con todos los acopios
     */
    public List<Acopio> getAllAcopios() {
        return acopioRepository.findAll();
    }

    /**
     * Funcion para obtener un acopio segun su ID
     * @param id: ID del acopio deseado
     * @return Acopio con el ID especificando
     */
    public Acopio getAcopioById(int id) {
        return acopioRepository.findById(id).orElse(null);
    }

    /**
     * Funcion para obtener los acopios asociados a un proveedor
     * @param proveedor String con el nombre del proveedor
     * @return List<DatosAcopioEntity> con los datos de los acopios asociados al proveedor
     */
    public List<Acopio> getAcopioByProveedor(String proveedor) {
        return acopioRepository.findByProveedor(proveedor);
    }

    /**
     * Funcion para obtener los acopios asociados a una quincena en específico
     * @param quincena: String con el formato "AAAA/MM/Q1" o "AAAA/MM/Q2"
     * @param proveedor: String con el nombre del proveedor
     * @return List<DatosAcopioEntity> con los datos de los acopios asociados a la quincena
     */
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

    /**
     * Funcion para obtener los acopios asociados a una quincena en específico
     * @param file Archivo de acopio
     * @return String con el resultado de la operacion
     */
    public String guardarAcopio(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null){
            if ((!file.isEmpty()) && (fileName.toUpperCase().equals("DATA.TXT"))){
                try{
                    byte [] bytes = file.getBytes();
                    Path path = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo de analisis del Laboratiorio guardado");
                }
                catch (IOException e){
                    logg.error("Error", e);
                }
            }
            return "Archivo de analisis del Laboratiorio guardado";
        }else {
            return "No se guardo el Archivo de analisis del Laboratiorio";
        }
    }

    /**
     * Funcion para leer el archivo de acopio
     * @param direccion direccion del archivo de acopio
     */
    public void leerArchivoAcopio(String direccion){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(direccion));
            StringBuilder temp = new StringBuilder();
            String bfRead;
            int count = 1;
            while ((bfRead = br.readLine()) != null) {
                if (count == 1) {
                    count = 0;
                }
                else {
                    guardarAcopioDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2], bfRead.split(";")[3]);
                    temp.append("\n").append(bfRead);
                }
            }
            logg.info("Archivo de Acopio leido correctamente");
        }
        catch (Exception e){
            logg.error("Error al leer el archivo de Acopio",e);
        }
        finally {
            try{
                if (null != br){
                    br.close();
                }
            }
            catch (IOException e){
                logg.error("Error", e);
            }
        }
    }

    /**
     * Funcion para guardar los datos de acopio en la base de datos
     * @param fecha fecha del acopio
     * @param turno turno del acopio
     * @param proveedor proveedor del acopio
     * @param klsLeche kilos de leche del acopio
     */
    public void guardarAcopioDB(String fecha, String turno, String proveedor, String klsLeche){
        Acopio newDato = new Acopio();
        newDato.setFecha(fecha);
        newDato.setTurno(turno);
        newDato.setProveedor(proveedor);
        newDato.setKlsLeche(klsLeche);
        acopioRepository.save(newDato);
    }
}
