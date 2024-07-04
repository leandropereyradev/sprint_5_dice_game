package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.ServiceException;
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
        if (playerRepository.findByNickName(playerDTO.getNickName()).isPresent())
            throw new ServiceException("'" + playerDTO.getNickName() + "' already exists");

        PlayerEntity player = new PlayerEntity();
        player.setNickName(playerDTO.getNickName().isEmpty() ? "Anonymous" : playerDTO.getNickName());
        player.setPassword(playerDTO.getPassword());
        player.setRegistrationDate(new Date());

        playerRepository.save(player);

        return convertToDTO(player);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("User not found"));

        if (playerDTO.getNickName() != null) player.setNickName(playerDTO.getNickName());

        if (playerDTO.getPassword() != null) player.setPassword(playerDTO.getPassword());

        playerRepository.save(player);

        return convertToDTO(player);
    }

    @Override
    public boolean deletePlayer(Long id) {
        boolean exists = playerRepository.existsById(id);

        if (exists) {
            playerRepository.deleteById(id);
            return true;

        } else throw new ServiceException("User not found");
    }

    @Override
    public PlayerDTO getPlayerById(Long id) {
        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("User not found"));

        return convertToDTO(player);
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PlayerDTO convertToDTO(PlayerEntity player) {
        PlayerDTO dto = new PlayerDTO();

        dto.setId(player.getId());
        dto.setNickName(player.getNickName());
        dto.setPassword(player.getPassword());
        dto.setRegistrationDate(player.getRegistrationDate());

        return dto;
    }
}
