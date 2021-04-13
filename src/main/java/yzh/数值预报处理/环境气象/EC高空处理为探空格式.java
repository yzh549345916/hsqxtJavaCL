package yzh.数值预报处理.环境气象;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import yzh.util.SqlSessionFactoryUtil;
import yzh.天擎.myself.EC高空;
import yzh.数值预报处理.风向风速转换;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EC高空处理为探空格式 {
    @Test
    public void cs() {
        DateTime myDate = new DateTime("2021-04-11 08:00:00", DatePattern.NORM_DATETIME_FORMAT);
        处理EC高空(myDate);
    }

    public static void 处理EC高空(Date edate) {

        EC高空入库(edate);

    }

    private static void EC高空数据转换(Date edate) {
        try {
            Integer[] validTimes = new Integer[]{0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51, 54, 57, 60, 63, 66, 69, 72, 78, 84, 90, 96, 102, 108, 114, 120, 126, 132, 138, 144, 150, 156, 162, 168, 174, 180, 186, 192, 198, 204, 210, 216, 222, 228, 234, 240};
            SqlSessionFactory sqlSessionFactoryEc = SqlSessionFactoryUtil.getSqlSessionFactoryHuanbao();
            SqlSession sessionEc = sqlSessionFactoryEc.openSession(true);
            huanbao ecDao = sessionEc.getMapper(huanbao.class);
            List<站点信息> stations = ecDao.GetStationsByType("EC");
            List<EC高空Model> allDatas = ecDao.GetECHeightByDatetime(edate);
            if (allDatas.size() == 0) {
                return;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
            String format1 = df.format(edate);
            String format2 = df2.format(edate);
            String basePath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/EC/tlogp/" + format1 + "/";
            if (!FileUtil.exist(basePath)) {
                FileUtil.mkdir(basePath);
            }
            for (Integer validTime : validTimes
            ) {
                try {
                    String myFilePath = basePath + format2 + "." + String.format("%03d", validTime);
                    if (!FileUtil.exist(myFilePath)) {
                        File myFile = FileUtil.touch(myFilePath);
                        StringBuilder data = new StringBuilder(StrUtil.format("diamond 5 {}00_TLOGP（ECMWF）\r\n", format2));
                        ;
                        for (站点信息 station : stations
                        ) {
                            List<EC高空Model> mydatas = allDatas.stream().filter(y -> y.getStationID().equals(station.getID()) && y.getValidTime().equals(validTime)).sorted(Comparator.comparingInt(EC高空Model::getFcstLevel).reversed()).collect(Collectors.toList());
                            data.append(StrUtil.format("{} {} {} {} {}\r\n", station.getID(), station.getLon(), station.getLat(), station.getHigh(), mydatas.size() * 6));
                            for (EC高空Model dataItem : mydatas
                            ) {
                                data.append(StrUtil.format("{} {} {} {} {} {}\r\n", dataItem.getFcstLevel(), NumberUtil.round(dataItem.getGPH(), 2), NumberUtil.round(dataItem.getTEM(), 2), NumberUtil.round(dataItem.getSHU() * 100, 2), NumberUtil.round(风向风速转换.fxjs(dataItem.getWIV(), dataItem.getWIU()), 2), NumberUtil.round(风向风速转换.GetFS(dataItem.getWIV(), dataItem.getWIU()), 2)));
                            }
                        }
                        String dataStr = data.substring(0, data.length() - 2);
                        FileUtil.appendUtf8String(dataStr, myFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void EC高空入库(Date edate) {
        try {
            SqlSessionFactory sqlSessionFactoryEc = SqlSessionFactoryUtil.getSqlSessionFactoryHuanbao();
            SqlSession sessionEc = sqlSessionFactoryEc.openSession(true);
            huanbao ecDao = sessionEc.getMapper(huanbao.class);
            List<站点信息> stations = ecDao.GetStationsByType("EC");
            Integer count = stations.size() * 19 * 53;

            if (stations.size() > 0) {

                try {
                    ecDao.deleteHistoryEC(DateUtil.offsetDay(edate,-14));
                    String latlons = "";
                    for (站点信息 station : stations
                    ) {
                        latlons += station.getLat() + "/" + station.getLon() + ",";
                    }
                    latlons = latlons.substring(0, latlons.length() - 1);
                    List<高空要素Model> dataLists;
                    Integer count1 = ecDao.CountEC("WIV", edate);
                    if (count1 < count) {
                        dataLists = 处理天擎JSON数据(edate, stations, latlons, "WIV");
                        if (dataLists != null && dataLists.size() > 0) {
                            ecDao.insert_ECheigh(dataLists, "WIV");
                        }
                    }

                    count1 = ecDao.CountEC("WIU", edate);
                    if (count1 < count) {
                        dataLists = 处理天擎JSON数据(edate, stations, latlons, "WIU");
                        if (dataLists != null && dataLists.size() > 0) {
                            ecDao.insert_ECheigh(dataLists, "WIU");
                        }
                    }

                    //比湿
                    count1 = ecDao.CountEC("SHU", edate);
                    if (count1 < count) {
                        dataLists = 处理天擎JSON数据(edate, stations, latlons, "SHU");
                        if (dataLists != null && dataLists.size() > 0) {
                            for (高空要素Model item : dataLists
                            ) {
                                item.setYsValue(item.getYsValue() * 1000);
                            }
                            ecDao.insert_ECheigh(dataLists, "SHU");
                        }
                    }

                    count1 = ecDao.CountEC("TEM", edate);
                    if (count1 < count) {
                        dataLists = 处理天擎JSON数据(edate, stations, latlons, "TEM");
                        if (dataLists != null && dataLists.size() > 0) {
                            for (高空要素Model item : dataLists
                            ) {
                                item.setYsValue(item.getYsValue() - 273.15);
                            }
                            ecDao.insert_ECheigh(dataLists, "TEM");
                        }
                    }
                    count1 = ecDao.CountEC("GPH", edate);
                    if (count1 < count) {
                        dataLists = 处理天擎JSON数据(edate, stations, latlons, "GPH");
                        if (dataLists != null && dataLists.size() > 0) {
                            for (高空要素Model item : dataLists
                            ) {
                                item.setYsValue(item.getYsValue() / 10);
                            }
                            ecDao.insert_ECheigh(dataLists, "GPH");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    EC高空数据转换(edate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<高空要素Model> 处理天擎JSON数据(Date edate, List<站点信息> stations, String latlons, String fcstEle) {
        List<高空要素Model> dataLists = new ArrayList<>();
        Integer[] levels = new Integer[]{10, 20, 50, 70, 100, 150, 200, 250, 300, 400, 500, 600, 700, 800, 850, 900, 925, 950, 1000};
        for (Integer level : levels
        ) {
            JSONArray ecDatas = EC高空.EC高空(edate, fcstEle, "100", level, 0, 240, latlons);
            for (站点信息 station : stations
            ) {
                JSONArray arrayls = new JSONArray();
                for (Object item : ecDatas
                ) {
                    JSONObject jsonItem = (JSONObject) item;
                    double mylat = Double.parseDouble(jsonItem.get("lat").toString());
                    if (mylat != station.getLat()) {
                        break;
                    }
                    double mylon = Double.parseDouble(jsonItem.get("lon").toString());
                    if (mylon != station.getLon()) {
                        break;
                    }
                    dataLists.add(new 高空要素Model((int) Double.parseDouble(jsonItem.get("validtime").toString()), level, Double.parseDouble(jsonItem.get(fcstEle + "_100").toString()), station.getID(), edate));
                    arrayls.add(item);
                }
                ecDatas.removeAll(arrayls);
            }
        }
        return dataLists;
    }
}
