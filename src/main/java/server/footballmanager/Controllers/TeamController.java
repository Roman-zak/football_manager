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
import server.footballmanager.Entities.Player;
import server.footballmanager.EntityModelAssemblers.TeamModelAssembler;
import server.footballmanager.Exceptions.PlayerNotFoundException;
import server.footballmanager.Exceptions.TeamNotFoundException;
import server.footballmanager.Entities.Team;
import server.footballmanager.Repos.TeamRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TeamController {
    private final TeamRepository repository;
    private final TeamModelAssembler assembler;
    TeamController(TeamRepository repository, TeamModelAssembler assembler)
    {
        this.repository = repository;
        this.assembler = assembler;
    }
    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/teams")
    public CollectionModel<EntityModel<Team>> all() {
        List<EntityModel<Team>> teams = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(teams, linkTo(methodOn(TeamController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/teams")
    public Team newTeam(@Valid @RequestBody Team newTeam) {
        return repository.save(newTeam);
    }

    // Single item

    @GetMapping("/teams/{id}")
    public EntityModel<Team> one(@PathVariable Long id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        return assembler.toModel(team);
    }

    @PutMapping("/teams/{id}")
    public Team replaceTeam(@Valid @RequestBody Team newTeam, @PathVariable Long id) {

        return repository.findById(id)
                .map(team -> {
                    team.setName(newTeam.getName());
                    team.setAccountSum(newTeam.getAccountSum());
                    team.setCommission(newTeam.getCommission());
                    team.setPlayers(newTeam.getPlayers());
                    return repository.save(team);
                })
                .orElseGet(() -> {
                    newTeam.setId(id);
                    return repository.save(newTeam);
                });
    }

    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Long id) {
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
