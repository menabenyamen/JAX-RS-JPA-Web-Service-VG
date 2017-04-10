package se.mebe.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.mebe.converter.EntityConverter;
import se.mebe.filter.AuthorizationRequestFilter;
import se.mebe.resource.IssueResource;
import se.mebe.resource.TeamResource;
import se.mebe.resource.UserResource;
import se.mebe.resource.WorkItemResource;
import se.springdata.repository.IssueRepository;
import se.springdata.service.IssueService;
import se.springdata.service.TeamService;
import se.springdata.service.UserService;
import se.springdata.service.WorkitemService;

@Component
public final class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		register(UserResource.class);
		register(UserService.class);
		register(EntityConverter.class);
		register(TeamResource.class);
		register(TeamService.class);
		register(WorkItemResource.class);
		register(WorkitemService.class);
		register(IssueResource.class);
		register(IssueService.class);
		register(IssueRepository.class);
		register(AuthorizationRequestFilter.class);

	}

}
