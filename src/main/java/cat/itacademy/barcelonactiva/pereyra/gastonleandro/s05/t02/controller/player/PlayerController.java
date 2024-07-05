package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.player.PlayerDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/players")
@Slf4j
@Validated
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerDTO> addPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO addedPlayer = playerService.addPlayer(playerDTO);

        return ResponseEntity.ok(addedPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, playerDTO);

        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        boolean isDeleted = playerService.deletePlayer(id);

        if (!isDeleted) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        PlayerDTO player = playerService.getPlayerById(id);

        return ResponseEntity.ok(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers();

        return ResponseEntity.ok(players);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Double> getAverageWinRate() {
        double averageWinRate = playerService.getAverageWinRate();

        return ResponseEntity.ok(averageWinRate);
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDTO> getPlayerWithLowestWinRate() {
        PlayerDTO playerWithLowestWinRate = playerService.getPlayerWithLowestWinRate();

        return ResponseEntity.ok(playerWithLowestWinRate);
    }

    @GetMapping("/ranking/winner")
    public ResponseEntity<PlayerDTO> getPlayerWithHighestWinRate() {
        PlayerDTO playerWithHighestWinRate = playerService.getPlayerWithHighestWinRate();

        return ResponseEntity.ok(playerWithHighestWinRate);
    }
}
