package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PlayerService {

    PlayerDTO register(PlayerEntity player);

    AuthResponse login(PlayerEntity player);

    void logout(HttpServletRequest request);

    PlayerDTO updatePlayer(Long id, PlayerEntity player, HttpServletRequest request);

    boolean deletePlayer(Long id, HttpServletRequest request);

    PlayerDTO getPlayerById(Long id, HttpServletRequest request);

    List<PlayerDTO> getAllPlayers(HttpServletRequest request);

    double getAverageWinRate();

    PlayerDTO getPlayerWithLowestWinRate();

    PlayerDTO getPlayerWithHighestWinRate();
}
