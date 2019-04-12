package com.project.base;

import com.project.base.util.AESUtils;
import com.project.base.util.Base64Utils;
import com.project.base.util.KeyGenerator;

/**
 * @ClassName: MainTest
 * @Description:
 * @author: yinshaobo
 * @date: 2019/4/12 15:58
 */
public class MainTest {

    public static void main(String[] args) {
        String s = Base64Utils.encode("abcde");
        System.out.println(s);
        System.out.println(Base64Utils.decode(s));

        String key = KeyGenerator.generate();
        String ss = AESUtils.encrypt(key, "abcde");
        System.out.println(ss);
        System.out.println(AESUtils.decrypt(key, ss));
    }
}
