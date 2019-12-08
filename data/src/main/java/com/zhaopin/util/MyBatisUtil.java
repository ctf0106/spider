package com.zhaopin.util;


import com.alibaba.fastjson.JSON;
import com.zhaopin.config.DynamicDbConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MyBatisUtil {
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(MyBatisUtil.class);
	private static final String resource = "mybatis-config.xml";
	private static final String defaultKey = "development"; // "默认数据库配置节点Id";
	private static ConcurrentHashMap<String, SqlSessionFactory> poolMap = new ConcurrentHashMap<String, SqlSessionFactory>();

	public static SqlSessionFactory getSqlSessionFactory() {
		return getSqlSessionFactory(defaultKey);
	}

	public static void main(String[] args) {

	}

	public synchronized static final SqlSessionFactory getSqlSessionFactory(
			String environment) {
		Reader reader = null;
		try {
			if (!poolMap.containsKey(environment)) {
				reader = Resources.getResourceAsReader(resource);
				poolMap.put(environment, new SqlSessionFactoryBuilder().build(
						reader, environment));
			}
		} catch (Exception ex) {
			logger.error("ConnectionPool.getSqlSessionFactory is error!", ex);
		}
		return poolMap.get(environment);
	}

	public synchronized static final SqlSessionFactory getSqlSessionFactory(
			DynamicDbConfig config) {
		String key = JSON.toJSONString(config);
		try {
			if (!poolMap.containsKey(key)) {
				String configXml = formatConfigXml(readConfigXml(resource),
						config);
				poolMap.put(key, new SqlSessionFactoryBuilder().build(
						new StringReader(configXml), config.getEnvironment()));
			}
		} catch (Exception ex) {
			logger.error("ConnectionPool.getSqlSessionFactory is error!", ex);
		}
		return poolMap.get(key);
	}

	/**
	 * 使用反射根据属性名称获取属性值
	 */
	public static String formatConfigXml(String configXml,
			DynamicDbConfig config) {
		try {
			Field[] fields = config.getClass().getDeclaredFields();
			for (Field field : fields) {
				configXml = configXml.replace("{" + field.getName() + "}",
						String.valueOf(getFieldValueByName(field.getName(),
								config)));
			}
			return configXml;
		} catch (Exception e) {
			return configXml;
		}
	}

	/**
	 * 使用反射根据属性名称获取属性值
	 *
	 * @return Object 属性值
	 */
	private static <T> Object getFieldValueByName(String fieldName, T entity) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = entity.getClass().getMethod(getter, new Class[] {});
			return method.invoke(entity, new Object[] {});
		} catch (Exception e) {
			return null;
		}
	}

	private static String readConfigXml(String fileName) {
		BufferedReader reader = null;
		String configXml = "";
		try {
			reader = new BufferedReader(
					Resources.getResourceAsReader(fileName), 5 * 1024 * 1024);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				configXml += tempString;
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
		return configXml;
	}

	public static SqlSessionFactory rebuildSqlSessionFactory() {
		return rebuildSqlSessionFactory(defaultKey);
	}

	public synchronized static final SqlSessionFactory rebuildSqlSessionFactory(
			String environment) {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
			poolMap.put(environment,
					new SqlSessionFactoryBuilder().build(reader, environment));
		} catch (Exception ex) {
			logger.error("ConnectionPool.rebuildSqlSessionFactory is error!",
					ex);
		}
		return poolMap.get(environment);
	}

	public synchronized static final SqlSessionFactory rebuildSqlSessionFactory(
			DynamicDbConfig config) {
		String key = JSON.toJSONString(config);
		SqlSessionFactory factory = poolMap.get(key);
		try {
			String configXml = formatConfigXml(readConfigXml(resource), config);
			poolMap.put(key, new SqlSessionFactoryBuilder().build(
					new StringReader(configXml), config.getEnvironment()));
		} catch (Exception ex) {
			logger.error("ConnectionPool.rebuildSqlSessionFactory is error!",
					ex);
		}
		return poolMap.get(key);
	}

	/**
	 * 截取字符串
	 * 
	 * @param text
	 *            要截取的字符串
	 * @param len
	 *            截取长度
	 * @return
	 */
	public static String cutString(String text, int len) {
		if (StringUtils.isNotBlank(text)) {
			if (text.length() > len) {
				return text.substring(0, len);
			}
		}
		return text;
	}
}
