package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface GameService {
    GameDTO rollDices(Long playerId, HttpServletRequest request);

    List<GameDTO> getRollsByPlayer(Long playerId, HttpServletRequest request);

    void deleteRollsByPlayer(Long playerId, HttpServletRequest request);
}

