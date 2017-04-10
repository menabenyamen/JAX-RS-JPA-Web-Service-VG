package se.springdata.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.springdata.model.Issue;
import se.springdata.model.Workitem;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

	@Transactional
	@Modifying
	@Query("update Issue i set workitem_id = ?1 where i.id = ?2")
	Integer assignWorkitemToIssue(long workitemId, long issueId);

	@Query("select w.status from Workitem w where w.id = ?1 ")
	String findWorkitemStatusById(Long workitemId);

	@Transactional
	@Modifying
	@Query("update Issue i set i.issueReason = ?1 where i.id = ?2")
	Integer updateIssue(String newReson, long issueId);

	@Transactional
	@Modifying
	@Query("update Issue i set i.answer = ?1 where i.id = ?2")
	Integer updateIssueAnswer(String answer, long issueId);

	@Query("select i.id from Issue i")
	Collection<Long> findAllIssueId();

	@Query("select i.workitem from Issue i")
	Collection<Workitem> findAllWorkitemFromIssue();
	
	@Query("select i.workitem.id from Issue i")
	Workitem findWorkitemFromIssue();
	
	

}