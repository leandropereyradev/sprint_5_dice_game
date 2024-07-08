package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PlayerService {
    void createAdminUserIfNotExists();

    PlayerDTO register(PlayerDTO playerDTO);

    AuthResponse login(PlayerDTO playerDTO);

    void logout(HttpServletRequest request);

    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO, HttpServletRequest request);

    boolean deletePlayer(Long id);

    PlayerDTO getPlayerById(Long id, HttpServletRequest request);

    List<PlayerDTO> getAllPlayers(HttpServletRequest request);

    double getAverageWinRate();

    PlayerDTO getPlayerWithLowestWinRate();

    PlayerDTO getPlayerWithHighestWinRate();
}
