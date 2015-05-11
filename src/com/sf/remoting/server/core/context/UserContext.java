package com.sf.remoting.server.core.context;

/**
 * 重写这个类，不知道内部调用是否会有问题
 */
public class UserContext {

	public UserContext() {
	}

	public static void setUserSession(String session, String user) {
		sessionTL.set(session);
		userIdTL.set(user);
	}

	public static String getSession() {
		return sessionTL.get();
	}

	public static void setSession(String s) {
		sessionTL.set(s);
	}

	public static String getUserId() {
		return userIdTL.get();
	}

	public static void setUserId(String s) {
		userIdTL.set(s);
	}

	private static ThreadLocal<String> sessionTL = new ThreadLocal<String>();
	private static ThreadLocal<String> userIdTL = new ThreadLocal<String>();

}
