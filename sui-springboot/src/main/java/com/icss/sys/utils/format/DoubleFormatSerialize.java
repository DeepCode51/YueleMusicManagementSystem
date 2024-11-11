package com.icss.sys.utils.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

//double保留两位小数
//属性字段上加 @JsonSerialize(using = DoubleFormatSerialize.class)
public class DoubleFormatSerialize extends JsonSerializer<Double> {

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(Double arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        if(arg0 != null && !arg0.equals("-")) {
            arg1.writeString(df.format(arg0));
        }
    }
}