package org.bond.jsondemo.fastjson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bond.jsondemo.entity.UserInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastjsonHelper {
	/**
	 * 对象bean转json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteNullNumberAsZero);
	}

	/**
	 * json字符串转java独享
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> c) {
		T obj = JSON.parseObject(jsonString, c);
		return obj;
	}

	/**
	 * json字符串转List
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> c) {
		List<T> list = JSON.parseArray(jsonString, c);
		return list;
	}

	private static void test1() {
		UserInfo user = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		Calendar cal = Calendar.getInstance();
		// int year = cal.get(Calendar.YEAR);
		// int month = cal.get(Calendar.MONTH);
		// int day = cal.get(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < 3; i++) {
			user = new UserInfo();
			user.setUserName("李四fastjson" + i);
			user.setUserAge(20 + i);
			user.setUserSex("性别" + i);
			user.setUserAddr("上海市虹口区XXX路123号" + i);

			cal.add(Calendar.DAY_OF_MONTH, i);
			user.setBirthDay(cal.getTime());

			list.add(user);
		}
		String rtn = toJson(user);
		// String rtn = toJson(list);
		System.out.println(rtn);

		FileWriter fw = null;
		try {
			File file = new File("C:\\json.json");
			fw = new FileWriter(file);
			fw.write(rtn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void test2() {
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		try {
			char[] chs = new char[1024];
			File file = new File("C:\\json.json");
			fr = new FileReader(file);
			while (fr.read(chs, 0, chs.length) > 0) {
				sb.append(String.valueOf(chs));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("AAA:" + sb.toString());
		// 单个对象
		UserInfo user = jsonToObject(sb.toString(), UserInfo.class);
		if (user != null) {
			System.out.println(user.toString());
		}
		// 多个对象
		// List<UserInfo> user = jsonToList(sb.toString(), UserInfo.class);
		// if (user != null) {
		// for (UserInfo item : user) {
		// System.out.println(item.toString());
		// }
		// }

		System.out.println(sb.toString());

	}

	public static void main(String[] args) {
		test1();
		test2();
	}

}
