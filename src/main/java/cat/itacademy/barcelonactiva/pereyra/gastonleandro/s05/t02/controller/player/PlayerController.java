package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.dao.response.AuthResponse;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/players")
@Slf4j
@Validated
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    @Operation(summary = "Register a new player", description = "Register a new player with a nickname and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PlayerDTO> registerPlayer(@Valid @RequestBody PlayerEntity player) {
        PlayerDTO authResponse = playerService.register(player);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a player", description = "Authenticate a player with a nickname and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<AuthResponse> loginPlayer(@Valid @RequestBody PlayerEntity player) {
        AuthResponse authResponse = playerService.login(player);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a player", description = "Invalidate the current token for the logged-in player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    public ResponseEntity<String> logoutPlayer(HttpServletRequest request) {
        playerService.logout(request);

        return ResponseEntity.ok("Successfully logged out");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update player details", description = "Update the details of an existing player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerEntity player, HttpServletRequest request) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, player, request);

        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a player", description = "Delete an existing player by ID - Only by ROLE ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable Long id, HttpServletRequest request) {
        PlayerDTO playerDeleted = playerService.deletePlayer(id, request);

        return ResponseEntity.ok(playerDeleted);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player details", description = "Retrieve the details of an existing player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id, HttpServletRequest request) {
        PlayerDTO playerDTO = playerService.getPlayerById(id, request);

        return ResponseEntity.ok(playerDTO);
    }

    @GetMapping
    @Operation(summary = "Get all players", description = "Retrieve a list of all players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players retrieved successfully")
    })
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(HttpServletRequest request) {
        List<PlayerDTO> players = playerService.getAllPlayers(request);

        return ResponseEntity.ok(players);
    }

    @GetMapping("/ranking")
    @Operation(summary = "Get average win rate", description = "Retrieve the average win rate of all players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average win rate retrieved successfully")
    })
    public ResponseEntity<Double> getAverageWinRate() {
        double averageWinRate = playerService.getAverageWinRate();

        return ResponseEntity.ok(averageWinRate);
    }

    @GetMapping("/ranking/loser")
    @Operation(summary = "Get player with lowest win rate", description = "Retrieve the player with the lowest win rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No players found")
    })
    public ResponseEntity<PlayerDTO> getPlayerWithLowestWinRate() {
        PlayerDTO playerWithLowestWinRate = playerService.getPlayerWithLowestWinRate();

        return ResponseEntity.ok(playerWithLowestWinRate);
    }

    @GetMapping("/ranking/winner")
    @Operation(summary = "Get player with highest win rate", description = "Retrieve the player with the highest win rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No players found")
    })
    public ResponseEntity<PlayerDTO> getPlayerWithHighestWinRate() {
        PlayerDTO playerWithHighestWinRate = playerService.getPlayerWithHighestWinRate();

        return ResponseEntity.ok(playerWithHighestWinRate);
    }
}
