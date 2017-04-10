package se.mebe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Issue {

	private long id;
	private final String answer;
	private final String issueReason;

	public Issue(@JsonProperty("id") long id, @JsonProperty("answer") String answer,
			@JsonProperty("issueReason") String issueReason) {
		this.id = id;
		this.answer = answer;
		this.issueReason = issueReason;
	}

	public String getAnswer() {
		return answer;
	}

	public String getIssueReason() {
		return issueReason;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + ", answer=" + answer + ", issueReason=" + issueReason + "]";
	}

}
