package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageDownloader {
    public static void download(URL fileLink, Path filePath) throws IOException {

        if (!Files.exists(filePath.getParent())){
            Files.createDirectories(filePath.getParent());
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        try (InputStream in = fileLink.openStream();
             OutputStream out = new FileOutputStream(filePath.toFile())) {

             in.transferTo(out);

        } catch (Exception e) {
            Files.delete(filePath);
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception{
        URL link = new URL("https://im.kommersant.ru/Issues.photo/CORP/2023/12/11/KMO_128025_07630_1_t247_135426.webp");
        Path path = Path.of("D:\\test\\myFile.jpg");

        if (!Files.exists(path.getParent())){
            Files.createDirectories(path.getParent());
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        ImageDownloader.download(link, path);
    }
}
