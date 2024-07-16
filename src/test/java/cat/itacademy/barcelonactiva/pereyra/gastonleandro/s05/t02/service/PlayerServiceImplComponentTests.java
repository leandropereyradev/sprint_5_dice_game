package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.service;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.enums.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.dao.response.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.security.JwtService;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.mapper.PlayerMapper;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player.PlayerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerServiceImplComponentTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private PlayerEntity player;
    private PlayerEntity adminPlayer;
    private PlayerDTO playerDTO;
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        player = createPlayer(
                1L,
                "Angelo",
                "angelo@example.com",
                "password",
                Role.ROLE_USER
        );

        adminPlayer = createPlayer(
                2L,
                "Admin User",
                "admin@example.com",
                "adminpassword",
                Role.ROLE_ADMIN
        );

        playerDTO = createPlayerDTO(
                "Angelo",
                "angelo@example.com"
        );
    }

    private PlayerEntity createPlayer(Long id, String nickName, String email, String password, Role role) {
        return PlayerEntity.builder()
                .id(id)
                .nickName(nickName)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    private PlayerDTO createPlayerDTO(String nickName, String email) {
        return PlayerDTO.builder()
                .nickName(nickName)
                .email(email)
                .build();
    }

    private void setupCommonMocks() {
        when(playerMapper.convertToDTO(any(PlayerEntity.class))).thenReturn(playerDTO);
    }

    private void setupAuthMocks(String token, String email, PlayerEntity playerEntity) {
        request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getEmailFromToken(token)).thenReturn(email);
        when(playerRepository.findByEmail(email)).thenReturn(Optional.of(playerEntity));
    }

    @Test
    @DisplayName("Test Register Player - Success")
    public void testRegisterPlayer_Success() {
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(player);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        setupCommonMocks();

        PlayerDTO result = playerService.register(player);

        assertEquals("Angelo", result.getNickName());
    }

    @Test
    @DisplayName("Test Login Player - Success")
    public void testLoginPlayer_Success() {
        when(playerRepository.findByEmail(anyString())).thenReturn(Optional.of(player));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("angelo@example.com");
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("authToken");

        AuthResponse result = playerService.login(player);

        assertTrue(result.getToken() != null && !result.getToken().isEmpty());
        assertEquals("authToken", result.getToken());
    }

    @Test
    @DisplayName("Test Update Player - Success")
    public void testUpdatePlayer_Success() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(playerRepository.existsById(anyLong())).thenReturn(true);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(player);
        setupCommonMocks();

        setupAuthMocks("authToken", "angelo@example.com", player);

        PlayerDTO result = playerService.updatePlayer(1L, player, request);

        assertEquals("Angelo", result.getNickName());
    }

    @Test
    @DisplayName("Test Delete Player - Success")
    public void testDeletePlayer_Success() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(playerRepository.existsById(anyLong())).thenReturn(true);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(player);
        setupCommonMocks();
        setupAuthMocks("dmin-token", "admin@example.com", adminPlayer);

        PlayerDTO result = playerService.deletePlayer(1L, request);

        assertEquals("Angelo", result.getNickName());
    }

    @Test
    @DisplayName("Test Get Player By Id - Success")
    public void testGetPlayerById_Success() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        setupCommonMocks();
        setupAuthMocks("authToken", "angelo@example.com", player);

        PlayerDTO result = playerService.getPlayerById(1L, request);

        assertEquals("Angelo", result.getNickName());
    }

    @Test
    @DisplayName("Test Get All Players - As Admin - Success")
    public void testGetAllPlayers_AsAdmin_Success() {
        setupAuthMocks("admin-token", "admin@example.com", adminPlayer);
        when(playerRepository.findAll()).thenReturn(List.of(player, adminPlayer));
        setupCommonMocks();

        List<PlayerDTO> result = playerService.getAllPlayers(request);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test Get All Players - As User - Success")
    public void testGetAllPlayers_AsUser_Success() {
        setupAuthMocks("user-token", "angelo@example.com", player);
        when(playerRepository.findAll()).thenReturn(List.of(player, adminPlayer));
        setupCommonMocks();

        List<PlayerDTO> result = playerService.getAllPlayers(request);

        assertEquals(1, result.size());
        assertEquals("Angelo", result.get(0).getNickName());
    }
}
