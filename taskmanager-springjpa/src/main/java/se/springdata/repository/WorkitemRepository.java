package se.springdata.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import se.springdata.model.Status;
import se.springdata.model.Workitem;

public interface WorkitemRepository extends PagingAndSortingRepository<Workitem, Long> {

	@Transactional
	@Modifying
	@Query("update Workitem w set w.status = 'UNSTARTED' WHERE w.id = ?1")
	Integer setTaskToUnstarted(long workitemId);

	@Transactional
	@Modifying
	@Query("update Workitem w set w.status = 'STARTED' WHERE w.id = ?1")
	Integer setTaskToStarted(long workitemId);

	@Transactional
	@Modifying
	@Query("update Workitem w set w.status = 'DONE' WHERE w.id = ?1")
	Integer setTaskToDone(long workitemId);

	@Transactional
	@Modifying
	@Query("update Workitem w set w.user.id = ?1 where w.id = ?2")
	Integer assignWorkitemToUser(long userId, long workitemId);

	@Transactional
	@Modifying
	@Query("update Workitem w set w.team.id = ?1 where w.id = ?2")
	Integer assignTeamToWorkitem(long teamId, long workitemId);

	@Query("select w from Workitem w where status = ?1")
	Collection<Workitem> findAllByWorkitemStatus(Status status);

	@Query("select w from Workitem w where team_id = ?1")
	Collection<Workitem> findAllWorkitemByTeam(long teamId);

	@Query("select w from Workitem w where user_id = ?1")
	Collection<Workitem> findAllWorkitemByUser(long userId);

	@Query("select w from Workitem w where w.description like %:sys%")
	Collection<Workitem> findAllByCertainDescription(@Param("sys") String searchValue);

	@Query("select w.id from Workitem w")
	Collection<Long> findAllWorkitemsId();

	@Query("select w.status from Workitem w where w.status = ?1")
	Collection<String> findWorkitemStatusByStatus(Status status);

	@Query("select w.status from Workitem w where w.id = ?1")
	Collection<se.springdata.model.Status> findWorkitemStatusById(long id);

	@Query("select w.user.id from Workitem w")
	Collection<Long> findUserIdFromWorkitem();

	@Query("select w.team.id from Workitem w")
	Collection<Long> findTeamIdFromWorkitem();

	@Query("select w from Workitem w where w.id = ?1")
	Collection<Workitem> findWorkitemById(long workitemId);

	@Query("select w.title from Workitem w")
	Collection<String> getWorkItemsTitle();

	Workitem findByDescription(String description);
//	
	Workitem findByStatus(Status state);
//	
	Workitem findByTeam(long id);
	
	

}