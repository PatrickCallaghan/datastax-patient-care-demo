package com.datastax.patientcare.search;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public Main() {

		String urlString = "http://localhost:8983/solr/datastax_patient_care_demo.users";
		SolrServer solr = new HttpSolrServer(urlString);

		String queryString = "passport:*123141";

		SolrQuery query = new SolrQuery();
		query.set("q", queryString);
		query.addFacetField("county_name");

		QueryResponse response;
		try {
			response = solr.query(query);
			SolrDocumentList list = response.getResults();

			logger.info(list.toString());

			for (SolrDocument document : list) {

				logger.info("User ID : " + document.getFieldValue("user_id"));
			}
			
			List<FacetField> facetFields = response.getFacetFields();

			
			for (FacetField facetField : facetFields){
				List<Count> values = facetField.getValues();
				
				for (Count count : values){
					logger.info(count.getName() + " " + count.getCount());
				}
			}

		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
