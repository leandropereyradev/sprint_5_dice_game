package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        PlayerEntity player = new PlayerEntity();

        player.setEmail(playerDTO.getEmail());
        player.setPassword(playerDTO.getPassword());
        player.setNickName(playerDTO.getNickName().isEmpty() ? "Anonymous" : playerDTO.getNickName());
        player.setRegistrationDate(new Date());

        playerRepository.save(player);

        return convertToDTO(player);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        player.setEmail(playerDTO.getEmail());
        player.setPassword(playerDTO.getPassword());
        player.setNickName(playerDTO.getNickName());

        playerRepository.save(player);

        return convertToDTO(player);
    }

    @Override
    public boolean deletePlayer(Long id) {
        boolean exists = playerRepository.existsById(id);

        if (exists) {
            playerRepository.deleteById(id);
            return true;

        } else return false;
    }

    @Override
    public PlayerDTO getPlayerById(Long id) {
        Optional<PlayerEntity> playerEntity = playerRepository.findById(id);
        return playerEntity.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PlayerDTO convertToDTO(PlayerEntity user) {
        PlayerDTO dto = new PlayerDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setNickName(user.getNickName());
        dto.setRegistrationDate(user.getRegistrationDate());

        return dto;
    }
}
