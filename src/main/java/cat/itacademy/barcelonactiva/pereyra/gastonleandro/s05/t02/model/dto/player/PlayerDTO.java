package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing a player")
public class PlayerDTO {

    @Schema(description = "Unique identifier of the player - MySQL")
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email of the player", example = "player@example.com", required = true)
    private String email;

    @Size(min = 3, max = 20, message = "Nickname must be between 3 and 20 characters")
    @Schema(description = "Nickname of the player", example = "Player1")
    private String nickName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    @Schema(description = "Password of the player", example = "password123", required = true)
    private String password;

    @Schema(description = "Role of the player", example = "ROLE_USER")
    private Role role;

    @Schema(description = "Date when the player registered", example = "2024-07-07T15:30:00Z")
    private Date registrationDate;

    @Schema(description = "Win rate of the player", example = "0.75")
    private double winRate;
}
