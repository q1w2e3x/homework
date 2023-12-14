package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // only works for tags in format <img src="pathToFile/myFile.extension" . . .
        if (args.length > 1) downloadImages(args[0], args[1]);
        else System.out.println("Not enough arguments");
    }

    private static void downloadImages(String url, String folder) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements imgs = doc.select("img");
            Path filePath = Path.of(folder);

            int fileGeneratedIndex = 1;
            for (Element img : imgs) {
                try {
                    String imgLink = img.attr("src");

                    // remove symbols before extension start - first replaceAll(..)
                    // remove possible parameters after ? - second replaceAll(..)
                    String fileExtension = imgLink.replaceAll("^.+\\.", "")
                            .replaceAll("\\?.*$", "")
                            .replace("?.+$", "");

                    String fileName = "img" + fileGeneratedIndex++ + "." + fileExtension;

                    URI imglinkURI = new URI(imgLink);

                    // get full URL to image file
                    URI fullLink;
                    fullLink = imglinkURI.isAbsolute() ? imglinkURI : new URI(url).resolve(imglinkURI);

                    // encoding URL
                    // explanation - https://stackoverflow.com/a/25735202
                    URL temp = fullLink.toURL();
                    fullLink = new URI(temp.getProtocol(), temp.getUserInfo(), temp.getHost(), temp.getPort(),
                                       temp.getPath(), temp.getQuery(), temp.getRef());
                    fullLink = new URI(fullLink.toASCIIString());

                    ImageDownloader.download(fullLink.toURL(), filePath.resolve(fileName));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't fetch the page from " + url);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected exception");
            e.printStackTrace();
        }
    }
}