package com.knubisoft;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        BufferedImage img1 = ImageIO.read(new File("src/main/resources/test1.jpg"));
        BufferedImage img2 = ImageIO.read(new File("src/main/resources/test2.jpg"));
        ImageIO.write(new CompareUtils().compareImages(img1, img2), "png", new File("src/main/resources/output.png"));
    }
}
