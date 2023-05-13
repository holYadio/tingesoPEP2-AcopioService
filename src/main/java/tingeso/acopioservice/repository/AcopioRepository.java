package tingeso.acopioservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.acopioservice.entity.Acopio;

import java.util.List;

@Repository
public interface AcopioRepository extends JpaRepository<Acopio, Integer> {
    List<Acopio> findByProveedor(String proveedor);
}
