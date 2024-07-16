package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players/{id}/games")
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    @Operation(summary = "Roll dices for a player", description = "Roll dices for a specific player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dices rolled successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<GameDTO> rollDices(@PathVariable Long id, HttpServletRequest request) {
        GameDTO rollDices = gameService.rollDices(id, request);

        return ResponseEntity.ok(rollDices);
    }

    @GetMapping
    @Operation(summary = "Get all rolls by player", description = "Retrieve all dice rolls for a specific player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rolls retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<List<GameDTO>> getRollsByPlayer(@PathVariable Long id, HttpServletRequest request) {
        List<GameDTO> rollsByPlayer = gameService.getRollsByPlayer(id, request);

        return ResponseEntity.ok(rollsByPlayer);
    }

    @DeleteMapping
    @Operation(summary = "Delete all rolls by player", description = "Delete all dice rolls for a specific player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rolls deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<Void> deleteRollsByPlayer(@PathVariable Long id, HttpServletRequest request) {
        boolean deleted = gameService.deleteRollsByPlayer(id, request);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

