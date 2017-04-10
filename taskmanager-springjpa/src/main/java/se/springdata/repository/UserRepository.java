package se.springdata.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import se.springdata.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@Transactional
	@Modifying
	@Query("update User u set u.firstName = ?1 WHERE u.id = ?2")
	Integer updateUserFirstName(String newValue, long userId);

	@Transactional
	@Modifying
	@Query("update User u set u.lastName = ?1 WHERE u.id = ?2")
	Integer updateUserLastName(String newValue, long userId);

	@Transactional
	@Modifying
	@Query("update User u set u.userName = ?1 WHERE u.id = ?2")
	Integer updateUserName(String newValue, long userId);

	@Transactional
	@Modifying
	@Query("update User u set u.userStatus = 'inactive' WHERE u.id = ?1")
	Integer disableUser(long userId);

	@Transactional
	@Modifying
	@Query("update User u set u.userStatus = 'active' WHERE u.id = ?1")
	Integer enableUser(long userId);

	@Query("select u.userStatus from User u where u.id = ?1")
	String findUserStatusById(long userId);

	@Query("select u.userName from User u WHERE u.id = ?1")
	String findUserNameById(long userId);
	
	@Query("select u.userName from User u WHERE u.userName = ?1")
	String findUserNameByUserName(String userName);

	@Query("select u.userName from User u")
	Collection<User> findAllUserName();
	
	@Query("select u.userName from User u")
	Collection<String> findAllUserNamee();

	@Query("select u.userNumber from User u")
	Collection<String> findAllUserNumber();

	@Query("select u.team.id from User u where u.id = ?1")
	Long findTeamIdByUserId(long userId);

	@Query("select u.team.id from User u")
	Collection<Long> findTeamIdInUser();

	@Transactional
	@Modifying
	@Query("update Workitem w set w.status = 'UNSTARTED' where user_id = ?1")
	Integer setWorkitemToUnstarted(long userId);

	Collection<User> findByFirstName(String name);

	Collection<User> findByLastName(String name);

	Collection<User> findByUserName(String name);

	Collection<User> findByUserNumber(String number);

	@Query("select u from User u where team_id = ?1")
	Collection<User> findAllUserByTeamId(long teamId);
}