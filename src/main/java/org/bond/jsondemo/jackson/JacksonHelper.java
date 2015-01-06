package org.bond.jsondemo.jackson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bond.jsondemo.entity.UserInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;

public class JacksonHelper {
	private static final Logger log = LogManager.getLogger(JacksonHelper.class);

	final static ObjectMapper objectMapper;

	/**
	 * 是否打印美观格式
	 */
	static boolean isPretty = true;

	static {
		DefaultSerializerProvider sp = new Impl();

		sp.setNullValueSerializer(NullSerializer.instance);
		objectMapper = new ObjectMapper(null, sp, null);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * 
	 * @param <T>
	 * @param jsonString
	 *            JSON字符串
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToGenericObject(String jsonString, TypeReference<T> tr) {

		if (jsonString == null || "".equals(jsonString)) {
			return null;
		} else {
			try {
				return (T) objectMapper.readValue(jsonString, tr);
			} catch (Exception e) {
				log.error("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Java对象转Json字符串
	 * 
	 * @param object
	 *            Java对象，可以是对象，数组，List,Map等
	 * @return json 字符串
	 */
	public static String toJson(Object object) {
		String jsonString = "";
		try {
			if (isPretty) {
				jsonString = objectMapper.writeValueAsString(object);
			} else {
				jsonString = objectMapper.writeValueAsString(object);
			}
		} catch (Exception e) {
			log.error("json error:" + e.getMessage());
		}
		return jsonString;

	}

	/**
	 * Json字符串转Java对象
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> c) {

		if (jsonString == null || "".equals(jsonString)) {
			return null;
		} else {
			try {
				return objectMapper.readValue(jsonString, c);
			} catch (Exception e) {
				log.error("json error:" + e.getMessage());
			}

		}
		return null;
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
			user.setUserName("张三" + i);
			user.setUserAge(20 + i);
			user.setUserSex("性别" + i);
			user.setUserAddr("上海市杨浦区XXX路123号" + i);
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

		// 单个对象
		// UserInfo user = json2Object(sb.toString(), UserInfo.class);
		// if (user != null) {
		// System.out.println(user.toString());
		// }
		// 多个对象
		List<UserInfo> user = jsonToGenericObject(sb.toString(), new TypeReference<List<UserInfo>>() {
		});
		if (user != null) {
			for (UserInfo item : user) {
				System.out.println(item.toString());
			}
		}

		System.out.println(sb.toString());

	}

	public static void main(String[] args) {
		test1();
		test2();
	}

}
