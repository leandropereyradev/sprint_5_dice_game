package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Component
public class PlayerMapper {

    @Autowired
    private GameRepository gameRepository;

    public PlayerDTO convertToDTO(PlayerEntity player) {
        List<GameEntity> games = gameRepository.findByPlayerId(player.getId());

        long totalGames = games.size();
        long totalWins = games.stream().filter(GameEntity::isHasWon).count();
        double winRate = totalGames == 0 ? 0 : (double) totalWins / totalGames * 100;

        return PlayerDTO
                .builder()
                .id(player.getId())
                .email(player.getEmail())
                .nickName(player.getNickName())
                .role(player.getRole())
                .registrationDate(player.getRegistrationDate())
                .winRate(winRate)
                .build();
    }

    double calculateWinRate(PlayerEntity player) {
        List<GameEntity> games = gameRepository.findByPlayerId(player.getId());

        long totalGames = games.size();
        long totalWins = games.stream().filter(GameEntity::isHasWon).count();

        return totalGames == 0 ? 0 : (double) totalWins / totalGames * 100;
    }
}
