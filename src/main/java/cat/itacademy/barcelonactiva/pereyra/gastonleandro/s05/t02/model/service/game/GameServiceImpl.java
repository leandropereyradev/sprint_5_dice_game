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
        GameEntity game = new GameEntity();

        game.setPlayerId(playerId);
        game.setDice1((int) (Math.random() * 6) + 1);
        game.setDice2((int) (Math.random() * 6) + 1);
        game.setGameResult(game.getDice1() + game.getDice2());
        game.setHasWon(game.getGameResult() == 7);
        game.setGameRollDate(new Date());

        gameRepository.save(game);

        return gameMapper.convertToDTO(game);
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
