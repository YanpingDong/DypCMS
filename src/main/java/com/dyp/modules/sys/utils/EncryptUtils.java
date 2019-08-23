package com.dyp.modules.sys.utils;

import com.dyp.common.security.Digests;
import com.dyp.common.utils.Encodes;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: xu.dm
 * @Date: 2018/9/25 23:01
 * @Description:
 */
public class EncryptUtils {
    public static String ALGORITHM_NAME = "md5";
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    public static String salt = "8d78869f47095133";

    public static String encrypt(String str, String salt, String algorithmName, int hashIterations)
    {
        return new SimpleHash(algorithmName, str, ByteSource.Util.bytes(salt), hashIterations).toString();
    }


    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */

    public static String encryptPassword(String plainPassword) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(SALT_SIZE);

        String hashedPassword = encrypt(plain, new String(salt), ALGORITHM_NAME, HASH_INTERATIONS);

        //Encodes.encodeHex(salt)的长度是16位
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashedPassword.getBytes());
    }

    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Encodes.decodeHex(password.substring(0,16));
        String hashedPassword = encrypt(plain, new String(salt), ALGORITHM_NAME, HASH_INTERATIONS);

        return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashedPassword.getBytes()));
    }



    public static String getPasswordPart(String encryptPassword)
    {
        return new String(Encodes.decodeHex(encryptPassword.substring(16)));
    }

    public static byte[] getSaltPart2ByteArry(String encryptPassword)
    {
        System.out.println(encryptPassword.substring(0,16));
        return Encodes.decodeHex(encryptPassword.substring(0,16));
    }

    public static String getSaltPart(String encryptPassword)
    {
        return new String(Encodes.decodeHex(encryptPassword.substring(0, 16)));
    }

    public static void  main(String[] args)
    {

        System.out.println(encryptPassword("admin"));
        System.out.println(encryptPassword("123456"));
    }
}
