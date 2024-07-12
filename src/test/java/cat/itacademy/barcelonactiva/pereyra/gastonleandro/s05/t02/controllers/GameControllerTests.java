package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controllers;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.game.GameController;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GameControllerTests {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Mock
    private Authentication authentication;

    private GameDTO gameDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        gameDTO = GameDTO.builder()
                .playerId(1L)
                .dice1(3)
                .dice2(4)
                .hasWon(true)
                .gameResult(7)
                .build();
    }

    @Test
    @DisplayName("Test Roll Dices - Success")
    public void testRollDices_Success() {
        when(gameService.rollDices(anyLong())).thenReturn(gameDTO);

        ResponseEntity<GameDTO> response = gameController.rollDices(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getPlayerId());
        assertEquals(7, response.getBody().getGameResult());
        assertEquals(true, response.getBody().isHasWon());
    }

    @Test
    @DisplayName("Test Get Rolls By Player - Success")
    public void testGetRollsByPlayer_Success() {
        when(gameService.getRollsByPlayer(anyLong())).thenReturn(Collections.singletonList(gameDTO));

        ResponseEntity<List<GameDTO>> response = gameController.getRollsByPlayer(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().get(0).getPlayerId());
        assertEquals(true, response.getBody().get(0).isHasWon());
    }

    @Test
    @DisplayName("Test Delete Rolls By Player - Success")
    public void testDeleteRollsByPlayer_Success() {
        when(gameService.deleteRollsByPlayer(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = gameController.deleteRollsByPlayer(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test Delete Rolls By Player - Not Found")
    public void testDeleteRollsByPlayer_NotFound() {
        when(gameService.deleteRollsByPlayer(anyLong())).thenReturn(false);

        ResponseEntity<Void> response = gameController.deleteRollsByPlayer(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
