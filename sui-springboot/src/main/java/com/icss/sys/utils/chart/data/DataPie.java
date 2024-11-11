package com.icss.sys.utils.chart.data;

import java.util.ArrayList;
import java.util.List;

public class DataPie {
    //柱状图
    private List<Item> data = new ArrayList<>();

    public Item createItem() {
        return new Item();
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    //设置选项
    public class Item {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
