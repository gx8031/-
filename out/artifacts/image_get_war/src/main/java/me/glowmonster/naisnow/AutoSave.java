package me.glowmonster.naisnow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AutoSave {

    public void autoDownload(String[] detailUrls, String imageCssQuery, String browser, String attributeName, String pageTotalCssQuery,
                                          String pageTitleCssQuery, String baseSaveDirectory) {
        try {
            int imageTotal = 0;
            for (String detailUrl: detailUrls) {
                System.out.println(">>> 当前页面地址: " + detailUrl);
                Document detailDocument = Jsoup.connect(detailUrl).userAgent(browser).get();
                // 获取页面的页数
                Element pagesElement = detailDocument.select(pageTotalCssQuery).first();
                // 获取页面标题
                Element pageTitleElement = detailDocument.select(pageTitleCssQuery).first();
                String pageTitle = pageTitleElement.text();
                // 文件不能包含的字符
                String[] canNotS = {"\\", "/", ":", "*", "?", "\"", "<", ">", "|"};
                boolean hasNotString = false;
                for(String canNot: canNotS) {
                    if (pageTitle.contains(canNot)) {
                        hasNotString = true;
                        break;
                    }
                }
                if (hasNotString) {
                    pageTitle = Math.floor(Math.random() * 1000) + "-" + imageTotal + "-" + Math.floor(Math.random() * 1000);
                }

                int pageTotal = Integer.parseInt(pagesElement.text());
                int imageIndex = 1;
                for (int temp = 1; temp <= pageTotal; temp++) {
                    String tempString = detailUrl;
                    // 获取页面的图片元素
                    Elements hasImageUrlElements = detailDocument.select(imageCssQuery);
                    System.out.println("----第" + temp + "页----");
                    if (temp != 1) {
                        StringBuffer reBlend = new StringBuffer(tempString);
                        reBlend.insert(reBlend.lastIndexOf("."), "_" + temp);
                        detailDocument = Jsoup.connect(reBlend.toString()).userAgent(browser).get();
                    }
                    for (Element hasImageUrlElement: hasImageUrlElements) {
                        String imageUrl = hasImageUrlElement.attr(attributeName);
                        imageTotal++;
                        System.out.println("总第" + imageTotal + "张" + imageUrl);

                        // 文件后缀名
                        String fileSuffix = imageUrl.substring(imageUrl.lastIndexOf("."));

                        // 保存的路径
                        File saveDirectory = new File(baseSaveDirectory + "\\" + pageTitle);
                        if (!saveDirectory.exists()) {
                            saveDirectory.mkdirs();
                        }

                        File imageFile = new File(saveDirectory + "", imageIndex + fileSuffix);
                        imageFile.createNewFile();
                        imageIndex++;


                        URL imageRealUrl = new URL(imageUrl);
                        URLConnection imageFileConnection = imageRealUrl.openConnection();
                        imageFileConnection.setRequestProperty("User-agent", browser);
                        InputStream fileInputStream = imageFileConnection.getInputStream();
                        OutputStream fileOutputStream = new FileOutputStream(imageFile);

                        int tempByte = fileInputStream.read();
                        while (tempByte != -1) {
                            fileOutputStream.write(tempByte);
                            tempByte = fileInputStream.read();
                        }
                        fileInputStream.close();
                        fileOutputStream.close();
                        System.out.println("> 第" + (imageIndex - 1) + "张传输完毕");
                    }
                }
                System.out.println(">>> 已完成一套");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("All download is done!");
    }


}
