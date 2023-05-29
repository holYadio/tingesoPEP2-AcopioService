package tingeso.acopioservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Laboratorio {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    private String proveedor;
    @Column(name = "porcentaje_grasa")
    private String porcentajeGrasa;
    @Column(name = "porcentaje_solido_total")
    private String porcentajeSolidoTotal;
    private String quincena;
}
