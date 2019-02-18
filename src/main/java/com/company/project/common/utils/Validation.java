/**
 *
 */
package com.company.project.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @file Validation.java
 * @author thon
 * @email thon.ju@gmail.com
 * @date Jul 9, 2013 3:12:00 PM
 * @description TODO
 */
public class Validation {

	private static final String REGEX_EMAIL = "^[A-Za-z0-9][\\w\\-\\.]{1,12}@([\\w\\-]+\\.)+[\\w]{2,3}$";
	private static final String REGEX_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private static final String REGEX_NICKNAME = "^[\u4E00-\u9FA5a-zA-Z0-9]{2,10}$";

	public static Boolean checkEmail(String email) {

		if(email == null) {
			return false;
		}

	    Pattern pattern = Pattern.compile(REGEX_EMAIL);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.find();
	}


	public static Boolean checkPhone(String phone) {

		if(phone == null) {
			return false;
		}

	    Pattern pattern = Pattern.compile(REGEX_PHONE);
	    Matcher matcher = pattern.matcher(phone);
	    return matcher.find();
	}

	public static Boolean checkNickname(String nickname) {

		if(nickname == null) {
			return false;
		}

	    Pattern pattern = Pattern.compile(REGEX_NICKNAME);
	    Matcher matcher = pattern.matcher(nickname);
	    return matcher.find();
	}

}
