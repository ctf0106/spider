package com.zhaopin.config;

public class DynamicDbConfig {

	private String environment = "dynamic";
	private String dbName = "crawler";
	private String dbServerIp = "127.0.0.1";
	private String dbPort = "3306";
	private String dbUserName = "root";
	private String dbPassword = "root";
	private String transactionType = "MANAGED";
	private String dataSourceType = "POOLED";
	private String dbDriver = "com.mysql.jdbc.Driver";

	public DynamicDbConfig() {
	}

	public DynamicDbConfig(String dbName) {
		this.dbName = dbName;
	}

	public DynamicDbConfig(String dbName, String dbAddress) {
		this(dbName);
		this.dbServerIp = dbAddress;
	}

	public DynamicDbConfig(String dbName, String dbAddress, String dbPort) {
		this(dbName, dbAddress);
		this.dbPort = dbPort;
	}

	public DynamicDbConfig(String dbName, String dbAddress, String dbPort,
			String dbUserName, String dbPassword) {
		this(dbName, dbAddress, dbPort);
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbServerIp() {
		return dbServerIp;
	}

	public void setDbServerIp(String dbServerIp) {
		this.dbServerIp = dbServerIp;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
}
