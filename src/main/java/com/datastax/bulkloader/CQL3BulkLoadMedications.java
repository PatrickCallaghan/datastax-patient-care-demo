package com.datastax.bulkloader;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.cassandra.exceptions.InvalidRequestException;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.patientcare.model.Medication;

/**
 * To run this code, you need to have your cluster 'cassandra.yaml' and
 * 'log4j-tools.properties' in the 'src/main/resources' directory.
 * 
 */
public class CQL3BulkLoadMedications{

	private static Logger logger = LoggerFactory.getLogger(CQL3BulkLoadMedications.class);
	
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private DateFormat monthYearFormat = new SimpleDateFormat("yyyyMM");

	private File filePath;
	private String keyspace = "datastax_patient_care_demo";
	private String tableName = keyspace + ".medications";

	private String schema = "create table " + tableName + ""
			+ "(user_id text,start_date timestamp,medication_name text,end_date timestamp,dose text,"
			+ "notes text,PRIMARY KEY(user_id, start_date))WITH CLUSTERING ORDER BY (start_date desc)";
	
	private String INSERT_INTO_MEDICATION_TABLE = "Insert into " + tableName +"(user_id, start_date, medication_name, end_date, dose, notes) values (?,?,?,?,?,?)";
	private CQLSSTableWriter writer;

	public CQL3BulkLoadMedications() throws IOException {
		
		logger.info("Using CQL3 Writer");
		
		createDirectories(keyspace, tableName);
		initWriter();
	}
	
	public void initWriter() {
		this.writer = CQLSSTableWriter.builder()
				.forTable(schema)
				.using(INSERT_INTO_MEDICATION_TABLE)
				.inDirectory(getFilePath().getAbsolutePath())
				.build();
	}

	
	public File getFilePath(){
		return this.filePath;
	}

	private void createDirectories(String keyspace, String tableName) {
		File directory = new File(keyspace);
		if (!directory.exists())
			directory.mkdir();
		
		filePath = new File(directory, tableName);
		if (!filePath.exists())
			filePath.mkdir();
	}

	/**
	 * 
	 */
	public void load(List<Medication> medications)
			throws IOException {

		for (Medication medication : medications) {
			
			try {
				this.writer.addRow(medication.getUserId(),
						medication.getStartDate(),
						medication.getName(),
						medication.getEndDate(),
						medication.getDose(),
						medication.getNotes());
				
			} catch (InvalidRequestException e) {
				e.printStackTrace();				
				System.exit(5);
			}	
		}
	}
	
	public void finish() throws IOException {
		writer.close();
	}

}