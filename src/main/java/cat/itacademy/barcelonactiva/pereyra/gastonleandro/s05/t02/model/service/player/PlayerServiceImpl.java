package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.ServiceException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        if (playerRepository.findByNickName(playerDTO.getNickName()).isPresent())
            throw new ServiceException("'" + playerDTO.getNickName() + "' already exists");

        PlayerEntity player = new PlayerEntity();
        player.setNickName(playerDTO.getNickName().isEmpty() ? "Anonymous" : playerDTO.getNickName());
        player.setPassword(playerDTO.getPassword());
        player.setRegistrationDate(new Date());

        playerRepository.save(player);

        return playerMapper.convertToDTO(player);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));

        if (playerDTO.getNickName() != null) player.setNickName(playerDTO.getNickName());

        if (playerDTO.getPassword() != null) player.setPassword(playerDTO.getPassword());

        playerRepository.save(player);

        return playerMapper.convertToDTO(player);
    }

    @Override
    public boolean deletePlayer(Long id) {
        boolean exists = playerRepository.existsById(id);

        if (exists) {
            playerRepository.deleteById(id);
            return true;

        } else throw new ServiceException("Player not found");
    }

    @Override
    public PlayerDTO getPlayerById(Long id) {
        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));

        return playerMapper.convertToDTO(player);
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream().map(playerMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public double getAverageWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        double totalWinRate = players.stream()
                .mapToDouble(playerMapper::calculateWinRate)
                .sum();

        return players.isEmpty() ? 0 : totalWinRate / players.size();
    }

    @Override
    public PlayerDTO getPlayerWithLowestWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        return players.stream()
                .map(playerMapper::convertToDTO)
                .min(Comparator.comparingDouble(PlayerDTO::getWinRate))
                .orElseThrow(() -> new ServiceException("No players found"));
    }

    @Override
    public PlayerDTO getPlayerWithHighestWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        return players.stream()
                .map(playerMapper::convertToDTO)
                .max(Comparator.comparingDouble(PlayerDTO::getWinRate))
                .orElseThrow(() -> new ServiceException("No players found"));
    }
}
