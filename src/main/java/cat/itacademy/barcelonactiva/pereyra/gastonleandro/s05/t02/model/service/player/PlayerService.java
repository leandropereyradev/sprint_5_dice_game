package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;

import java.util.List;

public interface PlayerService {
    AuthResponse register(PlayerDTO playerDTO);

    AuthResponse login(PlayerDTO playerDTO);

    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO);

    boolean deletePlayer(Long id);

    PlayerDTO getPlayerById(Long id);

    List<PlayerDTO> getAllPlayers();

    double getAverageWinRate();

    PlayerDTO getPlayerWithLowestWinRate();

    PlayerDTO getPlayerWithHighestWinRate();
}
