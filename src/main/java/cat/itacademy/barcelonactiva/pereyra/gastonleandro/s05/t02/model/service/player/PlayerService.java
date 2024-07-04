package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;

import java.util.List;

public interface PlayerService {
    PlayerDTO addPlayer(PlayerDTO playerDTO);

    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO);

    boolean deletePlayer(Long id);

    PlayerDTO getPlayerById(Long id);

    List<PlayerDTO> getAllPlayers();
}
