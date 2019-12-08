package com.zpcampus.crawler.platform.dataaccess;

import com.zhaopin.build.BuildTableName;
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

	private final static String newDataBaseSql = readFile("NewDataBase.sql");
	private final static String dropDataBaseSql = readFile("DropDataBase.sql");
	private final static String fetchConfigSql = readFile("FetchConfig.sql");
	private final static String fetchQueueNamePoolsSql = readFile("FetchQueueNamePools.sql");
	private final static String fetchPageAndHtmlTotalSql = readFile("FetchPageAndHtmlTotal.sql");
	private final static String pageInfoSql = readFile("PageInfo.sql");
	private final static String htmlInfoSql = readFile("HtmlInfo.sql");
	private final static String jobInfoSql = readFile("JobInfo.sql");

	private DynamicDbConfig config = null;
	private String mysqlUrl = "jdbc:mysql://{dbServerIp}:{dbPort}/";

	public static void main(String[] args) {

	}

	public void createCrawlerDB() {
		buildDataBase();
		buildTable(fetchConfigSql);
		buildTable(fetchQueueNamePoolsSql);
		buildTable(fetchPageAndHtmlTotalSql);
		buildTable(pageInfoSql);
		for (int i = 0; i < 10; i++) {
			buildTable(htmlInfoSql.replace("{HtmlInfoName}",
					BuildTableName.getTabName(i)));
		}
		buildTable(jobInfoSql);
	}

	public void createCrawlerDB(String customSqlFileName) {
		createCrawlerDB();
		createCustomDB(customSqlFileName);
	}

	public void createCustomDB(String customSqlFileName) {
		try {
			String customSql = readFile(customSqlFileName);
			if (StringUtils.isNotBlank(customSql)) {
				buildTable(customSql);
			}
		} catch (Exception ex) {
			logger.error(
					"DataBaseFactory.createCustomDB exec customSql error!", ex);
		}
	}

	public DataBaseFactory(DynamicDbConfig config) throws Exception {
		if (config != null) {
			this.config = config;
			this.mysqlUrl = this.mysqlUrl.replace("{dbServerIp}",
					config.getDbServerIp()).replace("{dbPort}",
					config.getDbPort());
		} else {
			throw new Exception(
					"DataBaseFactory.init:DynamicDbConfig config is null!");
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
			logger.error("CreateDataBase.buildDataBase getDbDriver is error!",
					ex);
			return false;
		}
		try {
			String databaseSql = newDataBaseSql.replace("{DataBaseName}",
					this.config.getDbName());
			conn = DriverManager.getConnection(this.mysqlUrl + "mysql",
					this.config.getDbUserName(), this.config.getDbPassword());
			Statement smt = conn.createStatement();
			if (conn != null) {
				System.out.println("数据库连接成功!");
				smt.executeUpdate(databaseSql);
			}
		} catch (SQLException ex) {
			logger.error("CreateDataBase.buildDataBase execute sql is error!",
					ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(
						"CreateDataBase.buildDataBase close  conn is error!", e);
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
			logger.error("CreateDataBase.dropDataBase getDbDriver is error!",
					ex);
			return false;
		}
		try {
			String databaseSql = dropDataBaseSql.replace("{DataBaseName}",
					dataBaseName);
			conn = DriverManager.getConnection(this.mysqlUrl + "mysql",
					this.config.getDbUserName(), this.config.getDbPassword());
			Statement smt = conn.createStatement();
			if (conn != null) {
				System.out.println("数据库连接成功!");
				smt.executeUpdate(databaseSql);
			}
		} catch (SQLException ex) {
			logger.error("CreateDataBase.dropDataBase execute sql is error!",
					ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(
						"CreateDataBase.buildDataBase close  conn is error!", e);
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
			logger.error("CreateDataBase.buildTable getDbDriver is error!", ex);
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
			logger.error("CreateDataBase.buildTable execute sql is error!", ex);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("CreateDataBase.buildTable close  conn is error!",
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