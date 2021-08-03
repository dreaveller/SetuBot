package com.ecc.setubot.helper;

import com.ecc.setubot.constant.ConfigConst;
import com.ecc.setubot.constant.ConstantCommon;
import com.ecc.setubot.constant.ImageConst;
import com.ecc.setubot.pojo.ImageInfo;
import com.ecc.setubot.utils.FileUtils;
import com.ecc.setubot.utils.ImageUtils;
import com.ecc.setubot.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

@Component
public class ImageHelper {

    private static Logger logger = LoggerFactory.getLogger(ImageHelper.class);

    /**
     * 压缩图片
     * @param localImagePath 本地文件路径，带文件和后缀的那种
     * @return 本地图片路径
     */
    public String genScaledImage(String localImagePath) throws IOException {
        if (StringUtils.isEmpty(localImagePath)) {
            return "";
        }
        if (!FileUtils.exists(localImagePath)) {
            return "";
        }

        String fileName = FileUtils.getFileName(localImagePath);

        boolean forceScale = ImageConst.ON.equalsIgnoreCase(ConstantCommon.common_config.get(ConfigConst.CONFIG_IMAGE_SCALE_FORCE));
        if (imageNeedScale(ImageUtils.getImageInfo(localImagePath)) || forceScale) {
            //生成修改后的文件名和路径，后缀为jpg
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            String scaleImageName = ImageConst.IMAGE_SCALE_PREFIX + fileName + ".jpg";
            String scaleImagePath = ImageConst.DEFAULT_IMAGE_SCALE_SAVE_PATH + File.separator + scaleImageName;

            if (!FileUtils.exists(scaleImagePath)) {
                Thumbnails.of(localImagePath).scale(1).toFile(scaleImagePath);
            }
            return scaleImagePath;
        }

        return localImagePath;
    }

    /**
     * 随机左上像素点修改
     */
    public File genSafeImage(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int x = Math.min(image.getWidth(), new Random().nextInt(3));
        int y = Math.min(image.getHeight(), new Random().nextInt(3));

        Color color = new Color(image.getRGB(x, y));
        int r = color.getRed();
        if (r == 255) {
            r = 253;
        }
        int g = color.getGreen();
        int b = color.getBlue();
        Color newColor = new Color(r + 1, g, b);
        image.setRGB(x, y, newColor.getRGB());
        File safeImage = new File(ImageConst.TMP_SETU_PATH + File.separator + FileUtils.md5(file) + ".png");
        ImageIO.write(image, "png", safeImage);
        return safeImage;
    }

    private boolean imageNeedScale(ImageInfo imageInfo) {
        boolean overSize = ImageConst.IMAGE_SCALE_MIN_SIZE * 1024 * 1024 < imageInfo.getSize();
        boolean overHeight = ImageConst.IMAGE_SCALE_MIN_HEIGHT < imageInfo.getHeight();
        boolean overWidth = ImageConst.IMAGE_SCALE_MIN_WIDTH < imageInfo.getWidth();
        return overSize || overHeight || overWidth;
    }
}
