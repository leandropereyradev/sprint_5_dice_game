package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.mapper;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    public GameDTO convertToDTO(GameEntity gameEntity) {

        return GameDTO
                .builder()
                .id(gameEntity.getId())
                .dice1(gameEntity.getDice1())
                .dice2(gameEntity.getDice2())
                .gameResult(gameEntity.getGameResult())
                .playerId(gameEntity.getPlayerId())
                .hasWon(gameEntity.isHasWon())
                .gameRollDate(gameEntity.getGameRollDate())
                .build();
    }
}
