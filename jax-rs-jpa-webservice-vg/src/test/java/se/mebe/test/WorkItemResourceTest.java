package se.mebe.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import se.mebe.converter.EntityConverter;
import se.mebe.exception.BadMessageException;
import se.mebe.exception.ResourceException;
import se.mebe.model.WorkItem;
import se.mebe.resource.WorkItemResource;
import se.springdata.exception.ServiceException;
import se.springdata.model.Status;
import se.springdata.repository.UserRepository;
import se.springdata.repository.WorkitemRepository;

@RunWith(MockitoJUnitRunner.class)
public final class WorkItemResourceTest {

	@Mock
	private WorkitemRepository workItemRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private WorkItemResource workItemResource;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private Entity<WorkItem> entity;
	private Client client;
	private WebTarget target;
	private Response response;

	@Test
	public void willAddWorkItem() {

		WorkItem workItem = new WorkItem(0, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work Item is exist !!");

		when(workItemRepository.save(EntityConverter.convertToWorkItemEntity(workItem)))
				.thenThrow(new ServiceException("This work Item is exist !!"));

		assertEquals(201, response.getStatus());

	}

	@Test
	public void shouldThrowExceptionWithNullWorkItem() {
		WorkItem workItem = new WorkItem(90, "Java Web Service", "web", Status.DONE);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/state/90/DONE");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("You are trying with not existing workitem");

		when(workItemRepository.exists(90L))
				.thenThrow(new BadMessageException("You are trying with not existing workitem"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willNotUpdateWithSameInformation() {
		WorkItem workItem = new WorkItem(17, "Java Web Service", "web", Status.DONE);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/state/17/Done");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("You are trying to update to same information");

		when(workItemRepository.findWorkitemStatusById(workItem.getId()))
				.thenThrow(new BadMessageException("You are trying to update to same information"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willUpdateWorkItemState() {
		WorkItem workItem = new WorkItem(17, "Java Web Service", "web", Status.DONE);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/state/17/STARTED");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work Item is exist !!");

		when(workItemRepository.setTaskToDone(workItem.getId()))
				.thenThrow(new ServiceException("This work Item is exist !!"));
		when(workItemRepository.setTaskToStarted(workItem.getId()))
				.thenThrow(new ServiceException("This work Item is exist !!"));
		when(workItemRepository.setTaskToUnstarted(workItem.getId()))
				.thenThrow(new ServiceException("This work Item is exist !!"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void willNotDeleteNotExistingWorkItem() {

		client = createClient();
		target = client.target("http://localhost:8080/items/remove/34/90");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt exist to delete");

		when(workItemRepository.exists(34L)).thenThrow(new BadMessageException("This work item dosnt exist to delete"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willNotDeleteWithNullUserId() {
		client = createClient();
		target = client.target("http://localhost:8080/items/remove/90/63");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt have issue to delete");

		when(workItemRepository.exists(63L))
				.thenThrow(new BadMessageException("This work item dosnt have issue to delete"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willDeleteWroItem() {
		client = createClient();
		target = client.target("http://localhost:8080/items/remove/41/20");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt exist to delete");

		when(workItemRepository.findAllWorkitemsId())
				.thenThrow(new BadMessageException("This work item dosnt exist to delete"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExsitingWorkItems() {
		WorkItem workItem = new WorkItem(36, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/assign/1/90");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt exist to assign to user");

		when(workItemRepository.exists(90L))
				.thenThrow(new BadMessageException("This work item dosnt exist to assign to user"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExsitingUsers() {
		WorkItem workItem = new WorkItem(36, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/assign/89/36");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt exist to assign to user");

		when(workItemRepository.exists(1L))
				.thenThrow(new BadMessageException("This work item dosnt exist to assign to user"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotInactiveUser() {
		WorkItem workItem = new WorkItem(36, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/assign/37/36");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The user is inactive and can therefore not be assigned a workitem !!!");

		when(userRepository.findUserStatusById(37L)).thenThrow(
				new BadMessageException("The user is inactive and can therefore not be assigned a workitem !!!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWith5WorkItemsToOneUser() {
		WorkItem workItem = new WorkItem(36, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/assign/1/36");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The user can't be assigned any more task, since 5 is the limit!");

		when(userRepository.findUserStatusById(37L))
				.thenThrow(new BadMessageException("The user can't be assigned any more task, since 5 is the limit!"));

		assertEquals(500, response.getStatus());
	}

	@Test
	public void willAssignWorkItemToUser() {

		WorkItem workItem = new WorkItem(36, "HTML", "web", Status.STARTED);

		entity = Entity.json(workItem);
		client = createClient();
		target = client.target("http://localhost:8080/items/assign/43/57");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(entity);

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This work item dosnt exist to assign to user");

		when(workItemRepository.exists(90L))
				.thenThrow(new BadMessageException("This work item dosnt exist to assign to user"));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExistingState() {

		client = createClient();
		target = client.target("http://localhost:8080/items/state/DONE");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		assertThat(workItemRepository.findByStatus(Status.DONE), is(nullValue()));
		assertEquals(401, response.getStatus());

	}

	@Test
	public void willGetAllWorkItemByState() {
		client = createClient();
		target = client.target("http://localhost:8080/items/state/STARTED");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("HTTP 404 Not Found");

		when(workItemRepository.findWorkitemStatusByStatus(Status.STARTED))
				.thenThrow(new ResourceException("HTTP 404 Not Found"));

		assertThat(workItemRepository.findWorkitemStatusByStatus(Status.STARTED), is(notNullValue()));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void willNotGetAnyThingWithMissingTeam() {
		client = createClient();
		target = client.target("http://localhost:8080/items/team/7");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		assertThat(workItemRepository.exists(7L), is(equalTo(false)));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void willGetAnyWorkItemsWithTeam() {
		client = createClient();
		target = client.target("http://localhost:8080/items/team/48");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("HTTP 404 Not Found");

		when(workItemRepository.findByTeam(48L))
				.thenThrow(new ResourceException("HTTP 404 Not Found"));

		assertThat(workItemRepository.findByTeam(48L), is(notNullValue()));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNoUser() {
		client = createClient();
		target = client.target("http://localhost:8080/items/user/90");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		assertThat(userRepository.exists(90L), is(equalTo(false)));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void willFindWorkItemsWithUser() {
		client = createClient();
		target = client.target("http://localhost:8080/items/user/1");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("HTTP 404 Not Found");

		when(workItemRepository.findUserIdFromWorkitem())
				.thenThrow(new ResourceException("HTTP 404 Not Found"));

		assertThat(workItemRepository.findUserIdFromWorkitem(), is(notNullValue()));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void shouldThrowExceptionWithNotExistingDescription() {
		client = createClient();
		target = client.target("http://localhost:8080/items/search/KOKOKOKOKO");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		assertThat(workItemRepository.findByDescription("KOKOKOKOKO"), is(nullValue()));
		assertEquals(401, response.getStatus());
	}

	@Test
	public void willGetWorkItemsWithExistingDescription() {
		client = createClient();
		target = client.target("http://localhost:8080/items/search/web service");
		response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("HTTP 404 Not Found");

		when(workItemRepository.findByDescription("doing web server"))
				.thenThrow(new ResourceException("HTTP 404 Not Found"));

		assertThat(workItemRepository.findByDescription("doing web server"), is(notNullValue()));
		assertEquals(200, response.getStatus());
	}

	private Client createClient() {
		return ClientBuilder.newBuilder().register(JacksonJaxbJsonProvider.class).build();
	}

}
