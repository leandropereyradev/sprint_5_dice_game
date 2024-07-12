package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controllers;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.player.PlayerController;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.auth.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PlayerControllerTests {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Mock
    private Authentication authentication;

    private PlayerDTO playerDTO;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        playerDTO = PlayerDTO.builder()
                .nickName("Angelo")
                .email("angelo@test.com")
                .password("password")
                .build();

        authResponse = new AuthResponse("secretToken");
    }

    @Test
    @DisplayName("Test Register Player - Success")
    public void testRegisterPlayer_Success() {
        when(playerService.register(any(PlayerDTO.class))).thenReturn(playerDTO);

        ResponseEntity<PlayerDTO> response = playerController.registerPlayer(playerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().getNickName());
    }

    @Test
    @DisplayName("Test Login Player - Success")
    public void testLoginPlayer_Success() {
        when(playerService.login(any(PlayerDTO.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = playerController.loginPlayer(playerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("secretToken", response.getBody().getToken());
    }

    @Test
    @DisplayName("Test Logout Player - Success")
    public void testLogoutPlayer_Success() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.doNothing().when(playerService).logout(any(HttpServletRequest.class));

        ResponseEntity<String> response = playerController.logoutPlayer(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successfully logged out", response.getBody());
    }

    @Test
    @DisplayName("Test Update Player - Success")
    public void testUpdatePlayer_Success() {
        when(playerService.updatePlayer(anyLong(), any(PlayerDTO.class), any(HttpServletRequest.class))).thenReturn(playerDTO);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        ResponseEntity<PlayerDTO> response = playerController.updatePlayer(1L, playerDTO, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().getNickName());
    }

    @Test
    @DisplayName("Test Delete Player - Success")
    public void testDeletePlayer_Success() {
        when(playerService.deletePlayer(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = playerController.deletePlayer(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test Delete Player - Not Found")
    public void testDeletePlayer_NotFound() {
        when(playerService.deletePlayer(anyLong())).thenReturn(false);

        ResponseEntity<Void> response = playerController.deletePlayer(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test Get Player By Id - Success")
    public void testGetPlayerById_Success() {
        when(playerService.getPlayerById(anyLong(), any(HttpServletRequest.class))).thenReturn(playerDTO);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        ResponseEntity<PlayerDTO> response = playerController.getPlayerById(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().getNickName());
    }

    @Test
    @DisplayName("Test Get All Players - Success")
    public void testGetAllPlayers_Success() {
        when(playerService.getAllPlayers(any(HttpServletRequest.class))).thenReturn(Collections.singletonList(playerDTO));

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        ResponseEntity<List<PlayerDTO>> response = playerController.getAllPlayers(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().get(0).getNickName());
    }

    @Test
    @DisplayName("Test Get Player With Lowest Win Rate - Success")
    public void testGetPlayerWithLowestWinRate_Success() {
        when(playerService.getPlayerWithLowestWinRate()).thenReturn(playerDTO);

        ResponseEntity<PlayerDTO> response = playerController.getPlayerWithLowestWinRate();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().getNickName());
    }

    @Test
    @DisplayName("Test Get Player With Highest Win Rate - Success")
    public void testGetPlayerWithHighestWinRate_Success() {
        when(playerService.getPlayerWithHighestWinRate()).thenReturn(playerDTO);

        ResponseEntity<PlayerDTO> response = playerController.getPlayerWithHighestWinRate();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Angelo", response.getBody().getNickName());
    }
}