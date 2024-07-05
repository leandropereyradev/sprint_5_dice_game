package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game;

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
public class GameDTO {
    private String id;

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @Min(value = 1, message = "Dice value must be between 1 and 6")
    @Max(value = 6, message = "Dice value must be between 1 and 6")
    private int dice1;

    @Min(value = 1, message = "Dice value must be between 1 and 6")
    @Max(value = 6, message = "Dice value must be between 1 and 6")
    private int dice2;

    private boolean hasWon;
    private int gameResult;
    private Date gameRollDate;
}
