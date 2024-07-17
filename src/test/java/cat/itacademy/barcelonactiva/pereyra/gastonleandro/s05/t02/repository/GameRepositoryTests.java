package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.repository;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.game.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepository;

    private GameEntity testGame;

    @BeforeEach
    void setUp() {
        testGame = createTestGame(1L, 3, 4, true, 7);
        gameRepository.deleteAll();
    }

    private GameEntity createTestGame(Long playerId, int dice1, int dice2, boolean hasWon, int gameResult) {
        return GameEntity.builder()
                .playerId(playerId)
                .dice1(dice1)
                .dice2(dice2)
                .hasWon(hasWon)
                .gameResult(gameResult)
                .gameRollDate(new Date())
                .build();
    }

    @Test
    @DisplayName("Test findByPlayerId with existing playerId returns games")
    public void GameRepository_FindByPlayerId_ReturnsGames() {
        gameRepository.save(testGame);

        List<GameEntity> foundGames = gameRepository.findByPlayerId(1L);

        assertFalse(foundGames.isEmpty());
        assertEquals(1L, foundGames.get(0).getPlayerId());
    }

    @Test
    @DisplayName("Test findByPlayerId with non-existing playerId returns empty list")
    public void GameRepository_FindByPlayerId_NonExistingPlayerId_ReturnsEmptyList() {
        List<GameEntity> foundGames = gameRepository.findByPlayerId(2L);

        assertTrue(foundGames.isEmpty());
    }

    @Test
    @DisplayName("Test deleteByPlayerId with existing playerId deletes games")
    public void GameRepository_DeleteByPlayerId_DeletesGames() {
        gameRepository.save(testGame);

        gameRepository.deleteByPlayerId(1L);
        List<GameEntity> foundGames = gameRepository.findByPlayerId(1L);
        assertTrue(foundGames.isEmpty());
    }
}
