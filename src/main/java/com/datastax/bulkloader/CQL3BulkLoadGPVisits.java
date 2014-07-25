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

import com.datastax.patientcare.model.GPVisit;

/**
 * To run this code, you need to have your cluster 'cassandra.yaml' and
 * 'log4j-tools.properties' in the 'src/main/resources' directory.
 * 
 */
public class CQL3BulkLoadGPVisits{

	private static Logger logger = LoggerFactory.getLogger(CQL3BulkLoadGPVisits.class);
	
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private DateFormat monthYearFormat = new SimpleDateFormat("yyyyMM");

	private File filePath;
	private String keyspace = "datastax_patient_care_demo";
	private String tableName = keyspace + ".gpvisits";

	private String schema = "create table " + tableName + 
			"(user_id text,date_time timestamp,gp_name text,gp_address text,reason text,"
			+ "gp_notes text,PRIMARY KEY (user_id, date_time)) WITH CLUSTERING ORDER BY (date_time desc);";
	
	private String INSERT_INTO_GPVISITS_TABLE = "INSERT into " + tableName + " (user_id, date_time, gp_name, gp_address, reason, gp_notes) values (?,?,?,?,?,?)";
	private CQLSSTableWriter writer;

	public CQL3BulkLoadGPVisits() throws IOException {
		
		logger.info("Using CQL3 Writer");
		
		createDirectories(keyspace, tableName);	
		initWriter();
	}
	
	public void initWriter(){
		this.writer = CQLSSTableWriter.builder()
				.forTable(schema)
				.using(INSERT_INTO_GPVISITS_TABLE)
				.inDirectory(getFilePath().getAbsolutePath())
				.withBufferSizeInMB(20)
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
	public void load(List<GPVisit> visits)
			throws IOException {

		for (GPVisit visit: visits) {
			
			try {
				this.writer.addRow(visit.getUserId(),
						visit.getDateTime(),
						visit.getName(),
						visit.getAddress(),
						visit.getReason(),
						visit.getNotes()
						);
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