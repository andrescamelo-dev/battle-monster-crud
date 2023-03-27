package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.io.Serializable;

import java.util.*;
import java.util.stream.Collectors;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class BattleServiceImpl implements BattleService, Serializable {
    public static final long serialVersionUID = 4328944;
    private transient BattleRepository battleRepository;
    private transient ModelMapper modelMapper;
    private static final String ATTACKER = "ATTACKER";
    private static final String ATTACKED = "ATTACKED";


    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository, ModelMapper modelMapper) {
        this.battleRepository = battleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * List all existence battles
     */
    @Override
    public List<BattleDTO> getAll() {
        List<Battle> battles = battleRepository.findAll();
        return battles.stream().map(battle -> modelMapper.map(battle, BattleDTO.class))
                .collect(Collectors.toList());
    }

    private Battle findBattleById(int id) {
        return battleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Battle not found"));
    }


    @Override
    public void delete(Integer id) {
        Battle monster = findBattleById(id);
        battleRepository.delete(monster);
    }

    @Override
    public BattleDTO create(BattleDTO battleDTO) {
        Battle battle = modelMapper.map(battleDTO, Battle.class);
        battle = battleRepository.save(battle);
        return modelMapper.map(battle, BattleDTO.class);
    }

    @Override
    public BattleDTO startBattle(BattleDTO battleDTO) {

        Optional.ofNullable(battleDTO).
                filter(b -> !Objects.isNull(b.getMonsterA())  && !Objects.isNull(b.getMonsterB()) ).
                orElseThrow(() -> new ResourceNotFoundException("non-exist monster"));
        Optional.of(battleDTO).
                filter(b -> !Objects.isNull(b.getMonsterA().getId())  && !Objects.isNull(b.getMonsterB().getId()) )
                .orElseThrow(() -> new ResourceNotFoundException("undefined monster"));

        Map<String, MonsterDTO> monsters = getMonsterAttackerAndAttacked(battleDTO);
        MonsterDTO attacker = monsters.get(ATTACKER);
        MonsterDTO attacked = monsters.get(ATTACKED);

        int damage = calculateDamage(attacker.getAttack(), attacked.getDefense());
        int hp = subtractDamage(attacked.getHp(),damage);

        processesResults(battleDTO,attacked.getId(),hp,attacker);

        return battleDTO;
    }

    private Map<String,MonsterDTO> getMonsterAttackerAndAttacked(BattleDTO battleDTO){
        Map<String,MonsterDTO> result = new HashMap<>();
        MonsterDTO monsterA = battleDTO.getMonsterA();
        MonsterDTO monsterB = battleDTO.getMonsterB();

        if (monsterA.getSpeed() > monsterB.getSpeed()){
            result.put(ATTACKER,monsterA);
            result.put(ATTACKED,monsterB);
        } else if (monsterB.getSpeed() > monsterA.getSpeed()){
            result.put(ATTACKER,monsterB);
            result.put(ATTACKED,monsterA);
        }  else if (monsterA.getSpeed().equals(monsterB.getSpeed())){
            if (monsterA.getAttack() > monsterB.getAttack()){
                result.put(ATTACKER,monsterA);
                result.put(ATTACKED,monsterB);
            } else if (monsterB.getAttack() > monsterA.getAttack()) {
                result.put(ATTACKER, monsterB);
                result.put(ATTACKED, monsterA);
            }
        }
        return  result;
    }

    private int calculateDamage(int attack, int defense){
        if (attack <= defense){
            return 1;
        }else {
            return attack - defense;
        }
    }

    private int subtractDamage(int hp, int damage){
        return hp - damage;
    }

    private void processesResults(BattleDTO battleDTO, int attackedId, int hpDamage, MonsterDTO attacker) {

        if (battleDTO.getMonsterA().getId().equals(attackedId)){
            battleDTO.getMonsterA().setHp(hpDamage);
        } else {
            battleDTO.getMonsterB().setHp(hpDamage);
        }

        if (hpDamage <= 0){
            battleDTO.setWinner(attacker);
            BattleDTO battleSaved = create(battleDTO);
            battleDTO.setId(battleSaved.getId());
        }
    }

}
