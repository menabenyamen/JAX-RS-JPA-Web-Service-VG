package se.mebe.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.stereotype.Component;
import se.mebe.converter.EntityConverter;
import se.mebe.exception.BadMessageException;
import se.mebe.exception.ResourceException;
import se.springdata.exception.ServiceException;
import se.springdata.repository.TeamRepository;
import se.springdata.repository.UserRepository;
import se.springdata.service.TeamService;

@Component
@Path("/teams")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })

public final class TeamResource {

	private final TeamService teamService;
	private final EntityConverter entityConverter;
	private final TeamRepository teamRepository;
	private final UserRepository userRepository;
	private Collection<se.springdata.model.Team> teams;
	private List<se.mebe.model.Team> teamsJson;
	private se.springdata.model.Team newTeam;
	private se.mebe.model.Team allTeams;
	private URI location;

	@Context
	private UriInfo uriInfo;
	@Context
	private HttpHeaders headers;

	public TeamResource(TeamService teamService, EntityConverter entityConverter, TeamRepository teamRepository,
			UserRepository userRepository) {
		this.teamService = teamService;
		this.entityConverter = entityConverter;
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.teamsJson = new ArrayList<>();
	}

	@POST
	public Response addTeam(se.mebe.model.Team team) throws ServiceException {

		newTeam = teamService.addTeam(EntityConverter.convertToJpaTeam(team));
		location = uriInfo.getAbsolutePathBuilder().path(newTeam.getId().toString()).build();

		return Response.created(location).build();
	}

	@PUT
	@Path("update/{teamName}/{teamId}")
	public Response updateTeam(@PathParam("teamName") String teamName, @PathParam("teamId") long teamId)
			throws ServiceException, BadMessageException {

		if (!teamRepository.exists(teamId)) {
			throw new ResourceException("This team dosnt exist for update");

		} else {
			teamService.updateTeam(teamName, teamId);
			return Response.noContent().build();
		}
	}

	@PUT
	@Path("state/{id}/{status}")
	public Response inactiveTeam(@PathParam("id") long teamId, @PathParam("status") String status)
			throws ServiceException, BadMessageException {
		if (!teamRepository.exists(teamId)) {
			throw new BadMessageException("This team dosnt exist for update");

		} else if (teamRepository.findTeamStatusById(teamId).equals(status)) {
			throw new BadMessageException("This team is already have this status");

		} else {

			teamService.setTeamStatus(teamId, status);
			return Response.noContent().build();
		}
	}

	@GET
	@Path("/all")
	public Response findAllTeams() throws ServiceException, BadMessageException {
		if (teamRepository.findAllTeams().isEmpty()) {
			throw new BadMessageException("There is not any team in your table");

		} else {

			teams = teamService.getAllTeams();
			teams.forEach(team -> {

				allTeams = entityConverter.convertToRestTeam(team);
				teamsJson.add(allTeams);
			});

			return Response.ok(teamsJson, headers.getMediaType()).build();
		}
	}

	@PUT
	@Path("/asign/{teamId}/{userId}")
	public Response asignUserToTeam(@PathParam("teamId") long teamId, @PathParam("userId") long userId)
			throws ServiceException, BadMessageException {
		if (!userRepository.exists(userId)) {
			throw new ResourceException("This user dosnt exist to assign it to team");

		} else if (!teamRepository.exists(teamId)) {
			throw new ResourceException("This team do not exist to assign user to !!");

		} else if (teamRepository.findAllUserForTeamById(teamId).size() == 10) {
			throw new BadMessageException("This team has 10 member no more member allwoed");

		} else if (userRepository.findTeamIdByUserId(userId) != null) {
			throw new BadMessageException("This user is already a member !!");

		} else {

			teamService.addUserToTeam(teamId, userId);
			return Response.noContent().build();
		}
	}
}
