package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.controller.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.dto.game.GameDTO;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.service.game.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GameDTO> rollDices(@PathVariable Long id) {
        GameDTO rollDices = gameService.rollDices(id);

        return ResponseEntity.ok(rollDices);
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getRollsByPlayer(@PathVariable Long id) {
        List<GameDTO> rollsByPlayer = gameService.getRollsByPlayer(id);

        return ResponseEntity.ok(rollsByPlayer);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRollsByPlayer(@PathVariable Long id) {
        boolean isDeleted = gameService.deleteRollsByPlayer(id);

        if (!isDeleted) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.noContent().build();
    }
}

