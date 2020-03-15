package com.xll.common.utils.base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class SecurityUtil {
    public static Color[] background = new Color[]{new Color(132, 132, 132)};
    public static Color[] words = new Color[]{new Color(255, 255, 255)};
    public static Color[] line;
    public static Random random;

    public SecurityUtil() {
    }

    public static void generateSecurityImage(OutputStream os, boolean needConfuse, String sRand, int numLength) throws IOException {
        int width = numLength * 20;
        int height = 25;
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics g = image.getGraphics();
        g.setColor(getRandColor(background));
        g.fillRect(0, 0, width, height + 10);
        g.setFont(new Font("Arial", 0, 18));
        g.setColor(Color.white);
        g.drawRect(0, 0, width - 1, height - 1);
        int i;
        int drawY;
        if (needConfuse) {
            g.setColor(getRandColor(line));

            for(i = 0; i < 2; ++i) {
                i = random.nextInt(width);
                drawY = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(i, drawY, xl, yl);
            }
        }

        char[] chars = sRand.toCharArray();

        for(i = 0; i < chars.length; ++i) {
            drawY = random.nextInt(6);
            g.setColor(getRandColor(words));
            g.drawString(String.valueOf(chars[i]), 20 * i + 5, 14 + drawY);
        }

        ImageIO.write(image, "JPEG", os);
    }

    private static Color getRandColor(Color[] colorRanges) {
        int j = (int)(Math.random() * (double)colorRanges.length);
        return colorRanges[j];
    }

    static {
        line = words;
        random = new Random();
    }
}

