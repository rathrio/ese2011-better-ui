package controllers;

import models.User;

public class Security extends Secure.Security {
	
	public static boolean authenticate(String username, String password) {
		User user = User.find("byName", username).first();
		return user!=null && user.password.equals(password);
	}
	
	public static void onDisconnected() {
		Welcome.index();
	}

}
