package me.glowmonster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;

public class PictureLu {

	public static void main(String[] args) {
		// 前缀url,访问大图的地址前缀,地址最后/要加
		String preUrl = "https://mtl.gzhuibei.com/images/img/17806/";
		// 链接是否可以正常访问,如果404了说明往后没图片了
		// boolean is404 = true;
		// 要存入的文件地址 (精) (精+)
		String outputDirectory = "D:\\Acg\\meitulu\\(精+)文件夹名称";
		// 第几张开始
		int temp = 1;
		// 文件类型
		String fileType = ".jpg";
		for (; true; temp++) {
			String url = preUrl + temp + fileType;
			try {
				Jsoup.connect(url).userAgent("Mozilla").timeout(3000).get();
			} catch (IOException e) {
				if ("HTTP error fetching URL".equals(e.getMessage())) {
					System.out.println("访问到404,终止!");
					break;
				} else {
					System.out.print("正在获取第" + temp + "张...");
					try {
						// url地址
						URL fileUrl = new URL(url);
						// 创建url连接
						URLConnection urlConnection = fileUrl.openConnection();
						// 文件输出的地方
						File outputDir = new File(outputDirectory);
						// 如果路径不存在将创建路径
						if (!outputDir.exists()) {
							outputDir.mkdirs();
						}
						// 创建要写入的文件
						File outputFile = File.createTempFile("00" + temp, fileType, outputDir);
						// 得到要写入的文件的输出流
						FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
						// 得到uriFile文件的输入流(读取)
						InputStream inputStream = urlConnection.getInputStream();
						// 读取
						int readUriFile = inputStream.read();
						while (readUriFile != -1) {
							fileOutputStream.write(readUriFile);
							readUriFile = inputStream.read();
						}
						fileOutputStream.close();
						inputStream.close();
						System.out.println("OK");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		System.out.print("已完成");
	}

}
