package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.game.GameRepository;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
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
    private PlayerRepository playerRepository;

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

        return convertToDTO(game);
    }

    @Override
    public List<GameDTO> getRollsByPlayer(Long playerId) {
        return gameRepository.findByPlayerId(playerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteRollsByPlayer(Long playerId) {
        Long deletedCount = gameRepository.deleteByPlayerId(playerId);
        return deletedCount > 0;
    }

    private GameDTO convertToDTO(GameEntity gameEntity) {
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
