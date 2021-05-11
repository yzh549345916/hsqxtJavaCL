import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import model.*;
import model.openlayers.OpenlayersQJMethodBaseModel;
import model.openlayers.OpenlayersQJMethodModel;
import model.openlayers.OpenlayersQJMethodPropertiesModel;
import model.openlayers.OpenlayersgeometryModel;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import ucar.nc2.grib.GdsHorizCoordSys;
import ucar.nc2.grib.grib2.*;
import ucar.nc2.grib.grib2.Grib2Gds.LambertConformal;
import ucar.nc2.time.CalendarDate;
import ucar.unidata.geoloc.Earth;
import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.io.RandomAccessFile;
import yzh.dao.RMAPSDao;
import yzh.dao.StationDaoImpl;
import yzh.util.SqlSessionFactoryUtil;
import yzh.数值预报处理.地面实况数据类型转换;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import static cn.hutool.core.util.NumberUtil.round;


public class Grib2处理 {
    @Test
    public void main() {
        DateTime myDate = new DateTime("2021-05-05 08:00:00", DatePattern.NORM_DATETIME_FORMAT);
        // rmaps格点提取(myDate);
        //rmapsJson风流场数据是否存在(myDate);
        rmapsJson格点提取(myDate);
        //boolean b1 = rmaps格点数据是否存在(myDate);
        //var b2 = b1;
    }

