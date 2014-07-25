package com.datastax.patientcare.model;

import java.util.Date;

/**
 * create table medications(
	user_id text,
	start_date timestamp,
	medication_name text,
	end_date timestamp,
	dose text,
	notes text,	
	PRIMARY KEY(user_id, start_date)
)WITH CLUSTERING ORDER BY (start_date desc);

 * @author patrickcallaghan
 *
 */
public class Medication {
	private String userId;
	private Date startDate;
	private String name;
	private Date endDate;
	private String dose;
	private String notes;
	
	public Medication(){}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Medication [userId=" + userId + ", startDate=" + startDate + ", name=" + name + ", endDate=" + endDate
				+ ", dose=" + dose + ", notes=" + notes + "]";
	}
	
	
}
