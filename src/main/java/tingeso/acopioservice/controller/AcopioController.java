package tingeso.acopioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    @PostMapping()
    public void createAcopio(@RequestParam("file") MultipartFile file,
                             RedirectAttributes ms){
        acopioService.guardarAcopio(file);
        ms.addFlashAttribute("mensaje", "Se ha subido correctamente el archivo " + file.getOriginalFilename() + "!");
        acopioService.leerArchivoAcopio(file.getOriginalFilename());
    }
}
