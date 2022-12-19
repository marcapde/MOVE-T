package com.example.move_t;

import java.io.Serializable;

public class ListElement implements Serializable {
    public int id;
    public String color;
    public String name;
    public String desc;
    public boolean checked;
    public String imgName;
    public ListElement(String color, String name, String desc, boolean checked, int id,String imgName) {
        this.color = color;
        this.name = name;
        this.desc = desc;
        this.checked = checked;
        this.id = id;
        this.imgName = imgName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
