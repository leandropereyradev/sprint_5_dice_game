package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;
    private String password;
    private Date registrationDate;
}
