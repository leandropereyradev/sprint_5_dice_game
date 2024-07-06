package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "game")
public class GameEntity {

    @Id
    private String id;

    private Long playerId;
    private int dice1;
    private int dice2;
    private boolean hasWon;
    private int gameResult;
    private Date gameRollDate;
}
