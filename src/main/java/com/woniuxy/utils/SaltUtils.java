package com.woniuxy.utils;



import java.util.Random;

//设置一个加盐工具类
public class SaltUtils {

    public static  String getSalt(int count){
        //创建一个字符数组
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=~!@#$%^&*()_+".toCharArray();

        //创建一个stringbuilder
        StringBuilder sb = new StringBuilder();

        //通过输入的次数随机生成字符串
        for (int i=0;i<count;i++){
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }

        //将字符串返回
        return sb.toString();
    }
}
