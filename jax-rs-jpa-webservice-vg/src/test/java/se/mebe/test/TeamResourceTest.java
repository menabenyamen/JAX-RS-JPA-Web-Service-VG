package se.mebe.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
import se.mebe.model.Team;
import se.mebe.resource.TeamResource;
import se.springdata.exception.ServiceException;
import se.springdata.repository.TeamRepository;
import se.springdata.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public final class TeamResourceTest {

	@Mock
	private TeamRepository teamRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private TeamResource teamResource;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private Entity<Team> entity;
	private Client client;
	private WebTarget target;
	private Response response;

	@Test
	public void willAddTeam() throws ServiceException {

		Team team = new Team(0, "Sliver", "Active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The team already exist, and can't therefore be add!");

		when(teamRepository.save(EntityConverter.convertToJpaTeam(team)))
				.thenThrow(new ServiceException("The team already exist, and can't therefore be add!"));

		assertEquals(201, response.getStatus());

	}

	@Test
	public void shouldThrowExceptionIfNoTeamFound() {
		Team team = new Team(80, "Green", "Active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/Black/80");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		assertThat(teamRepository.findOne(80L), is(nullValue()));
		assertEquals(404, response.getStatus());
	}

	@Test
	public void willUpdateTeamName() {

		Team team = new Team(47, "Yellow", "Active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/update/Orange/47");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Error the team you are trying to update is already using as team name!");

		when(teamRepository.updateTeam("Orange", 47L)).thenThrow(
				new ServiceException("Error the team you are trying to update is already using as team name!"));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExistingTeam() {
		Team team = new Team(77, "Yellow", "Active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/state/77/disable");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This team dosnt exist for update");

		when(teamRepository.exists(77L)).thenThrow(new BadMessageException("This team dosnt exist for update"));

		// assertThat(teamRepository.findOne(77L),is(nullValue()));
		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithSameState() {
		Team team = new Team(77, "Yellow", "Active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/state/16/disable");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This team is already have this status");

		when(teamRepository.exists(77L)).thenThrow(new BadMessageException("This team is already have this status"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willInactiveTeam() {

		Team team = new Team(47, "Yellow", "active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/state/47/disable");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Team is already disabled!");

		when(teamRepository.disableTeam(47L)).thenThrow(new ServiceException("Team is already disabled!"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotTeams() {

		client = createClient();
		target = client.target("http://localhost:8080/teams/all");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage(("There is not any team in your table"));

		when(teamRepository.findAll()).thenThrow(new BadMessageException("There is not any team in your table"));
		// assertThat(teamRepository.findOne(55L), is(nullValue()));
		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldGetAllTeams() {

		client = createClient();
		target = client.target("http://localhost:8080/teams/all");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage(("There is not any team in your table"));

		when(teamRepository.findAll()).thenThrow(new BadMessageException("There is not any team in your table"));

		assertEquals(200, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNullUser() {
		Team team = new Team(47, "Yellow", "active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/asign/47/90");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		assertThat(userRepository.findOne(90L), is(nullValue()));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotNullTeam() {
		Team team = new Team(47, "Yellow", "active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/asign/90/1");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		assertThat(teamRepository.findOne(90L), is(nullValue()));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void canNotBeMoreThan10UsersInTeam() {
		Team team = new Team(13, "Yellow", "active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/asign/13/1");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This team has 10 member no more member allwoed");

		when(teamRepository.findAllUserForTeamById(13L))
				.thenThrow(new BadMessageException("This team has 10 member no more member allwoed"));

		// assertThat(teamRepository.findAllUserForTeamById(13L),is(notNullValue()));
		assertEquals(500, response.getStatus());
	}

	@Test
	public void willAsignUserToTeam() {

		Team team = new Team(47, "Yellow", "active");

		entity = Entity.json(team);
		client = createClient();
		target = client.target("http://localhost:8080/teams/asign/48/45");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user is already a member !!");

		when(teamRepository.addUserToTeam(48L, 45L))
				.thenThrow(new ServiceException("This user is already a member !!"));
		assertEquals(204, response.getStatus());
	}

	private Client createClient() {
		return ClientBuilder.newBuilder().register(JacksonJaxbJsonProvider.class).build();
	}

}
