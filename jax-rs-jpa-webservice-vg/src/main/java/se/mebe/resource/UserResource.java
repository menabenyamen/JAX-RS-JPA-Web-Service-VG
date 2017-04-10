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
import com.fasterxml.jackson.core.JsonProcessingException;
import se.mebe.converter.EntityConverter;
import se.mebe.exception.BadMessageException;
import se.mebe.exception.ResourceException;
import se.springdata.exception.ServiceException;
import se.springdata.repository.UserRepository;
import se.springdata.service.UserService;

@Component
@Path("users")

@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })

public final class UserResource {

	private final UserService userService;
	private final UserRepository userRepository;
	private final EntityConverter entityConverter;
	private final List<se.mebe.model.User> usersJson;
	private final List<se.mebe.model.User> infoJson;
	private Collection<se.springdata.model.User> users;
	private URI location;
	private se.springdata.model.User newUser;
	private se.springdata.model.User user;
	private se.mebe.model.User userToRest;

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpHeaders headers;

	public UserResource(UserService userService, EntityConverter entityConverter, UserRepository userRepository) {
		this.userService = userService;
		this.entityConverter = entityConverter;
		this.userRepository = userRepository;
		this.usersJson = new ArrayList<>();
		this.infoJson = new ArrayList<>();

	}

	@POST
	public Response addUser(se.mebe.model.User user) throws ServiceException {
		if (user.getUserName().length() < 10) {
			throw new ResourceException("Username is to short must have atleast 10 character!");

		} else {
			newUser = userService.addUser(EntityConverter.convertToJpaUser(user));
			location = uriInfo.getAbsolutePathBuilder().path(newUser.getId().toString()).build();

			return Response.created(location).build();
		}
	}

	@PUT
	@Path("{userId}/{column}/{newValue}")
	public Response updateUser(@PathParam("userId") long userId, @PathParam("column") String column,
			@PathParam("newValue") String newValue) throws ServiceException, BadMessageException {

		if (!userRepository.exists(userId)) {
			throw new BadMessageException("This user dosnt exist for update user values !!");

		} else {
			userService.updateUser(userId, column, newValue);
			return Response.noContent().build();
		}

	}

	@PUT
	@Path("{id}/{status}")
	public Response inactiveUser(@PathParam("id") long userId, @PathParam("status") String status)
			throws ServiceException, BadMessageException {

		if (!userRepository.exists(userId)) {
			throw new BadMessageException("This user dosnt exist for update users values !!");

		} else if (userRepository.findUserStatusById(userId).equals(status)) {
			throw new BadMessageException("This user state is alredy same !!");

		} else {
			userService.setUserStatus(userId, status);
			return Response.noContent().build();
		}
	}

	@GET
	@Path("{userNumber}")
	public Response findUserByUserNumber(@PathParam("userNumber") String userNumber)
			throws ServiceException, JsonProcessingException, BadMessageException {

		if (userRepository.findByUserNumber(userNumber).isEmpty()) {
			throw new ResourceException("This user dosnt exist by this user number or user number is worng !!");

		} else {
			user = userService.findByUserNumber(userNumber).iterator().next();
			userToRest = entityConverter.convertToRestUser(user);

			return Response.ok(userToRest, headers.getMediaType()).build();
		}
	}

	@GET
	@Path("{findNameTyp}/{name}")
	public Response findUserByManyInformation(@PathParam("findNameTyp") String findNameTyp,
			@PathParam("name") String name) throws ServiceException, JsonProcessingException, BadMessageException {

		if (userRepository.findByFirstName(name).isEmpty() && userRepository.findByLastName(name).isEmpty()
				&& userRepository.findByUserName(name).isEmpty()) {
			throw new BadMessageException("This user values dosnt exist");

		} else {
			users = userService.findByName(findNameTyp, name);
			users.forEach(userInfo -> {

				userToRest = entityConverter.convertToRestUser(userInfo);
				infoJson.add(userToRest);
			});

			return Response.ok(infoJson, headers.getMediaType()).build();

		}
	}

	@GET
	@Path("team/{teamId}")
	public Response findUserByTeam(@PathParam("teamId") long teamId)
			throws ServiceException, JsonProcessingException, BadMessageException {

		if (!userRepository.findTeamIdInUser().contains(teamId)) {
			throw new BadMessageException("This user have not team !!, make sure that team id is correct");

		} else {
			users = userService.getAllUserForATeam(teamId);
			users.forEach(user -> {

				userToRest = entityConverter.convertToRestUser(user);
				usersJson.add(userToRest);

			});

			return Response.ok(usersJson, headers.getMediaType()).build();
		}
	}

}
