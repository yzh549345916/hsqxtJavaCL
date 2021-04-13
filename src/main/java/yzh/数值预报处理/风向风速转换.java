package yzh.数值预报处理;

import cn.hutool.core.convert.Convert;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class 风向风速转换 {
    public static String GetFXFS(double v, double u) {
        String fxfs = "";
        double fx=fxjs(v, u);
        //风速是uv分量的平方和
        double fs = sqrt(pow(u, 2) + pow(v, 2));
        int intfx = Convert.toInt(round(fx / 45));
        switch (intfx) {
            case 0:
                fxfs = "北风";
                break;
            case 1:
                fxfs = "东北风";
                break;
            case 2:
                fxfs = "东风";
                break;
            case 3:
                fxfs = "东南风";
                break;
            case 4:
                fxfs = "南风";
                break;
            case 5:
                fxfs = "西南风";
                break;
            case 6:
                fxfs = "西风";
                break;
            case 7:
                fxfs = "西北风";
                break;
            case 999017:
                fxfs = "静风";
                break;
            default:
                fxfs = "北风";
                break;
        }

        fxfs += ',';
        if (fs >= 0 && fs <= 0.2) {
            fxfs += "0级";
        } else if (fs >= 0.3 && fs <= 1.5) {
            fxfs += "1级";
        } else if (fs >= 1.6 && fs <= 3.3) {
            fxfs += "2级";
        } else if (fs >= 3.4 && fs <= 5.4) {
            fxfs += "3级";
        } else if (fs >= 5.5 && fs <= 7.9) {
            fxfs += "4级";
        } else if (fs >= 8 && fs <= 10.7) {
            fxfs += "5级";
        } else if (fs >= 10.8 && fs <= 13.8) {
            fxfs += "6级";
        } else if (fs >= 13.9 && fs <= 17.1) {
            fxfs += "7级";
        } else if (fs >= 17.2 && fs <= 20.7) {
            fxfs += "8级";
        } else if (fs >= 20.8 && fs <= 24.4) {
            fxfs += "9级";
        } else if (fs >= 24.5 && fs <= 28.4) {
            fxfs += "10级";
        } else if (fs >= 28.5 && fs <= 32.6) {
            fxfs += "11级";
        } else if (fs >= 32.7 && fs <= 36.9) {
            fxfs += "12级";
        } else if (fs >= 37 && fs <= 41.4) {
            fxfs += "13级";
        } else if (fs >= 41.5 && fs <= 46.1) {
            fxfs += "14级";
        } else if (fs >= 46.2 && fs <= 50.9) {
            fxfs += "15级";
        } else if (fs >= 51 && fs <= 56) {
            fxfs += "16级";
        } else if (fs >= 56.1 && fs <= 61.2) {
            fxfs += "17级";
        } else if (fs >= 61.3) {
            fxfs += "17级以上";
        } else {
            fxfs += "3级";
        }

        return fxfs;
    }

    public static String GetFX(double v, double u) {
        String fxStr = "";
        double fx=fxjs(v, u);
        //风速是uv分量的平方和
        int intfx = Convert.toInt(round(fx / 45));
        switch (intfx) {
            case 0:
                fxStr = "北风";
                break;
            case 1:
                fxStr = "东北风";
                break;
            case 2:
                fxStr = "东风";
                break;
            case 3:
                fxStr = "东南风";
                break;
            case 4:
                fxStr = "南风";
                break;
            case 5:
                fxStr = "西南风";
                break;
            case 6:
                fxStr = "西风";
                break;
            case 7:
                fxStr = "西北风";
                break;
            case 999017:
                fxStr = "静风";
                break;
            default:
                fxStr = "北风";
                break;
        }
        return fxStr;
    }

    public static double GetFXDouble(double v, double u) {
        double fx = 0; //风向
        if ((u > 0) & (v > 0)) {
            fx = 1.5*Math.PI - atan(v / u);
        } else if ((u < 0) & (v > 0)) {
            fx = 0.5*Math.PI- atan(v / u) ;
        } else if ((u < 0) & (v < 0)) {
            fx = 0.5*Math.PI - atan(v / u) ;
        } else if ((u > 0) & (v < 0)) {
            fx = 1.5*Math.PI - atan(v / u);
        } else if ((u == 0) & (v > 0)) {
            fx = Math.PI;
        } else if ((u == 0) & (v < 0)) {
            fx = 0;
        } else if ((u > 0) & (v == 0)) {
            fx = 1.5*Math.PI;
        } else if ((u < 0) & (v == 0)) {
            fx = 0.5*Math.PI;
        } else if ((u == 0) & (v == 0)) {
            fx = 0;
        }
        return fx;
    }

    public static double fxjs(double v, double u) {
        double fx = 999.9; //风向
        if ((u > 0) & (v > 0)) {
            fx = 270 - atan(v / u) * 180 / Math.PI;
        } else if ((u < 0) & (v > 0)) {
            fx = 90 - atan(v / u) * 180 / Math.PI;
        } else if ((u < 0) & (v < 0)) {
            fx = 90 - atan(v / u) * 180 / Math.PI;
        } else if ((u > 0) & (v < 0)) {
            fx = 270 - atan(v / u) * 180 / Math.PI;
        } else if ((u == 0) & (v > 0)) {
            fx = 180;
        } else if ((u == 0) & (v < 0)) {
            fx = 0;
        } else if ((u > 0) & (v == 0)) {
            fx = 270;
        } else if ((u < 0) & (v == 0)) {
            fx = 90;
        } else if ((u == 0) & (v == 0)) {
            fx = 999.9;
        }
        return fx;
    }

    public static double GetFS(double v, double u) {
        if (v < -99999 || u < -99999 || v > 99999 || u > 99999) {
            return -999999;
        }
        //风速是uv分量的平方和
        return sqrt(pow(u, 2) + pow(v, 2));
    }
    public static String GetFL(double v, double u) {
        double fs=GetFS(v,u);
        String fxfs="";
        if (fs >= 0 && fs <= 0.2) {
            fxfs += "0级";
        } else if (fs >= 0.3 && fs <= 1.5) {
            fxfs += "1级";
        } else if (fs >= 1.6 && fs <= 3.3) {
            fxfs += "2级";
        } else if (fs >= 3.4 && fs <= 5.4) {
            fxfs += "3级";
        } else if (fs >= 5.5 && fs <= 7.9) {
            fxfs += "4级";
        } else if (fs >= 8 && fs <= 10.7) {
            fxfs += "5级";
        } else if (fs >= 10.8 && fs <= 13.8) {
            fxfs += "6级";
        } else if (fs >= 13.9 && fs <= 17.1) {
            fxfs += "7级";
        } else if (fs >= 17.2 && fs <= 20.7) {
            fxfs += "8级";
        } else if (fs >= 20.8 && fs <= 24.4) {
            fxfs += "9级";
        } else if (fs >= 24.5 && fs <= 28.4) {
            fxfs += "10级";
        } else if (fs >= 28.5 && fs <= 32.6) {
            fxfs += "11级";
        } else if (fs >= 32.7 && fs <= 36.9) {
            fxfs += "12级";
        } else if (fs >= 37 && fs <= 41.4) {
            fxfs += "13级";
        } else if (fs >= 41.5 && fs <= 46.1) {
            fxfs += "14级";
        } else if (fs >= 46.2 && fs <= 50.9) {
            fxfs += "15级";
        } else if (fs >= 51 && fs <= 56) {
            fxfs += "16级";
        } else if (fs >= 56.1 && fs <= 61.2) {
            fxfs += "17级";
        } else if (fs >= 61.3) {
            fxfs += "17级以上";
        } else {
            fxfs += "3级";
        }
        return fxfs;
    }
}

