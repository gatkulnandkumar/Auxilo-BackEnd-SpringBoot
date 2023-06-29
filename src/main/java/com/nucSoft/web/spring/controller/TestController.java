package com.nucSoft.web.spring.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
 

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private Mono<Boolean> checkApiStatus(String apiEndpoint) {
	    WebClient client = WebClient.create();

	    return client.head()
	            .uri(apiEndpoint)
	            .retrieve()
	            .toBodilessEntity()
	            .map(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
	            .onErrorReturn(false);
	}
	
	@GetMapping("/fetchAllConstants")
	public Mono<ResponseEntity<Map<String, Map<String, Map<String, String>>>>> fetchAllConstants() {
		String sql = "SELECT * FROM fetchallconstants()";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		Map<String, Map<String, Map<String, String>>> response = new ConcurrentHashMap<>();

		return Flux.fromIterable(rows).parallel().runOn(Schedulers.parallel()).flatMap(row -> {
			String table_Name = (String) row.get("table_name");
			String service_Type = (String) row.get("servicetype");

			Map<String, String> serviceData = response.computeIfAbsent(table_Name, k -> new HashMap<>())
					.computeIfAbsent(service_Type, k -> new HashMap<>());

			String key = (String) row.get("key");
			String value = (String) row.get("value");
			serviceData.put(key, value);

			if (key.equals("sourceurl")) {
				String apiEndpoint = value;
				return checkApiStatus(apiEndpoint).map(isActive -> {
					serviceData.put("apiStatus", isActive ? "active" : "inactive");
					return row;

				});
			} else {
				return Mono.just(row);
			}
		})
				.sequential().collectList().map(ignored -> ResponseEntity.ok(response));
	}
	 

	@GetMapping("/fetchAllConstants/{tableName}/{serviceType}")
	public ResponseEntity<Map<String, String>> fetchAllConstantsById(@PathVariable String tableName,@PathVariable String serviceType) {
		String sql = "SELECT key,value FROM fetchallconstants() where table_name='" + tableName + "' and servicetype='"
				+ serviceType + "'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		Map<String, String> response = new HashMap<>();
		for (Map<String, Object> row : rows) {

			String key = (String) row.get("key");
			String value = (String) row.get("value");
			response.put(key, value);
		}

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/updateConstant")
	public ResponseEntity<Map<String, String>> updateConstant(@RequestBody Map<String, Object> request) {
	    String tableName = request.get("table_name").toString();
	    String serviceType = request.get("service_type").toString();

	    if (tableName == null || tableName.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "tableName is required");
	        return ResponseEntity.badRequest().body(response);
	    }

	    if (serviceType == null || serviceType.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "serviceType is required");
	        return ResponseEntity.badRequest().body(response);
	    }

	    String userIdSql = "SELECT id FROM users WHERE username = 'Admin'";
	    Integer userId = jdbcTemplate.queryForObject(userIdSql, Integer.class);

	    Map<String, Object> otherData = (Map<String, Object>) request.get("otherData");

	    int updatedRows = 0;

	    for (Map.Entry<String, Object> entry : otherData.entrySet()) {
	        String key = entry.getKey();
	        String newValue = entry.getValue().toString();

	        String selectOldValueSql = "SELECT value FROM " + tableName + " WHERE servicetype = ? AND key = ?";
	        String oldValue = jdbcTemplate.queryForObject(selectOldValueSql, String.class, serviceType, key);

	        if (oldValue != null && !newValue.equals(oldValue)) {
	            String updateSql = "UPDATE " + tableName + " SET value = ? WHERE servicetype = ? AND key = ?";
	            int updatedRowCount = jdbcTemplate.update(updateSql, newValue, serviceType, key);

	            if (updatedRowCount > 0) {
	                String insertSql = "INSERT INTO audit_log (table_name, record_id, service_type, key, old_value, new_value, action, updated_by, updated_date) " +
	                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	                Object[] insertParams = {tableName, updatedRowCount, serviceType, key, oldValue, newValue, "update", userId, LocalDateTime.now()};
	                jdbcTemplate.update(insertSql, insertParams);

	                updatedRows++;
	            }
	        }
	    }

	    if (updatedRows > 0) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Constant updated successfully");
	        return ResponseEntity.ok(response);
	    }

	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Constant not found or not updated");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	  
		
      @GetMapping("/fetchallServiceLog")
	  public ResponseEntity<Map<String, Map<String, List<Map<String, Object>>>>> fetchAllServiceLog() {
	      String sql = "SELECT * FROM fetchallservicelog()";
	      List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

	      Map<String, Map<String, List<Map<String, Object>>>> response = new HashMap<>();
	      for (Map<String, Object> row : rows) {
	          String table_Name = (String) row.get("table_name");
	          String service_Type = (String) row.get("servicetype");
	          String transactionID = (String) row.get("transactionid");

	          if (table_Name != null && service_Type != null && transactionID != null) {
	              Map<String, List<Map<String, Object>>> tableData = response.computeIfAbsent(table_Name, k -> new HashMap<>());
	              List<Map<String, Object>> serviceData = tableData.computeIfAbsent(service_Type, k -> new ArrayList<>());

	              Map<String, Object> transactionData = new HashMap<>();
	              transactionData.put("id", row.get("id"));
	              transactionData.put("requestdata", row.get("requestdata"));
	              transactionData.put("responsedata", row.get("responsedata"));
	              transactionData.put("updatedtime", row.get("updatedtime"));
	              transactionData.put("createdtime", row.get("createdtime"));
	              transactionData.put("username", row.get("username"));
	              transactionData.put("clientid", row.get("clientid"));
	              transactionData.put("transactionid", row.get("transactionid"));

	              serviceData.add(transactionData);
	          }
	      }

	      return ResponseEntity.ok(response);
	  }
	  
      @GetMapping("/fetchallServiceLog/{tableName}/{serviceType}")
      public ResponseEntity<List<Map<String, String>>> fetchallServiceLogById(@PathVariable String tableName,
              @PathVariable String serviceType) {
          String sql = "SELECT id, requestdata, responsedata, "
                  + "TO_TIMESTAMP((CAST(updatedtime AS BIGINT)) / 1000) AS updatedtime, "
                  + "TO_TIMESTAMP((CAST(createdtime AS BIGINT)) / 1000) AS createdtime, "
                  + "username, clientid, transactionid "
                  + "FROM " + tableName
                  + " WHERE servicetype = ?";

          List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serviceType);

          List<Map<String, String>> response = new ArrayList<>();
          for (Map<String, Object> row : rows) {
              Map<String, String> entry = new HashMap<>();
              entry.put("id", row.get("id").toString());
              entry.put("requestdata", (String) row.get("requestdata"));
              entry.put("responsedata", (String) row.get("responsedata"));
              entry.put("updatedtime", row.get("updatedtime").toString());
              entry.put("createdtime", row.get("createdtime").toString());
              entry.put("username", (String) row.get("username"));
              entry.put("clientid", (String) row.get("clientid"));
              entry.put("transactionid", (String) row.get("transactionid"));

              response.add(entry);
          }

          return ResponseEntity.ok(response);
      } 
      
	  
      @GetMapping("/fetchallAuditLog/{tableName}/{serviceType}")
      public ResponseEntity<List<Map<String, String>>> fetchAllAuditLogById(@PathVariable String tableName,
              @PathVariable String serviceType) {
          String sql = "SELECT al.table_name, al.record_id, al.service_type, al.key, al.old_value, al.new_value, al.action, u.username as updated_by, al.updated_date "
                  + "FROM audit_log al "
                  + "JOIN users u ON al.updated_by = u.id "
                  + "WHERE al.table_name = ? AND al.service_type = ?";

          List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, tableName, serviceType);

          List<Map<String, String>> response = new ArrayList<>();
          for (Map<String, Object> row : rows) {
              Map<String, String> entry = new HashMap<>();
              entry.put("table_name", (String) row.get("table_name"));
              entry.put("record_id", row.get("record_id").toString());
              entry.put("service_type", (String) row.get("service_type"));
              entry.put("key", (String) row.get("key"));
              entry.put("old_value", (String) row.get("old_value"));
              entry.put("new_value", (String) row.get("new_value"));
              entry.put("action", (String) row.get("action"));
              entry.put("updated_by", row.get("updated_by").toString());
              entry.put("updated_date", ((Timestamp) row.get("updated_date")).toString());
              response.add(entry);
          }

          return ResponseEntity.ok(response);
      }
      
      @GetMapping("/fetchServiceLogByDateRange/{tableName}/{serviceType}/{startDate}/{endDate}")
      public ResponseEntity<List<Map<String, String>>> fetchServiceLogsByDateRange(
          @PathVariable String tableName,
          @PathVariable String serviceType,
          @PathVariable @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
          @PathVariable @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate
      ) {
          String sql = "SELECT id, requestdata, responsedata, "
              + "TO_TIMESTAMP((CAST(updatedtime AS BIGINT)) / 1000) AS updatedtime, "
              + "TO_TIMESTAMP((CAST(createdtime AS BIGINT)) / 1000) AS createdtime, "
              + "username, clientid, transactionid "
              + "FROM " + tableName
              + " WHERE servicetype = ?::text"
              + " AND TO_TIMESTAMP((CAST(createdtime AS BIGINT)) / 1000) >= ?"
              + " AND TO_TIMESTAMP((CAST(createdtime AS BIGINT)) / 1000) <= ?"
              + " AND TO_TIMESTAMP((CAST(updatedtime AS BIGINT)) / 1000) >= ?"
              + " AND TO_TIMESTAMP((CAST(updatedtime AS BIGINT)) / 1000) <= ?";

          LocalDateTime startDateTime = startDate.atStartOfDay();
          LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

          List<Map<String, Object>> rows = jdbcTemplate.queryForList(
              sql,
              serviceType,
              startDateTime,
              endDateTime,
              startDateTime,
              endDateTime
          );

          List<Map<String, String>> response = new ArrayList<>();
          for (Map<String, Object> row : rows) {
              Map<String, String> entry = new HashMap<>();
              entry.put("id", row.get("id").toString());
              entry.put("requestdata", (String) row.get("requestdata"));
              entry.put("responsedata", (String) row.get("responsedata"));
              entry.put("updatedtime", row.get("updatedtime").toString());
              entry.put("createdtime", row.get("createdtime").toString());
              entry.put("username", (String) row.get("username"));
              entry.put("clientid", (String) row.get("clientid"));
              entry.put("transactionid", (String) row.get("transactionid"));

              response.add(entry);
          }
          return ResponseEntity.ok(response);
      }
      




}
