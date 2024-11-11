//package com.admin.module.apply.web;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: jiangli
// * @Date: 2021/1/6 16:37
// * 将多张图片合并转为PDF
// */
//class ImageToPdfUtil {
//    /**
//     * 保存到本地
//     *
//     * @param pathList 图片文件夹地址
//     * @param pdfPath         PDF文件保存地址
//     */
//    public static void toPdf(List<String> pathList, String pdfPath) {
//        //创建一个文档对象
//        Document doc = new Document();
//        doc.setMargins(0,0,0,0);
//        try {
//            // 输入流
//            FileOutputStream fos = new FileOutputStream(pdfPath);
//            // 写入PDF文档
//            PdfWriter.getInstance(doc, fos);
//            //开启文档
//            doc.open();
//            // 获取图片文件夹对象
//            //File file = new File(imageFolderPath);
//            //File[] files = file.listFiles();
//            List<File> files = new ArrayList<>();
//            for (String path : pathList) {
//                files.add(new File(path));
//            }
//            // 循环获取图片文件夹内的图片
//            if (files != null && files.size() > 0) {
//                for (File f : files) {
//                    if (f.getName().endsWith(".png")
//                            || f.getName().endsWith(".jpg")
//                            || f.getName().endsWith(".gif")
//                            || f.getName().endsWith(".jpeg")
//                            || f.getName().endsWith(".tif")) {
//                        String imagePath = f.getPath();
//                        // 实例化图片
//                        Image image = Image.getInstance(imagePath);
//
//                        float imageHeight=image.getScaledHeight();
//                        float imageWidth=image.getScaledWidth();
//                        //设置页面宽高与图片一致
//                        Rectangle rectangle = new Rectangle(imageWidth, imageHeight);
//                        doc.setPageSize(rectangle);
//                        //图片居中
//                        image.setAlignment(Image.ALIGN_CENTER);
//                        //image.setAlignment(Image.MIDDLE);
//                        doc.newPage();
//                        // 添加图片到文档
//                        doc.add(image);
//                        // 换行,增加间距
//                        //doc.add(new Paragraph("\n"));
//                    }
//                }
//            }
//            // 关闭文档
//            doc.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("D:/attach_files/upload/xmjbqkb/2022/03/27/1508052598872223745.jpg");
//        toPdf(list,"D:\\attach_files\\test.pdf");
//    }
//}