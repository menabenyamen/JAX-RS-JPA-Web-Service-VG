package se.springdata.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public final class Workitem extends AbstractEntity {

	@Column(name = "TITLE")
	private String title;
	@Column(name = "DESCRIPTION")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "STATUS")
	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "TEAM_ID")
	private Team team;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "USER_ID")
	private User user;

	@OneToOne(mappedBy = "workitem", cascade = CascadeType.REMOVE)
	private Issue issue;

	protected Workitem() {
	}

	public Workitem(String title, String description, Status status) {
		super();
		this.title = title;
		this.description = description;
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Status getStatus() {
		return status;
	}

	public Team getTeam() {
		return team;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Workitem [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", team=" + team + ", user=" + user + ", issue=" + issue + "]";
	}

}