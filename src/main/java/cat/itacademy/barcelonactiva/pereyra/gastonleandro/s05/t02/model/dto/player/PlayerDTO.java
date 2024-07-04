package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private Date registrationDate;

    private double winRate;
}
