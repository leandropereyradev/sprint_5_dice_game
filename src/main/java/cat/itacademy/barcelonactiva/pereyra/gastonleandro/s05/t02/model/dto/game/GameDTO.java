package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing a game")
public class GameDTO {

    @Schema(description = "Unique identifier of the game - MongoDB")
    private String id;

    @NotNull(message = "Player ID is required")
    @Schema(description = "ID of the player who played the game", example = "1", required = true)
    private Long playerId;

    @Min(value = 1, message = "Dice value must be between 1 and 6")
    @Max(value = 6, message = "Dice value must be between 1 and 6")
    @Schema(description = "Value of the first dice roll", example = "4")
    private int dice1;

    @Min(value = 1, message = "Dice value must be between 1 and 6")
    @Max(value = 6, message = "Dice value must be between 1 and 6")
    @Schema(description = "Value of the second dice roll", example = "3")
    private int dice2;

    @Schema(description = "Indicates if the player has won the game", example = "true")
    private boolean hasWon;

    @Schema(description = "Result of the game", example = "7")
    private int gameResult;

    @Schema(description = "Date when the game was played", example = "2024-07-07T15:30:00Z")
    private Date gameRollDate;
}
