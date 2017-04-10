package se.springdata.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public final class Team extends AbstractEntity {

	@Column(unique = true, name = "TEAM_NAME")
	private String teamName;

	@Column(name = "TEAM_STATUS")
	private String teamStatus;

	@OneToMany(mappedBy = "team")
	private Collection<User> user = new ArrayList<User>();

	protected Team() {
	}

	public Team(String teamName, String teamStatus) {

		super();
		this.teamName = teamName;
		this.teamStatus = teamStatus;
	}

	public Long getId() {
		return id;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getTeamStatus() {
		return teamStatus;
	}

	@Override
	public String toString() {
		return "Team [id=" + getId() + ", teamName=" + teamName + ", teamStatus=" + teamStatus + "]";
	}
}