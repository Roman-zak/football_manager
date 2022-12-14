package server.footballmanager.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.footballmanager.Entities.Player;
import server.footballmanager.Entities.Team;
import server.footballmanager.Exceptions.NotEnoughMoneyException;
import server.footballmanager.Exceptions.PlayerNotFoundException;
import server.footballmanager.Repos.PlayerRepository;
import server.footballmanager.Repos.TeamRepository;

import java.time.LocalDate;

@RestController
public class TransferController {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public TransferController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @PostMapping("/transfer")
    public HttpStatus transferPlayer(@RequestParam long player_id, @RequestParam long newTeam_id) {
        System.out.println("new team id "+newTeam_id);
        Player player = playerRepository.findById(player_id).orElseThrow(()-> new PlayerNotFoundException(player_id));
        Team newTeam = teamRepository.findById(newTeam_id).orElseThrow(()-> new PlayerNotFoundException(newTeam_id));
        Team oldTeam = player.getTeam();
        System.out.println("before");
        System.out.println("player`s team: "+player.getTeam());
       // System.out.println(oldTeam);
        System.out.println("new team: "+newTeam);

        double transferSum = player.getMonthExperience()*100000 / getYears(player.getYearOfBirth());
        double commissionSum = oldTeam.getCommission()*transferSum;
        double fullSum = transferSum+commissionSum;
        if(newTeam.getAccountSum()<fullSum){
            throw new NotEnoughMoneyException(newTeam_id);
        }
        newTeam.setAccountSum(newTeam.getAccountSum()-fullSum);
        oldTeam.setAccountSum(oldTeam.getAccountSum()+fullSum);
        player.setTeam(newTeam);
        newTeam.getPlayers().add(player);
        oldTeam.getPlayers().remove(player);
        System.out.println("after");
        System.out.println("player`s new team: "+newTeam);
        System.out.println("old team: "+oldTeam);
        playerRepository.save(player);
        teamRepository.save(oldTeam);
        teamRepository.save(newTeam);
        return HttpStatus.OK;
    }
    private int getYears(int yearOfBirth){
        return LocalDate.now().getYear() - yearOfBirth;
    }
}
