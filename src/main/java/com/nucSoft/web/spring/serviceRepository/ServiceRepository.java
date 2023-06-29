package com.nucSoft.web.spring.serviceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ServiceRepository {

	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	@Qualifier("pgJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Value("${apiname}")
	private String apiname;

	public Map<String, Map<String, String>> fetchConstantMap() {
		long start_time = System.currentTimeMillis();
		String Query = "select * from " + apiname + "_constants";
		logger.info("Query---->" + Query);
		Map<String, Map<String, String>> constantsMap = new HashMap<>();
		logger.info("constantsMap---->" + constantsMap);
		PreparedStatementCreator statementCreator = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(Query);
				return ps;
			}
		};

		jdbcTemplate.query(statementCreator, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				String entityType = rs.getString("servicetype");
				if (!"".equals(entityType) && !constantsMap.containsKey(entityType)) {
					Map<String, String> temp = new HashMap<String, String>();
					temp.put(rs.getString("key"), rs.getString("value"));
					constantsMap.put(entityType, temp);
				} else {
					constantsMap.get(entityType).put(rs.getString("key"), rs.getString("value"));
				}
				return null;
			}
		});

		long end_time = System.currentTimeMillis();
		logger.info("Time taken to fetch the constant values from database is --->" + (end_time - start_time));
		return constantsMap;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, value = "pgTxnManager", propagation = Propagation.REQUIRED)
	public String insertRecords(Map<String, Object> insertRecordsMap) {
		logger.info("inside insertRecords");
		long start_time = System.currentTimeMillis();
		int rowinserted = 0;
		String Query = "select * from public." + apiname + "_transid(?,?,?,?,?,?,?)";
		String transid = "";
		logger.info(insertRecordsMap);
		try {
			Object[] params = new Object[] { insertRecordsMap.get("requestdata"), insertRecordsMap.get("responsedata"),
					insertRecordsMap.get("updatedtime"), insertRecordsMap.get("createdtime"),
					insertRecordsMap.get("username"), insertRecordsMap.get("clientid"),
					insertRecordsMap.get("servicetype") };

			logger.info("Query----------------------" + Query);
			logger.info("params----------------------" + params);

			transid = (String) this.jdbcTemplate.queryForObject(Query, params, String.class);
			
			logger.info("transid-----------------" + transid);
			long end_time = System.currentTimeMillis();
			logger.info("Time taken to insert the record in database is --->" + (end_time - start_time));
		} catch (Exception e) {
			logger.error("Error while inserting records into database caused by--->", e);
		}
		return transid;
	}
}
