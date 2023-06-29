package com.nucSoft.web.spring.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class WebClientBeans {

	HikariDataSource hikariDs;

	@Bean
	public NucsoftDataSourceFactory nSDSF() {
		return new NucsoftDataSourceFactory();
	}

	@Bean(name = "hikariDs")
	@Primary
	public HikariDataSource pgHikariDs() {
		return new NucsoftDataSourceFactory().fetchDataSource("pg_pff1_api", "ds.json");
	}

	@Bean(name = "pgTxnManager")
	public DataSourceTransactionManager pgTxtManager() {
		return new DataSourceTransactionManager(pgHikariDs());
	}

	@Bean(name = "pgJdbcTemplate")
	public JdbcTemplate pgJdbcTemplate() {
		return new JdbcTemplate(pgHikariDs());
	}

//	@Bean(name = "transactionManager")
//
//	@Primary
//	public PlatformTransactionManager transactionManager() {
//		return new JpaTransactionManager();
//	}

}
