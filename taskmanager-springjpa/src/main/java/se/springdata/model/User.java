package se.springdata.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public final class User extends AbstractEntity {

	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "USER_NAME", unique = true)
	private String userName;
	@Column(name = "USER_STATUS", nullable = false)
	private String userStatus;
	@Column(name = "USER_NUMBER", nullable = false)
	private String userNumber;

	@OneToMany(mappedBy = "user")
	private Collection<Workitem> workitem = new ArrayList<Workitem>();

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, name = "TEAM_ID")
	private Team team;

	protected User() {
	}

	public User(String firstName, String lastName, String userName, String userStatus, String userNumber) {

		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.userStatus = userStatus;
		this.userNumber = userNumber;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public Collection<Workitem> getWorkitem() {
		return workitem;
	}

	public Team getTeam() {
		return team;
	}

	@Override
	public String toString() {
		return " User [id=" + getId() + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName
				+ ", userStatus=" + userStatus + ", teamId=" + team.getId() + "]\n";
	}
}
