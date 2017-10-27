package com.aaron.huifubao_pay;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by yangbin on 16/9/14.
 */
public class HfbUtils {


    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create HEX String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String sTmp = Integer.toHexString(0xFF & messageDigest[i]);
                switch (sTmp.length()) {
                    case 0:
                        hexString.append("00");
                        break;
                    case 1:
                        hexString.append("0");
                        hexString.append(sTmp);
                        break;
                    default:
                        hexString.append(sTmp);
                        break;
                }
            }

            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String generateUserIdentity(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        String m_sPhoneID = tm.getDeviceId();
        if (!TextUtils.isEmpty(m_sPhoneID)) {
            return md5(m_sPhoneID);
        } else {
            return md5(UUID.randomUUID().toString());
        }
    }
}
