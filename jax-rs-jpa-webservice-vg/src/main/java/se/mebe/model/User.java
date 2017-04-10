package se.mebe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class User {

	private Long id;
	private final String userNumber;
	private final String firstName;
	private final String lastName;
	private final String userName;
	private final String userState;

	public User(@JsonProperty("id") long id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
			@JsonProperty("userName") String userName, @JsonProperty("userNumber") String userNumber,
			@JsonProperty("userState") String userState) {

		this.id = id;
		this.userNumber = userNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.userState = userState;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public String getUserState() {
		return userState;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName
				+ ", userNumber=" + userNumber + ", userState=" + userState + "]";
	}

}
