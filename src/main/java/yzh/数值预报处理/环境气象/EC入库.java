package yzh.数值预报处理.环境气象;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import yzh.dao.ECSurfaceMapper;
import yzh.model.ECSurface;
import yzh.util.SqlSessionFactoryUtil;
import yzh.天擎.myself.天擎EC;
import yzh.数值预报处理.风向风速转换;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static yzh.天擎.myself.天擎EC.EC高空下载;

public class EC入库 {
    @Test
    public void cs() {
        DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8-24);
        if(DateUtil.hour(DateUtil.date(),true)>12){
            myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8);
        }
        EC高空文件入库(myDate);
        DateTime myDate2 = new DateTime("2021-06-15 20:00:00", DatePattern.NORM_DATETIME_FORMAT);
        for(int i=0;i<9;i++){
            DateTime mydate1=DateUtil.offsetDay(myDate2,i);
            EC高空文件入库(mydate1);
        }

    }

    public static void 处理EC(Date sdate) {
        try {
            String format1 = new SimpleDateFormat("yyyyMMdd").format(sdate);
            String basePath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/ECMWF/" + format1 + "/surface/";
            if (!FileUtil.exist(basePath)) {
                EC地面入库(sdate);
            }else if(FileUtil.loopFiles(basePath).size()<9){
                EC地面入库(sdate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EC高空文件入库(sdate);
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
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
            String format1 = df.format(edate);
            String format2 = df2.format(edate);
            String format3 = new SimpleDateFormat("yyyyMMddHHmm").format(edate);
            String basePath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/ECMWF/" + format1 + "/high/";
            if (!FileUtil.exist(basePath)) {
                FileUtil.mkdir(basePath);
            }
            for (Integer validTime : validTimes
            ) {
                try {
                    String myFilePath = basePath + format2 + "." + String.format("%03d", validTime);
                    if (!FileUtil.exist(myFilePath)) {
                        File myFile = FileUtil.touch(myFilePath);
                        StringBuilder data = new StringBuilder(StrUtil.format("diamond 5  TLOGP(ECMWF){}_Prs/Hgt/Tmp/RHu/SHu/WinD/WinS_Vars7_Stn{}\r\n", format3,stations.size()));
                        for (站点信息 station : stations
                        ) {
                            List<EC高空Model> mydatas = allDatas.stream().filter(y -> y.getStationID().equals(station.getID()) && y.getValidTime().equals(validTime)).sorted(Comparator.comparingInt(EC高空Model::getFcstLevel).reversed()).collect(Collectors.toList());
                            data.append(StrUtil.format("{} {} {} {} {}\r\n", station.getID(), station.getLon(), station.getLat(), station.getHigh(), mydatas.size() * 6));
                            for (EC高空Model dataItem : mydatas
                            ) {
                                data.append(StrUtil.format("{}\t{}\t{}\t{}\t{}\t{}\t{}\r\n", dataItem.getFcstLevel(), NumberUtil.round(dataItem.getGPH(), 2), NumberUtil.round(dataItem.getTEM(), 2), NumberUtil.round(dataItem.getRHU() * 100, 2), NumberUtil.round(dataItem.getSHU() * 100, 2), NumberUtil.round(风向风速转换.fxjs(dataItem.getWIV(), dataItem.getWIU()), 2), NumberUtil.round(风向风速转换.GetFS(dataItem.getWIV(), dataItem.getWIU()), 2)));
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
                    ecDao.deleteHistoryEC(DateUtil.offsetDay(edate,-30));
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
    private static void EC地面入库(Date sdate) {
        try {
            SqlSessionFactory sqlSessionFactoryEc = SqlSessionFactoryUtil.getSqlSessionFactoryHuanbao();
            SqlSession sessionEc = sqlSessionFactoryEc.openSession(true);
            ECSurfaceMapper ecDao = sessionEc.getMapper(ECSurfaceMapper.class);
            List<站点信息> stations = ecDao.GetStationsByType("EC");
            Integer count = stations.size() * 9;
            if (stations.size() > 0) {
                try {
                    ecDao.deleteByDatetimeBefore(DateUtil.offsetDay(sdate,-30));
                    Integer count1 = ecDao.countByDatetimeAndTmaxIsNotNull(sdate);
                    if (count1 < count) {
                        //MN2T6
                        String myFileName=天擎EC.EC地面下载(sdate,"MX2T6");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                开尔文转摄氏度(dataLists);
                                ecDao.insert_ECSurface(dataLists, "Tmax");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndTminIsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"MN2T6");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                开尔文转摄氏度(dataLists);
                                ecDao.insert_ECSurface(dataLists, "Tmin");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndTEMIsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"TEM");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                开尔文转摄氏度(dataLists);
                                ecDao.insert_ECSurface(dataLists, "TEM");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndPRSIsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"PRS");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECSurface(dataLists, "PRS");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndWIU10IsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"WIU10");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECSurface(dataLists, "WIU10");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndWIV10IsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"WIV10");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECSurface(dataLists, "WIV10");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndDPTIsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"DPT");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                开尔文转摄氏度(dataLists);
                                ecDao.insert_ECSurface(dataLists, "DPT");
                            }
                        }
                    }
                    count1 = ecDao.countByDatetimeAndRainIsNotNull(sdate);
                    if (count1 < count) {
                        String myFileName=天擎EC.EC地面下载(sdate,"TPE");
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC地面grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                降水量米转毫米(dataLists);
                                ecDao.insert_ECSurface(dataLists, "Rain");
                                try{
                                    var direLS=FileUtil.getParent(myFileName,1);
                                    for(int i=0;i<10;i++){
                                        Thread.sleep( 600 );
                                        System.gc();
                                        if(FileUtil.del(direLS)){
                                            break;
                                        }
                                    }
                                } catch (IORuntimeException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    EC地面保存为探空文件(sdate,ecDao,stations);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void EC高空文件入库(Date sdate) {
        try {
            SqlSessionFactory sqlSessionFactoryEc = SqlSessionFactoryUtil.getSqlSessionFactoryHuanbao();
            SqlSession sessionEc = sqlSessionFactoryEc.openSession(true);
            huanbao ecDao = sessionEc.getMapper(huanbao.class);
            List<站点信息> stations = ecDao.GetStationsByType("EC");
            Integer count = stations.size() * 19 * 50;
            if (stations.size() > 0) {
                try {
                    ecDao.deleteHistoryEC(DateUtil.offsetDay(sdate,-30));
                    Integer count1 = ecDao.CountEC("TEM", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"TEM");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                for (高空要素Model item : dataLists
                                ) {
                                    item.setYsValue(item.getYsValue() - 273.15);
                                }
                                ecDao.insert_ECheigh(dataLists, "TEM");
                            }
                        }
                    }
                     count1 = ecDao.CountEC("WIU", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"WIU");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECheigh(dataLists, "WIU");
                            }
                        }
                    }
                    count1 = ecDao.CountEC("WIV", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"WIV");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECheigh(dataLists, "WIV");
                            }
                        }
                    }
                    count1 = ecDao.CountEC("GPH", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"GPH");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                for (高空要素Model item : dataLists
                                ) {
                                    item.setYsValue(item.getYsValue() / 10);
                                }
                                ecDao.insert_ECheigh(dataLists, "GPH");
                            }
                        }
                    }
                    count1 = ecDao.CountEC("SHU", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"SHU");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECheigh(dataLists, "SHU");
                            }
                        }
                    }
                    count1 = ecDao.CountEC("RHU", sdate);
                    if (count1 < count) {
                        //WIU
                        String myFileName= EC高空下载( sdate,"RHU");;
                        if(!myFileName.isBlank()){
                            List<高空要素Model> dataLists = ECgrid处理.EC高空grid处理(sdate,myFileName,stations);
                            if(!dataLists.isEmpty()){
                                ecDao.insert_ECheigh(dataLists, "RHU");
                                try{
                                    var direLS=FileUtil.getParent(myFileName,1);
                                    for(int i=0;i<10;i++){
                                        Thread.sleep( 600 );
                                        System.gc();
                                        if(FileUtil.del(direLS)){
                                            break;
                                        }
                                    }
                                } catch (IORuntimeException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    EC高空数据转换(sdate);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void EC地面保存为探空文件(Date sdate,ECSurfaceMapper ecDao,List<站点信息> stations){
        String format2 = new SimpleDateFormat("yyyyMMddHHmm").format(sdate);
        String format3 = new SimpleDateFormat("yyyyMMddHH").format(sdate);
        String format1 = new SimpleDateFormat("yyyyMMdd").format(sdate);
        String basePath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/ECMWF/" + format1 + "/surface/";
        if (!FileUtil.exist(basePath)) {
            FileUtil.mkdir(basePath);
        }
        for(int i=0;i<240;i=i+3){
            if(i>72){
                i+=3;
            }
            StringBuilder data=new StringBuilder(StrUtil.format("diamond 3  SURFACE(ECMWF){}_PRS/TEM/DPT/RHu/WinD/WinS/TMax24/Tmin24/R24_Vars9_Stn{}\r\n", format2,stations.size()));
            List<ECSurface> allDatas= ecDao.findAllByDatetimeAndValidtimeGreaterThanAndValidtimeLessThanOrEqualTo(sdate,i,i+24);
            List<ECSurface> allDatas2= ecDao.findAllByDatetimeAndValidtimeInOrderByValidtime(sdate, new ArrayList<>(Arrays.asList(i, i+24)));
            List<ECSurface> allDatas3= ecDao.findAllByDatetimeAndValidtime(sdate,i);
            if(allDatas3.size()>0){
                for (站点信息 station : stations
                ) {
                    try{
                        List<ECSurface> mydatas = allDatas.stream().filter(y -> y.getStationid().equals(station.getID())).collect(Collectors.toList());
                        List<ECSurface> mydatasOther = allDatas3.stream().filter(y -> y.getStationid().equals(station.getID())).collect(Collectors.toList());
                        List<ECSurface> myraindatas = allDatas2.stream().filter(y -> y.getStationid().equals(station.getID())).collect(Collectors.toList());
                        if(mydatasOther.size()>0){
                            double tmax=-999999,tmin=999999,rain;
                            for (ECSurface dataItem : mydatas
                            ) {
                                Double lsTmax=dataItem.getTmax();
                                Double lsTmin=dataItem.getTmin();
                                if(lsTmax!=null&&lsTmax>-999&&lsTmax<999){
                                    if(lsTmax>tmax){
                                        tmax=lsTmax;
                                    }
                                }
                                if(lsTmin!=null&&lsTmin>-999&&lsTmin<999){
                                    if(lsTmin<tmin){
                                        tmin=lsTmin;
                                    }
                                }
                            }
                            if(tmax< -999){
                                tmax=999999;
                            }
                            if(myraindatas.size()==2){
                                Double lsRain0=myraindatas.get(0).getRain();
                                Double lsRain1=myraindatas.get(1).getRain();
                                if(lsRain0!=null&&lsRain0>-999&&lsRain0<999&&lsRain1!=null&&lsRain1>-999&&lsRain1<999){
                                    rain=lsRain1-lsRain0;
                                    if(rain<0){
                                        rain=0;
                                    }
                                }else{
                                    rain=999999;
                                }
                            }else{
                                rain=999999;
                            }
                            ECSurface mydatainsert=mydatasOther.get(0);
                            data.append(StrUtil.format("{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\r\n", station.getID(), station.getLon(), station.getLat(), station.getHigh(), NumberUtil.round(mydatainsert.getPRS()==null?999999:mydatainsert.getPRS(),1), NumberUtil.round(mydatainsert.getTEM()==null?999999:mydatainsert.getTEM(),1), NumberUtil.round(mydatainsert.getDPT()==null?999999:mydatainsert.getDPT(),1), NumberUtil.round(风向风速转换.fxjs(mydatainsert.getWIV10()==null?999999:mydatainsert.getWIV10(), mydatainsert.getWIU10()==null?999999:mydatainsert.getWIU10()), 1), NumberUtil.round(风向风速转换.GetFS(mydatainsert.getWIV10()==null?999999:mydatainsert.getWIV10(), mydatainsert.getWIU10()==null?999999:mydatainsert.getWIU10()), 1), NumberUtil.round(tmax,1),NumberUtil.round(tmin,1),NumberUtil.round(rain,1)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                String dataStr = data.substring(0, data.length() - 2);
                String myFilePath = basePath + format3 + "." + String.format("%03d", i);
                if (!FileUtil.exist(myFilePath)) {
                    File myFile = FileUtil.touch(myFilePath);
                    FileUtil.appendUtf8String(dataStr, myFile);
                }
            }


        }
    }
    private static List<高空要素Model> 处理天擎JSON数据(Date edate, List<站点信息> stations, String latlons, String fcstEle) {
        List<高空要素Model> dataLists = new ArrayList<>();
        Integer[] levels = new Integer[]{10, 20, 50, 70, 100, 150, 200, 250, 300, 400, 500, 600, 700, 800, 850, 900, 925, 950, 1000};
        for (Integer level : levels
        ) {
            JSONArray ecDatas = 天擎EC.EC高空(edate, fcstEle, "100", level, 0, 240, latlons);
            if(ecDatas!=null){
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

        }
        return dataLists;
    }
    private static List<高空要素Model> 处理天擎EC文件数据(Date edate, List<站点信息> stations, String fcstEle) {
        List<高空要素Model> dataLists = new ArrayList<>();
        EC高空下载(edate,fcstEle);
        return dataLists;
    }

    private static List<高空要素Model> 处理天擎EC地面JSON数据(Date edate, List<站点信息> stations, String latlons, String fcstEle) {
        List<高空要素Model> dataLists = new ArrayList<>();
        JSONArray ecDatas = 天擎EC.EC地面(edate, fcstEle, "1", 0, 0, 240, latlons);
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
                dataLists.add(new 高空要素Model((int) Double.parseDouble(jsonItem.get("validtime").toString()), 0, Double.parseDouble(jsonItem.get(fcstEle + "_1").toString()), station.getID(), edate));
                arrayls.add(item);
            }
            ecDatas.removeAll(arrayls);
        }
        return dataLists;
    }
   private static void 开尔文转摄氏度(List<高空要素Model> dataLists){
       for (高空要素Model item:dataLists){
           Double myVal=item.getYsValue();
           if(myVal>-9999&&myVal<9999){
               item.setYsValue(NumberUtil.round((myVal-273.15),3).doubleValue());
           }
       }
   }
    private static void 降水量米转毫米(List<高空要素Model> dataLists){
        for (高空要素Model item:dataLists){
            Double myVal=item.getYsValue();
            if(myVal>-9999&&myVal<9999){
                item.setYsValue(NumberUtil.round((item.getYsValue()*1000),3).doubleValue());
            }
        }
    }
}
