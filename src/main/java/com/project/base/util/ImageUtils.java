package com.project.base.util;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Shaobo Yin at 2019/12/12
 */
public class ImageUtils {

    /**
     * 按照 压缩比例 进行压缩
     *
     * @param imgSrc  源图片地址
     * @param imgDest 目标图片地址
     * @param rate    压缩的比例
     * @throws IOException IOException
     */
    public static void compressImg(String imgSrc, String imgDest, Float rate) throws IOException {
        //获得源图片的宽高存入数组中
        int[] results = getImgWidthHeight(new File(imgSrc));
        //按比例缩放或扩大图片大小，将浮点型转为整型
        int widthDest = (int) (results[0] * rate);
        int heightDest = (int) (results[1] * rate);
        compressImg(imgSrc, imgDest, widthDest, heightDest);
    }

    /**
     * 按照 指定图片宽高 进行压缩
     *
     * @param imgSrc     源图片地址
     * @param imgDest    目标图片地址
     * @param widthDest  压缩后图片的宽度
     * @param heightDest 压缩后图片的高度
     * @throws IOException IOException
     */
    public static void compressImg(String imgSrc, String imgDest, int widthDest, int heightDest) throws IOException {
        FileOutputStream out = null;
        try {
            File srcfile = new File(imgSrc);
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(srcfile);

            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage(widthDest, heightDest, BufferedImage.TYPE_INT_RGB);

            //绘制图像  getScaledInstance表示创建此图像的缩放版本，返回一个新的缩放版本Image,按指定的width,height呈现图像
            //Image.SCALE_SMOOTH,选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            tag.getGraphics().drawImage(src.getScaledInstance(widthDest, heightDest, Image.SCALE_SMOOTH), 0, 0, null);

            //创建文件输出流
            out = new FileOutputStream(imgDest);
            //将图片按JPEG压缩，保存到out中
            ImageIO.write(tag, "jpg", out);

            out.flush();
            tag.flush();
        } finally {
            IOUtils.close(out);
        }
    }

    /**
     * 获取图片的宽高
     *
     * @param file 图片文件
     * @return 图片宽高数组
     * @throws IOException IOException
     */
    public static int[] getImgWidthHeight(File file) throws IOException {
        InputStream is = null;
        int[] result = {0, 0};
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            BufferedImage src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
            return result;
        } finally {
            IOUtils.close(is);
        }
    }

}
