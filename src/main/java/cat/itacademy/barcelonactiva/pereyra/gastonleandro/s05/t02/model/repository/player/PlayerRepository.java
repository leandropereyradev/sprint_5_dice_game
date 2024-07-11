package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByNickName(String nickName);

    Optional<PlayerEntity> findByEmail(String email);
}
