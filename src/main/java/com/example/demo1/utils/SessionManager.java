// SessionManager.java (Located in com.example.demo1.utils package)

package com.example.demo1.utils;

import com.example.demo1.User; // আপনার User ক্লাস আমদানি করুন

public class SessionManager {
    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
        System.out.println("Session started for user: " + (user != null ? user.getUsername() : "null"));
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }
    // SessionManager.java

    public static void clearSession() {
        loggedInUser = null;
        System.out.println("🛑 Session cleared. Caller: " + new Throwable().getStackTrace()[1].getClassName()); // 🛑 এই লাইনটি যোগ করুন
    }


}