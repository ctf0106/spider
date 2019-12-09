package com.zhaopin.build;

import com.zhaopin.config.DynamicDbConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseFactory {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(DataBaseFactory.class);

	private final static String newDataBaseSql = readFile("sql/create_database.sql");
	private final static String dropDataBaseSql = readFile("sql/drop_database.sql");
	private final static String spiderDetail = readFile("sql/spider_detail.sql");
	private final static String spiderList = readFile("sql/spider_list.sql");

	private DynamicDbConfig config = null;
	private String mysqlUrl = "jdbc:mysql://{dbServerIp}:{dbPort}/";

	public void createCrawlerDB() {
		buildDataBase();
		buildTable(spiderList);
		buildTable(spiderDetail);
	}

	public DataBaseFactory(DynamicDbConfig config) throws Exception {
		if (config != null) {
			this.config = config;
			this.mysqlUrl = this.mysqlUrl.replace("{dbServerIp}",
					config.getDbServerIp()).replace("{dbPort}",
					config.getDbPort());
		} else {
			throw new Exception("DataBaseFactory.init:DynamicDbConfig config is null!");
		}
	}

	/**
	 * 创建数据库
	 * 
	 * @return
	 */
	public boolean buildDataBase() {
		Connection conn = null;
		try {
			Class.forName(this.config.getDbDriver());
		} catch (ClassNotFoundException ex) {
			logger.error("buildDataBase getDbDriver is error!", ex);
			return false;
		}
		try {
			String databaseSql = newDataBaseSql.replace("{DataBaseName}",
					this.config.getDbName());
			conn = DriverManager.getConnection(this.mysqlUrl + "mysql",
					this.config.getDbUserName(), this.config.getDbPassword());
			Statement smt = conn.createStatement();
			if (conn != null) {
				smt.executeUpdate(databaseSql);
			}
		} catch (SQLException ex) {
			logger.error("DataBaseFactory.buildDataBase execute sql is error!", ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("DataBaseFactory.buildDataBase close  conn is error!", e);
			}
		}
		return true;
	}

	/**
	 * 创建数据库
	 * 
	 * @return
	 */
	public boolean dropDataBase(String dataBaseName) {
		Connection conn = null;
		try {
			Class.forName(this.config.getDbDriver());
		} catch (ClassNotFoundException ex) {
			logger.error("dropDataBase getDbDriver is error!", ex);
			return false;
		}
		try {
			String databaseSql = dropDataBaseSql.replace("{DataBaseName}",
					dataBaseName);
			conn = DriverManager.getConnection(this.mysqlUrl + "mysql",
					this.config.getDbUserName(), this.config.getDbPassword());
			Statement smt = conn.createStatement();
			if (conn != null) {
				smt.executeUpdate(databaseSql);
			}
		} catch (SQLException ex) {
			logger.error("删除数据库异常!", ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("删除数据库，连接异常", e);
			}
		}
		return true;
	}

	/**
	 * 创建表及存储过程
	 * 
	 * @param buildTableSql
	 * @return
	 */
	public boolean buildTable(String buildTableSql) {
		Connection conn = null;
		try {
			Class.forName(this.config.getDbDriver());
		} catch (ClassNotFoundException ex) {
			logger.error("加载数据库驱动异常......", ex);
			return false;
		}
		try {
			conn = DriverManager.getConnection(
					this.mysqlUrl + this.config.getDbName(),
					this.config.getDbUserName(), this.config.getDbPassword());

			if (conn != null) {
				Statement smt = conn.createStatement();
				String[] sqls = buildTableSql.split("\\$\\$");
				for (String sql : sqls) {
					smt.addBatch(sql);
				}
				smt.executeBatch();
			}
		} catch (SQLException ex) {
			logger.error("创建表单执行sql异常!", ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("创建数表连接异常!",
						e);
			}
		}
		return true;
	}

	private static String readFile(String fileName) {
		if (DataBaseFactory.class.getClassLoader().getResource(fileName) == null) {
			return "";
		}
		BufferedReader reader = null;
		String content = "";
		try {
			reader = new BufferedReader(
					Resources.getResourceAsReader(fileName), 5 * 1024 * 1024);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				content += tempString;
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return content;
	}
}