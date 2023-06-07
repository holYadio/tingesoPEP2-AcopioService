package tingeso.acopioservice.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Acopio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAcopio;
    private String fecha;
    private String turno;
    private String proveedor;
    @Column(name = "kls_leche")
    private String klsLeche;
}
