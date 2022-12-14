package server.footballmanager.EntityModelAssemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import server.footballmanager.Controllers.TeamController;
import server.footballmanager.Entities.Team;

@Component
public class TeamModelAssembler implements RepresentationModelAssembler<Team, EntityModel<Team>> {

    @Override
    public EntityModel<Team> toModel(Team employee) {

        return EntityModel.of(employee, //
                linkTo(methodOn(TeamController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(TeamController.class).all()).withRel("teams"));
    }
}