package org.maptalks.javasdk.utils;

import java.util.List;

public class ArrayUtils {
	/**
	 *
	 * @param strArr
	 * @return
	 */
	public static String join(String[] strArr) {
		if (strArr == null)
			return null;
		StringBuilder ret = new StringBuilder();
		for (int i = 0, len = strArr.length; i < len; i++) {
			ret.append(strArr[i]);
			if (i < len - 1) {
				ret.append(",");
			}
		}
		return ret.toString();
	}

	/**
	 *
	 * @param strArr
	 * @return
	 */
	public static String join(List<String> strArr) {
		if (strArr == null)
			return null;
		StringBuilder ret = new StringBuilder();
		for (int i = 0, len = strArr.size(); i < len; i++) {
			ret.append(strArr.get(i));
			if (i < len - 1) {
				ret.append(",");
			}
		}
		return ret.toString();
	}
}
