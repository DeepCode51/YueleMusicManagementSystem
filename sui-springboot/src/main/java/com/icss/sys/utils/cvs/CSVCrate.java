package com.icss.sys.utils.cvs;

import java.io.*;
import java.util.List;


/**
 * 创建CSV文件
 */
public class CSVCrate {

    /**
     * 创建CSV文件
     */
    public static void createCSV(String fileName, String filePath, List<List<String>> dataList) {
        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(filePath + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
            //int num = headList.size() / 2;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < dataList.size(); i++) {
                buffer.append(",");
            }
            // 写入文件内容
            for (List<String> row : dataList) {
                writeRow(row, csvWtriter);
            }
            System.out.println("推荐模型数据写入成功");
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一行数据
     *
     * @param row       数据列表
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<String> row, BufferedWriter csvWriter) throws IOException {
        for (int i = 0; i < row.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(row.get(i));
            if (i < row.size()-1) {
                sb.append(",");
            }
            String rowStr = sb.toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}