package co.fullstacklabs.battlemonsters.challenge.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@RestController
@RequestMapping("/battle")
public class BattleController implements Serializable {
    public static final long serialVersionUID = 4328744;
    private transient BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping
    public List<BattleDTO> getAll() {
        return battleService.getAll();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int battleId) {
        battleService.delete(battleId);
    }

    @PostMapping(value = "/startBattle")
    public BattleDTO startBattle(@RequestBody BattleDTO battleDTO) {
        return battleService.startBattle(battleDTO);
    }

}
