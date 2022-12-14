package server.footballmanager;

import server.footballmanager.Entities.Player;
import server.footballmanager.Entities.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import server.footballmanager.Repos.PlayerRepository;
import server.footballmanager.Repos.TeamRepository;

@SpringBootApplication
public class FootballManagerApplication {
    private static final Logger log = LoggerFactory.getLogger(FootballManagerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(FootballManagerApplication.class, args);
    }
    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository, PlayerRepository playerRepository) {

        return args -> {
            Team team1 = new Team("Team1", 100000, 0.05);
            Team team2 = new Team("Team2", 100000, 0.08);
            log.info("Preloading " + teamRepository.save(team1));
            log.info("Preloading " + teamRepository.save(team2));
            log.info("Preloading " + playerRepository.save(new Player("Name","Surname", 15, 2000, team1)));
        };
    }
}
