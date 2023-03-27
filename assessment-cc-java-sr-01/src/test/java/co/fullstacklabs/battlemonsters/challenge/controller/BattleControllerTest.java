package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import java.util.Optional;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;


import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class BattleControllerTest {
    private static final String BATTLE_PATH = "/battle";
    private static final String START_BATTLE = "/startBattle";
    private static final String PATH_ID = "/{id}";

    private transient ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    public transient BattleRepository battleRepository;

    @Test
    void shouldFetchAllBattles() throws Exception {
        Battle battle1 = BattleTestBuilder.builder().id(1).build();
        Battle battle2 = BattleTestBuilder.builder().id(2).build();
        List<Battle> battleList = Lists.newArrayList(battle1, battle2);
        Mockito.when(battleRepository.findAll()).thenReturn(battleList);

        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)));
    }

    @Test
    void shouldFailBattleWithUndefinedMonster() throws Exception {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().build();
        MonsterDTO monsterB = MonsterDTO.builder().build();
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);
        this.mockMvc.perform(post(BATTLE_PATH + START_BATTLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(battleDTO))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("undefined monster")));
    }

    @Test
    void shouldFailBattleWithInexistentMonster() throws Exception {
        BattleDTO battleDTO = new BattleDTO();
        this.mockMvc.perform(post(BATTLE_PATH + START_BATTLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(battleDTO))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("non-exist monster")));
    }

    @Test
    void shouldInsertBattleWithMonsterAWinning() throws Exception {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().id(1).name("A").attack(100).defense(100).hp(100).speed(100).build();
        MonsterDTO monsterB = MonsterDTO.builder().id(2).name("B").attack(1).defense(1).hp(1).speed(1).build();
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);
        Battle battle1 = BattleTestBuilder.builder().build();
        Mockito.when(battleRepository.save(any(Battle.class))).thenReturn(battle1);
        this.mockMvc.perform(post(BATTLE_PATH + START_BATTLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(battleDTO))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.name", Is.is(monsterA.getName())));
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
        BattleDTO battleDTO = new BattleDTO();
        MonsterDTO monsterA = MonsterDTO.builder().id(1).name("A").attack(1).defense(1).hp(1).speed(1).build();
        MonsterDTO monsterB = MonsterDTO.builder().id(2).name("B").attack(100).defense(100).hp(100).speed(100).build();
        battleDTO.setMonsterA(monsterA);
        battleDTO.setMonsterB(monsterB);
        Battle battle1 = BattleTestBuilder.builder().build();
        Mockito.when(battleRepository.save(any(Battle.class))).thenReturn(battle1);
        this.mockMvc.perform(post(BATTLE_PATH + START_BATTLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(battleDTO))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.name", Is.is(monsterB.getName())));
    }

    @Test
    void shouldDeleteBattleSucessfully() throws Exception {
        int id = 4;
        Battle battle1 = BattleTestBuilder.builder().id(id).build();
        Mockito.when(battleRepository.findById(id)).thenReturn(Optional.of(battle1));
        this.mockMvc.perform(delete(BATTLE_PATH + PATH_ID, id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailDeletingInexistentBattle() throws Exception {
        int id = 4;
        this.mockMvc.perform(delete(BATTLE_PATH + PATH_ID, id))
                .andExpect(status().isNotFound());
    }
}