package se.springdata.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public final class Issue extends AbstractEntity {

	@Column(name = "DESCRIPTION")
	private String issueReason;
	@Column(name = "Answer")
	private String answer;

	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKITEM_ID")
	private Workitem workitem;

	protected Issue() {
	}

	public Issue(String answer, String issueReason) {
		super();
		this.issueReason = issueReason;
		this.answer = answer;
	}

	public String getIssueReason() {
		return issueReason;
	}

	public String getAnswer() {
		return answer;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + ", issueReason=" + issueReason + ", answer=" + answer + ", workitem=" + workitem
				+ "]";
	}

}