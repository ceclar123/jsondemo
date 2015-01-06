package org.bond.jsondemo.jsonlib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bond.jsondemo.entity.UserInfo;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

/**
 * 这个很怪异
 * 
 * @author Administrator
 *
 */
public class JsonlibHelper {
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 单个对象bean转json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String toJson(T obj) {
		JsonConfig config = new JsonConfig();
		// 设置循环策略为忽略
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// 设置 json转换的处理器用来处理日期类型
		// 凡是反序列化Date类型的对象，都会经过该处理器进行处理
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			// 参数1 ：属性名参数2：json对象的值参数3：jsonConfig对象
			public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
				SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
				Date d = (Date) arg1;
				return sdf.format(d);
			}

			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});

		JSONObject json = JSONObject.fromObject(obj, config);
		return json.toString();
	}

	/**
	 * Map转json字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String toJson(Map<String, String> map) {
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

	/**
	 * 数组到字符串
	 * 
	 * @param array
	 * @return
	 */
	public static <T> String toJson(T[] array) {
		JSONArray jsonArray = JSONArray.fromObject(array);
		return jsonArray.toString();
	}

	/**
	 * List到字符串
	 * 
	 * @param list
	 * @return
	 */
	public static <T> String toJson(List<T> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}

	/**
	 * json字符串到普通对象bean
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> pojoCalss) {
		String[] DATE_FORMAT = { DEFAULT_DATE_PATTERN };
		MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
		morpherRegistry.registerMorpher(new DateMorpher(DATE_FORMAT));

		Map<String, Class<Date>> typeMap = new HashMap<String, Class<Date>>();
		typeMap.put("date", Date.class);

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		@SuppressWarnings("unchecked")
		T bean = (T) JSONObject.toBean(jsonObject, pojoCalss, typeMap);
		return bean;
	}

	public static Object[] jsonToArray(String jsonString) {
		return JSONArray.fromObject(jsonString).toArray();
	}

	/**
	 * json转数组
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] jsonToArray(String jsonString, Class<T> pojoCalss) {
		Object[] objs = JSONArray.fromObject(jsonString).toArray();

		T[] array = (T[]) Array.newInstance(pojoCalss, objs.length);
		for (int i = 0; i < objs.length; i++) {
			array[i] = (T) objs[i];
		}

		return array;
	}

	@SuppressWarnings("unchecked")
	public static Collection<Object> jsonToList(String jsonString) {
		return JSONArray.toCollection(JSONArray.fromObject(jsonString));
	}

	/**
	 * json 转List
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToList(String jsonString, Class<T> pojoCalss) {
		List<T> list = (List<T>) JSONArray.toCollection(JSONArray.fromObject(jsonString));
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
			user.setUserName("李四json-lib" + i);
			user.setUserAge(20 + i);
			user.setUserSex("性别" + i);
			cal.add(Calendar.DAY_OF_MONTH, i);
			user.setBirthDay(cal.getTime());
			user.setUserAddr("上海市虹口区XXX路123号" + i);

			list.add(user);
		}

		// String rtn = toJson(user);
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

			// 单个对象
			// UserInfo user = jsonToObject(sb.toString(), UserInfo.class);
			// if (user != null) {
			// System.out.println(user.toString());
			// }
			// 多个对象
			// List<UserInfo> user = jsonToList(sb.toString(), UserInfo.class);
			List<Object> user = (List<Object>) jsonToList(sb.toString());
			// UserInfo[] user = (UserInfo[]) jsonToArray(sb.toString(),
			// UserInfo.class);
			// Object[] user = jsonToArray(sb.toString());
			if (user != null) {
				for (Object dd : user) {
					// UserInfo item = (UserInfo) dd;
					System.out.println("----------------------");
					System.out.println(dd.toString());
				}
			}

		} catch (Exception e) {
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

	}

	public static void main(String[] args) {
		test1();
		test2();
	}

}
