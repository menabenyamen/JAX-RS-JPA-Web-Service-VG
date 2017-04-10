package se.mebe.test;

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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import se.mebe.converter.EntityConverter;
import se.mebe.exception.BadMessageException;
import se.mebe.model.Issue;
import se.mebe.resource.IssueResource;
import se.springdata.exception.ServiceException;
import se.springdata.repository.IssueRepository;

@RunWith(MockitoJUnitRunner.class)
public final class IssueResourceTest {

	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueResource issueResource;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private Entity<Issue> entity;
	private Client client;
	private WebTarget target;
	private Response response;

	@Test
	public void shouldThrowExcptionWithNotExistingWorkItem() {
		Issue issue = new Issue(0, "Good", "Service is good");

		entity = Entity.json(issue);
		client = createClient();
		target = client.target("http://localhost:8080/issues/post/90");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item id dosnt exist !!");

		when(issueRepository.save(EntityConverter.convertToJpaIssue(issue)))
				.thenThrow(new BadMessageException("This work item id dosnt exist !!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotStateDone() {
		Issue issue = new Issue(17, "Good", "Service is good");

		entity = Entity.json(issue);
		client = createClient();
		target = client.target("http://localhost:8080/issues/post/17");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This workitem can't be assigned an issue because it is not done yet!");

		when(issueRepository.save(EntityConverter.convertToJpaIssue(issue))).thenThrow(
				new BadMessageException("This workitem can't be assigned an issue because it is not done yet!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willAddIssue() {

		Issue issue = new Issue(36, "Good", "Service is good");

		entity = Entity.json(issue);
		client = createClient();
		target = client.target("http://localhost:8080/issues/post/36");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This workitem can't be assigned an issue because it is not done yet!");

		when(issueRepository.save(EntityConverter.convertToJpaIssue(issue))).thenThrow(
				new ServiceException("This workitem can't be assigned an issue because it is not done yet!"));

		assertEquals(200, response.getStatus());
	}

	@Test
	public void canNotUpdateIssueWhichNotExsit() {

		Issue issue = new Issue(45, "Good", "Good");

		entity = Entity.json(issue);
		client = createClient();
		target = client.target("http://localhost:8080/issues/update/45/Good/Good");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This issues dosnt exist for update");

		when(issueRepository.save(EntityConverter.convertToJpaIssue(issue)))
				.thenThrow(new BadMessageException("This issues dosnt exist for update"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willUpdateIssue() {
		Issue issue = new Issue(32, "Good", "Good");

		entity = Entity.json(issue);
		client = createClient();
		target = client.target("http://localhost:8080/issues/update/32/Good/Good");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Update failed since no issue could be found corresponding to the issue id");

		when(issueRepository.save(EntityConverter.convertToJpaIssue(issue))).thenThrow(
				new ServiceException("Update failed since no issue could be found corresponding to the issue id"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExistingWorkItems() {

		client = createClient();
		target = client.target("http://localhost:8080/issues/all/items");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		assertThat(issueRepository.findWorkitemFromIssue(), is(nullValue()));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void willGetAllItems() {
		client = createClient();
		target = client.target("http://localhost:8080/issues/all/items");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("issue hasn't been assigned to any task!");

		when(issueRepository.findAllWorkitemFromIssue())
				.thenThrow(new BadMessageException("issue hasn't been assigned to any task!"));

		assertEquals(200, response.getStatus());
	}

	private Client createClient() {
		return ClientBuilder.newBuilder().register(JacksonJaxbJsonProvider.class).build();
	}

}
