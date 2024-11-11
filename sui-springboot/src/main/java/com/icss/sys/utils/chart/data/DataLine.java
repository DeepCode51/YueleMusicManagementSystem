package com.icss.sys.utils.chart.data;

import java.util.ArrayList;
import java.util.List;

public class DataLine {
    //柱状图
    private List<String> xData = new ArrayList<>();
    private List<String> yData = new ArrayList<>();

    public List<String> getxData() {
        return xData;
    }

    public void setxData(List<String> xData) {
        this.xData = xData;
    }

    public List<String> getyData() {
        return yData;
    }

    public void setyData(List<String> yData) {
        this.yData = yData;
    }
}
