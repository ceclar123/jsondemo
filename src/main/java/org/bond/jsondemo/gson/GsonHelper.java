package org.bond.jsondemo.gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bond.jsondemo.entity.UserInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonHelper {

	public static String toJson(Object obj) {
		// Gson gson=new Gson();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(obj);
	}

	public static <T> T jsonToObject(Reader rd, TypeToken<T> type) {
		// Gson gson=new Gson();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(rd, type.getType());
	}

	public static <T> T json2Object(Reader rd, Class<T> c) {
		// Gson gson=new Gson();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(rd, c);
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
			user.setUserName("李四" + i);
			user.setUserAge(20 + i);
			user.setUserSex("性别" + i);
			user.setUserAddr("上海市虹口区XXX路123号" + i);

			cal.add(Calendar.DAY_OF_MONTH, i);
			user.setBirthDay(cal.getTime());

			list.add(user);
		}

		String rtn = toJson(list);
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
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("C:\\json.json"));

			// 单个对象
			// UserInfo user = json2Object(reader, UserInfo.class);
			// if (user != null) {
			// System.out.println(user.toString());
			// }
			// 多个对象
			List<UserInfo> user = jsonToObject(reader, new TypeToken<List<UserInfo>>() {
			});
			if (user != null) {
				for (UserInfo item : user) {
					System.out.println(item.toString());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		test1();
		test2();
	}

}
