package se.mebe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Team {

	private long id;
	private final String teamName;
	private final String teamStatus;

	public Team(@JsonProperty("id") long id, @JsonProperty("teamName") String teamName,
			@JsonProperty("teamStatus") String teamStatus) {
		this.id = id;
		this.teamName = teamName;
		this.teamStatus = teamStatus;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getTeamStatus() {
		return teamStatus;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", teamName=" + teamName + ", teamStatus=" + teamStatus + "]";
	}

}
