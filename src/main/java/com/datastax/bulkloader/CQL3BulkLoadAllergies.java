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

import com.datastax.patientcare.model.Allergy;

/**
 * To run this code, you need to have your cluster 'cassandra.yaml' and
 * 'log4j-tools.properties' in the 'src/main/resources' directory.
 * 
 */
public class CQL3BulkLoadAllergies{

	private static Logger logger = LoggerFactory.getLogger(CQL3BulkLoadAllergies.class);
	
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private DateFormat monthYearFormat = new SimpleDateFormat("yyyyMM");

	private File filePath;
	private String keyspace = "datastax_patient_care_demo";
	private String tableName = keyspace + ".allergies";

	private String schema = "create table " + tableName + "(user_id text,allergy_name text,allergy_type text,"
			+ "severity text,PRIMARY KEY (user_id, allergy_name));";
			
	private String INSERT_INTO_ALLERGY_TABLE = "INSERT into " + tableName + " (user_id, allergy_name, allergy_type, severity) VALUES (?, ?, ?, ?)";
	private CQLSSTableWriter writer;

	public CQL3BulkLoadAllergies() throws IOException {
		
		logger.info("Using CQL3 Writer");
		
		createDirectories(keyspace, tableName);
		initWriter();
	}
	
	public void initWriter(){
		this.writer = CQLSSTableWriter.builder()
				.forTable(schema)
				.using(INSERT_INTO_ALLERGY_TABLE)
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
	public void load(List<Allergy> allergies)
			throws IOException {

		for (Allergy allergy : allergies) {
			
			try {
				this.writer.addRow(allergy.getUserId(),
						allergy.getName(),
						allergy.getType(),
						allergy.getSeverity());
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