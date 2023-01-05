package com.example.totp;

import static com.example.totp.Utils.hexStr2Bytes;
import static com.example.totp.Utils.hmac_sha;

public class Totp {
    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     *
     * @param key
     * @param time
     * @param returnDigits
     * @param crypto
     * @return
     */
    public static String generateTOTP(String key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits);
        String result = null;

        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16)
            timeBuilder.insert(0, "0");
        time = timeBuilder.toString();

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }
}
