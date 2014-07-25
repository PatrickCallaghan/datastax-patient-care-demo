package com.datastax.sampledata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.bulkloader.CQL3BulkLoadAllergies;
import com.datastax.bulkloader.CQL3BulkLoadGPVisits;
import com.datastax.bulkloader.CQL3BulkLoadMedications;
import com.datastax.bulkloader.CQL3BulkLoadUsers;
import com.datastax.demo.utils.LoadUser;
import com.datastax.demo.utils.SampleData;
import com.datastax.patientcare.model.Allergy;
import com.datastax.patientcare.model.GPVisit;
import com.datastax.patientcare.model.Medication;
import com.datastax.patientcare.model.User;

public class CreateSSTables {

	private static Logger logger = LoggerFactory.getLogger(CreateSSTables.class);
	private static Long userId = 1l;
	private static int batch = 5000;

	public static void createSSTablesUsers(CQL3BulkLoadUsers bulkLoader, int totalUsers) throws IOException {

		int cycles = totalUsers / batch;

		List<User> mainBatch = LoadUser.processUserFile("user_list_final.csv");

		logger.info("Starting " + cycles + " cycles.");
		for (int i = 0; i < cycles; i++) {

			List<User> users = randomize(mainBatch);
			bulkLoader.load(users);

			logger.info("Wrote " + (i+1) + " of " + cycles + " cycles. Batch size : " + batch);
		}
		bulkLoader.finish();

		logger.info("Finished file with " + totalUsers + " users.");
	}

	private static List<User> randomize(List<User> mainBatch) {
		List<User> results = new ArrayList<User>();

		int batchSize = mainBatch.size();

		for (int i = 0; i < batchSize; i++) {

			String first = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getFirstname();
			String middleName = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getMiddlename();
			String last = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getLastname();
			String county = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getCountyName();
			String country = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getCountryCode();
			Date dob = randomDateInLastNYears(100);
			String streetAddress = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getStreetAddress();
			String email = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getEmail();
			String gender = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getGender();
			String postCode = mainBatch.get((new Double(Math.random() * batchSize).intValue())).getPostCode();

			String passport = (buffer(userId));
			String driversLicense = "" + dob.getTime();

			User user = new User();
			user.setUserId("U" + userId);
			user.setFirstname(first);
			user.setMiddlename(middleName);
			user.setLastname(last);
			user.setPassport(passport);
			user.setDriversLicense(driversLicense);
			user.setStreetAddress(streetAddress);
			user.setEmail(email);
			user.setCountryCode(country);
			user.setCountyName(county);
			user.setDob(dob);
			user.setGender(gender);
			user.setPhoneNumber(new Double(100000000 + Math.random() * 900000000).toString());
			user.setPostCode(postCode);

			results.add(user);
			userId++;
		}

		return results;
	}

	private static String buffer(Long userId) {

		String result = "" + userId;

		while (result.length() < 12) {
			result = "0" + result;
		}

		return result;
	}

	private static Date randomDateInLastNYears(int years) {

		DateTime dateTime = new DateTime();
		dateTime = dateTime.minusYears(new Double(Math.random() * years).intValue());
		dateTime = dateTime.minusMonths(new Double(Math.random() * 12).intValue());
		dateTime = dateTime.minusDays(new Double(Math.random() * 30).intValue());

		dateTime = dateTime.minusHours(new Double(Math.random() * 24).intValue());
		dateTime = dateTime.minusMinutes(new Double(Math.random() * 60).intValue());
		dateTime = dateTime.minusSeconds(new Double(Math.random() * 60).intValue());

		return dateTime.toDate();
	}

	public static void createSSTablesGPVisits(CQL3BulkLoadGPVisits bulkLoader, int noOfRows) throws IOException {

		int cycles = noOfRows / batch;

		int noOfVisits = new Double(Math.random() * 10).intValue();
		logger.info("Starting " + cycles + " cycles.");
		
		for (int i = 0; i < cycles; i++) {
			List<GPVisit> visits = createRandomVisits(noOfVisits, noOfRows);
			bulkLoader.load(visits);			
			logger.info("Wrote " + (i+1) + " of " + cycles + " cycles. Batch size : " + batch);
		}

		bulkLoader.finish();
	}

