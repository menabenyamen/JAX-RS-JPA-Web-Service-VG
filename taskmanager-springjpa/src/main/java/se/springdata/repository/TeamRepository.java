package se.springdata.repository;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import se.springdata.model.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long>{

	@Transactional
	@Modifying
	@Query("update Team t set t.teamName = ?1 WHERE t.id = ?2")
	Integer updateTeam(String teamName, long teamId);
	
	@Transactional
	@Modifying
	@Query("update Team t set t.teamStatus = 'inactive' WHERE t.id = ?1")
	Integer disableTeam(long teamId);
	
	@Transactional
	@Modifying
	@Query("update Team t set t.teamStatus = 'active' WHERE t.id = ?1")
	Integer enableTeam(long teamId);
	
	@Transactional
	@Modifying
	@Query("update User u set u.team.id = ?1 WHERE u.id = ?2")
	Integer addUserToTeam(long teamId, long userId); 
	
	@Query("select t.teamStatus from Team t where t.id = ?1")
	String findTeamStatusById(long teamId);
	
	@Query("select t.teamName from Team t where t.teamName = ?1") 
	String findByTeamName(String teamName);
	
	@Query("select t.teamName from Team t where t.id = ?1") 
	String findTeamNameById(long teamId);
	
	@Query("select t.teamName from Team t ") 
	Collection<Team> findAllTeamName();
	
	@Query("select t from Team t")
	Collection<Team> findAllTeams(); 

	@Query("select u.team.id from User u where team_id = ?1")
	ArrayList<Long> findAllUserForTeamById(long teamId);
	
}