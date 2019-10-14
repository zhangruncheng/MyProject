package com.my.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptDemo {
    public static void main(String[] args) {
        String password = "123456";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashed);
        String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt(12));

        String candidate = "123456";
        if (BCrypt.checkpw(candidate, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
}