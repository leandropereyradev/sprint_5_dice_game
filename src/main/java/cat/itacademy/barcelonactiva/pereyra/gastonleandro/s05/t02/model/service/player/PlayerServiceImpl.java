package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.ServiceException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private final PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @PostConstruct
    public void createAdminUserIfNotExists() {
        boolean adminExists = playerRepository.findById(1L).isPresent();

        if (!adminExists) {
            PlayerEntity admin = PlayerEntity
                    .builder()
                    .id(1L)
                    .nickName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .registrationDate(new Date())
                    .build();

            playerRepository.save(admin);
        }
    }

    @Override
    @Transactional
    public AuthResponse register(PlayerDTO playerDTO) {
        if (playerRepository.findByNickName(playerDTO.getNickName()).isPresent())
            throw new ServiceException("'" + playerDTO.getNickName() + "' already exists");

        PlayerEntity player = PlayerEntity
                .builder()
                .nickName(playerDTO.getNickName().isEmpty() ? "Anonymous" : playerDTO.getNickName())
                .password(passwordEncoder.encode(playerDTO.getPassword()))
                .role(Role.USER)
                .registrationDate(new Date())
                .build();

        playerRepository.save(player);

        return AuthResponse.builder()
                .token(jwtService.getToken(player))
                .build();
    }

    @Override
    public AuthResponse login(PlayerDTO playerDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        playerDTO.getNickName(),
                        playerDTO.getPassword()
                )
        );

        UserDetails userDetails = playerRepository.findByNickName(playerDTO.getNickName())
                .orElseThrow(() -> new ServiceException("User not found"));

        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
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
