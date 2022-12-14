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
import server.footballmanager.Entities.Team;
import server.footballmanager.EntityModelAssemblers.PlayerModelAssembler;
import server.footballmanager.Exceptions.PlayerNotFoundException;
import server.footballmanager.Entities.Player;
import server.footballmanager.Repos.PlayerRepository;
import server.footballmanager.Repos.TeamRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PlayerController {
    private final PlayerRepository repository;
    private final PlayerModelAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerModelAssembler assembler) {
        this.repository = repository;
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
    public Player newPlayer(@Valid @RequestBody Player newPlayer) {
        return repository.save(newPlayer);
    }

//    @PostMapping("/players/transfer")
//    public boolean transferPlayer(@RequestParam Long player_id, @RequestParam Long newTeam_id) {
//        boolean result = false;
//        Player player = repository.findById(player_id).orElseThrow(()-> new PlayerNotFoundException(player_id));
//
//        // Team newTeam = TeamRepository.findById(player_id).orElseThrow(()-> new PlayerNotFoundException(player_id));
//        return result;
//    }

    // Single item

    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable Long id) {

        Player player = repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        return assembler.toModel(player);
    }

    @PutMapping("/players/{id}")
    public Player replacePlayer(@Valid @RequestBody Player newPlayer,@PathVariable Long id) {

        return repository.findById(id)
                .map(player -> {
                    player.setName(newPlayer.getName());
                    player.setSurname(newPlayer.getSurname());
                    player.setMonthExperience(newPlayer.getMonthExperience());
                    player.setYearOfBirth(newPlayer.getYearOfBirth());
                    player.setTeam(newPlayer.getTeam());
                    return repository.save(player);
                })
                .orElseGet(() -> {
                    newPlayer.setId(id);
                    return repository.save(newPlayer);
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
