package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    GameDTO convertToDTO(GameEntity gameEntity) {
        GameDTO dto = new GameDTO();

        dto.setId(gameEntity.getId());
        dto.setDice1(gameEntity.getDice1());
        dto.setDice2(gameEntity.getDice2());
        dto.setGameResult(gameEntity.getGameResult());
        dto.setPlayerId(gameEntity.getPlayerId());
        dto.setHasWon(gameEntity.isHasWon());
        dto.setGameRollDate(gameEntity.getGameRollDate());

        return dto;
    }
}
