package com.github.xianzhan.swing.util;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xianzhan
 * @since 2018-03-22
 */
public class ImageUtils {

    /**
     * 图片缩放
     *
     * @param width       缩放后长度
     * @param height      缩放后高度
     * @param prototypeIS 原图片流
     * @return 缩放后图片
     * @throws IOException 异常
     */
    public static BufferedImage zoom(double width, double height, InputStream prototypeIS) throws IOException {
        BufferedImage prototypeImage = ImageIO.read(prototypeIS);

        double sx = width / prototypeImage.getWidth();
        double sy = height / prototypeImage.getHeight();

        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);

        return op.filter(prototypeImage, null);
    }

    /**
     * 图片旋转
     *
     * @param degree      旋转角度
     * @param prototypeIS 原图片流
     * @return 旋转后的图片
     * @throws IOException 异常
     */
    public static BufferedImage spin(int degree, InputStream prototypeIS) throws IOException {
        int sWidth; // 旋转后的宽度
        int sHeight; // 旋转后的高度
        int x; // 原点横坐标
        int y; // 原点纵坐标


        BufferedImage bi = ImageIO.read(prototypeIS); // 读取该图片
        // 处理角度--确定旋转弧度
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double theta = Math.toRadians(degree);// 将角度转为弧度

        // 确定旋转后的宽和高
        if (degree == 180 || degree == 0 || degree == 360) {
            sWidth = bi.getWidth();
            sHeight = bi.getHeight();
        } else if (degree == 90 || degree == 270) {
            sHeight = bi.getWidth();
            sWidth = bi.getHeight();
        } else {
            sWidth = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                                              + bi.getHeight() * bi.getHeight()));
            sHeight = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                                               + bi.getHeight() * bi.getHeight()));
        }

        x = (sWidth / 2) - (bi.getWidth() / 2);// 确定原点坐标
        y = (sHeight / 2) - (bi.getHeight() / 2);

        BufferedImage spinImage = new BufferedImage(sWidth, sHeight,
                                                    bi.getType());

        AffineTransform at = new AffineTransform();
        at.rotate(theta, sWidth / 2, sHeight / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(bi, spinImage);
    }
}
