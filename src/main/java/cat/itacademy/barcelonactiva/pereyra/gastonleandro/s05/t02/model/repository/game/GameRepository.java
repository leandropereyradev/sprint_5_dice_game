package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.game;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.game.GameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<GameEntity, String> {
    List<GameEntity> findByPlayerId(Long playerId);

    void deleteByPlayerId(Long playerId);
}
