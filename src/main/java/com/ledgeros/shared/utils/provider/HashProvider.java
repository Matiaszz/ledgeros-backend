package com.ledgeros.shared.utils.provider;


import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class HashProvider {
    public static boolean verifyHash(String secret, String hash) {
        Verifyer verifyer = BCrypt.verifyer();
        Result result = verifyer.verify(secret.toCharArray(), hash);

        return result.verified;
    }

    public static String hash(String secret) {

    }
}
