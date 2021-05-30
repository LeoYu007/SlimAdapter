package com.mathew.slimadapter.sample.entity;


import com.mathew.slimadapter.entity.SelectableItem;

/**
 * @author yu
 * @date 2018/1/11
 */

public class OnePiece implements SelectableItem {

    private String desc;
    private int imageRes;
    private boolean isBigType;
    private boolean isChecked;

    @Override
    public boolean isSelected() {
        return isChecked;
    }

    @Override
    public void setSelected(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public OnePiece(String desc) {
        this(desc, -1, false);
    }

    public OnePiece(String desc, int imageRes, boolean isBigType) {
        this.desc = desc;
        this.imageRes = imageRes;
        this.isBigType = isBigType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isBigType() {
        return isBigType;
    }

    public void setBigType(boolean bigType) {
        isBigType = bigType;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

}
