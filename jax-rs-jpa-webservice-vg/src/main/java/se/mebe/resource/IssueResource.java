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
import se.springdata.exception.ServiceException;
import se.springdata.repository.IssueRepository;
import se.springdata.repository.WorkitemRepository;
import se.springdata.service.IssueService;

@Component
@Path("/issues")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })

public final class IssueResource {

	private final IssueService issueService;
	private final EntityConverter entityConverter;
	private final IssueRepository issueRepository;
	private final WorkitemRepository workitemRepository;
	private se.springdata.model.Issue newIssue;
	private Collection<se.springdata.model.Workitem> items;
	private se.mebe.model.WorkItem newItems;
	private List<se.mebe.model.WorkItem> allItems;
	private URI location;

	@Context
	private UriInfo uriIfno;
	@Context
	private HttpHeaders headers;

	public IssueResource(IssueService issueService, EntityConverter entityConverter, IssueRepository issueRepository,
			WorkitemRepository workitemRepository) {
		this.issueService = issueService;
		this.entityConverter = entityConverter;
		this.issueRepository = issueRepository;
		this.workitemRepository = workitemRepository;
		this.allItems = new ArrayList<>();
	}

	@POST
	@Path("post/{id}")
	public Response addIssue(se.mebe.model.Issue issue, @PathParam("id") long id)
			throws ServiceException, BadMessageException {

		if (!workitemRepository.findAllWorkitemsId().contains(id)) {
			throw new BadMessageException("This work item id dosnt exist !!");

		} else if (!issueRepository.findWorkitemStatusById(id).contains("DONE")) {
			throw new BadMessageException("This workitem can't be assigned an issue because it is not done yet!");

		} else {
			newIssue = issueService.addAndAssignIssue(EntityConverter.convertToJpaIssue(issue), id);
			location = uriIfno.getAbsolutePathBuilder().path(newIssue.getId().toString()).build();

			return Response.created(location).build();
		}
	}

	@PUT
	@Path("/update/{issueId}/{answer}/{newReason}")
	public Response updateIssue(@PathParam("issueId") long issueId, @PathParam("answer") String answer,
			@PathParam("newReason") String newReason) throws ServiceException, BadMessageException {

		if (!issueRepository.exists(issueId)) {
			throw new BadMessageException("This issues dosnt exist for update");

		} else {
			issueService.updateIssue(issueId, answer, newReason);
			return Response.noContent().build();

		}
	}

	@GET
	@Path("all/items")
	public Response findAllWorkItemsByIssue() throws ServiceException, BadMessageException {
		if (issueRepository.findAllWorkitemFromIssue().isEmpty()) {
			throw new BadMessageException("This issues has not work items !!");

		} else {
			items = issueService.getAllWorkItemByIssue();
			items.forEach(item -> {
				newItems = entityConverter.convertToRestWorkItem(item);
				allItems.add(newItems);
			});
			return Response.ok(allItems, headers.getMediaType()).build();
		}
	}

}
