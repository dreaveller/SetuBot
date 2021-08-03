package com.ecc.setubot.test;

import com.ecc.setubot.constant.ImageConst;
import com.ecc.setubot.utils.FileUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImagePixelTest {
    @Test
    void setPixel() throws IOException {
        File file = new File(ImageConst.SETU_PATH + File.separator + "1.jpg");
        BufferedImage image = ImageIO.read(file);
        int height = image.getHeight();
        int width = image.getWidth();
        int x = new Random().nextInt(3), y = new Random().nextInt(3);
        x = x > width ? width : x;
        y = y > height ? height : y;
        int rgb = image.getRGB(x, y);
        Color color = new Color(rgb);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        Color newColor = new Color(r + 1, g, b);
        image.setRGB(x, y, newColor.getRGB());
        File file1 = new File(ImageConst.SETU_PATH + File.separator + "tmptmptmptmptmp.png");
        ImageIO.write(image, "png", file1);

        System.out.println(FileUtils.md5(file1));
        System.out.println(FileUtils.md5(file));
    }
}
