package com.example.secret.booklist60.utils;


import com.wangjie.shadowviewhelper.ShadowProperty;

import java.io.Serializable;

/**
 * Created by Secret on 2017/5/27.
 */

public class MyShadowProperty extends ShadowProperty implements Serializable {
    public static final int ALL = 0x1111;
    public static final int LEFT = 0x0001;
    public static final int TOP = 0x0010;
    public static final int RIGHT = 0x0100;
    public static final int BOTTOM = 0x1000;

    /**
     * 阴影颜色
     */
    private int shadowColor;
    /**
     * 阴影半径
     */
    private int shadowRadius;
    /**
     * 阴影x偏移
     */
    private int shadowDx;
    /**
     * 阴影y偏移
     */
    private int shadowDy;

    /**
     * 阴影边
     */
    private int shadowSide ;

    public int getShadowSide() {
        return shadowSide;
    }

    public MyShadowProperty setShadowSide(int shadowSide) {
        this.shadowSide = shadowSide;
        return this;
    }

    public int getShadowOffset() {
        return getShadowOffsetHalf() * 2;
    }

    public int getShadowOffsetHalf() {
        return 0 >= shadowRadius ? 0 : Math.max(shadowDx, shadowDy) + shadowRadius;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public MyShadowProperty setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public MyShadowProperty setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public int getShadowDx() {
        return shadowDx;
    }

    public MyShadowProperty setShadowDx(int shadowDx) {
        this.shadowDx = shadowDx;
        return this;
    }

    public int getShadowDy() {
        return shadowDy;
    }

    public MyShadowProperty setShadowDy(int shadowDy) {
        this.shadowDy = shadowDy;
        return this;
    }
}
