package com.icss.sys.utils.chart;

import com.icss.sys.utils.chart.data.DataBend;
import com.icss.sys.utils.chart.data.DataLine;
import com.icss.sys.utils.chart.data.DataPie;

public class ChartUtils {
    public static DataLine createLine(){
        DataLine dataLine = new DataLine();
        return dataLine;
    }
    public static DataPie createPie(){
        DataPie dataPie = new DataPie();
        return dataPie;
    }
    public static DataBend createBend(){
        DataBend dataBend = new DataBend();
        return dataBend;
    }
}
