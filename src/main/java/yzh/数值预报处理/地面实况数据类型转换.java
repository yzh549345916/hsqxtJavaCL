package yzh.数值预报处理;

public  class 地面实况数据类型转换 {
    public static String cimiss地面实况数据类型转换(int paramType) {
        String myString = "";
        switch (paramType) {
            case 0:
                myString = "TEM";
                break;
            case 1:
                myString = "RHU";
                break;
            case 2:
                myString = "PRE";
                break;
            case 4:
                myString = "WIN_D_Avg_10mi,WIN_S_Avg_10mi";
                break;
            case 6:
                myString = "PRS";
                break;
            case 7:
                myString = "TEM_Max";
                break;
            case 8:
                myString = "TEM_Min";
                break;
            default:
                myString = "";
                break;
        }
        return myString;
    }
    public static String 地面实况数据类型文字描述转换(int paramType) {
        String myString = "";
        switch (paramType) {
            case 0:
                myString = "气温";
                break;
            case 1:
                myString = "相对湿度";
                break;
            case 2:
                myString = "降水量";
                break;
            case 4:
                myString = "10米风速";
                break;
            case 6:
                myString = "气压";
                break;
            case 7:
                myString = "最高气温";
                break;
            case 8:
                myString = "最低气温";
                break;
            case 1900:
                myString = "能见度";
                break;
            default:
                myString = "气温";
                break;
        }
        return myString;
    }
    public static String 地面预报数据类型单位转换(int paramType) {
        String myString = "";
        switch (paramType) {
            case 0:
                myString = "℃";
                break;
            case 1:
                myString = "%";
                break;
            case 2:
                myString = "毫米";
                break;
            case 4:
                myString = "m/s";
                break;
            case 6:
                myString = "hPa";
                break;
            case 7:
                myString = "℃";
                break;
            case 8:
                myString = "℃";
                break;
            case 1900:
                myString = "m";
                break;
            default:
                myString = "℃";
                break;
        }
        return myString;
    }
    public static String 区局数值预报格点预报数据库地面实况数据类型转换(int paramType) {
        String myString = "";
        switch (paramType) {
            case 0:
                myString = "TEM";
                break;
            case 1:
                myString = "ERH";
                break;
            case 2:
                myString = "PRE_3h";
                break;
            case 4:
                myString = "WIU10,WIV10";
                break;
            case 6:
                myString = "PRS";
                break;
            case 7:
                myString = "TMAX";
                break;
            case 8:
                myString = "TMIN";
                break;
            default:
                myString = "TEM";
                break;
        }
        return myString;
    }
    public static String Rmaps预报数据库地面实况数据类型转换(int paramType,int levelType,double level) {
        if(paramType==0){
            if(levelType==1){
                return "TEM_surface";
            }else if(levelType==100){
                if(level==50000){
                    return "TEM_isobaric_500";
                }else if(level==70000){
                    return "TEM_isobaric_700";
                }else if(level==85000){
                    return "TEM_isobaric_850";
                }
            }else if(levelType==103){
                return "TEM";
            }
        }
        else if(paramType==2){
            return "PRE";
        }
        else if(paramType==4){
            if(levelType==100){
                if(level==50000){
                    return "WIU10_isobaric_500";
                }else if(level==70000){
                    return "WIU10_isobaric_700";
                }else if(level==85000){
                    return "WIU10_isobaric_850";
                }
            }else if(levelType==103){
                return "WIU10,WIV10";
            }
        }
        else if(paramType==5){
            if(levelType==100){
                if(level==50000){
                    return "WIV10_isobaric_500";
                }else if(level==70000){
                    return "WIV10_isobaric_700";
                }else if(level==85000){
                    return "WIV10_isobaric_850";
                }
            }else if(levelType==103){
                return "WIV10";
            }
        }
        else if(paramType==1900){
            return "VIS";
        }
        else if(paramType==603){
            return "Low_cloud";
        }
        else if(paramType==604){
            return "Medium_cloud";
        }
        else if(paramType==605){
            return "High_cloud";
        }
        else if(paramType==301){
            return "PRS";
        }
        else if(paramType==1){
            if(levelType==100){
                if(level==50000){
                    return "RHU_isobaric_500";
                }else if(level==70000){
                    return "RHU_isobaric_700";
                }else if(level==85000){
                    return "RHU_isobaric_850";
                }
            }else if(levelType==103){
                return "RHU";
            }
        }
        else if(paramType==305){
            if(levelType==100){
                if(level==50000){
                    return "Geopotential_height_500";
                }else if(level==70000){
                    return "Geopotential_height_700";
                }else if(level==85000){
                    return "Geopotential_height_850";
                }
            }
        }
        return "";
    }
    public static String EC预报数据库地面实况数据类型转换(int paramType,int levelType,double level) {
        switch (paramType) {
            case 0:
                if (levelType == 1) {
                    return "SKTE";
                } else if (levelType == 100) {
                    if (level == 50000) {
                        return "";
                    } else if (level == 70000) {
                        return "";
                    } else if (level == 85000) {
                        return "";
                    }
                } else if (levelType == 103) {
                    return "TEF0";
                }
                break;
            case 235:
                return "SKTE";//表面温度
            case 167:
                return "TEF0";//2米气温
            case 121:
                return "MN2T6";//过去6小时2米最低温度
            case 122:
                return "MX2T6";//过去6小时2米最高温度
            case 228027:
                return "MN2T3";//过去3小时2米最低温度
            case 34:
                return "SST";//海表温度
            case 4:
                return "WIU10,WIV10";
            case 4100:
                return "WIU100,WIV100";
            case 165:
                return "WIU10";//10米风的u分量
            case 166:
                return "WIV10";//10米风的v分量
            case 228246:
                return "WIU100";//100米风的u分量
            case 228247:
                return "WIV100";//100米风的v分量
            case 228028:
                return "GUST10T3";//过去3小时10米阵风
            case 123:
                return "GUST10T6";//过去6小时10米阵风
            case 143:
                return "CPE";//对流性降水
            case 228:
                return "TPE";//总降水量
            case 142:
                return "LPE";//大尺度降水
            case 260015:
                return "PRTY";//降水类型
            case 141:
                return "SDE";//雪深
            case 33:
                return "SNDE";//雪密度
            case 144:
                return "TTSP";//降雪量
            case 168:
                return "DPT";//露点温度
            case 151:
                return "GSSP";//海平面气压（相对于海面）
            case 3020:
                return "VIS";//能见度
            case 186:
                return "GRLCC";//低云量
            case 164:
                return "GRTCC";//总云量
            default:
                return "";
        }
        return "";
    }
    public static String EC地面数据类型文字描述转换(int paramType) {
        switch (paramType) {
            case 0:
            case 167:
                return "2米气温";
            case 235:
                return "表面温度";//
            case 121:
                return "过去6小时2米最低温度";//
            case 122:
                return "过去6小时2米最高温度";//
            case 228027:
                return "过去3小时2米最低温度";//
            case 34:
                return "海表温度";//
            case 4:
                return "10米风";//
            case 4100:
                return "100米风";//
            case 165:
                return "10米风的u分量";//
            case 166:
                return "10米风的v分量";//
            case 228246:
                return "100米风的u分量";//
            case 228247:
                return "100米风的v分量";//
            case 228028:
                return "过去3小时10米阵风";//
            case 123:
                return "过去6小时10米阵风";//
            case 143:
                return "对流性降水";//
            case 228:
                return "总降水量";//
            case 142:
                return "大尺度降水";//
            case 260015:
                return "降水类型";//
            case 141:
                return "雪深";//
            case 33:
                return "雪密度";//
            case 144:
                return "降雪量";//
            case 168:
                return "露点温度";//
            case 151:
                return "海平面气压（相对于海面）";//
            case 3020:
                return "能见度";//
            case 186:
                return "低云量";//
            case 164:
                return "总云量";//
            default:
                return "";
        }
    }
    public static String EC预报数据类型单位转换(int paramType) {
        switch (paramType) {
            case 0:
            case 167:
            case 235:
            case 121:
            case 122:
            case 228027:
            case 34:
            case 168://露点温度
                return "℃";
            case 4:
            case 4100:
            case 165:
            case 166:
            case 228246:
            case 228247:
            case 228028:
            case 123:
                return "m/s";//
            case 143:
            case 228:
            case 142:
                return "mm";//
            case 260015:
                return "";//降水类型
            case 141:
            case 144:
                return "m";//
            case 33:
                return "kg/m³";//

            case 151:
                return "Pa";//
            case 3020:
                return "m";//
            case 186:
                return "";//低云量
            case 164:
                return "";//总云量
            default:
                return "";
        }
    }
}
