package com.starter.service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownload {
    public static void main(String[] args) {
        String url = "http://thirdwx.qlogo.cn/mmopen/vi_32/Q2pWSGI7lwd7TusEp749OsgWlxosE1zzH4xKgvJt1KLbvoicQB5rZLG7lMFH2dzpUT3HPKS4DjSEg94YqibS8K7g/132";
        downloadPicture(url,new File("D://a.jpg"));
    }
    //链接url下载图片
    public static void downloadPicture(String imageURL,File path) {
        URL url = null;
        try {
            url = new URL(imageURL);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(context);
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}