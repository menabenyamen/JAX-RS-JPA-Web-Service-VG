package se.mebe.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import se.mebe.converter.EntityConverter;
import se.mebe.exception.BadMessageException;
import se.mebe.exception.ResourceException;
import se.mebe.model.User;
import se.mebe.resource.UserResource;
import se.springdata.exception.ServiceException;
import se.springdata.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)

public final class UserResourceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserResource userResource;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private Entity<User> entity;
	private Client client;
	private WebTarget target;
	private Response response;

	@Test
	public void shouldThrowExceptionWithUserNameMoreThan10Char() {

		User user = new User(0, "David", "karlman", "dedd", "Active", "Se29376");

		entity = Entity.json(user);
		client = createClient();
		target = client.target("http://localhost:8080/users");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Username is to short must have atleast 10 character!");

		when(userRepository.save(EntityConverter.convertToJpaUser(user)))
				.thenThrow(new ServiceException("Username is to short must have atleast 10 character!"));

		assertEquals(404, response.getStatus());
	}

	@Test
	public void willPostUser() throws ServiceException {

		User user = new User(0, "KarlDavid", "karlmasssonn", "dedddddddddd", "Se29377", "Active");

		entity = Entity.json(user);
		client = createClient();
		target = client.target("http://localhost:8080/users");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Username is to short must have atleast 10 character!");

		when(userRepository.save(EntityConverter.convertToJpaUser(user)))
				.thenThrow(new ServiceException("Username is to short must have atleast 10 character!"));

		assertEquals(201, response.getStatus());

	}

	@Test
	public void shouldThrwoExceptionIfUserNotFound() {

		User user = new User(90, "Tammi", "karlman", "deddddddddddddt", "Se29376", "Active");

		client = createClient();
		target = client.target("http://localhost:8080/users/90/firstName/Tammi");
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user dosnt exist for update user values !!");

		when(userRepository.exists(90L))
				.thenThrow(new ServiceException("This user dosnt exist for update user values !!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldUpdateUser() {
		User user = new User(45L, "Tammi", "karlman", "deddddddddddddt", "Se29376", "Active");

		client = createClient();
		target = client.target("http://localhost:8080/users/45/firstName/Tammi");
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user dosnt exist for update user values !!");

		when(userRepository.exists(90L))
				.thenThrow(new BadMessageException("This user dosnt exist for update user values !!"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNullUser() {
		User user = new User(90, "Tammi", "karlman", "deddddddddddddt", "Se29376", "Active");

		client = createClient();
		target = client.target("http://localhost:8080/users/90/disable");
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user dosnt exist for update users values !!");

		when(userRepository.exists(90L))
				.thenThrow(new BadMessageException("This user dosnt exist for update users values !!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithSameUserState() {

		User user = new User(45, "Tammi", "karlman", "deddddddddddddt", "Se29376", "Active");

		client = createClient();
		target = client.target("http://localhost:8080/users/37/disable");
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user dosnt exist for update users values !!");

		when(userRepository.exists(90L))
				.thenThrow(new BadMessageException("This user dosnt exist for update users values !!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldUpdateUserState() {

		User user = new User(45L, "Tammi", "karlman", "deddddddddddddt", "Se29376", "Active");

		client = createClient();
		target = client.target("http://localhost:8080/users/45/disable");
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("User is already disabled!");

		when(userRepository.disableUser(45L)).thenThrow(new ServiceException("User is already disabled!"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithWrongUserNumber() {

		client = createClient();
		target = client.target("http://localhost:8080/users/SE00000");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("HTTP 404 Not Found");

		when(userRepository.findByUserNumber("SE00000")).thenThrow(
				new ResourceException("HTTP 404 Not Found"));

		assertThat(userRepository.findByUserNumber("SE00000"),is(nullValue()));
		assertEquals(401, response.getStatus());
//		assertEquals(404, response.getStatus());
		
	}

	@Test
	public void mustGetUserByUserNumber() {
		client = createClient();
		target = client.target("http://localhost:8080/users/SE48");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user dosnt exist by this user number or user number is worng !!");

		when(userRepository.findByUserNumber("SE48")).thenThrow(
				new BadMessageException("This user dosnt exist by this user number or user number is worng !!"));

		assertEquals(200, response.getStatus());
	}

	@Test
	public void canNotGetUserIfValuesAreWrong() {
		client = createClient();
		target = client.target(
				"http://localhost:8080/users/firstname/kokeko, http://localhost:8080/users/lastname/kokeko, http://localhost:8080/users/username/kokeko");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user values dosnt exist");

		when(userRepository.exists(45L)).thenThrow(new BadMessageException("This user values dosnt exist"));

		assertEquals(404, response.getStatus());
	}

	@Test
	public void willGetUserByDifferentValues() {
		client = createClient();
		target = client.target("http://localhost:8080/users/firstname/Karlll");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user values dosnt exist");

		when(userRepository.exists(45L)).thenThrow(new BadMessageException("This user values dosnt exist"));

		assertEquals(200, response.getStatus());
	}

	@Test
	public void willThrowExceptionWithNotExistingTeam() {
		client = createClient();
		target = client.target("http://localhost:8080/users/team/100");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user have not team !!, make sure that team id is correct");

		when(userRepository.findTeamIdInUser())
				.thenThrow(new BadMessageException("This user have not team !!, make sure that team id is correct"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willGetUserByExistingTeam() {
		client = createClient();
		target = client.target("http://localhost:8080/users/team/13");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user have not team !!, make sure that team id is correct");

		when(userRepository.findTeamIdInUser()).thenThrow(new ServiceException("This user have not team !!, make sure that team id is correct"));

		assertEquals(200, response.getStatus());
	}

	private Client createClient() {
		return ClientBuilder.newBuilder().register(JacksonJaxbJsonProvider.class).build();
	}

}
