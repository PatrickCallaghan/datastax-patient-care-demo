package com.datastax.bulkloader;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;
import com.datastax.sampledata.CreateSSTables;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	private static int ONE_MILLION = 1000000;

	private static CQL3BulkLoadGPVisits bulkLoaderGPVisits;
	private static CQL3BulkLoadMedications bulkLoaderMedications;
	private static CQL3BulkLoadAllergies bulkLoaderAllergies;
	private static CQL3BulkLoadUsers bulkLoaderUsers;

	public Main() {

		String host = PropertyHelper.getProperty("jmxhost", "localhost");
		int port = Integer.parseInt(PropertyHelper.getProperty("jmxport", "7199"));
		int noOfRows = Integer.parseInt(PropertyHelper.getProperty("noOfRows", "1000000"));
		int batches = 0;

		// Initialise loaders
		try {
			bulkLoaderGPVisits = new CQL3BulkLoadGPVisits();
			bulkLoaderMedications = new CQL3BulkLoadMedications();
			bulkLoaderAllergies = new CQL3BulkLoadAllergies();
			bulkLoaderUsers = new CQL3BulkLoadUsers();
		} catch (IOException e) {

			e.printStackTrace();
			System.exit(1);
		}

//		if (noOfRows > ONE_MILLION) {
//			batches = noOfRows / ONE_MILLION;
//		}
//
//		logger.info("Running in " + batches + " batches");
//
//		for (int i = 0; i < batches; i++) {
//			logger.info("Running batch " + (i + 1));
//			runLoaders(host, port, ONE_MILLION);
//		}
//		logger.info("Running last batch with " + noOfRows % ONE_MILLION + " rows");

		runLoaders(host, port, noOfRows);
		
		System.exit(0);
	}

	private void runLoaders(String host, int port, int noOfRows) {

		if (noOfRows < 1)
			return;

		//createUsers(host, port, noOfRows);
		//createGPVisits(host, port, noOfRows);
		createMedications(host, port, noOfRows);
		createAllergies(host, port, noOfRows);
	}

	private void createGPVisits(String host, int port, int noOfRows) {
		try {
			Timer timer = new Timer();
			timer.start();

			JmxBulkLoader jmxLoader = new JmxBulkLoader(host, port);
			jmxLoader.deleteFiles(bulkLoaderGPVisits.getFilePath().getAbsolutePath());

			logger.info("Creating SSTables for GP Visits");
			CreateSSTables.createSSTablesGPVisits(bulkLoaderGPVisits, noOfRows);
			logger.info("Finished creating SSTables");

			logger.info("Running Bulk Load via JMX for " + bulkLoaderGPVisits.getFilePath().getAbsolutePath());
			jmxLoader.bulkLoad(bulkLoaderGPVisits.getFilePath().getAbsolutePath());
			timer.end();
			logger.info("Finished Bulk Load in " + timer.getTimeTakenSeconds() + " secs.");
			jmxLoader.close();
		} catch (Exception e) {
			logger.error("Could not process GPVisits, JMX Loader due to error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void createMedications(String host, int port, int noOfRows) {
		try {
			Timer timer = new Timer();

			timer.start();
			JmxBulkLoader jmxLoader = new JmxBulkLoader(host, port);
			jmxLoader.deleteFiles(bulkLoaderMedications.getFilePath().getAbsolutePath());

			logger.info("Creating SSTables for Medications");
			CreateSSTables.createSSTablesMedications(bulkLoaderMedications, noOfRows);
			logger.info("Finished creating SSTables");

			logger.info("Running Bulk Load via JMX for " + bulkLoaderMedications.getFilePath().getAbsolutePath());
			jmxLoader.bulkLoad(bulkLoaderMedications.getFilePath().getAbsolutePath());
			timer.end();
			logger.info("Finished Bulk Load in " + timer.getTimeTakenSeconds() + " secs.");

			jmxLoader.close();
		} catch (Exception e) {
			logger.error("Could not process Users, JMX Loader due to error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void createAllergies(String host, int port, int noOfRows) {
		try {
			Timer timer = new Timer();
			timer.start();
			JmxBulkLoader jmxLoader = new JmxBulkLoader(host, port);
			jmxLoader.deleteFiles(bulkLoaderAllergies.getFilePath().getAbsolutePath());

			logger.info("Creating SSTables for Allergies");
			CreateSSTables.createSSTablesAllergies(bulkLoaderAllergies, noOfRows);
			logger.info("Finished creating SSTables");

			logger.info("Running Bulk Load via JMX for " + bulkLoaderAllergies.getFilePath().getAbsolutePath());
			jmxLoader.bulkLoad(bulkLoaderAllergies.getFilePath().getAbsolutePath());
			timer.end();
			logger.info("Finished Bulk Load in " + timer.getTimeTakenSeconds() + " secs.");

			jmxLoader.close();
		} catch (Exception e) {
			logger.error("Could not process Users, JMX Loader due to error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void createUsers(String host, int port, int noOfRows) {
		try {
			Timer timer = new Timer();
			timer.start();

			JmxBulkLoader jmxLoader = new JmxBulkLoader(host, port);
			jmxLoader.deleteFiles(bulkLoaderUsers.getFilePath().getAbsolutePath());

			logger.info("Creating SSTables for Users");
			CreateSSTables.createSSTablesUsers(bulkLoaderUsers, noOfRows);
			logger.info("Finished creating SSTables");

			logger.info("Running Bulk Load via JMX for " + bulkLoaderUsers.getFilePath().getAbsolutePath());
			jmxLoader.bulkLoad(bulkLoaderUsers.getFilePath().getAbsolutePath());
			timer.end();
			logger.info("Finished Bulk Load in " + timer.getTimeTakenSeconds() + " secs.");

			jmxLoader.close();
		} catch (Exception e) {
			logger.error("Could not process Users, JMX Loader due to error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main();		
	}
}
