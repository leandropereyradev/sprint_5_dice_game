package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    @Query("SELECT p FROM PlayerEntity p WHERE p.nickName = :nickName")
    Optional<PlayerEntity> findByNickName(@Param("nickName") String nickName);
}
