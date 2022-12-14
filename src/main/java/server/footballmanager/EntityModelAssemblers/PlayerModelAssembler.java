package server.footballmanager.EntityModelAssemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import server.footballmanager.Controllers.PlayerController;
import server.footballmanager.Entities.Player;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {

    @Override
    public EntityModel<Player> toModel(Player employee) {

        return EntityModel.of(employee, //
                linkTo(methodOn(PlayerController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("players"));
    }
}