package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class MonsterControllerTest {
    private static final String MONSTER_PATH = "/monster";
    private static final String PATH_ID = "/{id}";
    private static final String MONSTER_1_NAME = "Monster 1";
    private static final String URL_NAME = "imageUrl1";

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    public transient MonsterRepository monsterRepository;

    @Test
    void shouldFetchAllMonsters() throws Exception {
        Monster monster1 = MonsterTestBuilder.builder().id(1)
                .name(MONSTER_1_NAME).attack(50).defense(40).hp(30).speed(25)
                .imageURL(URL_NAME).build();

        Monster monster2 = MonsterTestBuilder.builder().id(2)
                .name("Monster 2").attack(30).defense(20).hp(21).speed(15)
                .imageURL(URL_NAME).build();

        List<Monster> monsterList = Arrays.asList(monster1, monster2);
        Mockito.when(monsterRepository.findAll()).thenReturn(monsterList);
        this.mockMvc.perform(get(MONSTER_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].name", Is.is(MONSTER_1_NAME)))
                .andExpect(jsonPath("$[0].attack", Is.is(50)))
                .andExpect(jsonPath("$[0].defense", Is.is(40)))
                .andExpect(jsonPath("$[0].hp", Is.is(30)))
                .andExpect(jsonPath("$[0].speed", Is.is(25)));

    }

    @Test
    void shouldGetMonsterSuccessfully() throws Exception {
        Integer id = 1;
        Monster monster1 = MonsterTestBuilder.builder().id(1)
                .name(MONSTER_1_NAME).attack(50).defense(40).hp(30).speed(25)
                .imageURL(URL_NAME).build();

        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        this.mockMvc.perform(get(MONSTER_PATH + PATH_ID, id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(MONSTER_1_NAME)));
    }

    @Test
    void shoulGetMonsterNotExists() throws Exception {
        long id = 3L;
        this.mockMvc.perform(get(MONSTER_PATH + PATH_ID, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMonsterSuccessfully() throws Exception {
        int id = 4;
        Monster monster1 = MonsterTestBuilder.builder().id(id)
                .name(MONSTER_1_NAME).attack(50).defense(40).hp(30).speed(25)
                .imageURL(URL_NAME).build();

        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        this.mockMvc.perform(delete(MONSTER_PATH + PATH_ID, id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMonsterNotFound() throws Exception {
        int id = 5;

        this.mockMvc.perform(delete(MONSTER_PATH + PATH_ID, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testImportCsvSucessfully() {
        //TOOD: Implement take as a sample data/monstere-correct.csv
        assertEquals(1, 1);
    }

    @Test
    void testImportCsvInexistenctColumns() {
        //TOOD: Implement take as a sample data/monsters-wrong-column.csv
        assertEquals(1, 1);
    }

    @Test
    void testImportCsvInexistenctMonster () {
        //TOOD: Implement take as a sample data/monsters-empty-monster.csv
        assertEquals(1, 1);
    }
}
