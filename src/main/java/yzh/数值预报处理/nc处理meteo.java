package yzh.数值预报处理;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.Sftp;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.StationData;
import org.meteoinfo.data.meteodata.DataInfo;
import org.meteoinfo.data.meteodata.MeteoDataInfo;
import org.meteoinfo.data.meteodata.Variable;
import org.meteoinfo.ndarray.Dimension;
import org.meteoinfo.projection.KnownCoordinateSystems;
import org.meteoinfo.projection.ProjectionInfo;
import yzh.util.SqlSessionFactoryUtil;
import yzh.数值预报处理.环境气象.huanbao;
import yzh.数值预报处理.环境气象.区台沙尘Model;
import yzh.数值预报处理.环境气象.站点信息;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class nc处理meteo {
    public static void 区台沙尘模式数据处理(Date myDate, String resourPath) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = df.format(myDate);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd-HH");
        String format2 = df2.format(myDate);
        String myDirNameBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachen/" + format1 + "/";
        String myDirNameBaseStation = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachenStation/" + format1 + "/";
        if (!FileUtil.exist(myDirNameBase)) {
            FileUtil.mkdir(myDirNameBase);
        }
        MeteoDataInfo aDataInfo = new MeteoDataInfo();
        aDataInfo.openNetCDFData(resourPath);
        ProjectionInfo mypro = aDataInfo.getProjectionInfo();
        DataInfo datainfo = aDataInfo.getDataInfo();
        Dimension yDimension = datainfo.getYDimension();
        Dimension xDimension = datainfo.getXDimension();
        List<站点信息> stations = new ArrayList<>();
        StationData stationData = new StationData();
        SqlSessionFactory sqlSessionFactoryEc = SqlSessionFactoryUtil.getSqlSessionFactoryHuanbao();
        SqlSession sessionEc = sqlSessionFactoryEc.openSession(true);
        huanbao ecDao = sessionEc.getMapper(huanbao.class);
        try {
            stations = ecDao.GetStationsByType("shachen");
            for (int i = 0; i < stations.size(); i++) {
                stationData.addData(stations.get(i).getID(), stations.get(i).getLon(), stations.get(i).getLat(), 0);
            }
            stationData = stationData.project(KnownCoordinateSystems.geographic.world.WGS1984, mypro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<LocalDateTime> tDimension = aDataInfo.getDataInfo().getTimes();
        String[] variableStrs = new String[]{"DRYDEP_1", "DRYDEP_2", "DRYDEP_3", "DRYDEP_4", "DRYDEP_5"};
        aDataInfo.setLevelIndex(0);
        String paramType = "DUSTDRY", paramChineseName = "干沉降";
        String myFileNameBase = myDirNameBase + paramType + "/";
        String stationFileNameBase = myDirNameBaseStation + paramType + "/";
        沙尘json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, variableStrs, paramType, paramChineseName, myFileNameBase, true, "", stationData, stations, stationFileNameBase,ecDao);
        variableStrs = new String[]{"EDUST1", "EDUST2", "EDUST3", "EDUST4", "EDUST5"};
        aDataInfo.setLevelIndex(0);
        paramType = "EDUST";
        paramChineseName = "起沙";
        myFileNameBase = myDirNameBase + paramType + "/";
        stationFileNameBase = myDirNameBaseStation + paramType + "/";
        沙尘json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, variableStrs, paramType, paramChineseName, myFileNameBase, false, "ug/m2", stationData, stations, stationFileNameBase,ecDao);
        aDataInfo.setLevelIndex(0);
        variableStrs = new String[]{"PBLH"};
        paramType = "PBLH";
        paramChineseName = "边界层高度";
        myFileNameBase = myDirNameBase + paramType + "/";
        stationFileNameBase = myDirNameBaseStation + paramType + "/";
        沙尘json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, variableStrs, paramType, paramChineseName, myFileNameBase, false, "ug/m2", stationData, stations, stationFileNameBase,ecDao);
        aDataInfo.setLevelIndex(0);
        paramType = "SURFACEDUST";
        paramChineseName = "地面沙尘浓度";
        myFileNameBase = myDirNameBase + paramType + "/";
        stationFileNameBase = myDirNameBaseStation + paramType + "/";
        地面沙尘浓度json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, paramType, paramChineseName, myFileNameBase, "ug/m3", stationData, stations, stationFileNameBase,ecDao);
        /*variableStrs=new String[]{"PM10"};
        paramType="PM10";paramChineseName="PM10";
        myFileNameBase=myDirNameBase+paramType+"/";
        高空沙尘json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, variableStrs, paramType, paramChineseName, myFileNameBase,false,"");*/
        myFileNameBase = myDirNameBase + "PM10/";
        stationFileNameBase = myDirNameBaseStation + "PM10/";
        PM10json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, myFileNameBase, stationData, stations, stationFileNameBase,ecDao);
        myFileNameBase = myDirNameBase + "PM2.5/";
        stationFileNameBase = myDirNameBaseStation + "PM2.5/";
        PM25json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, myFileNameBase, stationData, stations, stationFileNameBase,ecDao);
       //暂时不用上传沙尘模式数据到20服务器，因此注释掉
        //同步沙尘模式数据(format1);
        /* variableStrs=new String[]{"PM2_5_DRY"};
        paramType="PM2.5";paramChineseName="PM2.5";
        myFileNameBase=myDirNameBase+paramType+"/";
        高空沙尘json转换(myDate, format2, aDataInfo, mypro, yDimension, xDimension, tDimension, variableStrs, paramType, paramChineseName, myFileNameBase,false,"");*/
    }

    private static void 沙尘json转换(Date myDate, String format2, MeteoDataInfo aDataInfo, ProjectionInfo mypro, Dimension yDimension, Dimension xDimension, List<LocalDateTime> tDimension, String[] variableStrs, String paramType, String paramChineseName, String myFileNameBase, boolean absBS, String defaultUnits, StationData stationData, List<站点信息> stations, String stationFileNameBase,huanbao ecDao) {
        JSONArray jsonArray = new JSONArray();
        List<区台沙尘Model> stationDataList=new ArrayList<>();
        for (int i = 0; i < tDimension.size(); i++) {
            aDataInfo.setTimeIndex(i);
            Variable variable = aDataInfo.getDataInfo().getVariable(variableStrs[0]);
            if (!variable.getUnits().isBlank()) {
                defaultUnits = variable.getUnits();
            }
            aDataInfo.setVariableName(variableStrs[0]);
            GridData myGridData = aDataInfo.getGridData();
            for (int j = 1; j < variableStrs.length; j++) {
                aDataInfo.setVariableName(variableStrs[j]);
                myGridData = myGridData.add(aDataInfo.getGridData());
            }
            if (absBS) {
                myGridData = myGridData.abs();
            }
            double[] a1 = (double[]) myGridData.getArray().copyTo1DJavaArray();
            JSONObject json1 = JSONUtil.createObj()
                    .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                    .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                    .set("paramType", paramType)
                    .set("paramChineseName", paramChineseName)
                    .set("unit", defaultUnits)
                    .set("projection", mypro.getProjectionName())
                    .set("CenterLat", mypro.getCenterLat())
                    .set("CenterLon", mypro.getCenterLon())
                    .set("dx", xDimension.getDeltaValue())
                    .set("startX", xDimension.getDimValue(0))
                    .set("xRows", xDimension.getLength())
                    .set("dy", yDimension.getDeltaValue())
                    .set("startY", yDimension.getDimValue(0))
                    .set("yRows", yDimension.getLength())
                    .set("EARTH_RADIUS", 6371229)
                    .set("data", a1);
            String myFileName = StrUtil.format("{}shachen_{}_{}_{}.json", myFileNameBase, paramType, format2, String.format("%04d", i));
            File myFile = FileUtil.touch(myFileName);
            FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(json1) + "]", myFile);
            StationData myStationData = myGridData.toStation(stationData);
            for (int k = 0; k < stations.size(); k++) {
                var mySta = stations.get(k);
                Date forecastDate = Date.from(tDimension.get(i).plusHours(8).toInstant(ZoneOffset.of("+8")));
                stationDataList.add(new 区台沙尘Model(mySta.getID(),myDate,(int)DateUtil.between(myDate,forecastDate, DateUnit.HOUR),paramType, 1000.0,myStationData.getValue(k)));
                JSONObject jsonStation = JSONUtil.createObj()
                        .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                        .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                        .set("paramType", paramType)
                        .set("paramChineseName", paramChineseName)
                        .set("unit", defaultUnits)
                        .set("stationID", mySta.getID())
                        .set("stationName", mySta.getName())
                        .set("stationLat", mySta.getLat())
                        .set("stationLon", mySta.getLon())
                        .set("value", myStationData.getValue(k));
                jsonArray.add(jsonStation);
            }
        }
        if(stationDataList.size()>0){
            ecDao.insert_qtShaChen( DateUtil.format(myDate, "yyyyMM"),stationDataList);
        }
        String myStationFileName = StrUtil.format("{}shachen_station_{}_{}.json", stationFileNameBase, paramType, format2);
        File myStationFile = FileUtil.touch(myStationFileName);
        FileUtil.writeUtf8String(jsonArray.toString(), myStationFile);
    }

    private static void 地面沙尘浓度json转换(Date myDate, String format2, MeteoDataInfo aDataInfo, ProjectionInfo mypro, Dimension yDimension, Dimension xDimension, List<LocalDateTime> tDimension, String paramType, String paramChineseName, String myFileNameBase, String defaultUnits, StationData stationData, List<站点信息> stations, String stationFileNameBase,huanbao ecDao) {
        String[] variableStrs = new String[]{"DUST_1", "DUST_2", "DUST_3", "DUST_4", "DUST_5"};
        JSONArray jsonArray = new JSONArray();
        List<区台沙尘Model> stationDataList=new ArrayList<>();
        for (int i = 0; i < tDimension.size(); i++) {
            aDataInfo.setTimeIndex(i);
            aDataInfo.setVariableName(variableStrs[0]);
            Date forecastDate = Date.from(tDimension.get(i).plusHours(8).toInstant(ZoneOffset.of("+8")));
            GridData myGridData = aDataInfo.getGridData();
            for (int j = 1; j < variableStrs.length; j++) {
                aDataInfo.setVariableName(variableStrs[j]);
                myGridData = myGridData.add(aDataInfo.getGridData());
            }
            myGridData = myGridData.div(aDataInfo.getGridData("ALT"));
            double[] a1 = (double[]) myGridData.getArray().copyTo1DJavaArray();
            JSONObject json1 = JSONUtil.createObj()
                    .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                    .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                    .set("paramType", paramType)
                    .set("paramChineseName", paramChineseName)
                    .set("unit", defaultUnits)
                    .set("projection", mypro.getProjectionName())
                    .set("CenterLat", mypro.getCenterLat())
                    .set("CenterLon", mypro.getCenterLon())
                    .set("dx", xDimension.getDeltaValue())
                    .set("startX", xDimension.getDimValue(0))
                    .set("xRows", xDimension.getLength())
                    .set("dy", yDimension.getDeltaValue())
                    .set("startY", yDimension.getDimValue(0))
                    .set("yRows", yDimension.getLength())
                    .set("EARTH_RADIUS", 6371229)
                    .set("data", a1);
            String myFileName = StrUtil.format("{}shachen_{}_{}_{}.json", myFileNameBase, paramType, format2, String.format("%04d", i));
            File myFile = FileUtil.touch(myFileName);
            FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(json1) + "]", myFile);
            StationData myStationData = myGridData.toStation(stationData);
            for (int k = 0; k < stations.size(); k++) {
                var mySta = stations.get(k);
                JSONObject jsonStation = JSONUtil.createObj()
                        .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                        .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                        .set("paramType", paramType)
                        .set("paramChineseName", paramChineseName)
                        .set("unit", defaultUnits)
                        .set("stationID", mySta.getID())
                        .set("stationName", mySta.getName())
                        .set("stationLat", mySta.getLat())
                        .set("stationLon", mySta.getLon())
                        .set("value", myStationData.getValue(k));
                jsonArray.add(jsonStation);
                stationDataList.add(new 区台沙尘Model(mySta.getID(),myDate,(int)DateUtil.between(myDate,forecastDate, DateUnit.HOUR),paramType, 1000.0,myStationData.getValue(k)));
            }
        }
        String myStationFileName = StrUtil.format("{}shachen_station_{}_{}.json", stationFileNameBase, paramType, format2);
        File myStationFile = FileUtil.touch(myStationFileName);
        FileUtil.writeUtf8String(jsonArray.toString(), myStationFile);
        if(stationDataList.size()>0){
            ecDao.insert_qtShaChen( DateUtil.format(myDate, "yyyyMM"),stationDataList);
        }
    }

    private static void PM10json转换(Date myDate, String format2, MeteoDataInfo aDataInfo, ProjectionInfo mypro, Dimension yDimension, Dimension xDimension, List<LocalDateTime> tDimension, String myFileNameBase, StationData stationData, List<站点信息> stations, String stationFileNameBase,huanbao ecDao) {
        String[] variableStrs = new String[]{"DUST_1", "DUST_2", "DUST_3", "DUST_4"};
        List<区台沙尘Model> stationDataList=new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        Dimension zDimension = aDataInfo.getDataInfo().getZDimension();
        for (int l = 0; l < zDimension.getLength(); l++) {
            String heightStr = NumberUtil.round(zDimension.getDimValue(l) * 1000, 2).toString();
            String myFileNameBaseLeve = myFileNameBase + heightStr + "/";
            aDataInfo.setLevelIndex(l);
            for (int i = 0; i < tDimension.size(); i++) {
                Date forecastDate = Date.from(tDimension.get(i).plusHours(8).toInstant(ZoneOffset.of("+8")));
                aDataInfo.setTimeIndex(i);
                aDataInfo.setVariableName(variableStrs[0]);
                GridData myGridData = aDataInfo.getGridData();
                for (int j = 1; j < variableStrs.length; j++) {
                    aDataInfo.setVariableName(variableStrs[j]);
                    if (j != variableStrs.length - 1) {
                        myGridData = myGridData.add(aDataInfo.getGridData());
                    } else {
                        myGridData = myGridData.add(aDataInfo.getGridData().mul(0.87));
                    }
                }
                myGridData = myGridData.div(aDataInfo.getGridData("ALT"));
                double[] a1 = (double[]) myGridData.getArray().copyTo1DJavaArray();
                JSONObject json1 = JSONUtil.createObj()
                        .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                        .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                        .set("paramType", "PM10")
                        .set("paramChineseName", "PM10")
                        .set("unit", "ug/m3")
                        .set("height", heightStr)
                        .set("projection", mypro.getProjectionName())
                        .set("CenterLat", mypro.getCenterLat())
                        .set("CenterLon", mypro.getCenterLon())
                        .set("dx", xDimension.getDeltaValue())
                        .set("startX", xDimension.getDimValue(0))
                        .set("xRows", xDimension.getLength())
                        .set("dy", yDimension.getDeltaValue())
                        .set("startY", yDimension.getDimValue(0))
                        .set("yRows", yDimension.getLength())
                        .set("EARTH_RADIUS", 6371229)
                        .set("data", a1);
                String myFileName = StrUtil.format("{}shachen_{}_{}_{}_{}.json", myFileNameBaseLeve, "PM10", format2, String.format("%04d", i), heightStr);
                File myFile = FileUtil.touch(myFileName);
                FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(json1) + "]", myFile);
                StationData myStationData = myGridData.toStation(stationData);
                for (int k = 0; k < stations.size(); k++) {
                    var mySta = stations.get(k);
                    JSONObject jsonStation = JSONUtil.createObj()
                            .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                            .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                            .set("paramType", "PM10")
                            .set("paramChineseName", "PM10")
                            .set("unit", "ug/m3")
                            .set("height", heightStr)
                            .set("stationID", mySta.getID())
                            .set("stationName", mySta.getName())
                            .set("stationLat", mySta.getLat())
                            .set("stationLon", mySta.getLon())
                            .set("value", myStationData.getValue(k));
                    jsonArray.add(jsonStation);
                    stationDataList.add(new 区台沙尘Model(mySta.getID(),myDate,(int)DateUtil.between(myDate,forecastDate, DateUnit.HOUR),"PM10",  NumberUtil.round(zDimension.getDimValue(l) * 1000, 2).doubleValue(),myStationData.getValue(k)));
                }
            }
            if(stationDataList.size()>0){
                try{
                    ecDao.insert_qtShaChen( DateUtil.format(myDate, "yyyyMM"),stationDataList);
                    stationDataList.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String myStationFileName = StrUtil.format("{}shachen_station_{}_{}.json", stationFileNameBase, "PM10", format2);
        File myStationFile = FileUtil.touch(myStationFileName);
        FileUtil.writeUtf8String(jsonArray.toString(), myStationFile);

    }

    private static void PM25json转换(Date myDate, String format2, MeteoDataInfo aDataInfo, ProjectionInfo mypro, Dimension yDimension, Dimension xDimension, List<LocalDateTime> tDimension, String myFileNameBase, StationData stationData, List<站点信息> stations, String stationFileNameBase,huanbao ecDao) {
        Dimension zDimension = aDataInfo.getDataInfo().getZDimension();
        JSONArray jsonArray = new JSONArray();
        List<区台沙尘Model> stationDataList=new ArrayList<>();
        for (int l = 0; l < zDimension.getLength(); l++) {
            String heightStr = NumberUtil.round(zDimension.getDimValue(l) * 1000, 2).toString();
            String myFileNameBaseLeve = myFileNameBase + heightStr + "/";
            aDataInfo.setLevelIndex(l);
            for (int i = 0; i < tDimension.size(); i++) {
                aDataInfo.setTimeIndex(i);
                Date forecastDate = Date.from(tDimension.get(i).plusHours(8).toInstant(ZoneOffset.of("+8")));
                GridData myGridData = aDataInfo.getGridData("DUST_1").add(aDataInfo.getGridData("DUST_2").mul(0.3125));
                myGridData = myGridData.div(aDataInfo.getGridData("ALT"));
                double[] a1 = (double[]) myGridData.getArray().copyTo1DJavaArray();
                JSONObject json1 = JSONUtil.createObj()
                        .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                        .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                        .set("paramType", "PM2.5")
                        .set("paramChineseName", "PM2.5")
                        .set("unit", "ug/m3")
                        .set("height", heightStr)
                        .set("projection", mypro.getProjectionName())
                        .set("CenterLat", mypro.getCenterLat())
                        .set("CenterLon", mypro.getCenterLon())
                        .set("dx", xDimension.getDeltaValue())
                        .set("startX", xDimension.getDimValue(0))
                        .set("xRows", xDimension.getLength())
                        .set("dy", yDimension.getDeltaValue())
                        .set("startY", yDimension.getDimValue(0))
                        .set("yRows", yDimension.getLength())
                        .set("EARTH_RADIUS", 6371229)
                        .set("data", a1);
                String myFileName = StrUtil.format("{}shachen_{}_{}_{}_{}.json", myFileNameBaseLeve, "PM2.5", format2, String.format("%04d", i), heightStr);
                File myFile = FileUtil.touch(myFileName);
                FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(json1) + "]", myFile);
                StationData myStationData = myGridData.toStation(stationData);
                for (int k = 0; k < stations.size(); k++) {
                    var mySta = stations.get(k);
                    JSONObject jsonStation = JSONUtil.createObj()
                            .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                            .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                            .set("paramType", "PM2.5")
                            .set("paramChineseName", "PM2.5")
                            .set("unit", "ug/m3")
                            .set("height", heightStr)
                            .set("stationID", mySta.getID())
                            .set("stationName", mySta.getName())
                            .set("stationLat", mySta.getLat())
                            .set("stationLon", mySta.getLon())
                            .set("value", myStationData.getValue(k));
                    jsonArray.add(jsonStation);
                    stationDataList.add(new 区台沙尘Model(mySta.getID(),myDate,(int)DateUtil.between(myDate,forecastDate, DateUnit.HOUR),"PM2.5",  NumberUtil.round(zDimension.getDimValue(l) * 1000, 2).doubleValue(),myStationData.getValue(k)));
                }
            }
            if(stationDataList.size()>0){
                try{
                    ecDao.insert_qtShaChen( DateUtil.format(myDate, "yyyyMM"),stationDataList);
                    stationDataList.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String myStationFileName = StrUtil.format("{}shachen_station_{}_{}.json", stationFileNameBase, "PM2.5", format2);
        File myStationFile = FileUtil.touch(myStationFileName);
        FileUtil.writeUtf8String(jsonArray.toString(), myStationFile);
    }

    private static void 高空沙尘json转换(Date myDate, String format2, MeteoDataInfo aDataInfo, ProjectionInfo mypro, Dimension yDimension, Dimension xDimension, List<LocalDateTime> tDimension, String[] variableStrs, String paramType, String paramChineseName, String myFileNameBase, boolean absBS, String defaultUnits) {
        Dimension zDimension = aDataInfo.getDataInfo().getZDimension();
        for (int l = 0; l < zDimension.getLength(); l++) {
            String myFileNameBaseLeve = myFileNameBase + NumberUtil.round(zDimension.getDimValue(l) * 1000, 4).toString() + "/";
            aDataInfo.setLevelIndex(l);
            for (int i = 0; i < tDimension.size(); i++) {
                aDataInfo.setTimeIndex(i);
                Variable variable = aDataInfo.getDataInfo().getVariable(variableStrs[0]);
                if (!variable.getUnits().isBlank()) {
                    defaultUnits = variable.getUnits();
                }
                aDataInfo.setVariableName(variableStrs[0]);
                GridData myGridData = aDataInfo.getGridData();
                for (int j = 1; j < variableStrs.length; j++) {
                    aDataInfo.setVariableName(variableStrs[j]);
                    myGridData = myGridData.add(aDataInfo.getGridData());
                }
                if (absBS) {
                    myGridData = myGridData.abs();
                }
                double[] a1 = (double[]) myGridData.getArray().copyTo1DJavaArray();
                JSONObject json1 = JSONUtil.createObj()
                        .set("datetime", DateUtil.format(myDate, "yyyy/MM/dd HH:mm:ss"))
                        .set("forecastTime", DateUtil.format(tDimension.get(i).plusHours(8), "yyyy/MM/dd HH:mm:ss"))
                        .set("paramType", paramType)
                        .set("paramChineseName", paramChineseName)
                        .set("unit", defaultUnits)
                        .set("height", zDimension.getDimValue(l) * 1000)
                        .set("projection", mypro.getProjectionName())
                        .set("CenterLat", mypro.getCenterLat())
                        .set("CenterLon", mypro.getCenterLon())
                        .set("dx", xDimension.getDeltaValue())
                        .set("startX", xDimension.getDimValue(0))
                        .set("xRows", xDimension.getLength())
                        .set("dy", yDimension.getDeltaValue())
                        .set("startY", yDimension.getDimValue(0))
                        .set("yRows", yDimension.getLength())
                        .set("EARTH_RADIUS", 6371229)
                        .set("data", a1);
                String myFileName = StrUtil.format("{}shachen_{}_{}_{}.json", myFileNameBaseLeve, paramType, format2, String.format("%04d", i));
                File myFile = FileUtil.touch(myFileName);
                FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(json1) + "]", myFile);
            }
        }

    }
    public static void 同步沙尘模式数据(String dateStr){
        try{
            Sftp sftp = new Sftp("172.18.142.20", 22, "qxt", "qxt4348050");
            String myDirNameBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachen/" + dateStr + "/";
            sftp.cd("/other/shachenJson/gedian");
            List<String> myfiles=sftp.ls(".", lsEntry -> lsEntry.getFilename().endsWith(dateStr));
            if(myfiles.size()==0){
                sftp.mkdir(dateStr);
            }
            递归上传文件夹(sftp,myDirNameBase,"/other/shachenJson/gedian/"+dateStr);
            String myDirNameBaseStation = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachenStation/" + dateStr;
            sftp.cd("/");
            sftp.cd("/other/shachenJson/zhandian");
            myfiles=sftp.ls(".", lsEntry -> lsEntry.getFilename().endsWith(dateStr));
            if(myfiles.size()==0){
                sftp.mkdir(dateStr);
            }
            递归上传文件夹(sftp,myDirNameBaseStation,"/other/shachenJson/zhandian/"+dateStr);
            sftp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void 递归上传文件夹(Sftp sftp,String sPath,String dPath){
        File[] myFiles1=FileUtil.ls(sPath);
        if(sftp.cd("/")&&sftp.cd(dPath)){
            for(File file1:myFiles1){
                if(file1.isDirectory()){
                    List<String> myfiles2=sftp.ls(".", lsEntry -> lsEntry.getFilename().endsWith(file1.getName()));
                    if(myfiles2.size()==0){
                        if(sftp.cd("/")&&sftp.cd(dPath)&&sftp.mkdir(file1.getName())) {
                            递归上传文件夹(sftp,file1.getPath(),dPath+"/"+file1.getName());
                        }
                    }
                }else{
                    if(sftp.cd("/")&&sftp.cd(dPath)){
                        sftp.put(file1.getPath(),".");
                    }
                }
            }
        }


    }
    @Test
    public void cs() {
        //同步沙尘模式数据("2021-05-12");
        DateTime myDate = new DateTime("2021-06-03 20:00:00", DatePattern.NORM_DATETIME_FORMAT);
        String path = "E:\\cx\\java\\hsqxtJavaCL\\target\\区台数值预报文件\\szyb\\huanbao\\shachen\\qtshachen_2021-06-03.nc";
        区台沙尘模式数据处理(myDate, path);
    }
}
