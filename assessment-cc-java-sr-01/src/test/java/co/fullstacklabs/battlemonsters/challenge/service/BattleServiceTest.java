package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {

    @InjectMocks
    public transient BattleServiceImpl battleService;

    @Mock
    public transient BattleRepository battleRepository;

    @Mock
    private transient ModelMapper mapper;

    @Test
    public void testGetAll() {

        Battle battle1 = BattleTestBuilder.builder().id(1).build();
        Battle battle2 = BattleTestBuilder.builder().id(2).build();

        List<Battle> battleList = Lists.newArrayList(battle1, battle2);
        Mockito.when(battleRepository.findAll()).thenReturn(battleList);

        battleService.getAll();

        Mockito.verify(battleRepository).findAll();
        Mockito.verify(mapper).map(battleList.get(0), BattleDTO.class);
        Mockito.verify(mapper).map(battleList.get(1), BattleDTO.class);
    }

    @Test
    void shouldFailBattleWithUndefinedMonster() {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().build();
        MonsterDTO monsterB = MonsterDTO.builder().build();
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);

        assertThrows(ResourceNotFoundException.class,() -> battleService.startBattle(battleDTO));

    }

    @Test
    void shouldFailBattleWithInexistentMonster() {
        BattleDTO battleDTO = new BattleDTO();

        assertThrows(ResourceNotFoundException.class,() -> battleService.startBattle(battleDTO));
    }

    @Test
    void shouldInsertBattleWithMonsterAWinning() {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().id(1).name("A").attack(100).defense(100).hp(100).speed(100).build();
        MonsterDTO monsterB = MonsterDTO.builder().id(2).name("B").attack(1).defense(1).hp(1).speed(1).build();
        battleDTO.setId(1);
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);
        Mockito.when(battleService.create(battleDTO)).thenReturn(battleDTO);
        BattleDTO startBattle = battleService.startBattle(battleDTO);

        assertEquals(monsterA.getId(), startBattle.getWinner().getId());
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().id(1).name("A").attack(1).defense(1).hp(1).speed(1).build();
        MonsterDTO monsterB = MonsterDTO.builder().id(2).name("B").attack(100).defense(100).hp(100).speed(100).build();
        battleDTO.setId(1);
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);
        Mockito.when(battleService.create(battleDTO)).thenReturn(battleDTO);
        BattleDTO startBattle = battleService.startBattle(battleDTO);

        assertEquals(monsterB.getId(), startBattle.getWinner().getId());
    }

    @Test
    void shouldDeleteBattleSucessfully() {
        int id = 1;
        Battle battle = BattleTestBuilder.builder().build();
        Mockito.when(battleRepository.findById(id)).thenReturn(Optional.of(battle));
        Mockito.doNothing().when(battleRepository).delete(battle);

        battleService.delete(id);

        Mockito.verify(battleRepository).findById(id );
        Mockito.verify(battleRepository).delete(battle);
    }

    @Test
    void shouldFailDeletingInexistentBattle() {
        int id = 1;
        Mockito.when(battleRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> battleService.delete(id));
    }



}
