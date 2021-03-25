package com.project.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author yinshaobo at 2021/1/18 19:27
 */
public class MathUtils {

    public static void main(String[] args) {
        System.out.println(threePositionSeg(new BigDecimal("1234567.126")));
    }

    /**
     * 数字三位分节法，保留两位小数（小数四舍五入）
     * @param data 1000000.126
     * @return 1,000,000.13
     */
    public static String threePositionSeg(BigDecimal data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);
    }

}
