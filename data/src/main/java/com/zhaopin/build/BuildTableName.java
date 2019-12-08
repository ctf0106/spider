package com.zhaopin.build;

import java.util.ArrayList;
import java.util.List;

public class BuildTableName {
	private static int tabCount = 10;
	private static String tabPrefix = "HtmlInfo_";

	public BuildTableName() {

	}

	public BuildTableName(int tabCount) {
		BuildTableName.tabCount = tabCount;
	}

	public BuildTableName(int tabCount, String tabPrefix) {
		BuildTableName.tabCount = tabCount;
		BuildTableName.tabPrefix = tabPrefix;
	}

	/**
	 * 通过htmlInfoId获取HtmlInfo的数据库名称
	 * 
	 * @param htmlInfoId
	 * @return
	 */
	public static String getTabName(long htmlInfoId) {
		return tabPrefix + htmlInfoId % tabCount;
	}

	/**
	 * 获取全部HtmlInfo数据库名称
	 *
	 * @return
	 */
	public static List<String> getAllHtmlInfoTabNames() {
		List<String> htmlTabNames = new ArrayList<String>();
		for (int i = 0; i < tabCount; i++) {
			htmlTabNames.add(tabPrefix + i);
		}
		return htmlTabNames;
	}
}
