package se.mebe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.springdata.model.Status;

public final class WorkItem {

	private long id;
	private final String title;
	private final String description;
	private final Status status;

	public WorkItem(@JsonProperty("id") long id, @JsonProperty("title") String title,
			@JsonProperty("description") String description, @JsonProperty("status") Status status) {
		this.id = id;
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

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "WorkItem [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status + "]";
	}

}