	private static List<GPVisit> createRandomVisits(int noOfVisits, int noOfRows) {

		List<GPVisit> visits = new ArrayList<GPVisit>();

		for (int y = 0; y < batch; y++) {

			int userIdRandom = new Double(Math.random() * noOfRows).intValue();

			for (int i = 0; i < noOfVisits; i++) {

				GPVisit visit = new GPVisit();

				visit.setUserId("U" + userIdRandom);
				visit.setDateTime(randomDateInLastNYears(3));
				visit.setName("GP" + new Double(Math.random() * 10000).intValue());
				visit.setAddress(new Double(Math.random() * 10000).intValue() + " Doctors Lane, Wimbledon.");
				visit.setNotes(SampleData.notes.get(new Double(Math.random()
						* SampleData.notes.size()).intValue()));
				visit.setReason(SampleData.reasons.get(new Double(Math.random()
						* SampleData.reasons.size()).intValue()));

				visits.add(visit);
			}
		}

		return visits;
	}

	public static void createSSTablesMedications(CQL3BulkLoadMedications bulkLoader, int noOfRows) throws IOException {

		int cycles = noOfRows / batch;

		int noOfMedications = new Double(Math.random() * 5).intValue();
		int userIdRandom = new Double(Math.random() * noOfRows).intValue();

		//bulkLoader.initWriter();
		
		logger.info("Starting " + cycles + " cycles.");
		for (int i = 0; i < cycles; i++) {
			List<Medication> medications = createRandomMedications(noOfMedications, userIdRandom);
			bulkLoader.load(medications);
			
			logger.info("Wrote " + (i+1) + " of " + cycles + " cycles. Batch size : " + batch);
		}

		bulkLoader.finish();
	}

	private static List<Medication> createRandomMedications(int noOfMedications, int noOfRows) {

		List<Medication> medications = new ArrayList<Medication>();

		for (int y = 0; y < batch; y++) {

			int userIdRandom = new Double(Math.random() * noOfRows).intValue();

			for (int i = 0; i < noOfMedications; i++) {

				Medication medication = new Medication();

				String name = SampleData.medications.get(new Double(Math.random() * SampleData.medications.size())
						.intValue());

				if (!name.equals("")) {
					medication.setUserId("U" + userIdRandom);
					medication.setStartDate(randomDateInLastNYears(2));
					medication.setName(name);
					medication.setDose(SampleData.doses.get(new Double(Math.random() * SampleData.doses.size())
							.intValue()));
					medication.setNotes(SampleData.notes.get(new Double(Math.random() * SampleData.notes.size())
					.intValue()));

					if (Math.random() > .5) {
						medication.setEndDate(randomDateInLastNYears(1));
					}

					medications.add(medication);
				}
			}
		}
		return medications;
	}

	public static void createSSTablesAllergies(CQL3BulkLoadAllergies bulkLoader, int noOfRows) throws IOException {

		int cycles = noOfRows / batch;

		int noOfAllergies = 2;

		//bulkLoader.initWriter();
		
		logger.info("Starting " + cycles + " cycles.");
		for (int i = 0; i < cycles; i++) {
			List<Allergy> allergies = createRandomAllergies(noOfAllergies, noOfRows);
			bulkLoader.load(allergies);
			
			logger.info("Wrote " + (i+1) + " of " + cycles + " cycles. Batch size : " + batch);			
		}

		bulkLoader.finish();
	}

	private static List<Allergy> createRandomAllergies(int noOfAllergies, int noOfRows) {

		List<Allergy> allergies = new ArrayList<Allergy>();

		for (int y = 0; y < batch; y++) {

			int userIdRandom = new Double(Math.random() * noOfRows).intValue();

			for (int i = 0; i < noOfAllergies; i++) {

				String name = SampleData.allergies.get(new Double(Math.random() * SampleData.allergies.size())
						.intValue());

				if (!name.equals("")) {

					Allergy allergy = new Allergy();
					allergy.setName(name);
					allergy.setType("Common");
					allergy.setSeverity(SampleData.severities.get(new Double(Math.random()
							* SampleData.severities.size()).intValue()));
					allergy.setUserId("U" + userIdRandom);
					allergies.add(allergy);
				}
			}
		}
		return allergies;
	}
}
