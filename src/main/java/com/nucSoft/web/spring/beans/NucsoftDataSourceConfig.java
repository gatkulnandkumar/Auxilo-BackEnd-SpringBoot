package com.nucSoft.web.spring.beans;

public class NucsoftDataSourceConfig {

	private int minIdle;
	private int maxPoolSize;
	private int defaultPoolSize;
	private long idleTimeout;
	private long connectionTimeout;
	private boolean isAutoCommit;
	private String poolName;
	private String jdbcUrl;
	private String serverName;
	private String port;
	private String databaseName;
	private String user;
	private String password;
	private String connectionTestQuery;
	private String driverClassName;
	private String dataSourceClassName;
	
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public int getDefaultPoolSize() {
		return defaultPoolSize;
	}
	public void setDefaultPoolSize(int defaultPoolSize) {
		this.defaultPoolSize = defaultPoolSize;
	}
	public long getIdleTimeout() {
		return idleTimeout;
	}
	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}
	public long getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public boolean isAutoCommit() {
		return isAutoCommit;
	}
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConnectionTestQuery() {
		return connectionTestQuery;
	}
	public void setConnectionTestQuery(String connectionTestQuery) {
		this.connectionTestQuery = connectionTestQuery;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getDataSourceClassName() {
		return dataSourceClassName;
	}
	public void setDataSourceClassName(String dataSourceClassName) {
		this.dataSourceClassName = dataSourceClassName;
	}
	
	
}
