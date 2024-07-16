package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.AccessDeniedException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.ServiceException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.mapper.PlayerMapper;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.enums.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.dao.response.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.security.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostConstruct
    public void createAdminUserIfNotExists() {
        boolean adminExists = playerRepository.findById(1L).isPresent();

        if (!adminExists) {
            PlayerEntity admin = PlayerEntity
                    .builder()
                    .id(1L)
                    .email("admin@admin.com")
                    .nickName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ROLE_ADMIN)
                    .registrationDate(new Date())
                    .build();

            playerRepository.save(admin);
        }
    }

    @Override
    @Transactional
    public PlayerDTO register(PlayerEntity player) {
        if (playerRepository.findByEmail(player.getEmail()).isPresent())
            throw new ServiceException("'" + player.getEmail() + "' already exists");

        PlayerEntity playerEntity = PlayerEntity
                .builder()
                .email(player.getEmail())
                .nickName(player.getNickName().isEmpty() ? "Anonymous" : player.getNickName())
                .password(passwordEncoder.encode(player.getPassword()))
                .role(Role.ROLE_USER)
                .registrationDate(new Date())
                .build();

        PlayerEntity savedPlayer = playerRepository.save(playerEntity);

        return playerMapper.convertToDTO(savedPlayer);
    }

    @Override
    public AuthResponse login(PlayerEntity player) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        player.getEmail(),
                        player.getPassword()
                )
        );

        UserDetails userDetails = playerRepository.findByEmail(player.getEmail())
                .orElseThrow(() -> new ServiceException("Player not found"));

        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = extractToken(request);
        jwtService.invalidateToken(token);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerEntity player, HttpServletRequest request) {
        verifyEmailMatch(id, request);

        PlayerEntity playerEntity = playerRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Player not found"));

        if (player.getEmail() != null) playerEntity.setEmail(player.getEmail());

        if (player.getNickName() != null) playerEntity.setNickName(player.getNickName());

        if (player.getPassword() != null) playerEntity.setPassword(
                passwordEncoder.encode(player.getPassword())
        );

        playerRepository.save(playerEntity);

        return playerMapper.convertToDTO(playerEntity);
    }

    @Override
    public PlayerDTO deletePlayer(Long id, HttpServletRequest request) {

        String token = extractToken(request);
        String email = jwtService.getEmailFromToken(token);

        PlayerEntity tokenPlayer = playerRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("Player not found"));

        if (!tokenPlayer.getRole().equals(Role.ROLE_ADMIN))
            throw new AccessDeniedException("Access denied: You are not an admin.");

        boolean exists = playerRepository.existsById(id);

        if (!exists) throw new ServiceException("Player not found");

        PlayerEntity playerEntity = playerRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Player not found"));

        playerRepository.deleteById(id);

        return playerMapper.convertToDTO(playerEntity);

    }

    @Override
    public PlayerDTO getPlayerById(Long id, HttpServletRequest request) {
        verifyEmailMatch(id, request);

        PlayerEntity player = playerRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Player not found"));

        return playerMapper.convertToDTO(player);
    }

    @Override
    public List<PlayerDTO> getAllPlayers(HttpServletRequest request) {
        PlayerEntity tokenPlayer = null;

        if (request != null) tokenPlayer = getPlayerFromToken(request);

        if (tokenPlayer != null && tokenPlayer.getRole().equals(Role.ROLE_ADMIN))
            return playerRepository.findAll().stream()
                    .map(playerMapper::convertToDTO)
                    .collect(Collectors.toList());

        else return playerRepository.findAll().stream()
                .filter(player -> !player.getId().equals(1L))
                .map(playerMapper::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public double getAverageWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        double totalWinRate = players.stream()
                .filter(player -> player.getId() != 1L)
                .mapToDouble(playerMapper::calculateWinRate)
                .sum();

        long count = players.stream()
                .filter(player -> player.getId() != 1L)
                .count();

        return count == 0 ? 0 : totalWinRate / count;
    }

    @Override
    public PlayerDTO getPlayerWithLowestWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        return players.stream()
                .filter(player -> player.getId() != 1L)
                .map(playerMapper::convertToDTO)
                .min(Comparator.comparingDouble(PlayerDTO::getWinRate))
                .orElseThrow(() -> new ServiceException("No players found"));
    }

    @Override
    public PlayerDTO getPlayerWithHighestWinRate() {
        List<PlayerEntity> players = playerRepository.findAll();

        return players.stream()
                .filter(player -> player.getId() != 1L)
                .map(playerMapper::convertToDTO)
                .max(Comparator.comparingDouble(PlayerDTO::getWinRate))
                .orElseThrow(() -> new ServiceException("No players found"));
    }


    private void verifyEmailMatch(Long playerId, HttpServletRequest request) {
        PlayerEntity tokenPlayer = getPlayerFromToken(request);

        if (!tokenPlayer.getRole().equals(Role.ROLE_ADMIN)) {
            PlayerEntity player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new ServiceException("Player not found"));

            if (!player.getEmail().equals(tokenPlayer.getEmail()))
                throw new AccessDeniedException("Access denied: You are trying to access a user that does not correspond to your credential.");

        }
    }

    private String extractToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    private PlayerEntity getPlayerFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtService.getEmailFromToken(token);

        return playerRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("Player not found"));
    }
}
