package com.datastax.patientcare.model;

import java.util.Date;

/**
 * create table gpvisits(
	user_id text,
	date_time timestamp,
	gp_name text,
	gp_address text,
	reason text,
	gp_notes text,	
	PRIMARY KEY (user_id, date_time)
) WITH CLUSTERING ORDER BY (date_time desc);

 * @author patrickcallaghan
 *
 */
public class GPVisit {
	private String userId;
	private Date dateTime;
	private String name;
	private String address;
	private String reason;
	private String notes;
	
	public GPVisit(){}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "GPVisits [userId=" + userId + ", dateTime=" + dateTime + ", name=" + name + ", address=" + address
				+ ", reason=" + reason + ", notes=" + notes + "]";
	}	
}
