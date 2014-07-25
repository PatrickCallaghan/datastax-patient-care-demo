package com.datastax.patientcare.model;

/**
 *  
 create table allergies(
	user_id text,
	allergy_name text,
	allergy_type text,
	severity text,
	PRIMARY KEY (user_id, allergy_name)
);
 * @author patrickcallaghan
 *
 */
public class Allergy {

	private String userId;
	private String name;
	private String type;
	private String severity;
	
	public Allergy(){}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		return "Allergy [userId=" + userId + ", name=" + name + ", type=" + type + ", severity=" + severity + "]";
	}	
}
