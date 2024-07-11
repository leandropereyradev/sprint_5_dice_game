package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.repository;

import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.PlayerEntity;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.domain.player.Role;
import cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.model.repository.player.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
class PlayerRepositoryTests {

    @MockBean
    private PlayerRepository playerRepository;

    private PlayerEntity testPlayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testPlayer = createTestPlayer("Angelo", "angelo@example.com", "password", Role.ROLE_USER);
    }

    private PlayerEntity createTestPlayer(String nickName, String email, String password, Role role) {
        return PlayerEntity.builder()
                .nickName(nickName)
                .email(email)
                .password(password)
                .role(role)
                .registrationDate(new Date())
                .build();
    }

    @Test
    @DisplayName("Test findByNickName with existing nickname returns player")
    public void PlayerRepository_FindByNickName_ReturnsPlayerNickName() {
        String name = "Angelo";

        when(playerRepository.findByNickName(name)).thenReturn(Optional.of(testPlayer));
        Optional<PlayerEntity> foundPlayerOpt = playerRepository.findByNickName(name);

        assertEquals(name, foundPlayerOpt.orElseThrow().getNickName());
    }

    @Test
    @DisplayName("Test findByNickName with non-existing nickname returns empty optional")
    public void PlayerRepository_FindByNickName_NonExistingName_ReturnsEmptyOptional() {
        when(playerRepository.findByNickName("Angelo")).thenReturn(Optional.empty());

        Optional<PlayerEntity> foundPlayerOpt = playerRepository.findByNickName("Other name");

        assertFalse(foundPlayerOpt.isPresent());
    }

    @Test
    @DisplayName("Test findByEmail with existing email returns player")
    public void PlayerRepository_FindByEmail_ReturnsPlayerEmail() {
        String email = "angelo@example.com";

        when(playerRepository.findByEmail(email)).thenReturn(Optional.of(testPlayer));
        Optional<PlayerEntity> foundPlayerOpt = playerRepository.findByEmail(email);

        assertEquals(email, foundPlayerOpt.orElseThrow().getEmail());
    }
}
