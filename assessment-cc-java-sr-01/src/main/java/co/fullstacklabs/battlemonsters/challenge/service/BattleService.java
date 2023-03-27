package co.fullstacklabs.battlemonsters.challenge.service;

import java.io.Serializable;
import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface BattleService extends Serializable{
    
    List<BattleDTO> getAll();

    void delete(Integer id);

    BattleDTO startBattle(BattleDTO battleDTO);

    BattleDTO create(BattleDTO battleDTO);

}
