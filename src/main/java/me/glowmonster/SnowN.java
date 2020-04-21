package me.glowmonster;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SnowN {
    // 还没写该页面, 因为懒加载和页面事件模拟无法实现, 除非前后端交互, 但是工作量非常大, 暂时不写
    public static void main(String[] args) {
        // 主页面
        String mainUrl = "https://www.naixue.org/rosi.html";
        // 要下载的目录
        File downloadBasePath = new File("D:\\Acg\\snow-N\\");
        if (!downloadBasePath.exists()) {
            downloadBasePath.mkdirs();
        }
        try {
            Document doc = Jsoup.connect(mainUrl).userAgent("Mozilla").get();
            // 获取页码集合 ArrayList
            Elements pagesDigit = doc.select(".pagination > li > a");
            // 获取最后一页页码的 a 元素
            Element lastDigitLinksElement = pagesDigit.get(pagesDigit.size() - 2);

            // 获取页码字符串
            String lastDigitString = lastDigitLinksElement.text();
            // 将最后页码字符串转换为 integer 类型
            int lastDigitInt = Integer.parseInt(lastDigitString);
            // System.out.println(lastDigitInt);



            // 解析并重组页码前缀地址
            String lastPageLink = lastDigitLinksElement.attr("href");
            // 页面前缀链接 link
            String pagePreLink = lastPageLink.substring(0, lastPageLink.lastIndexOf("" + lastDigitInt));
            // System.out.println(pagePreLink);
            int preDigit = 0;
            for (int temp = 1; temp <= lastDigitInt; temp++) {
                String pageUrl = pagePreLink + temp;
                System.out.println(">>> 当前页链接" + pageUrl);
                doc = Jsoup.connect(pageUrl).userAgent("Mozilla").get();

                // 获取页面上所有的 详细内容链接 link
                Elements links = doc.select("div.thumb > a");
                for (Element link : links) {
                    // 图片文件数字名称
                    int imageQuantity = 1;
                    String linkHref = link.attr("href");
                    System.out.println(">>> 当前detail connect 地址为:" + linkHref);
                    Document pageDetail = Jsoup.connect(linkHref).userAgent("Mozilla").get();
                    Elements pageTitleElement = pageDetail.select(".blog-post h4 a");
                    // detailPageTitle
                    // String pageTitle = preDigit + pageTitleElement.get(0).text().substring(0, 1);
                    String pageTitle = preDigit + "";
                    preDigit++;
                    System.out.println("标题为:" + pageTitle);
                    // images 图片的地址集合
                    Elements pageImagesLink = pageDetail.select(".blog-post p a");
                    // 遍历集合并准备下载
                    for (Element pageImageLink : pageImagesLink) {
                        System.out.print("开始下载第" + imageQuantity + "张.");
                        // System.out.println(pageImageLink.attr("href"));
                        String imageLink = pageImageLink.attr("href");
                        String fileSuffix = imageLink.substring(imageLink.lastIndexOf("."));
                        // System.out.println(fileSuffix);
                        // 创建图片存储的路径
                        File imageSavePath = new File(downloadBasePath + "\\" + pageTitle);
                        if (!imageSavePath.exists()) {
                            imageSavePath.mkdirs();
                        }
                        String imageSavePathString = downloadBasePath + "\\" + pageTitle;
                        System.out.println("存储路径为: " + imageSavePathString);
                        // 创建的图片名称
                        String imageName = imageQuantity + fileSuffix;
                        imageQuantity++;
                        File imageFile = new File(imageSavePathString, imageName);
                        imageFile.createNewFile();
                        // 创建图片 url
                        URL webSiteFileUrl = new URL(imageLink);
                        // 创建链接
                        URLConnection webSiteFileUrlConnection = webSiteFileUrl.openConnection();
                        webSiteFileUrlConnection.setRequestProperty("User-agent", "Mozilla");
                        // 文件输入流
                        InputStream fileInputStream = webSiteFileUrlConnection.getInputStream();
                        // 文件输出流
                        OutputStream fileOutputStream = new FileOutputStream(imageFile);
                        int tempByte = fileInputStream.read();
                        while (tempByte != -1) {
                            fileOutputStream.write(tempByte);
                            tempByte = fileInputStream.read();
                        }
                        fileInputStream.close();
                        fileOutputStream.close();
                        System.out.println("Done.");
                    }
                }
            }




        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("已完成所有!!!");
        }
    }
}
