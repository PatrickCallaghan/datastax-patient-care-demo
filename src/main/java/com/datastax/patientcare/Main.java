package com.datastax.patientcare;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;
import com.datastax.patientcare.dao.PatientCareDao;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private DateTime date;
	private static int BATCH = 10000;

	public Main() {

		// Create yesterdays date at midnight
		this.date = new DateTime().minusDays(1).withTimeAtStartOfDay();

		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		String noOfCreditCardsStr = PropertyHelper.getProperty("noOfCreditCards", "1000");
		String noOfTransactionsStr = PropertyHelper.getProperty("noOfTransactions", "20000");

		PatientCareDao dao = new PatientCareDao(contactPointsStr.split(","));
		
	}

}
