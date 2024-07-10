package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception.ServiceException;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.InvalidTokenService;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private InvalidTokenService invalidTokenService;

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
    public PlayerDTO register(PlayerDTO playerDTO) {
        if (playerRepository.findByEmail(playerDTO.getEmail()).isPresent())
            throw new ServiceException("'" + playerDTO.getEmail() + "' already exists");

        PlayerEntity player = PlayerEntity
                .builder()
                .email(playerDTO.getEmail())
                .nickName(playerDTO.getNickName().isEmpty() ? "Anonymous" : playerDTO.getNickName())
                .password(passwordEncoder.encode(playerDTO.getPassword()))
                .role(Role.ROLE_USER)
                .registrationDate(new Date())
                .build();

        playerRepository.save(player);

        return playerMapper.convertToDTO(player);
    }

    @Override
    public AuthResponse login(PlayerDTO playerDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        playerDTO.getEmail(),
                        playerDTO.getPassword()
                )
        );

        UserDetails userDetails = playerRepository.findByEmail(playerDTO.getEmail())
                .orElseThrow(() -> new ServiceException("User not found"));

        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = extractToken(request);
        invalidTokenService.invalidateToken(token);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO, HttpServletRequest request) {
        verifyEmailMatch(id, request);

        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));

        if (playerDTO.getEmail() != null) player.setEmail(playerDTO.getEmail());

        if (playerDTO.getNickName() != null) player.setNickName(playerDTO.getNickName());

        if (playerDTO.getPassword() != null) player.setPassword(passwordEncoder.encode(playerDTO.getPassword()));

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
    public PlayerDTO getPlayerById(Long id, HttpServletRequest request) {
        verifyEmailMatch(id, request);

        PlayerEntity player = playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));

        return playerMapper.convertToDTO(player);
    }

    @Override
    public List<PlayerDTO> getAllPlayers(HttpServletRequest request) {
        PlayerEntity tokenPlayer = getPlayerFromToken(request);

        if (tokenPlayer.getRole().equals(Role.ROLE_ADMIN))
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

    private void verifyEmailMatch(Long playerId, HttpServletRequest request) {
        PlayerEntity tokenPlayer = getPlayerFromToken(request);

        if (!tokenPlayer.getRole().equals(Role.ROLE_ADMIN)) {
            PlayerEntity player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new ServiceException("Player not found"));

            if (!player.getEmail().equals(tokenPlayer.getEmail()))
                throw new ServiceException("Access denied: You are trying to access a user that does not correspond to your credential.");

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
