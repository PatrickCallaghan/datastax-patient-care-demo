package com.datastax.patientcare.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.datastax.patientcare.dao.PatientCareDao;
import com.datastax.patientcare.model.Allergy;
import com.datastax.patientcare.model.GPVisit;
import com.datastax.patientcare.model.Medication;
import com.datastax.patientcare.model.User;

@Path("/patient")
public class PatientWebService {
	
	private PatientCareDao patientCareDao = new PatientCareDao(new String[]{"127.0.0.1"});

	@GET
	@Path("/get/user/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("user_id") String userId){
		
		User user = patientCareDao.getUser(userId);
		
		return user;
	}

	@GET
	@Path("/get/medications/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Medication> getMedications(@PathParam("user_id") String userId){
		
		List<Medication> medications = patientCareDao.getMediciations(userId);
		
		return medications;
	}
	
	@GET
	@Path("/get/gpvisits/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GPVisit> getGPVisits(@PathParam("user_id") String userId){
		
		List<GPVisit> visits = patientCareDao.getGPVisits(userId);
		
		return visits;
	}

	@GET
	@Path("/get/allergies/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Allergy> getAllergies(@PathParam("user_id") String userId){
		
		List<Allergy> allergies = patientCareDao.getAllergies(userId);
		
		return allergies;
	}
	
}
