package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controllers;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.game.GameController;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game.GameService;
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
import static org.mockito.ArgumentMatchers.any;
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

        gameDTO = createGameDTO();
    }

    @Test
    @DisplayName("Test Roll Dices - Success")
    public void testRollDices_Success() {
        mockGameServiceMethod("rollDices", gameDTO);

        HttpServletRequest request = createMockHttpServletRequest();
        ResponseEntity<GameDTO> response = gameController.rollDices(1L, request);

        assertGameDTOResponse(response, 200, 1L, 7, true);
    }

    @Test
    @DisplayName("Test Get Rolls By Player - Success")
    public void testGetRollsByPlayer_Success() {
        mockGameServiceMethod("getRollsByPlayer", Collections.singletonList(gameDTO));

        HttpServletRequest request = createMockHttpServletRequest();
        ResponseEntity<List<GameDTO>> response = gameController.getRollsByPlayer(1L, request);

        assertGameDTOListResponse(response, 200, 1L, true);
    }

    @Test
    @DisplayName("Test Delete Rolls By Player - Success")
    public void testDeleteRollsByPlayer_Success() {
        mockGameServiceMethod("deleteRollsByPlayer", true);

        HttpServletRequest request = createMockHttpServletRequest();
        ResponseEntity<Void> response = gameController.deleteRollsByPlayer(1L, request);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test Delete Rolls By Player - Not Found")
    public void testDeleteRollsByPlayer_NotFound() {
        mockGameServiceMethod("deleteRollsByPlayer", false);

        HttpServletRequest request = createMockHttpServletRequest();
        ResponseEntity<Void> response = gameController.deleteRollsByPlayer(1L, request);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Métodos auxiliares

    private GameDTO createGameDTO() {
        return GameDTO.builder()
                .playerId(1L)
                .dice1(3)
                .dice2(4)
                .hasWon(true)
                .gameResult(7)
                .build();
    }

    private HttpServletRequest createMockHttpServletRequest() {
        return Mockito.mock(HttpServletRequest.class);
    }

    private void mockGameServiceMethod(String methodName, Object returnValue) {
        switch (methodName) {
            case "rollDices":
                when(gameService.rollDices(anyLong(), any(HttpServletRequest.class))).thenReturn((GameDTO) returnValue);
                break;
            case "getRollsByPlayer":
                when(gameService.getRollsByPlayer(anyLong(), any(HttpServletRequest.class))).thenReturn((List<GameDTO>) returnValue);
                break;
            case "deleteRollsByPlayer":
                when(gameService.deleteRollsByPlayer(anyLong(), any(HttpServletRequest.class))).thenReturn((Boolean) returnValue);
                break;
        }
    }

    private void assertGameDTOResponse(ResponseEntity<GameDTO> response, int expectedStatus, long expectedPlayerId, int expectedGameResult, boolean expectedHasWon) {
        assertEquals(expectedStatus, response.getStatusCodeValue());
        assertEquals(expectedPlayerId, response.getBody().getPlayerId());
        assertEquals(expectedGameResult, response.getBody().getGameResult());
        assertEquals(expectedHasWon, response.getBody().isHasWon());
    }

    private void assertGameDTOListResponse(ResponseEntity<List<GameDTO>> response, int expectedStatus, long expectedPlayerId, boolean expectedHasWon) {
        assertEquals(expectedStatus, response.getStatusCodeValue());
        assertEquals(expectedPlayerId, response.getBody().get(0).getPlayerId());
        assertEquals(expectedHasWon, response.getBody().get(0).isHasWon());
    }
}
