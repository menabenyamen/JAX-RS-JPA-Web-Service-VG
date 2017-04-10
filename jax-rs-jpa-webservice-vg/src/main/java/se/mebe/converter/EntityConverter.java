package se.mebe.converter;

import org.springframework.stereotype.Component;

@Component
public final class EntityConverter {

	public static se.springdata.model.User convertToJpaUser(se.mebe.model.User user) {
		se.springdata.model.User newUser = new se.springdata.model.User(user.getFirstName(), user.getLastName(),
				user.getUserName(), user.getUserState(), user.getUserNumber());
		return newUser;
	}

	public se.mebe.model.User convertToRestUser(se.springdata.model.User user) {
		se.mebe.model.User newUser = new se.mebe.model.User(user.getId(), user.getFirstName(), user.getLastName(),
				user.getUserName(), user.getUserStatus(), user.getUserNumber());
		return newUser;
	}

	public static se.springdata.model.Team convertToJpaTeam(se.mebe.model.Team team) {
		se.springdata.model.Team newTeam = new se.springdata.model.Team(team.getTeamName(), team.getTeamStatus());
		return newTeam;
	}

	public se.mebe.model.Team convertToRestTeam(se.springdata.model.Team team) {
		se.mebe.model.Team newTeam = new se.mebe.model.Team(team.getId(), team.getTeamName(), team.getTeamStatus());
		return newTeam;
	}

	public static se.springdata.model.Workitem convertToWorkItemEntity(se.mebe.model.WorkItem workItem) {
		se.springdata.model.Workitem newWorkItem = new se.springdata.model.Workitem(workItem.getTitle(),
				workItem.getDescription(), workItem.getStatus());
		return newWorkItem;
	}

	public se.mebe.model.WorkItem convertToRestWorkItem(se.springdata.model.Workitem workItem) {
		se.mebe.model.WorkItem newWorkItem = new se.mebe.model.WorkItem(workItem.getId(), workItem.getTitle(),
				workItem.getDescription(), workItem.getStatus());
		return newWorkItem;
	}

	public static se.springdata.model.Issue convertToJpaIssue(se.mebe.model.Issue issue) {
		se.springdata.model.Issue newIssue = new se.springdata.model.Issue(issue.getAnswer(), issue.getIssueReason());
		return newIssue;
	}

	public se.mebe.model.Issue convertToRestIssue(se.springdata.model.Issue issue) {
		se.mebe.model.Issue newIssue = new se.mebe.model.Issue(issue.getId(), issue.getAnswer(),
				issue.getIssueReason());
		return newIssue;
	}

}
