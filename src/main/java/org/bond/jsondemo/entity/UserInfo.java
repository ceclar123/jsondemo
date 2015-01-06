package org.bond.jsondemo.entity;

import java.util.Date;

public class UserInfo {
	private String userName;
	private int userAge;
	private String userSex;
	private Date birthDay;
	private String userAddr;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	@Override
	public String toString() {
		return "UserName:" + userName + "\r\nUserAge:" + userAge + "\r\nUserSex:" + userSex + "\r\nUserAddr:" + userAddr;
	}

}
