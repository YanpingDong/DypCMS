package com.dyp.modules.sys.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: xu.dm
 * @Date: 2018/9/25 23:01
 * @Description:
 */
public class EncryptUtils {
    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    public static String encrypt(String str,String salt, String algorithmName, int hashIterations)
    {
        return new SimpleHash(algorithmName,str, ByteSource.Util.bytes(salt),hashIterations).toString();
    }

    public static void  main(String[] args)
    {
        System.out.println(EncryptUtils.encrypt("admin", "123", algorithmName, hashIterations));
    }

}
