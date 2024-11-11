package com.icss.sys.utils.word;

//import org.apdplat.word.WordSegmenter;
//import org.apdplat.word.segmentation.Word;

import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.http.HttpClientUtils;

import java.util.Arrays;

public class WordFilterUtils {
    //分词接口获取关键词组
    public static java.util.List<String> getWordList(String title) {
        String str = HttpClientUtils.httpPost("https://www.jsonin.com/fenci.php?msg=" + title, "");
        String words = str.replace("[", "").replace("\"","").replace("]", "");
        if(StringUtils.isNotEmpty(words)){
            String[] split =words.split(",");
            return Arrays.asList(split);
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        WordFilterUtils.getWordList("软件销售员");
    }
}