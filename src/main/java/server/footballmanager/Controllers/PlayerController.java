package server.footballmanager.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import server.footballmanager.DTO.PlayerDTO;
import server.footballmanager.Entities.Team;
import server.footballmanager.EntityModelAssemblers.PlayerModelAssembler;
import server.footballmanager.Exceptions.PlayerNotFoundException;
import server.footballmanager.Entities.Player;
import server.footballmanager.Exceptions.TeamNotFoundException;
import server.footballmanager.Repos.PlayerRepository;
import server.footballmanager.Repos.TeamRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PlayerController {
    private final PlayerRepository repository;
    private final TeamRepository teamRepository;
    private final PlayerModelAssembler assembler;

    PlayerController(PlayerRepository repository, TeamRepository teamRepository, PlayerModelAssembler assembler) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.assembler = assembler;
    }
    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/players")
    public CollectionModel<EntityModel<Player>> all() {

        List<EntityModel<Player>> players = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/players")
    public Player newPlayer(/*@Valid*/ @RequestBody PlayerDTO newPlayerDTO) {
        System.out.println(newPlayerDTO);
        Team team = teamRepository.findById(newPlayerDTO.getTeam_id()).orElseThrow(() -> new TeamNotFoundException(newPlayerDTO.getTeam_id()));
        Player player = new Player(
                newPlayerDTO.getName(),
                newPlayerDTO.getSurname(),
                newPlayerDTO.getMonthExperience(),
                newPlayerDTO.getYearOfBirth(),
                team
        );

        return repository.save(player);
    }

    // Single item

    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable Long id) {

        Player player = repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        return assembler.toModel(player);
    }

    @PutMapping("/players/{id}")
    public Player replacePlayer(@Valid @RequestBody PlayerDTO newPlayer,@PathVariable Long id) {

        return repository.findById(id)
                .map(player -> {
                    player.setName(newPlayer.getName());
                    player.setSurname(newPlayer.getSurname());
                    player.setMonthExperience(newPlayer.getMonthExperience());
                    player.setYearOfBirth(newPlayer.getYearOfBirth());
                    player.setTeam(teamRepository.findById(newPlayer.getTeam_id())
                            .orElseThrow(()->new TeamNotFoundException(newPlayer.getTeam_id())));
                    return repository.save(player);
                })
                .orElseGet(() -> {
                    Player player = new Player(newPlayer.getName(),
                            newPlayer.getSurname(),
                            newPlayer.getMonthExperience(),
                            newPlayer.getYearOfBirth(),
                            teamRepository.findById(newPlayer.getTeam_id())
                                    .orElseThrow(()->new TeamNotFoundException(newPlayer.getTeam_id())));
                    player.setId(id);
                    return repository.save(player);
                });
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