    public List<区台数值预报数据Model> 根据文件路径_站点列表处理数据(String path, List<站点信息> stations) {
        List<区台数值预报数据Model> dataLists = new ArrayList<区台数值预报数据Model>();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            while (scan.hasNext()) {
                Grib2Record gr2 = scan.next();
                // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
                Grib2SectionIndicator iss = gr2.getIs();
                // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
                Grib2SectionIdentification ids = gr2.getId();

                // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
                Grib2SectionGridDefinition gds = gr2.getGDSsection();
                Grib2Gds tempGds = gds.getGDS();
                // section 4 产品定义段 包括段长、段号、数据的性质描述
                Grib2SectionProductDefinition pds = gr2.getPDSsection();
                Grib2Pds tempPds = pds.getPDS();
                int forecastTime = tempPds.getForecastTime();//预报时效

                int d = iss.getDiscipline();
                int c = tempPds.getParameterCategory();
                int l1 = tempPds.getLevelType1();
                int l2 = tempPds.getLevelType2();
                int n = tempPds.getParameterNumber();
                int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
                paramType = Grib2数据类型转换(d, c, n);
                if (paramType >= 0) {

                    try {
                        String stringType = 数据类型识别(paramType);
                        Grib2Gds.LatLon ll = (Grib2Gds.LatLon) tempGds;

                        // section 5 数据表示段 包括段长、段号、数据值表示法描述
                        Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                        long l = drs.getStartingPosition();
                        float[] data = gr2.readData(raf, drs.getStartingPosition());
                        CalendarDate referenceDate = ids.getReferenceDate();

                        Date mydate = DateUtil.offsetHour(referenceDate.toDate(), -8);//数据源时间戳时区错误，需要调整
                        //高低温08时起报时候预报时间为0，需纠正为24
                        if (paramType == 7 || paramType == 8) {
                            if (DateUtil.hour(mydate, true) == 8) {
                                forecastTime += 24;
                            }
                        }
                        for (站点信息 station : stations
                        ) {
                            try {
                                int[] rowsz = 根据经纬度获取数据行数(ll.la1, ll.deltaLat, ll.lo1, ll.deltaLon, station.getLat(), station.getLon());
                                if (rowsz.length == 2 && rowsz[0] >= 0 && rowsz[1] >= 0 && rowsz[0] < tempGds.getNyRaw() && rowsz[1] < tempGds.getNxRaw()) {
                                    Double value = round(data[(rowsz[0] * tempGds.getNxRaw() + rowsz[1])], 4).doubleValue();
                                    DateTime myTime = DateUtil.offsetHour(mydate, forecastTime);
                                    dataLists.add(new 区台数值预报数据Model(station.getID(), station.getName(), myTime, DateUtil.date(mydate), forecastTime, stringType, value));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return dataLists;
    }

    public List<区台数值预报数据Model> rmaps根据文件路径_站点列表处理数据(String path, List<站点信息> stations) {
        List<区台数值预报数据Model> dataLists = new ArrayList<区台数值预报数据Model>();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            while (scan.hasNext()) {
                Grib2Record gr2 = scan.next();
                // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
                Grib2SectionIndicator iss = gr2.getIs();
                // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
                Grib2SectionIdentification ids = gr2.getId();
                // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
                Grib2SectionGridDefinition gds = gr2.getGDSsection();
                Grib2Gds tempGds = gds.getGDS();
                // section 4 产品定义段 包括段长、段号、数据的性质描述
                Grib2SectionProductDefinition pds = gr2.getPDSsection();
                Grib2Pds tempPds = pds.getPDS();
                int forecastTime = tempPds.getForecastTime();//预报时效
                int d = iss.getDiscipline();
                int c = tempPds.getParameterCategory();
                //int type2 = tempPds.getLevelType2();
                //double value2 = tempPds.getLevelValue2();
                int n = tempPds.getParameterNumber();
                int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
                paramType = Grib2数据类型转换(d, c, n);
                if (paramType >= 0) {
                    try {
                        /*
                        int type1=tempPds.getLevelType1();
                        if (type1 == 100) {
                            Grib2Tables gc2 = Grib2Tables.factory(gr2);
                            String type1str = gc2.getLevelName(type1);
                            VertCoordType vertUnit = gc2.getVertUnit(type1);
                            String units = vertUnit.getUnits();
                            String s = type1str + ":" + tempPds.getLevelValue1() + "(" + units + ") ";
                        }*/

                        if ("LambertConformal".equalsIgnoreCase(tempGds.getNameShort())) {
                            //lambertConformal  投影坐标范围
                            String stringType = Rmaps数据类型识别(paramType, tempPds.getLevelType1(), tempPds.getLevelValue1());
                            if (!stringType.isBlank()) {
                                LambertConformal lc = (LambertConformal) tempGds;
                                GdsHorizCoordSys gg = lc.makeHorizCoordSys();
                                double[][] myLatLonSz = new double[2][stations.size()];
                                for (int i = 0; i < stations.size(); i++) {
                                    myLatLonSz[0][i] = stations.get(i).getLat();
                                    myLatLonSz[1][i] = stations.get(i).getLon();
                                }
                                double[][] myf3 = gg.proj.latLonToProj(myLatLonSz, 0, 1);
                                //  ucar.unidata.geoloc.projection.LambertConformal l1c =new ucar.unidata.geoloc.projection.LambertConformal(lpt.getLatitude(), lpt.getLongitude(), 43.5, 43.5, gg.dx, gg.dy, 6371.229);
                                //double[][] myf2= l1c.latLonToProj(myLatLonSz,0,1);
                                Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                                long l = drs.getStartingPosition();
                                float[] data = gr2.readData(raf, drs.getStartingPosition());
                                CalendarDate referenceDate = ids.getReferenceDate();
                                Date mydate = referenceDate.toDate();
                                if (tempPds.isTimeInterval()) {
                                    Date date1 = ((Grib2Pds.PdsInterval) tempPds).getIntervalTimeEnd().toDate();
                                    forecastTime = +(int) DateUtil.between(date1, mydate, DateUnit.HOUR);
                                }
                                for (int i = 0; i < stations.size(); i++) {
                                    try {
                                        int[] rowsz = 根据经纬度获取数据行数(gg.startx, gg.dx, gg.starty, gg.dy, myf3[0][i], myf3[1][i]);
                                        if (rowsz.length == 2 && rowsz[0] >= 0 && rowsz[1] >= 0 && rowsz[0] <= gg.nx && rowsz[1] < gg.ny) {
                                            Double value = round(data[(rowsz[1] * gg.nx + rowsz[0])], 4).doubleValue();
                                            dataLists.add(new 区台数值预报数据Model(stations.get(i).getID(), stations.get(i).getName(), DateUtil.offsetHour(mydate, forecastTime), DateUtil.date(mydate), forecastTime, stringType, value));
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return dataLists;
    }

    public static void rmapsJson处理(String path,DateTime myDate) {
        RandomAccessFile raf = null;
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
            String datePath1 = df.format(myDate);
            String timePath1 = df2.format(myDate);

            int myforecastTime=-1;
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            JSONArray array3 = new JSONArray();
            JSONArray array10 = new JSONArray();
            double dlo3=0.03,dlo10=0.1;
            while (scan.hasNext()) {
                Grib2Record gr2 = scan.next();
                // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
                Grib2SectionIndicator iss = gr2.getIs();
                // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
                Grib2SectionIdentification ids = gr2.getId();
                // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
                Grib2SectionGridDefinition gds = gr2.getGDSsection();
                Grib2Gds tempGds = gds.getGDS();
                // section 4 产品定义段 包括段长、段号、数据的性质描述
                Grib2SectionProductDefinition pds = gr2.getPDSsection();
                Grib2Pds tempPds = pds.getPDS();
                int forecastTime = tempPds.getForecastTime();//预报时效
                myforecastTime=forecastTime;
                int d = iss.getDiscipline();
                int c = tempPds.getParameterCategory();
                int n = tempPds.getParameterNumber();
                int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
                paramType = Grib2数据类型转换(d, c, n);
                if (paramType >= 0) {
                    try {
                        if ("LambertConformal".equalsIgnoreCase(tempGds.getNameShort())) {
                            //lambertConformal  投影坐标范围
                            String stringType = Rmaps数据类型识别(paramType, tempPds.getLevelType1(), tempPds.getLevelValue1());
                            if (!stringType.isBlank() && (stringType.compareTo("WIU10") == 0 || stringType.compareTo("WIV10") == 0)) {
                                LambertConformal lc = (LambertConformal) tempGds;
                                GdsHorizCoordSys gg = lc.makeHorizCoordSys();
                                Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                                float[] data = gr2.readData(raf, drs.getStartingPosition());
                                CalendarDate referenceDate = ids.getReferenceDate();
                                Date mydate = referenceDate.toDate();
                                if (tempPds.isTimeInterval()) {
                                    Date date1 = ((Grib2Pds.PdsInterval) tempPds).getIntervalTimeEnd().toDate();
                                    forecastTime = +(int) DateUtil.between(date1, mydate, DateUnit.HOUR);
                                }
                                array3.add(rmaps风流场Json处理(0.03,0.03,105.2,115,43.4,37.7,data,gg,mydate,c,n,stringType,forecastTime));
                                array10.add(rmaps风流场Json处理(0.1,0.1,92,121,53,32,data,gg,mydate,c,n,stringType,forecastTime));
                            }else if(tempPds.getLevelType1()==103&&!stringType.isBlank() ){
                                LambertConformal lc = (LambertConformal) tempGds;
                                GdsHorizCoordSys gg = lc.makeHorizCoordSys();
                                Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                                float[] data = gr2.readData(raf, drs.getStartingPosition());
                                CalendarDate referenceDate = ids.getReferenceDate();
                                Date mydate = referenceDate.toDate();
                                if (tempPds.isTimeInterval()) {
                                    Date date1 = ((Grib2Pds.PdsInterval) tempPds).getIntervalTimeEnd().toDate();
                                    forecastTime = +(int) DateUtil.between(date1, mydate, DateUnit.HOUR);
                                }
                                String myDirNameSaveBase1 = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/json/rmaps数据/"+stringType+"/";
                                String myDirNameSave1 = myDirNameSaveBase1  + datePath1 + "\\";
                                String myDataStr=rmaps常规要素Json处理(paramType,0.03,0.03,105.2,115,43.4,37.7,data,gg,mydate,c,n,stringType,forecastTime);
                                if(!myDataStr.isBlank()){
                                    保存常规要素数据(myDate, timePath1, myforecastTime,myDataStr  , dlo3, myDirNameSave1,stringType);
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/json/rmaps数据/风流场/";
            String myDirNameSave = myDirNameSaveBase  + datePath1 + "\\";
            保存风流场数据(myDate, timePath1, myforecastTime, array3, dlo3, myDirNameSave);
            保存风流场数据(myDate, timePath1, myforecastTime, array10, dlo10, myDirNameSave);
        } catch (Exception e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    private static void 保存风流场数据(DateTime myDate, String timePath1, int myforecastTime, JSONArray array10, double dlo10, String myDirNameSave) {
        String myFileName10 = myDirNameSave + "RMAPS_wind_"+dlo10+"_"+ timePath1 + "_" + String.format("%04d", myforecastTime) + ".json";
        if (!FileUtil.exist(myFileName10)) {
            File myFile = FileUtil.touch(myFileName10);
            FileUtil.writeUtf8String(array10.toString(), myFile);
            System.out.println(DateUtil.date() + "处理" + DateUtil.format(myDate, "MM月dd日HH时") + "起报" + myforecastTime+ "时"+dlo10+"°间隔Rmaps风流场数据。");
        }
    }
    private static void 保存常规要素数据(DateTime myDate, String timePath1, int myforecastTime, String dataStr, double dlo10, String myDirNameSave,String dataType) {
        String myFileName10 = myDirNameSave + "RMAPS_"+dataType+"_"+dlo10+"_"+ timePath1 + "_" + String.format("%04d", myforecastTime) + ".json";
        if (!FileUtil.exist(myFileName10)) {
            File myFile = FileUtil.touch(myFileName10);
            FileUtil.writeUtf8String(dataStr, myFile);
            System.out.println(DateUtil.date() + "处理" + DateUtil.format(myDate, "MM月dd日HH时") + "起报" + myforecastTime+ "时"+dlo10+"°间隔Rmaps"+dataType+"数据。");
        }
    }
    private static JSONObject rmaps风流场Json处理(double dlo,double dla,double lo1,double lo2,double la1,double la2, float[] data,GdsHorizCoordSys gg,Date mydate,int c,int n,String stringType,int forecastTime){
        //double dlo=0.05,dla=0.05,lo1=92,lo2=121,la1=53,la2=32;
        try{
            int loCount=(int)Math.round((lo2-lo1)/dlo);
            int laCount=(int)Math.round(-1*(la2-la1)/dlo);
            lo2=NumberUtil.round(loCount*dlo+lo1,3).doubleValue();
            la2=NumberUtil.round(laCount*dla*-1+la1,3).doubleValue();
            int count = 0;
            Double[] datasz = new Double[(loCount+1) * (laCount+1)];
            for (int j = 0; j <= laCount; j++) {
                for (int i = 0; i <= loCount; i++) {
                    var proLS = gg.proj.latLonToProj(la1 - j * dla, lo1 + i * dlo);
                    int[] rowsz = 根据经纬度获取数据行数(gg.startx, gg.dx, gg.starty, gg.dy, proLS.getX(), proLS.getY());
                    if (rowsz.length == 2 && rowsz[0] >= 0 && rowsz[1] >= 0 && rowsz[0] <= gg.nx && rowsz[1] < gg.ny) {
                        datasz[count++] = round(data[(rowsz[1] * gg.nx + rowsz[0])], 2).doubleValue();
                    }
                }
            }

            JSONObject json1 = JSONUtil.createObj()
                    .putOnce("parameterCategory", c)
                    .putOnce("parameterNumber", n)
                    .putOnce("lo1", lo1)
                    .putOnce("lo2", lo2)
                    .putOnce("la1", la1)
                    .putOnce("la2", la2)
                    .putOnce("dx", dlo)
                    .putOnce("dy", dla)
                    .putOnce("nx", loCount+1)
                    .putOnce("ny", laCount+1)
                    .putOnce("forecastTime", DateUtil.formatDateTime(DateUtil.offsetHour(mydate, forecastTime)))
                    .putOnce("datetime", DateUtil.formatDateTime(mydate))
                    .putOnce("parameterNumberName", stringType);
            JSONObject json2 = JSONUtil.createObj()
                    .putOnce("data", datasz)
                    .putOnce("header", json1);
            return json2;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
    private static String rmaps常规要素Json处理2(Integer DataTypeID,double dlo,double dla,double lo1,double lo2,double la1,double la2, float[] data,GdsHorizCoordSys gg,Date mydate,int c,int n,String stringType,int forecastTime){
        //double dlo=0.05,dla=0.05,lo1=92,lo2=121,la1=53,la2=32;
        try{
            String myYsText = 地面实况数据类型转换.地面实况数据类型文字描述转换(DataTypeID);
            String myYsUnit = 地面实况数据类型转换.地面预报数据类型单位转换(DataTypeID);
            OpenlayersQJMethodBaseModel myData = new OpenlayersQJMethodBaseModel("FeatureCollection");
            int loCount=(int)Math.round((lo2-lo1)/dlo);
            int laCount=(int)Math.round(-1*(la2-la1)/dlo);
            lo2=NumberUtil.round(loCount*dlo+lo1,3).doubleValue();
            la2=NumberUtil.round(laCount*dla*-1+la1,3).doubleValue();
            OpenlayersQJMethodModel[] myFeas = new OpenlayersQJMethodModel[(loCount+1) * (laCount+1)];
            int count = 0;
            for (int i = 0; i <= loCount; i++) {
                for (int j = 0; j <= laCount; j++) {
                    var proLS = gg.proj.latLonToProj(la1 - j * dla, lo1 + i * dlo);
                    int[] rowsz = 根据经纬度获取数据行数(gg.startx, gg.dx, gg.starty, gg.dy, proLS.getX(), proLS.getY());
                    if (rowsz.length == 2 && rowsz[0] >= 0 && rowsz[1] >= 0 && rowsz[0] <= gg.nx && rowsz[1] < gg.ny) {
                        myFeas[count++] = new OpenlayersQJMethodModel("Feature", new OpenlayersgeometryModel("Point", new Double[]{lo1 + i * dlo,la1 - j * dla}), new OpenlayersQJMethodPropertiesModel(la1 - j * dla, lo1 + i * dlo , round(data[(rowsz[1] * gg.nx + rowsz[0])], 2).doubleValue(), myYsText, myYsUnit));
                    }
                }
            }
            myData.setFeatures(myFeas);
            return JSONUtil.parseObj(myData, false).toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static String rmaps常规要素Json处理(Integer DataTypeID,double dlo,double dla,double lo1,double lo2,double la1,double la2, float[] data,GdsHorizCoordSys gg,Date mydate,int c,int n,String stringType,int forecastTime){
        try{
            String myYsText = 地面实况数据类型转换.地面实况数据类型文字描述转换(DataTypeID);
            String myYsUnit = 地面实况数据类型转换.地面预报数据类型单位转换(DataTypeID);
            int loCount=(int)Math.round((lo2-lo1)/dlo);
            int laCount=(int)Math.round(-1*(la2-la1)/dlo);
            lo2=NumberUtil.round(loCount*dlo+lo1,3).doubleValue();
            la2=NumberUtil.round(laCount*dla*-1+la1,3).doubleValue();
            int count = 0;
            Double[] datasz = new Double[(loCount+1) * (laCount+1)];
            for (int i = 0; i <= loCount; i++) {
                for (int j = 0; j <= laCount; j++) {
                    var proLS = gg.proj.latLonToProj(la1 - j * dla, lo1 + i * dlo);
                    int[] rowsz = 根据经纬度获取数据行数(gg.startx, gg.dx, gg.starty, gg.dy, proLS.getX(), proLS.getY());
                    if (rowsz.length == 2 && rowsz[0] >= 0 && rowsz[1] >= 0 && rowsz[0] <= gg.nx && rowsz[1] < gg.ny) {
                        datasz[count++] = round(data[(rowsz[1] * gg.nx + rowsz[0])], 2).doubleValue();
                    }
                }
            }

            JSONObject json1 = JSONUtil.createObj()
                    .putOnce("parameterCategory", c)
                    .putOnce("parameterNumber", n)
                    .putOnce("parameterName", myYsText)
                    .putOnce("unit", myYsUnit)
                    .putOnce("lo1", lo1)
                    .putOnce("lo2", lo2)
                    .putOnce("la1", la1)
                    .putOnce("la2", la2)
                    .putOnce("dx", dlo)
                    .putOnce("dy", dla*-1)
                    .putOnce("nx", loCount+1)
                    .putOnce("ny", laCount+1)
                    .putOnce("forecastTime", DateUtil.formatDateTime(DateUtil.offsetHour(mydate, forecastTime)))
                    .putOnce("datetime", DateUtil.formatDateTime(mydate))
                    .putOnce("parameterNumberName", stringType);
            JSONObject json2 = JSONUtil.createObj()
                    .putOnce("data", datasz)
                    .putOnce("header", json1);
            return json2.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void 历史数据删除(){
        DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8-24);
        for(int i=-24*30;i<-24*15;i=i+24){
            DateTime myTime=DateUtil.offsetHour(myDate,i);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String format1 = df.format(myTime);
            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/rmaps数据/" + format1;
            if(FileUtil.exist(myDirName)){
                FileUtil.del(myDirName);
            }
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
            String format2 = df2.format(myTime);
            String[] dirSz=new String[]{"/区台数值预报文件/szyb/格点数据/","/区台数值预报文件/szyb/站点数据/","/区台数值预报文件/szyb/json/"};
            for (String dirStr:dirSz
                 ) {
                myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + dirStr;
                List<File> files=FileUtil.loopFiles(myDirName, pathname -> pathname.getName().contains(format1)||pathname.getName().contains(format2));
                if(files.size()>0){
                    List<File> delDirs=new ArrayList<>();
                    for (File myFile:files
                    ) {
                        if(!delDirs.contains(myFile.getParentFile())){
                            delDirs.add(myFile.getParentFile());
                        }
                    }
                    for (File myDir:delDirs
                    ) {
                        FileUtil.del(myDir.getPath());
                        System.out.println(DateUtil.date()+"删除历史数据："+myDir.getPath());
                    }
                }
            }

        }


    }
    public int rmaps根据文件路径提取格点数据(String path, DateTime myDate) {
        int count = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        String datePath1 = df.format(myDate);
        String timePath1 = df2.format(myDate);
        String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\szyb\\格点数据\\rmaps数据\\";
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            while (scan.hasNext()) {
                Grib2Record gr2 = scan.next();

                // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
                Grib2SectionIndicator iss = gr2.getIs();
                // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
                Grib2SectionIdentification ids = gr2.getId();
                // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
                Grib2SectionGridDefinition gds = gr2.getGDSsection();
                Grib2Gds tempGds = gds.getGDS();
                // section 4 产品定义段 包括段长、段号、数据的性质描述
                Grib2SectionProductDefinition pds = gr2.getPDSsection();

                Grib2Pds tempPds = pds.getPDS();
                int forecastTime = tempPds.getForecastTime();//预报时效
                int d = iss.getDiscipline();
                int c = tempPds.getParameterCategory();
                //int type2 = tempPds.getLevelType2();
                //double value2 = tempPds.getLevelValue2();
                int n = tempPds.getParameterNumber();
                int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
                paramType = Grib2数据类型转换(d, c, n);
                if (paramType >= 0) {
                    try {
                        /*
                        int type1=tempPds.getLevelType1();
                        if (type1 == 100) {
                            Grib2Tables gc2 = Grib2Tables.factory(gr2);
                            String type1str = gc2.getLevelName(type1);
                            VertCoordType vertUnit = gc2.getVertUnit(type1);
                            String units = vertUnit.getUnits();
                            String s = type1str + ":" + tempPds.getLevelValue1() + "(" + units + ") ";
                        }*/


                        if ("LambertConformal".equalsIgnoreCase(tempGds.getNameShort())) {
                            //lambertConformal  投影坐标范围
                            String stringType = Rmaps数据类型识别(paramType, tempPds.getLevelType1(), tempPds.getLevelValue1());

                            if (!stringType.isBlank()) {
                                CalendarDate referenceDate = ids.getReferenceDate();
                                Date mydate = referenceDate.toDate();
                                if (tempPds.isTimeInterval()) {
                                    Date date1 = ((Grib2Pds.PdsInterval) tempPds).getIntervalTimeEnd().toDate();
                                    forecastTime = +(int) DateUtil.between(date1, mydate, DateUnit.HOUR);
                                }
                                String myType = Rmaps数据类型合并文件夹(stringType);
                                String myDirNameSave = myDirNameSaveBase + myType + "\\" + datePath1 + "\\";
                                String myFileName = myDirNameSave + "RMAPS_" + stringType + "_" + timePath1 + "_" + String.format("%04d", forecastTime) + ".txt";
                                if (!FileUtil.exist(myFileName)) {
                                    File myFile = FileUtil.touch(myFileName);
                                    LambertConformal lc = (LambertConformal) tempGds;
                                    GdsHorizCoordSys gg = lc.makeHorizCoordSys();
                                    LatLonPoint centerLatLon = gg.getCenterLatLon();
                                    // var ss2=gg.proj.getProjectionParameters();
                                    Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                                    float[] data = gr2.readData(raf, drs.getStartingPosition());
                                    rmaps格点数据Model myData = new rmaps格点数据Model(Earth.WGS84_EARTH_RADIUS_KM, tempGds.getNameShort(), centerLatLon.getLatitude(), centerLatLon.getLongitude(), gg.startx, gg.starty, gg.nxRaw, gg.nyRaw, gg.dx, gg.dy, forecastTime, stringType, data);
                                    FileUtil.writeUtf8String("[" + JSONUtil.toJsonStr(JSONUtil.parseObj(myData, true, true)) + "]", myFile);
                                    count++;
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return count;
    }

    public boolean 判断数据是否存在(int purpose, int level, String dataType, DateTime myDateTime) {
        try {
            StationDaoImpl stationDao = new StationDaoImpl();
            List<站点信息> stations = stationDao.获取站点信息();
            if (stations.size() > 0) {
                数值预报数据检索Model zd = new 数值预报数据检索Model("Szyb_GD_ZD_" + DateUtil.format(myDateTime, "yyyyMMdd"), purpose, level, myDateTime, dataType);
                int bjcount = 24;
                //最高最低气温为逐24小时，其他要素为逐三小时
                if (dataType.equals("TMAX") || dataType.equals("TMIN")) {
                    bjcount = 3;
                }
                if (stationDao.获取数据已入库个数(zd) >= (stations.size() * 0.9 * bjcount)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean 判断RMAPS数据是否存在(String dataType, DateTime myDateTime) {
        try {
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
            SqlSession session = sqlSessionFactory.openSession(true);
            RMAPSDao rmapsDao = session.getMapper(RMAPSDao.class);

            List<站点信息> stations = rmapsDao.getAllStations();
            if (stations.size() > 0) {
                int count = rmapsDao.count_Rmaps_GD_ZD("Rmaps_GD_ZD_" + DateUtil.format(myDateTime, "yyyyMMdd"), dataType, myDateTime);
                if (count >= (stations.size() * 0.9 * 72)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String 数据类型识别(int paramType) {
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
            case 3:
                myString = "风向";
                break;
            case 4:
                myString = "WIU10";
                break;
            case 5:
                myString = "WIV10";
                break;
            case 6:
                myString = "气压";
                break;
            case 7:
                myString = "TMAX";
                break;
            case 8:
                myString = "TMIN";
                break;
            default:
                myString = "";
                break;
        }
        return myString;
    }

    public static String Rmaps数据类型识别(int paramType, int levelType, double level) {
        if (paramType == 0) {
            if (levelType == 1) {
                return "TEM_surface";
            } else if (levelType == 100) {
                if (level == 50000) {
                    return "TEM_isobaric_500";
                } else if (level == 70000) {
                    return "TEM_isobaric_700";
                } else if (level == 85000) {
                    return "TEM_isobaric_850";
                }
            } else if (levelType == 103) {
                return "TEM";
            }
        } else if (paramType == 2) {
            return "PRE";
        } else if (paramType == 4) {
            if (levelType == 100) {
                if (level == 50000) {
                    return "WIU10_isobaric_500";
                } else if (level == 70000) {
                    return "WIU10_isobaric_700";
                } else if (level == 85000) {
                    return "WIU10_isobaric_850";
                }
            } else if (levelType == 103) {
                return "WIU10";
            }
        } else if (paramType == 5) {
            if (levelType == 100) {
                if (level == 50000) {
                    return "WIV10_isobaric_500";
                } else if (level == 70000) {
                    return "WIV10_isobaric_700";
                } else if (level == 85000) {
                    return "WIV10_isobaric_850";
                }
            } else if (levelType == 103) {
                return "WIV10";
            }
        } else if (paramType == 1900) {
            return "VIS";
        } else if (paramType == 603) {
            return "Low_cloud";
        } else if (paramType == 604) {
            return "Medium_cloud";
        } else if (paramType == 605) {
            return "High_cloud";
        } else if (paramType == 301) {
            return "PRS";
        } else if (paramType == 1) {
            if (levelType == 100) {
                if (level == 50000) {
                    return "RHU_isobaric_500";
                } else if (level == 70000) {
                    return "RHU_isobaric_700";
                } else if (level == 85000) {
                    return "RHU_isobaric_850";
                }
            } else if (levelType == 103) {
                return "RHU";
            }
        } else if (paramType == 305) {
            if (levelType == 100) {
                if (level == 50000) {
                    return "Geopotential_height_500";
                } else if (level == 70000) {
                    return "Geopotential_height_700";
                } else if (level == 85000) {
                    return "Geopotential_height_850";
                }
            }
        }
        return "";
    }

    public String Rmaps数据类型合并文件夹(String type) {
        if (type.contains("_")) {
            if (type.startsWith("Geopotential_height")) {
                return "Geopotential_height\\" + type;
            } else if (type.equals("High_cloud") || type.equals("Low_cloud") || type.equals("Medium_cloud")) {
                return type;
            }
            return type.split("_")[0] + "\\" + type;
        } else {
            if (type.equals("TEM") || type.equals("RHU") || type.equals("WIU10") || type.equals("WIV10")) {
                return type + "\\" + type;
            }
            return type;
        }
    }

    public static int Grib2数据类型转换(int d, int c, int n) {
        int paramType = -1;
        if (d == 0 && c == 0 && n == 0) {
            // 温度
            paramType = 0;
        } else if (d == 0 && c == 0 && n == 5) {
            // 最低温度
            paramType = 8;
        } else if (d == 0 && c == 0 && n == 4) {
            // 最高温度
            paramType = 7;
        } else if (d == 0 && c == 1 && n == 1) {
            // 相对湿度
            paramType = 1;
        } else if (d == 0 && c == 1 && n == 8) {
            // 降水
            paramType = 2;
        } else if (d == 0 & c == 2 && n == 0) {
            // 风向
            paramType = 3;
        } else if (d == 0 & c == 2 && n == 2) {
            // 风速U风量
            paramType = 4;
        } else if (d == 0 & c == 2 && n == 3) {
            // 风速V风量
            paramType = 5;
        } else if (d == 0 && c == 3 && n == 0) {
            // 气压
            paramType = 6;
        } else if (d == 0 && c == 3 && n == 1) {
            // 海平面气压
            paramType = 301;
        } else if (d == 0 && c == 19 && n == 0) {
            // 能见度
            paramType = 1900;
        } else if (d == 0 && c == 6 && n == 3) {
            // 低云
            paramType = 603;
        } else if (d == 0 && c == 6 && n == 4) {
            // 中云
            paramType = 604;
        } else if (d == 0 && c == 6 && n == 5) {
            // 高云
            paramType = 605;
        } else if (d == 0 && c == 3 && n == 5) {
            // 位势高度
            paramType = 305;
        }
        return paramType;
    }

    public int 本地预报要素名称转GRIB2(String dataType) {
        switch (dataType) {
            case "TEM":
                return 0;
            case "ERH":
                return 1;
            case "PRE_3h":
                return 2;
            case "WIU10":
                return 4;
            case "WIV10":
                return 5;
            case "TMAX":
                return 7;
            case "TMIN":
                return 8;
            default:
                return -1;
        }

    }

    public String[] 获取区台格点数值预报数据类型() {
        return new String[]{"TMP", "ERH", "EDA10", "TMAX", "TMIN"};
    }

    public static int[] 根据经纬度获取数据行数(double startLat, double deltaLat, double startLon, double deltaLon, double myLat, double myLon) {
        int[] myRow = {-1, -1};
        myRow[0] = (int) Math.round((myLat - startLat) / deltaLat);
        myRow[1] = (int) Math.round((myLon - startLon) / deltaLon);
        return myRow;
    }

    public List<区台格点数值预报站点Model> 区台格点数值预报站点数据Grib2文件处理(DateTime myDate) {
        List<区台格点数值预报站点Model> dataLists = new ArrayList<>();
        try {
            StationDaoImpl stationDao = new StationDaoImpl();
            List<站点信息> stations = stationDao.获取站点信息();
            String[] qtType = 获取区台格点数值预报数据类型();
            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\格点数据\\" + DateUtil.format(myDate, "yyyyMMdd") + "\\";
            for (String type : qtType
            ) {
                List<File> myfiles = FileUtil.loopFiles(myDirName, new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(DateUtil.format(myDate, "yyyyMMddHHmm")) && pathname.getName().contains(type);
                    }
                });
                if (myfiles.size() > 0) {
                    for (File file : myfiles
                    ) {
                        List<区台数值预报数据Model> datasLS = 根据文件路径_站点列表处理数据(file.getPath(), stations);
                        if (type.equals("TMP")) {
                            温度数据转换(dataLists, datasLS);
                        } else if (type.equals("ERH")) {
                            湿度数据转换(dataLists, datasLS);
                        } else if (type.equals("EDA10")) {
                            风数据转换(dataLists, datasLS);
                        } else if (type.equals("TMAX")) {
                            高温数据转换(dataLists, datasLS);
                        } else if (type.equals("TMIN")) {
                            低温数据转换(dataLists, datasLS);
                        }
                    }
                    for (区台格点数值预报站点Model mydata : dataLists
                    ) {
                        mydata.setPurpose(1);
                        mydata.setLevel(103);
                    }
                }
            }

        } catch (IORuntimeException e) {
            e.printStackTrace();
        }
        return dataLists;
    }

    public void rmaps格点数值预报站点数据Grib2文件处理(DateTime myDate) {

        try {
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
            SqlSession session = sqlSessionFactory.openSession(true);
            RMAPSDao rmapsDao = session.getMapper(RMAPSDao.class);

            List<站点信息> stations = rmapsDao.getAllStations();
            DateTime myDateUtc = DateUtil.offsetHour(myDate, -8);
            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\rmaps数据\\" + DateUtil.format(myDate, "yyyyMMdd") + "\\";
            List<File> myfilesLS = FileUtil.loopFiles(myDirName, new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().contains("ALL_" + DateUtil.format(myDateUtc, "yyyyMMddHHmm"));
                }
            });
            // myfiles.sort(Comparator.comparing(File::getName));
            List<File> myfiles = new ArrayList<>();
            for (int i = 0; i <= 96; i++) {
                for (File fil : myfilesLS
                ) {
                    if (fil.getName().endsWith(String.format("%03d", i) + "01.GRB2")) {
                        myfiles.add(fil);
                        break;
                    }
                }
            }
            if (myfiles.size() > 0) {
                List<Rmaps数值预报站点Model> dataListLS = new ArrayList<>();
                for (File file : myfiles
                ) {
                    try {
                        List<Rmaps数值预报站点Model> dataList = new ArrayList<>();
                        List<区台数值预报数据Model> datasLS = rmaps根据文件路径_站点列表处理数据(file.getPath(), stations);
                        预报数据ModelConvertRmaps数据处理(dataList, datasLS);
                        if (dataList.size() > 0) {
                            if (!dataListLS.isEmpty()) {
                                //判断上一组数据是否和当前数据是连续临近时次
                                boolean sxBS = dataListLS.get(0).getSX() + 1 == dataList.get(0).getSX();
                                List<Rmaps数值预报站点Model> dataListRk = new ArrayList<>();
                                for (Rmaps数值预报站点Model item : dataList
                                ) {
                                    try {
                                        if (sxBS) {
                                            if (dataListLS.stream().anyMatch(y -> y.getMyDate().equals(item.getMyDate()) && y.getSX() + 1 == item.getSX() && y.getID().equals(item.getID()))) {
                                                Rmaps数值预报站点Model datals = dataListLS.stream().filter(y -> y.getMyDate().equals(item.getMyDate()) && y.getSX() + 1 == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                                                dataListRk.add(new Rmaps数值预报站点Model(item, NumberUtil.round(item.getPRE() - datals.getPRE(), 2).doubleValue()));
                                            }
                                        } else {
                                            //如果时次不连续，则不对累计降水量进行处理
                                            dataListRk.add(new Rmaps数值预报站点Model(item));
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (dataListRk.size() > 0) {

                                    int n1 = rmapsDao.insert_rmaps_GD_ZD("Rmaps_GD_ZD_" + DateUtil.format(myDate, "yyyyMMdd"), dataListRk);
                                    System.out.println(DateUtil.date() + "共计入库" + DateUtil.format(dataListRk.get(0).getMyDate(), "MM月dd日HH时") + "起报" + dataListRk.get(0).getSX() + "时的数据" + n1 + "条。");
                                }
                            } else {
                                int n1 = rmapsDao.insert_rmaps_GD_ZD("Rmaps_GD_ZD_" + DateUtil.format(myDate, "yyyyMMdd"), dataList);
                                System.out.println(DateUtil.date() + "共计入库" + DateUtil.format(dataList.get(0).getMyDate(), "MM月dd日HH时") + "起报" + dataList.get(0).getSX() + "时的数据" + n1 + "条。");
                            }
                            dataListLS = new ArrayList<>(dataList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        } catch (IORuntimeException e) {
            e.printStackTrace();
        }

    }

    public void rmaps格点提取(DateTime myDate) {

        DateTime myDateUtc = DateUtil.offsetHour(myDate, -8);
        String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\rmaps数据\\" + DateUtil.format(myDate, "yyyyMMdd") + "\\";
        List<File> myfilesLS = FileUtil.loopFiles(myDirName, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().contains("ALL_" + DateUtil.format(myDateUtc, "yyyyMMddHHmm"));
            }
        });
        List<File> myfiles = new ArrayList<>();
        for (int i = 0; i <= 96; i++) {
            for (File fil : myfilesLS
            ) {
                if (fil.getName().endsWith(String.format("%03d", i) + "01.GRB2")) {
                    myfiles.add(fil);
                    break;
                }
            }
        }
        if (myfiles.size() > 0) {
            for (File file : myfiles
            ) {
                try {
                    int count = rmaps根据文件路径提取格点数据(file.getPath(), myDate);
                    if (count > 0) {
                        System.out.println(StrUtil.format("{}处理{}起报{}时{}条RMAPS格点数据", DateUtil.date(), DateUtil.format(myDate, "yyyy年MM月dd日HH时"), StrUtil.sub(file.getName(), -10, -7), count));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void rmapsJson格点提取(DateTime myDate) {

        try {
            DateTime myDateUtc = DateUtil.offsetHour(myDate, -8);
            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\rmaps数据\\" + DateUtil.format(myDate, "yyyyMMdd") + "\\";
            List<File> myfilesLS = FileUtil.loopFiles(myDirName, pathname -> pathname.getName().contains("ALL_" + DateUtil.format(myDateUtc, "yyyyMMddHHmm")));
            // myfiles.sort(Comparator.comparing(File::getName));
            List<File> myfiles = new ArrayList<>();
            for (int i = 0; i <= 96; i++) {
                for (File fil : myfilesLS
                ) {
                    if (fil.getName().endsWith(String.format("%03d", i) + "01.GRB2")) {
                        myfiles.add(fil);
                        break;
                    }
                }
            }
            if (myfiles.size() > 0) {
                for (File file : myfiles
                ) {
                    try {
                        rmapsJson处理(file.getPath(),myDate);
                        //rmaps风流场处理1公里(file.getPath(),myDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        } catch (IORuntimeException e) {
            e.printStackTrace();
        }

    }

    public boolean rmaps格点数据是否存在(DateTime myDate) {
        int maxHour = 72;
        if (myDate.hour(true) == 20) {
            maxHour = 96;
        }
        boolean bs = false;
        try {
            String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\szyb\\格点数据\\rmaps数据\\";
            int finalMaxHour = maxHour;
            var ssss = FileUtil.loopFiles(myDirNameSaveBase, pathname -> pathname.getName().endsWith(DateUtil.format(myDate, "yyyyMMddHH") + "_" + String.format("%04d", finalMaxHour) + ".txt"));
            if (ssss.size() >= 26) {
                bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bs = false;
        } finally {
            return bs;
        }
    }
    public static boolean rmapsJson风流场数据是否存在(DateTime myDate) {
        int maxHour = 72*2;
        if (myDate.hour(true) == 20) {
            maxHour = 96*2;
        }
        boolean bs = false;
        try {
            String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/json/rmaps数据/风流场/";
            var ssss = FileUtil.loopFiles(myDirNameSaveBase, pathname -> pathname.getName().contains(DateUtil.format(myDate, "yyyyMMddHH")));
            if (ssss.size() >= maxHour) {
                bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bs = false;
        } finally {
            return bs;
        }
    }

    private void 预报数据ModelConvertRmaps数据处理(List<Rmaps数值预报站点Model> dataLists, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {
                Rmaps数值预报站点Model data = new Rmaps数值预报站点Model();
                boolean bs = false;
                if (dataLists.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                    data = dataLists.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                    bs = true;
                } else {
                    data = new Rmaps数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                }
                switch (item.getDataType()) {
                    case "PRE":
                        data.setPRE(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "TEM":
                        data.setTEM(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                        break;
                    case "TEM_surface":
                        data.setTEM_surface(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                        break;
                    case "RHU":
                        data.setRHU(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "PRS":
                        data.setPRS(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIU10":
                        data.setWIU10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIV10":
                        data.setWIV10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "VIS":
                        data.setVIS(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "Low_cloud":
                        data.setLow_cloud(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "Medium_cloud":
                        data.setMedium_cloud(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "High_cloud":
                        data.setHigh_cloud(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "TEM_isobaric_500":
                        data.setTEM_isobaric_500(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                        break;
                    case "TEM_isobaric_700":
                        data.setTEM_isobaric_700(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                        break;
                    case "TEM_isobaric_850":
                        data.setTEM_isobaric_850(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                        break;
                    case "RHU_isobaric_500":
                        data.setRHU_isobaric_500(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "RHU_isobaric_700":
                        data.setRHU_isobaric_700(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "RHU_isobaric_850":
                        data.setRHU_isobaric_850(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIU10_isobaric_500":
                        data.setWIU10_isobaric_500(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIU10_isobaric_700":
                        data.setWIU10_isobaric_700(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIU10_isobaric_850":
                        data.setWIU10_isobaric_850(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIV10_isobaric_500":
                        data.setWIV10_isobaric_500(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIV10_isobaric_700":
                        data.setWIV10_isobaric_700(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "WIV10_isobaric_850":
                        data.setWIV10_isobaric_850(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "Geopotential_height_500":
                        data.setGeopotential_height_500(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "Geopotential_height_700":
                        data.setGeopotential_height_700(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                    case "Geopotential_height_850":
                        data.setGeopotential_height_850(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        break;
                }
                if (bs) {
                    dataLists.set(dataLists.indexOf(data), data);
                } else {
                    dataLists.add(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void 温度数据转换(List<区台格点数值预报站点Model> dataList, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {
                if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                    区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                    data.setTEM(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.set(dataList.indexOf(data), data);
                } else {
                    区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                    data.setTEM(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void 高温数据转换(List<区台格点数值预报站点Model> dataList, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {
                if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                    区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                    data.setTMAX(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.set(dataList.indexOf(data), data);
                } else {
                    区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                    data.setTMAX(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void 低温数据转换(List<区台格点数值预报站点Model> dataList, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {
                if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                    区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                    data.setTMIN(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.set(dataList.indexOf(data), data);
                } else {
                    区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                    data.setTMIN(NumberUtil.round(item.getForecastValue() - 273.15, 2).doubleValue());
                    dataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void 湿度数据转换(List<区台格点数值预报站点Model> dataList, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {

                if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                    区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                    data.setERH(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                    dataList.set(dataList.indexOf(data), data);
                } else {
                    区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                    data.setERH(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                    dataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void 风数据转换(List<区台格点数值预报站点Model> dataList, List<区台数值预报数据Model> datasLS) {
        for (区台数值预报数据Model item : datasLS
        ) {
            try {
                if (item.getDataType().equals("WIU10")) {
                    if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                        区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                        data.setWIU10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        dataList.set(dataList.indexOf(data), data);
                    } else {
                        区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                        data.setWIU10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        dataList.add(data);
                    }
                } else {
                    if (dataList.stream().anyMatch(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID()))) {
                        区台格点数值预报站点Model data = dataList.stream().filter(y -> y.getMyDate().equals(item.getBaseTime()) && y.getSX() == item.getSX() && y.getID().equals(item.getID())).findFirst().get();
                        data.setWIV10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        dataList.set(dataList.indexOf(data), data);
                    } else {
                        区台格点数值预报站点Model data = new 区台格点数值预报站点Model(item.getID(), item.getBaseTime(), item.getSX());
                        data.setWIV10(NumberUtil.round(item.getForecastValue(), 2).doubleValue());
                        dataList.add(data);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double 获取数据(String path, int[] rowsz) {
        double value = -99999;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            int i = 0;
            scan.hasNext();
            Grib2Record gr2 = scan.next();
            // do stuff
            // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
            Grib2SectionIndicator iss = gr2.getIs();


            // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
            Grib2SectionIdentification ids = gr2.getId();

            // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
            Grib2SectionGridDefinition gds = gr2.getGDSsection();
            // section 4 产品定义段 包括段长、段号、数据的性质描述
            Grib2SectionProductDefinition pds = gr2.getPDSsection();
            Grib2Pds tempPds = pds.getPDS();
            int forecastTime = tempPds.getForecastTime();

            int d = iss.getDiscipline();
            int c = tempPds.getParameterCategory();
            int n = tempPds.getParameterNumber();


            int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
            if (d == 0 && c == 0 && n == 0) {
                // 温度
                paramType = 0;
            }
            if (d == 0 && c == 1 && n == 1) {
                // 相对湿度
                paramType = 1;
            }
            if (d == 0 && c == 1 && n == 8) {
                // 降水
                paramType = 2;
            }
            if (d == 0 & c == 2 && n == 0) {
                // 风向
                paramType = 3;
            }
            if (d == 0 & c == 2 && n == 2) {
                // 风速U风量
                paramType = 4;
            }
            if (d == 0 & c == 2 && n == 3) {
                // 风速V风量
                paramType = 5;
            }
            if (d == 0 && c == 3 && n == 0) {
                // 气压
                paramType = 6;
            }

            // section 5 数据表示段 包括段长、段号、数据值表示法描述
            Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
            if (paramType != -1) {
                long l = drs.getStartingPosition();
                float[] data = gr2.readData(raf, drs.getStartingPosition());
                value = round(data[(rowsz[0] * 679 + rowsz[1])], 4).doubleValue();


            }

        } catch (IOException e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }
        }
        return value;
    }

    public void TestReadGrab2(String path) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            Grib2RecordScanner scan = new Grib2RecordScanner(raf);
            int i = 0;
            while (scan.hasNext()) {
                Grib2Record gr2 = scan.next();
                // do stuff
                // section 0 指示段 包含GRIB、学科、GRIB 码版本号、资料长度
                Grib2SectionIndicator iss = gr2.getIs();

                StringBuffer section0 = new StringBuffer();
                section0.append("section0 : ");
                section0.append("StartingPosition ").append(iss.getStartPos()).append(",");
                section0.append("MessageLength ").append(iss.getMessageLength()).append(",");
                section0.append("EndPos ").append(iss.getEndPos()).append(",");

                // section 1 标识段 包含段长、段号,应用于GRIB 资料中全部加工数据的特征---时间
                Grib2SectionIdentification ids = gr2.getId();
                StringBuilder section1 = new StringBuilder();
                CalendarDate referenceDate = ids.getReferenceDate();

                section1.append("section1 : ").append(ids.toString()).append(",");
                section1.append("referenceDate : ").append(referenceDate.toString());

                // section 2 本地使用段 包含段长、段号,由编报中心附加的本地使用的信息
                Grib2SectionLocalUse lus = gr2.getLocalUseSection();
                StringBuilder section2 = new StringBuilder();
                byte[] lu = lus.getRawBytes();
                section2.append("section2 : ");
                if (lu != null && lu.length != 0) {
                    section2.append("Length ").append(lu.length).append(",");
                    section2.append("str ").append(new String(lu)).append(",");
                } else {
                    section2.append("Length 0,");
                }
                // section 3 网格定义段 包含段长、段号、网格面和面内数据的几何形状定义
                Grib2SectionGridDefinition gds = gr2.getGDSsection();
                Grib2Gds tempGds = gds.getGDS();
                Formatter formatter = new Formatter();
                tempGds.testHorizCoordSys(formatter);
                tempGds.getNxRaw();// 每行格数
                tempGds.getNyRaw();// 行数
                StringBuilder section3 = new StringBuilder();
                section3.append("section3 : ");
                section3.append("Length ").append(gds.getLength()).append(",");
                section3.append("NumberPoints ").append(tempGds.getNyRaw()).append("*").append(tempGds.getNxRaw()).append("=").append(gds.getNumberPoints()).append(",");
                String gdsStr = null;


                if (tempGds.isLatLon()) {
                    // 经纬度范围


                }
                Grib2Gds.LatLon ll = (Grib2Gds.LatLon) tempGds;
                String la = "la:" + ll.la1 + "~" + ll.la2 + ",deltaLat:" + ll.deltaLat + ",";
                String lo = "lo:" + ll.lo1 + "~" + ll.lo2 + ",deltaLon:" + ll.deltaLon + ",";
                gdsStr = la + lo;


                section3.append(gdsStr);

                // section 4 产品定义段 包括段长、段号、数据的性质描述
                Grib2SectionProductDefinition pds = gr2.getPDSsection();
                int PDSTemplateNumber = pds.getPDSTemplateNumber();
                Grib2Pds tempPds = pds.getPDS();

                int forecastTime = tempPds.getForecastTime();

                // 参数层高由以下四个数值决定
                int type1 = tempPds.getLevelType1();
                float value1 = (float) tempPds.getLevelValue1();
                int type2 = tempPds.getLevelType2();
                double value2 = tempPds.getLevelValue2();


                // 从xml中读取层高类型
                // String type1str = gc2.getLevelName(type1);
                // String type2str = gc2.getLevelName(type2);
                //
                // VertUnit vertUnit = gc2.getVertUnit(type1);
                // String units = vertUnit.getUnits();
                // String s = type1str + ":" + value1 + "(" + units + ") ";
                // if (!"Missing".equalsIgnoreCase(type2str)) {
                // s += "-" + value2;
                // }

                // 参数类型由以下三个参数决定
                int d = iss.getDiscipline();
                int c = tempPds.getParameterCategory();
                int n = tempPds.getParameterNumber();


                int paramType = -1;// 0温度，1相对湿度，2降水,3风向,4风速U分量，5风速V分量，6气压,
                if (d == 0 && c == 0 && n == 0) {
                    // 温度
                    paramType = 0;
                }
                if (d == 0 && c == 1 && n == 1) {
                    // 相对湿度
                    paramType = 1;
                }
                if (d == 0 && c == 1 && n == 8) {
                    // 降水
                    paramType = 2;
                }
                if (d == 0 & c == 2 && n == 0) {
                    // 风向
                    paramType = 3;
                }
                if (d == 0 & c == 2 && n == 2) {
                    // 风速U风量
                    paramType = 4;
                }
                if (d == 0 & c == 2 && n == 3) {
                    // 风速V风量
                    paramType = 5;
                }
                if (d == 0 && c == 3 && n == 0) {
                    // 气压
                    paramType = 6;
                }

                //从xml中读取参数类型
				 /*GribTables.Parameter param = gc2.getParameter(d, c, n);
				 if (param == null) {
				 if (paramType != -1) {
				 System.out.println("error");
				 }
				 continue;
				 }*/

                StringBuilder section4 = new StringBuilder();
                section4.append("section4 : ");
                section4.append("Length:").append(pds.getLength()).append(",");
                section4.append("Length:").append("PDSTemplateNumber:" + PDSTemplateNumber).append(",");
                section4.append("d c n:").append(d + " " + c + " " + n).append(",");
				/* section4.append("name:").append(param.getName()).append(",");
				 section4.append("unit:").append(param.getUnit()).append(",");
				 section4.append("abbrev:").append(param.getAbbrev()).append(",");*/
                // section4.append(s);
                section4.append(",LevelType1:").append(type1).append(",");
                section4.append("forecastTime:" + forecastTime).append(",");

                // section 5 数据表示段 包括段长、段号、数据值表示法描述
                Grib2SectionDataRepresentation drs = gr2.getDataRepresentationSection();
                int dateTemplate = drs.getDataTemplate();
                StringBuilder section5 = new StringBuilder();
                section5.append("section5 : ");
                section5.append("DataTemplate : ").append(dateTemplate).append(",");
                section5.append("StartingPosition ").append(drs.getStartingPosition()).append(",");
                section5.append("Length ").append(drs.getLength(raf)).append(",");
                section5.append("DataPoints ").append(drs.getDataPoints()).append(",");

                // section 6 包括段长、段号,以及指示每个格点上的数据是否存在
                Grib2SectionBitMap bms = gr2.getBitmapSection();
                StringBuilder section6 = new StringBuilder();
                section6.append("section6 : ");
                section6.append("StartingPosition ").append(bms.getStartingPosition()).append(",");
                section6.append("BitMapIndicator ").append(bms.getBitMapIndicator()).append(",");

                // section 7
                Grib2SectionData bds = gr2.getDataSection();
                StringBuilder section7 = new StringBuilder();
                section7.append("section7 : ");
                section7.append("StartingPosition ").append(bds.getStartingPosition()).append(",");
                section7.append("MsgLength ").append(bds.getMsgLength()).append(",");

                if (paramType != -1) {
                    long l = drs.getStartingPosition();
                    float[] data = gr2.readData(raf, drs.getStartingPosition());
                    // System.out.println(drs.getStartingPosition());
                    // System.out.println(Arrays.toString(data));
                    int[] rowsz = 根据经纬度获取数据行数(ll.la1, ll.deltaLat, ll.lo1, ll.deltaLon, 55.78, 127.03);
                    int xx = rowsz[0] * 679 + rowsz[1];
                    float myData = data[xx];
                    System.out.println(i++ + "\n" + section0.toString() + "\n" + section1.toString() + "\n" + section2.toString() + "\n" + section3.toString() + "\n" + section4.toString() + "\n" + section5.toString() + "\n" + section6.toString() + "\n" + section7.toString() + "\n");
                    for (int j = 0; j < 5; j++) {
                        System.out.print(data[j] + " ");
                    }
                    System.out.println("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
