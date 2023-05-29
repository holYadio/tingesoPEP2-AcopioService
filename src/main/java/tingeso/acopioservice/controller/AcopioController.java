package tingeso.acopioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso.acopioservice.entity.Acopio;
import tingeso.acopioservice.service.AcopioService;

import java.util.List;

@RestController
@RequestMapping("/acopio")
public class AcopioController {
    @Autowired
    AcopioService acopioService;

    @GetMapping
    public ResponseEntity<List<Acopio>> getAll(){
        List<Acopio> acopios = acopioService.getAllAcopios();
        if(acopios.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(acopios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Acopio> getById(@PathVariable(value = "id") int id){
        Acopio acopio = acopioService.getAcopioById(id);
        if(acopio == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(acopio);
    }

    @GetMapping("/byproveedor/{proveedor}")
    public ResponseEntity<List<Acopio>> getByProveedor(@PathVariable(value = "proveedor") String proveedor){
        List<Acopio> acopios = acopioService.getAcopioByProveedor(proveedor);
        if(acopios.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(acopios);
    }

    @GetMapping("/byquincenaproveedor/{quincena}/{proveedor}")
    public ResponseEntity<List<Acopio>> getByQuincenaAndProveedor(@PathVariable(value = "quincena") String quincena,
                                                                  @PathVariable(value = "proveedor") String proveedor){
        List<Acopio> acopios = acopioService.getAcopioByQuincenaAndProveedor(quincena, proveedor);
        if(acopios.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(acopios);
    }

    @GetMapping("/klsleche")
    public double getKlsLeche(@RequestBody List<Acopio> acopios){
        return acopioService.klsTotalLeche(acopios);
    }

    @GetMapping("/diasleche")
    public double diasEnvioLeche(@RequestBody List<Acopio> acopios){
        return acopioService.diasEnvioLeche(acopios);
    }

    @PostMapping()
    public ResponseEntity<Acopio> createAcopio(@RequestBody Acopio acopio){
        Acopio newAcopio = acopioService.createAcopio(acopio);
        if(newAcopio == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(newAcopio);
    }
}
