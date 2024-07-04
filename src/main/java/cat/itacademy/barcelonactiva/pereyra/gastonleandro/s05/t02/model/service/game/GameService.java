package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;

import java.util.List;

public interface GameService {
    GameDTO rollDices(Long playerId);

    List<GameDTO> getRollsByPlayer(Long playerId);

    boolean deleteRollsByPlayer(Long playerId);
}

