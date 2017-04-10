package se.springdata.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import se.springdata.exception.ServiceException;
import se.springdata.model.Status;
import se.springdata.model.Workitem;
import se.springdata.repository.IssueRepository;
//import se.springdata.repository.IssueRepository;
import se.springdata.repository.UserRepository;
import se.springdata.repository.WorkitemRepository;
import se.springdata.transaction.ServiceTransaction;

@Component
public final class WorkitemService {

	private final WorkitemRepository workitemRepository;
	private final UserRepository userRepository;
	private final IssueRepository issueRepository;
	private final ServiceTransaction executor;

	@Autowired
	public WorkitemService(WorkitemRepository workitemRepository, UserRepository userRepository,
			IssueRepository issueRepository, ServiceTransaction executor) {
		this.workitemRepository = workitemRepository;
		this.userRepository = userRepository;
		this.issueRepository = issueRepository;
		this.executor = executor;
	}

	public Workitem addWorkItem(Workitem workitem) throws ServiceException {
		if (workitemRepository.getWorkItemsTitle().contains(workitem.getTitle())) {
			throw new ServiceException("This work Item is exist !!");
		}
		try {
			return executor.execute(() -> {
				return workitemRepository.save(workitem);
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not add workitem");
		}
	}

	public Integer setStatus(long workitemId, Status status) throws se.springdata.exception.ServiceException {

		try {
			return executor.execute(() -> {
				switch (status) {
				case UNSTARTED:
					return workitemRepository.setTaskToUnstarted(workitemId);
				case STARTED:
					return workitemRepository.setTaskToStarted(workitemId);
				case DONE:
					return workitemRepository.setTaskToDone(workitemId);
				default:
					try {
						throw new ServiceException("Invalid update command: " + status);
					} catch (ServiceException e) {

						e.printStackTrace();
					}

				}
				return 1;
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not update any one of workitems property");
		}

	}

	public Integer assignsWorkitemToUser(long userId, long workitemId) throws ServiceException {
		if (userRepository.findUserStatusById(userId).equals("inactive")) {
			throw new ServiceException("The user is inactive and can therefore not be assigned a workitem !!!");

		} else if (workitemRepository.findAllWorkitemByUser(userId).size() == 5) {
			throw new ServiceException("The user can't be assigned any more task, since 5 is the limit!");

		} else if (userRepository.findTeamIdByUserId(userId) == null) {
			throw new ServiceException("The user must be belong to a team, inorder to be assigned a task");

		} else if (workitemRepository.findWorkitemById(workitemId).isEmpty()) {
			throw new ServiceException("The task requested doesn't exist, assigning task to user failed!");

		} else {
			try {
				return executor.execute(() -> {
					Long teamIdOfUser = userRepository.findTeamIdByUserId(userId);
					workitemRepository.assignTeamToWorkitem(teamIdOfUser, workitemId);
					return workitemRepository.assignWorkitemToUser(userId, workitemId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not assign any user to workitem");
			}

		}
	}

	public Collection<Workitem> getAllWorkitemsByStatus(Status status) throws ServiceException {
		if (!workitemRepository.findWorkitemStatusByStatus(status).contains(status)) {
			throw new ServiceException("There is no workitem with the status " + status + " in progress");
		} else {
			return workitemRepository.findAllByWorkitemStatus(status);
		}
	}

	public Collection<Workitem> getAllWorkitemsByTeam(long teamId) throws ServiceException {
		if (!workitemRepository.findTeamIdFromWorkitem().contains(teamId)) {
			throw new ServiceException("The team you requested doesn't exist!");
		} else {
			try {
				return executor.execute(() -> {
					return workitemRepository.findAllWorkitemByTeam(teamId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not get any items by team");
			}

		}

	}

	public Collection<Workitem> getAllWorkitemsByUser(long userId) throws ServiceException {
		if (!workitemRepository.findUserIdFromWorkitem().contains(userId)) {
			throw new ServiceException("The requested user isn't assigned any task, or doesn't exist in the system!");
		} else {
			try {
				return executor.execute(() -> {
					return workitemRepository.findAllWorkitemByUser(userId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not get any items by team");
			}

		}

	}

	public Collection<Workitem> searchByDescription(String searchValue) throws ServiceException {
		if (workitemRepository.findAllByCertainDescription(searchValue).isEmpty()) {
			throw new ServiceException("Search came up empty, can't find a task with the description " + searchValue);
		} else {
			try {
				return executor.execute(() -> {
					return workitemRepository.findAllByCertainDescription(searchValue);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not get any items by team");
			}

		}

	}

	public void deleteWorkitem(long issueId, long workitemId) throws ServiceException {

		if (!workitemRepository.findAllWorkitemsId().contains(workitemId)) {
			throw new ServiceException(
					"The workitem couldn't be found, therefore your request to delete will be declined!");
		} else {
			issueRepository.delete(issueId);
			workitemRepository.delete(workitemId);
		}
	}

	// private void deleteIssue(long issueId, long workitemId) throws
	// ServiceException {
	// if(issueRepository.findOpenIssue(issueId) == null) {
	// throw new ServiceException("The requested workitem doesn't exist!");
	// } else if(issueRepository.findOpenIssue(issueId).equals(false)) {
	// throw new ServiceException("The requested workitem doesn't have an issue
	// assigned!");
	// }else{
	//
	// }
	// }
}