package se.springdata.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import se.springdata.exception.ServiceException;
import se.springdata.model.Issue;
import se.springdata.model.Workitem;
import se.springdata.repository.IssueRepository;
import se.springdata.repository.WorkitemRepository;
import se.springdata.transaction.ServiceTransaction;

@Component
public final class IssueService {

	private final IssueRepository issueRepository;
	private final WorkitemRepository workitemRepository;
	private final ServiceTransaction executor;

	@Autowired
	public IssueService(IssueRepository issueRepository, WorkitemRepository workitemRepository,
			ServiceTransaction executor) {
		this.issueRepository = issueRepository;
		this.workitemRepository = workitemRepository;
		this.executor = executor;
	}

	public Issue addAndAssignIssue(Issue issue, long workitemId) throws ServiceException {

		if (!issueRepository.findWorkitemStatusById(workitemId).contains("DONE")) {
			throw new ServiceException("This workitem can't be assigned an issue because it is not done yet!");
		} else {

			try {
				return executor.execute(() -> {
					Issue newIssue = issueRepository.save(issue);
					issueRepository.assignWorkitemToIssue(workitemId, issue.getId());
					workitemRepository.setTaskToUnstarted(workitemId);
					return newIssue;
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not add Issue");
			}
		}

	}

	public Integer updateIssue(long issueId, String answer, String newReason) throws ServiceException {
		if (!issueRepository.findAllIssueId().contains(issueId)) {
			throw new ServiceException(
					"Update failed since no issue could be found corresponding to the issue id");
		} else {
			try {
				return executor.execute(() -> {
					issueRepository.updateIssueAnswer(answer, issueId);
					return issueRepository.updateIssue(newReason, issueId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not update Issue");
			}
		}

	}

	public Collection<Workitem> getAllWorkItemByIssue() throws ServiceException {
		if (issueRepository.findAllWorkitemFromIssue().isEmpty()) {
			throw new ServiceException("issue hasn't been assigned to any task!");
		} else {
			try {
				return executor.execute(() -> {
					return issueRepository.findAllWorkitemFromIssue();
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not get all workitems");
			}
		}

	}
}
