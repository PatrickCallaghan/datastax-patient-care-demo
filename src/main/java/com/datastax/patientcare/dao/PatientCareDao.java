package com.datastax.patientcare.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.patientcare.model.Allergy;
import com.datastax.patientcare.model.GPVisit;
import com.datastax.patientcare.model.Medication;
import com.datastax.patientcare.model.User;

public class PatientCareDao {

	private static Logger logger = LoggerFactory.getLogger( PatientCareDao.class );	
	private Session session;
	
	private DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	private static String keyspaceName = "datastax_patient_care_demo";
	private static String userTable = keyspaceName + ".users";
	private static String gpVisitsTable = keyspaceName + ".gpvisits";
	private static String medicationsTable = keyspaceName + ".medications";
	private static String allergiesTable = keyspaceName + ".allergies";
	
	private String SELECT_USER = "select  * from " + userTable + " where user_id = ?";
	private String SELECT_GPVISITS = "select  * from " + gpVisitsTable + " where user_id = ?";
	private String SELECT_MEDICATIONS = "select  * from " + medicationsTable + " where user_id = ?";
	private String SELECT_ALLERGIES = "select  * from " + allergiesTable + " where user_id = ?";
		
	private PreparedStatement selectUserStmt;
	private PreparedStatement selectGPVisitsStmt;
	private PreparedStatement selectMedicationsStmt;
	private PreparedStatement selectAllergiesStmt;
	
	public PatientCareDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder()				
				.addContactPoints(contactPoints)
				.build();
		
		this.session = cluster.connect();

		this.selectUserStmt = session.prepare(SELECT_USER);
		this.selectGPVisitsStmt = session.prepare(SELECT_GPVISITS);
		this.selectMedicationsStmt = session.prepare(SELECT_MEDICATIONS);
		this.selectAllergiesStmt = session.prepare(SELECT_ALLERGIES);
		
	}
/*
 * 	first_name text,
	middle_name text,
	last_name text,
	passport text, 
	drivers_license text, 
	dob timestamp,
	street_address text,
	post_code text,
	county_name text,
	gender text,
	phone_number text,
	email text,
	country_code text
	
 */
	public User getUser(String userId) {
		
		BoundStatement boundStmt = new BoundStatement(selectUserStmt);
		ResultSet resultSet = session.execute(boundStmt.bind(userId));
				
		Row row = resultSet.one();
		
		User user = new User();
	  	user.setUserId(userId);
		user.setFirstname(row.getString("first_name"));
		user.setMiddlename(row.getString("middle_name"));
		user.setLastname(row.getString("last_name"));
		user.setPassport(row.getString("passport"));
		user.setDriversLicense(row.getString("drivers_license"));
		user.setDob(row.getDate("dob"));
		user.setStreetAddress(row.getString("street_address"));
		user.setPostCode(row.getString("post_code"));
		user.setCountyName(row.getString("county_name"));
		user.setGender(row.getString("gender"));
		user.setPhoneNumber(row.getString("phone_number"));
		user.setEmail(row.getString("email"));
		user.setCountryCode(row.getString("country_code"));
		
		return user;
	}

	public List<Medication> getMediciations(String userId) {
		BoundStatement boundStmt = new BoundStatement(selectMedicationsStmt);
		ResultSet resultSet = session.execute(boundStmt.bind(userId));
		List<Medication> medications = new ArrayList<Medication>();
		
		List<Row> rows = resultSet.all();
		
		for (Row row : rows){
			Medication medication = new Medication();
			medication.setUserId(userId);
			medication.setStartDate(row.getDate("start_date"));
			medication.setEndDate(row.getDate("end_date"));
			medication.setName(row.getString("medication_name"));
			medication.setDose(row.getString("dose"));
			medication.setNotes(row.getString("notes"));			
			
			medications.add(medication);
		}
				
		return medications;
	}

	public List<GPVisit> getGPVisits(String userId) {
		BoundStatement boundStmt = new BoundStatement(selectGPVisitsStmt);
		ResultSet resultSet = session.execute(boundStmt.bind(userId));
		
		List<GPVisit> gpVisits = new ArrayList<GPVisit>();
		
		List<Row> rows = resultSet.all();		
		
		for (Row row : rows){
			GPVisit gpVisit = new GPVisit();
			
			gpVisit.setUserId(userId);
			gpVisit.setDateTime(row.getDate("date_time"));
			gpVisit.setName(row.getString("gp_name"));
			gpVisit.setAddress(row.getString("gp_address"));
			gpVisit.setAddress(row.getString("gp_address"));
			gpVisit.setReason(row.getString("reason"));
			gpVisit.setNotes(row.getString("gp_notes"));
						
			gpVisits.add(gpVisit);
		}
		return gpVisits;
	}

	public List<Allergy> getAllergies(String userId) {
		BoundStatement boundStmt = new BoundStatement(selectAllergiesStmt);
		ResultSet resultSet = session.execute(boundStmt.bind(userId));
		
		List<Allergy> allergies = new ArrayList<Allergy>();
		
		List<Row> rows = resultSet.all();		
		
		for (Row row : rows){
			Allergy allergy = new Allergy();
			
			allergy.setUserId(userId);
			allergy.setName(row.getString("allergy_name"));
			allergy.setType(row.getString("allergy_type"));
			allergy.setSeverity(row.getString("severity"));
			
			allergies.add(allergy);
		}
		
		return allergies;
	}
}
