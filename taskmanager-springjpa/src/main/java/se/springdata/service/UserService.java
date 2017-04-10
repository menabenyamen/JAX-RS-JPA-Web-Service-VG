package se.springdata.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import se.springdata.exception.ServiceException;
import se.springdata.model.User;
import se.springdata.repository.UserRepository;
import se.springdata.transaction.ServiceTransaction;

@Component
public final class UserService {

	private final UserRepository userRepository;
	private final ServiceTransaction executor;

	@Autowired
	public UserService(UserRepository userRepository, ServiceTransaction executor) {
		this.userRepository = userRepository;
		this.executor = executor;
	}

	public User addUser(User user) throws ServiceException {
		if (user.getUserName().length() < 10) {
			throw new ServiceException("Username is to short must have atleast 10 character!");

		} else if (userRepository.findAllUserName().contains(user.getUserName())) {
			throw new ServiceException("The update failed, username already exist!");

		} else {
			try {
				return executor.execute(() -> {
					return userRepository.save(user);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not add User");
			}

		}
	}

	public Integer updateUser(long userId, String column, String newValue)
			throws se.springdata.exception.ServiceException {
		switch (column) {
		case "firstName":
			try {
				return executor.execute(() -> {
					return userRepository.updateUserFirstName(newValue, userId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not update first name");
			}

		case "lastName":
			try {
				return executor.execute(() -> {
					return userRepository.updateUserLastName(newValue, userId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not update last name");
			}

		case "userName":
			try {
				return executor.execute(() -> {
					return userRepository.updateUserName(newValue, userId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not update user name");
			}

		default:
			throw new ServiceException("Invalid update command: " + column);
		}
	}

	public Integer setUserStatus(long userId, String status) throws ServiceException {
		switch (status) {
		case "disable":
			if (userRepository.findUserStatusById(userId).equals("inactive")) {
				throw new ServiceException("User is already disabled!");
			} else {
				if (userRepository.findUserStatusById(userId).equals(null)) {
					return userRepository.disableUser(userId);
				} else {
					try {
						return executor.execute(() -> {
							userRepository.setWorkitemToUnstarted(userId);
							return userRepository.disableUser(userId);
						});
					} catch (DataAccessException e) {
						throw new ServiceException("Could not disable user state");
					}

				}
			}
		case "enable":
			if (userRepository.findUserStatusById(userId).equals("active")) {
				throw new ServiceException("User is already enabled!");
			} else {
				if (userRepository.findUserStatusById(userId).equals(null)) {
					return userRepository.enableUser(userId);
				} else {
					try {
						return executor.execute(() -> {
							return userRepository.enableUser(userId);
						});
					} catch (DataAccessException e) {
						throw new ServiceException("Could not enable user");
					}

				}
			}
		default:
			throw new ServiceException("Invalid update command: " + status);
		}
	}

	public Collection<User> findByUserNumber(String number) throws ServiceException {
		if (userRepository.findByUserNumber(number).isEmpty()) {
			throw new ServiceException("The user number could not be found!");
		} else {
			try {
				return executor.execute(() -> {
					return userRepository.findByUserNumber(number);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not find by user number");
			}

		}
	}

	public Collection<User> findByName(String findNameTyp, String name) throws ServiceException {
		switch (findNameTyp) {
		case "firstname":
			Collection<User> firstName = userRepository.findByFirstName(name);
			if (!firstName.contains(name) & (firstName.isEmpty())) {
				throw new ServiceException("The first name can't be found!");
			} else {
				try {
					return executor.execute(() -> {
						return userRepository.findByFirstName(name);
					});
				} catch (DataAccessException e) {
					throw new ServiceException("Could not find by first name");
				}

			}
		case "lastname":
			Collection<User> lastName = userRepository.findByLastName(name);
			if (!lastName.contains(name) & (lastName.isEmpty())) {
				throw new ServiceException("The last name can't be found!");
			} else {
				try {
					return executor.execute(() -> {
						return userRepository.findByLastName(name);
					});
				} catch (DataAccessException e) {
					throw new ServiceException("Could not find by last name");
				}

			}
		case "username":
			Collection<User> userName = userRepository.findByUserName(name);
			if (!userName.contains(name) & (userName.isEmpty())) {
				throw new ServiceException("The username can't be found!");
			} else {
				try {
					return executor.execute(() -> {
						return userRepository.findByUserName(name);
					});
				} catch (DataAccessException e) {
					throw new ServiceException("Could not find by user name");
				}

			}
		default:
			throw new ServiceException("Invalid search command: " + findNameTyp);
		}
	}

	public Collection<User> getAllUserForATeam(long teamId) throws ServiceException {
		if (userRepository.findAllUserByTeamId(teamId) == null) {
			throw new ServiceException("The team that was requested don't have any members or doesn't exist!");
		} else {
			try {
				return executor.execute(() -> {
					return userRepository.findAllUserByTeamId(teamId);
				});
			} catch (DataAccessException e) {
				throw new ServiceException("Could not get user by team");
			}
		}
	}
}