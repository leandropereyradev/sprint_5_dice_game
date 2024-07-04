package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class GameDTO {
    private String id;
    private Long playerId;
    private int dice1;
    private int dice2;
    private boolean hasWon;
    private int gameResult;
    private Date gameRollDate;

    private double successRate;
}
