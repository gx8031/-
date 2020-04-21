package me.glowmonster;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SingleSnow {
    public static void main(String[] args) {
        int saveFileDigit = 0;
        String baseUrl = "https://www.naixue.org";
        // 主页面
        String mainUrl = "https://www.naixue.org/rosi.html";
        // 要下载的目录
        File downloadBasePath = new File("D:\\Acg\\snow-single\\rosi");
        if (!downloadBasePath.exists()) {
            downloadBasePath.mkdirs();
        }
        try {
            Document mainDocumnet = Jsoup.connect(mainUrl).userAgent("Mozilla").get();
            Elements imagesLists = mainDocumnet.select(".cxudy-list-formatimage > a");
            for (Element imagesList : imagesLists) {
                int fileNameDigit = 1;
                String listUrl = imagesList.attr("href");
                listUrl = baseUrl + listUrl;
                System.out.println(">>> 正在访问的地址: " + listUrl);
                Document imagesPage = Jsoup.connect(listUrl).userAgent("Mozilla").get();
                // 获取页数
                String lastPageNumber = imagesPage.select("a.page-numbers:nth-last-of-type(2)").get(0).text();
                // System.out.println(lastPageNumber);
                for (int temp = 1; temp <= Integer.parseInt(lastPageNumber); temp++) {
                    if (temp == 1) {
                        imagesPage = Jsoup.connect(listUrl).userAgent("Mozilla").get();
                        // String baseBehind = imagesList.attr("href");
                    } else {
                        StringBuffer reBlend = new StringBuffer(imagesList.attr("href"));
                        reBlend.insert(reBlend.lastIndexOf("."), "_" + temp);
                        listUrl = baseUrl + reBlend.toString();
                        imagesPage = Jsoup.connect(listUrl).userAgent("Mozilla").get();
                    }

                    Elements imagesLinkElements = imagesPage.select(".image_div > p > a");
                    for (Element imageLinkElement : imagesLinkElements) {
                        System.out.println("正在传输第" + fileNameDigit + "张");
                        String imageLink = imageLinkElement.attr("href");
                        System.out.println("图片地址为: " + imageLink);
                        String fileSuffix = imageLink.substring(imageLink.lastIndexOf("."));
                        File imageSavePath = new File(downloadBasePath + "\\" + saveFileDigit);
                        if (!imageSavePath.exists()) {
                            imageSavePath.mkdirs();
                        }
                        File imageFile = new File(imageSavePath + "", fileNameDigit + fileSuffix);
                        fileNameDigit++;
                        imageFile.createNewFile();
                        URL webFileImageUrl = new URL(imageLink);
                        URLConnection webFileConnection = webFileImageUrl.openConnection();
                        webFileConnection.setRequestProperty("User-agent", "Mozilla");
                        InputStream fileInputStream = webFileConnection.getInputStream();
                        OutputStream fileOutputStream = new FileOutputStream(imageFile);
                        int tempByte = fileInputStream.read();
                        while (tempByte != -1) {
                            fileOutputStream.write(tempByte);
                            tempByte = fileInputStream.read();
                        }
                        fileInputStream.close();
                        fileOutputStream.close();
                        System.out.println("Done");
                    }
                }
                saveFileDigit++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("All done.");
        }
    }

}
