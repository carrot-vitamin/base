package com.project.base;

import com.project.base.util.FileUtils;

public class MainTest {

    private static final String PRIVATE_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIJz4Hi+dyGyFNdQ6laxFf4Q+nRrkhNieYSn75RpQ4EJCMdfb+pqdoeMmFaIa76ow9ISS+IbQR3vIz7dUgP4r+VEZJwm/xwNjDhYvvgetrQi9+xqALxA+ZAMS0BdHtzI1yvxYBLsRadSuVtMqFahC9An8k4abtmotCo1u7LPn2AwIDAQAB";

    private static final String PUBLIC_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIgnPgeL53IbIU11DqVrEV/hD6dGuSE2J5hKfvlGlDgQkIx19v6mp2h4yYVohrvqjD0hJL4htBHe8jPt1SA/iv5URknCb/HA2MOFi++B62tCL37GoAvED5kAxLQF0e3MjXK/FgEuxFp1K5W0yoVqEL0CfyThpu2ai0KjW7ss+fYDAgMBAAECgYAnapz1mFYn3ZzVjMbxsr5z8HcUqU/dQo1R9Hcv+XPdi0Yds+UOsfaTMyy8N5eR/YQeCmNVu/IinlWmHPtve5XkQSjGGIFs0jgyRktv0s8w/m6yuL3u/P/5SE07ONwsvDiui3M7oIsFl05d/0g4OAelsyAIypAgKSa8rhpnqrIR+QJBAL58gLZSh+L8PiNxBangT4+JjX0sgWQPaGltkrqYn8JQeHc0RK0K60pFQtBg3aXeErLCDA766G45x0nu3qr3bOUCQQC2+vMrEvBNAHkLS8T92aViUN4zu4Pb3Z4HLPwbNf22L4Be/Avf76x5he095PkqWqlpGGd94krOV4dmjXoJHhDHAkAtCLZNZHmn2ka+jkvue4+TuctvmoQEqfffZP5ZixfKEshINl0+I+S4rGM9bIk6W6RECshbCkrFNHe/LPALr29ZAkEAppjDT2N14FXe31cEKy+/MU15kdZXjpYvbE8b1laJAhgro8+NrDzWZup1/1V7OoHDHsm2aFnhTA9LWybShnyWPwJAFwKeYrg0Bw0NPAZ+nnWEu+7rJMkstaRxjJpY4iShY9x5PTSABGLYxyfO/yW5HAQHbKmpu7szsikr1YH4gBcZEw==";


    public static void main(String[] args) throws Exception {
//        RSAUtils.Key key = RSAUtils.genKeyPair();
//        String priKey = key.getPrivateKeyString();
//        String pubKey = key.getPublicKeyString();
//        System.out.println(pubKey + "\n");
//        System.out.println(priKey + "\n");

        System.out.println(FileUtils.readTextContent("/Users/yin/OneDrive/system.txt"));
    }

}
