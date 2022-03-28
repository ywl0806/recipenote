package com.example.recipenote.service.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public final class SaveImage {

    private static final String localResourcePath = "C:/Users/ywl08/resource/";

    public static String saveImage(MultipartFile image, String imagePath) throws IOException {

        String imageExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String uploadId = UUID.randomUUID().toString() + "." + imageExtension;//image fileのname

        String realDir = localResourcePath + imagePath +"/"+ uploadId; //実際のPath

        try {
            InputStream inputStream = image.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            inputStream.close();
            File file = new File(realDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            ImageIO.write(bufferedImage, Objects.requireNonNull(imageExtension),file);
        }catch (IOException e){
            throw new IOException("fail to upload");
        }
        return "/upload" + imagePath + "/" + uploadId;
    }

    public static String saveThumbnail(MultipartFile image, String imagePath, int width, int height){

        String imageExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String uploadId = UUID.randomUUID().toString() + "." + imageExtension;//image fileのname
        String realDir = localResourcePath + imagePath + "/" + uploadId; //実際のPath

        try {
            //imageを scaling
            InputStream inputStream = image.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            BufferedImage thumbnailImage = Thumbnails.of(bufferedImage).size(width,height).outputQuality(1.0f).asBufferedImage();
            inputStream.close();
            //　fileを作る
            File file = new File(realDir);
            if (!file.exists()){
                file.mkdirs();
            }
            //scalingしたimageを出力
            ImageIO.write(thumbnailImage, Objects.requireNonNull(imageExtension),file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/upload" + imagePath + "/" + uploadId;
    }

}
