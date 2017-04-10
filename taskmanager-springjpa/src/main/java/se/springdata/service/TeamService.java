package se.springdata.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import se.springdata.exception.ServiceException;
import se.springdata.model.Team;
import se.springdata.repository.TeamRepository;
import se.springdata.repository.UserRepository;
import se.springdata.transaction.ServiceTransaction;

@Component
public final class TeamService {

	private final TeamRepository teamRepository;
	private final UserRepository userRepository;
	private final ServiceTransaction executor;

	@Autowired
	public TeamService(TeamRepository teamRepository, UserRepository userRepository, ServiceTransaction executor) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.executor = executor;
	}

	public Team addTeam(Team team) throws ServiceException {
		if (teamRepository.findAllTeamName().contains(team.getTeamName())) {
			throw new ServiceException(
					"The team already exist, and can't therefore be add!");
		} else {
			try {
				return executor.execute(() -> {
					return teamRepository.save(team);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not add team");
			}

		}
	}

	public Integer updateTeam(String teamName, long teamId) throws ServiceException {

		if (teamRepository.findTeamNameById(teamId).equals(teamName)) {
			throw new ServiceException(
					"Error the team you are trying to update is already using as team name!");
		} else if (teamRepository.findAllTeamName().contains(teamName)) {
			throw new ServiceException("Invalid update, the team name is already in use!");
		
		} else {
			try {
				return executor.execute(() -> {
					return teamRepository.updateTeam(teamName, teamId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not update team");
			}

		}
	}

	public Integer setTeamStatus(long teamId, String status) throws ServiceException {
		switch (status) {
		case "disable":
			if (teamRepository.findTeamStatusById(teamId).equals("inactive")) {
				throw new ServiceException("Team is already disabled!");
			} else {
				if (teamRepository.findTeamStatusById(teamId).equals(null)) {
					return teamRepository.disableTeam(teamId);
				} else {
					try {
						return executor.execute(() -> {
							return teamRepository.disableTeam(teamId);
						});
					} catch (DataAccessException e) {
						throw new ServiceException("Could not disable team");
					}

				}
			}
		case "enable":
			if (teamRepository.findTeamStatusById(teamId).equals("active")) {
				throw new ServiceException("Team is already enabled!");
			} else {
				if (teamRepository.findTeamStatusById(teamId).equals(null)) {
					return teamRepository.enableTeam(teamId);
				} else {
					try {
						return executor.execute(() -> {
							return teamRepository.enableTeam(teamId);
						});
					} catch (DataAccessException e) {
						throw new ServiceException("Could not enable team");
					}

				}
			}
		default:
			throw new ServiceException("Invalid update command: " + status);
		}
	}

	public Collection<Team> getAllTeams() throws ServiceException {
		try {
			return executor.execute(() -> {
				return teamRepository.findAllTeams();
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not get all team");
		}

	}

	public Integer addUserToTeam(long teamId, long userId) throws ServiceException {
		if (userRepository.findTeamIdByUserId(userId) != null) {
			
			throw new ServiceException("User is already a member of a team!");
		} else if (teamRepository.findAllUserForTeamById(teamId).size() == 10) {
			throw new ServiceException("This team has 10 members, and can't therefore add any more user!");
		} else {
			try {
				return executor.execute(() -> {
					return teamRepository.addUserToTeam(teamId, userId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not add any user to team");
			}

		}
	}
}