package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Override
    public GameDTO rollDices(Long playerId) {
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        int result = dice1 + dice2;

        GameEntity gameEntity = GameEntity
                .builder()
                .playerId(playerId)
                .dice1(dice1)
                .dice2(dice2)
                .gameResult(dice1 + dice2)
                .hasWon(result == 7)
                .gameRollDate(new Date())
                .build();

        gameRepository.save(gameEntity);

        return gameMapper.convertToDTO(gameEntity);
    }

    @Override
    public List<GameDTO> getRollsByPlayer(Long playerId) {
        return gameRepository.findByPlayerId(playerId)
                .stream()
                .map(gameMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteRollsByPlayer(Long playerId) {
        Long deletedCount = gameRepository.deleteByPlayerId(playerId);

        return deletedCount > 0;
    }
}
