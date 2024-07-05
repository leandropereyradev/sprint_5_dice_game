package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDTO {
    private Long id;

    @NotBlank(message = "Nickname is required")
    @Size(min = 3, max = 20, message = "Nickname must be between 3 and 20 characters")
    private String nickName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    private Date registrationDate;
    private double winRate;
}
