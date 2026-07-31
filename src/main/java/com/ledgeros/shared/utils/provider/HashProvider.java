package com.ledgeros.shared.utils.provider;


import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class HashProvider {
    private static final int COST = 12;

    public static boolean verifyHash(String secret, String hash) {
        return BCrypt.verifyer()
                .verify(secret.toCharArray(), hash)
                .verified;
    }

    public static String hash(String secret) {
        return BCrypt.withDefaults()
                .hashToString(COST, secret.toCharArray());
    }
}
