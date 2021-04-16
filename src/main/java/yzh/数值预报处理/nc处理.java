package yzh.数值预报处理;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import ucar.nc2.*;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.util.CompareNetcdf2;
import ucar.nc2.write.NetcdfCopier;
import ucar.nc2.write.NetcdfFormatWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Float.NaN;


public class nc处理 {


    public static void CUACE数据处理(DateTime myDate) {
        DateTime myDateUtc = DateUtil.offsetHour(myDate, -8);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String format1 = df.format(myDateUtc);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd-HH");
        String format2 = df2.format(myDateUtc);
        String sourPath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/compressCUACE/" + format1;
        List<File> myfiles = FileUtil.loopFiles(sourPath, pathname -> pathname.getName().contains(DateUtil.format(myDateUtc, "yyyy-MM-dd-HH")));
        if (myfiles.size() > 0) {
            String resourPath = myfiles.get(0).getPath();
            String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/格点数据/CUACE/";
            if (!FileUtil.exist(myDirNameSaveBase)) {
                FileUtil.mkdir(myDirNameSaveBase);
            }
            try {
                NetCDFDataInfo netCDFDataInfo = new NetCDFDataInfo(resourPath);
                netCDFDataInfo.readDataInfo();
                if (netCDFDataInfo.xDim != null && netCDFDataInfo.yDim != null && netCDFDataInfo.tDim != null) {
                    List<Date> myDates = 时间戳转时间(netCDFDataInfo.tDim.getDimValue());
                    int xCountS = -1, xCountE = -1, yCountS = -1, yCountE = -1;
                    xCountS = (int) Math.round((60 - netCDFDataInfo.xDim.getDimValue(0)) / netCDFDataInfo.xDim.getStep());
                    xCountE = (int) Math.round((140 - netCDFDataInfo.xDim.getDimValue(0)) / netCDFDataInfo.xDim.getStep());
                    yCountS = (int) Math.round((40 - netCDFDataInfo.yDim.getDimValue(0)) / netCDFDataInfo.yDim.getStep());
                    yCountE = (int) Math.round((60 - netCDFDataInfo.yDim.getDimValue(0)) / netCDFDataInfo.yDim.getStep());
                    for (Variable var1 : netCDFDataInfo.ncVariables
                    ) {
                        if (var1.getRank() == 3) {
                            JSONObject json1;
                            String myName = var1.getFullName();
                            String myFileName1 = myDirNameSaveBase + myName + "/" + format1 + "/";
                            if (!FileUtil.exist(myFileName1)) {
                                FileUtil.mkdir(myFileName1);
                            }
                            List<Dimension> dims = var1.getDimensions();
                            for (int i = 0; i < myDates.size(); i++) {
                                try {
                                    String myFileName =myFileName1+ "CUACE_" + myName + "_" + format2 + "_" + String.format("%04d", DateUtil.between(myDateUtc, myDates.get(i), DateUnit.HOUR)) + ".txt";
                                    if (!FileUtil.exist(myFileName)) {
                                        List ranges = new ArrayList();
                                        for (var dimLs : dims
                                        ) {
                                            switch (dimLs.getName()) {
                                                case "lat":
                                                    ranges.add(new ucar.ma2.Range(yCountS, yCountE));
                                                    break;
                                                case "lon":
                                                    ranges.add(new ucar.ma2.Range(xCountS, xCountE));
                                                    break;
                                                case "time":
                                                    ranges.add(new ucar.ma2.Range(i, i));
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        float[] data = (float[]) var1.read(ranges).reduce(0).copyTo1DJavaArray();
                                        for (int k = 0; k < data.length; k++) {
                                            if (Float.isNaN(data[k])) {
                                                data[k] = -99999;
                                            }
                                        }
                                        json1 = JSONUtil.createObj()
                                                .putOnce("datetime", DateUtil.format(myDates.get(0), "yyyy/MM/dd HH:mm:ss"))
                                                .putOnce("forecastTime", DateUtil.format(myDates.get(i), "yyyy/MM/dd HH:mm:ss"))
                                                .putOnce("paramType", myName)
                                                .putOnce("projection", "LongLat")
                                                .putOnce("startLat", "40")
                                                .putOnce("startLon", "60.00")
                                                .putOnce("endLat", "60")
                                                .putOnce("endLon", "140.00")
                                                .putOnce("latCount", yCountE - yCountS + 1)
                                                .putOnce("lonCount", xCountE - xCountS + 1)
                                                .putOnce("latStep", netCDFDataInfo.yDim.getStep())
                                                .putOnce("lonStep", netCDFDataInfo.xDim.getStep())
                                                .putOnce("units", var1.findAttributeString("units", ""))
                                                .putOnce("data", data);
                                        File myFile = FileUtil.touch(myFileName);
                                        FileUtil.appendUtf8String(JSONUtil.toJsonStr(json1), myFile);
                                    }
                                } catch (ucar.ma2.InvalidRangeException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else if (var1.getRank() == 4) {
                            JSONObject json1;
                            String myName = var1.getFullName();
                            List<Dimension> dims = var1.getDimensions();
                            double[] levels = netCDFDataInfo.zDim.getValues();
                            for (int j = 0; j < levels.length; j++) {
                                String myFileName1 = myDirNameSaveBase + myName + "/" +myName+"_"+Math.round(levels[j])+"/"+ format1 + "/";
                                if (!FileUtil.exist(myFileName1)) {
                                    FileUtil.mkdir(myFileName1);
                                }

                                for (int i = 0; i < myDates.size(); i++) {
                                    try {
                                        String myFileName= myFileName1+"CUACE_" + myName+"_"+Math.round(levels[j]) + "_" + format2 + "_" + String.format("%04d", DateUtil.between(myDateUtc, myDates.get(i), DateUnit.HOUR)) + ".txt";
                                        if (!FileUtil.exist(myFileName)){
                                            List ranges = new ArrayList();
                                            for (var dimLs : dims
                                            ) {
                                                switch (dimLs.getName()) {
                                                    case "lat":
                                                        ranges.add(new ucar.ma2.Range(yCountS, yCountE));
                                                        break;
                                                    case "lon":
                                                        ranges.add(new ucar.ma2.Range(xCountS, xCountE));
                                                        break;
                                                    case "time":
                                                        ranges.add(new ucar.ma2.Range(i, i));
                                                        break;
                                                    case "level":
                                                        ranges.add(new ucar.ma2.Range(j, j));
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                            float[] data = (float[]) var1.read(ranges).reduce(0).copyTo1DJavaArray();
                                            for (int k = 0; k < data.length; k++) {
                                                if (Float.isNaN(data[k])) {
                                                    data[k] = -99999;
                                                }
                                            }
                                            json1 = JSONUtil.createObj()
                                                    .putOnce("datetime", DateUtil.format(myDates.get(0), "yyyy/MM/dd HH:mm:ss"))
                                                    .putOnce("forecastTime", DateUtil.format(myDates.get(i), "yyyy/MM/dd HH:mm:ss"))
                                                    .set("level", levels[j])
                                                    .set("paramType", myName)
                                                    .set("projection", "LongLat")
                                                    .set("startLat", "40")
                                                    .set("startLon", "60.00")
                                                    .set("endLat", "60")
                                                    .set("endLon", "140.00")
                                                    .set("latCount", yCountE - yCountS + 1)
                                                    .set("lonCount", xCountE - xCountS + 1)
                                                    .set("latStep", netCDFDataInfo.yDim.getStep())
                                                    .set("lonStep", netCDFDataInfo.xDim.getStep())
                                                    .set("units", var1.findAttributeString("units", ""))
                                                    .set("data", data);

                                            File myFile = FileUtil.touch(myFileName);
                                            FileUtil.appendUtf8String(JSONUtil.toJsonStr(json1), myFile);
                                            json1.size();
                                        }


                                    } catch (ucar.ma2.InvalidRangeException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            var1.getSize();
                        }
                    }
                }
                netCDFDataInfo.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
    public static void 人影数据(){
        String path="D:\\新建文件夹\\1";
        File[] files;
        files = FileUtil.ls(path);
        for (var myFile:files
             ) {
            if(myFile.isFile()&&myFile.getName().endsWith(".nc")){
                人影数据处理(myFile.getPath());
            }

        }
    }
    public static void 人影数据处理(String Path) {
        String resourPath = Path;
        String[] clDatasz=new String[]{"cc","ciwc","clwc"};
        String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/格点数据/renying/";
        try {
            NetCDFDataInfo netCDFDataInfo = new NetCDFDataInfo(resourPath);
            netCDFDataInfo.readDataInfo();
            if (netCDFDataInfo.xDim != null && netCDFDataInfo.yDim != null && netCDFDataInfo.tDim != null) {
                List<Date> myDates = 时间戳转时间(netCDFDataInfo.tDim.getDimValue());
                int xCountS = -1, xCountE = -1, yCountS = -1, yCountE = -1;
                xCountS = (int) Math.round((92 - netCDFDataInfo.xDim.getDimValue(0)) / netCDFDataInfo.xDim.getStep());
                xCountE = (int) Math.round((117 - netCDFDataInfo.xDim.getDimValue(0)) / netCDFDataInfo.xDim.getStep());
                yCountS = (int) Math.round((43 - netCDFDataInfo.yDim.getDimValue(0)) / netCDFDataInfo.yDim.getStep());
                yCountE = (int) Math.round((31 - netCDFDataInfo.yDim.getDimValue(0)) / netCDFDataInfo.yDim.getStep());
                for (Variable var1 : netCDFDataInfo.ncVariables
                ) {
                    if (var1.getRank() == 3) {
                        JSONObject json1;
                        String myName = var1.getFullName();


                    } else if (var1.getRank() == 4) {
                        JSONObject json1;
                        boolean bsval=false;
                        for (String sitem:clDatasz
                             ) {
                            if(sitem.equals(var1.getShortName())){
                                bsval=true;
                            }
                        }
                        if(bsval){
                            String myName = var1.getFullName();
                            List<Dimension> dims = var1.getDimensions();
                            double[] levels = netCDFDataInfo.zDim.getValues();
                            for (int j = 0; j < levels.length; j++) {


                                for (int i = 0; i < myDates.size(); i++) {
                                    try {
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy");
                                        String format1 = df.format(myDates.get(i));
                                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
                                        String format2 = df2.format(myDates.get(i));
                                        String myFileName1 = myDirNameSaveBase + myName + "/" +myName+"_"+String.format("%04d", Math.round(levels[j]))+"/"+ format1 + "/";
                                        if (!FileUtil.exist(myFileName1)) {
                                            FileUtil.mkdir(myFileName1);
                                        }
                                        String myFileName= myFileName1+ myName+"_"+String.format("%04d", Math.round(levels[j]))+ "_" + format2 + "_"  + ".txt";
                                        if (!FileUtil.exist(myFileName)){
                                            List ranges = new ArrayList();
                                            for (var dimLs : dims
                                            ) {
                                                switch (dimLs.getName()) {
                                                    case "latitude":
                                                        ranges.add(new ucar.ma2.Range(yCountS, yCountE));
                                                        break;
                                                    case "longitude":
                                                        ranges.add(new ucar.ma2.Range(xCountS, xCountE));
                                                        break;
                                                    case "time":
                                                        ranges.add(new ucar.ma2.Range(i, i));
                                                        break;
                                                    case "level":
                                                        ranges.add(new ucar.ma2.Range(j, j));
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                           // double add_offset, scale_factor, missingValue;
                                            double[] packData = getPackData(var1);
                                            //add_offset = packData[0];
                                           // scale_factor = packData[1];
                                            //missingValue = packData[2];
                                            double[] data=数据调整((short[])var1.read(ranges).reduce(0).copyTo1DJavaArray(),packData[0],packData[1],packData[2]);
                                            json1 = JSONUtil.createObj()
                                                    .putOnce("datetime", DateUtil.format(myDates.get(i), "yyyy/MM/dd HH:mm:ss"))
                                                    .set("level", levels[j])
                                                    .set("paramType", myName)
                                                    .set("projection", "LongLat")
                                                    .set("startLat", "43")
                                                    .set("startLon", "92.00")
                                                    .set("endLat", "31")
                                                    .set("endLon", "117.00")
                                                    .set("latCount", yCountE - yCountS + 1)
                                                    .set("lonCount", xCountE - xCountS + 1)
                                                    .set("latStep", netCDFDataInfo.yDim.getStep())
                                                    .set("lonStep", netCDFDataInfo.xDim.getStep())
                                                    .set("units", var1.findAttributeString("units", ""))
                                                    .set("data", data);

                                            File myFile = FileUtil.touch(myFileName);
                                            FileUtil.appendUtf8String(JSONUtil.toJsonStr(json1), myFile);
                                            json1.size();
                                        }


                                    } catch (ucar.ma2.InvalidRangeException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            var1.getSize();
                        }
                    }
                }
            }
            netCDFDataInfo.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    private static double[] 数据调整(short[] szin,Double add_offset,Double scale_factor,Double missingValue){
        double[] szout=new double[szin.length];
        for(int i=0;i<szin.length;i++){
            if((double)szin[i]==missingValue){
                szout[i]=szin[i];
            }else{
                szout[i] = szin[i] * scale_factor + add_offset;
            }
        }
        return szout;
    }
    private static double[] getPackData(ucar.nc2.Variable var) {
        double add_offset, scale_factor, missingValue = -9999.0;
        add_offset = 0;
        scale_factor = 1;
        for (int i = 0; i < var.getAttributes().size(); i++) {
            ucar.nc2.Attribute att = var.getAttributes().get(i);
            String attName = att.getShortName();
            if (attName.equals("add_offset")) {
                add_offset = Double.parseDouble(att.getValue(0).toString());
            }

            if (attName.equals("scale_factor")) {
                scale_factor = Double.parseDouble(att.getValue(0).toString());
            }

            if (attName.equals("missing_value")) {
                missingValue = Double.parseDouble(att.getValue(0).toString());
            }

            //MODIS NetCDF data
            if (attName.equals("_FillValue")) {
                try {
                    missingValue = Double.parseDouble(att.getValue(0).toString());
                } catch (NumberFormatException e) {

                }
            }
        }

//        //Adjust undefine data
//        if (Double.isNaN(missingValue)) {
//            missingValue = this.getMissingValue();
//        } else {
//            missingValue = missingValue * scale_factor + add_offset;
//        }
        return new double[]{add_offset, scale_factor, missingValue};
    }
    public static void 删除30天前的格点的数据(){
        try{
            Date myDate=DateUtil.offsetDay(new Date(),-30);
            String sPath=FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/格点数据/CUACE/" ;

            for(int i=-30;i<=0;i++){

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String format1 = df.format(myDate);
                List<File> files=FileUtil.loopFiles(sPath, pathname -> pathname.getName().contains(format1+"-"));
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
                }
                String myPath=sPath+format1 + "/";
                myDate=DateUtil.offsetDay(myDate,-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean cuace格点数据是否存在(DateTime myDate){
        int maxHour=168;
        boolean bs=false;
        try{
            String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/格点数据/CUACE/";
            int finalMaxHour = maxHour;
            var ssss=FileUtil.loopFiles(myDirNameSaveBase, pathname -> pathname.getName().endsWith(DateUtil.format(DateUtil.offsetHour(myDate,-8), "yyyy-MM-dd-HH")+"_"+String.format("%04d", finalMaxHour)+".txt"));
            if(ssss.size()>=28){
                bs= true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bs=false;
        }
        finally {
            return bs;
        }
    }
    public static List<Date> 时间戳转时间(List<Double> listd) {
        List<Date> dates = new ArrayList<>();
        for (Double dd : listd
        ) {
            dates.add(DateUtil.date(dd.longValue()));
        }
        return dates;
    }

    public static Boolean compressCUACE(String sPath, String dPath) {

        try {

            NetcdfFile ncFile = NetcdfFiles.open(sPath);
            NetcdfFormatWriter.Builder builder = NetcdfFormatWriter.createNewNetcdf3(dPath);
            var ncb = ncFile.toBuilder();
            ncb.rootGroup.removeVariable("V");
            ncb.rootGroup.removeVariable("U");
            ncb.rootGroup.removeVariable("T");
            ncb.rootGroup.removeVariable("T2");
            NetcdfCopier copier = NetcdfCopier.create(ncb.build(), builder);
            copier.write(null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void cs() {
        人影数据();
        //人影数据处理("D:\\新建文件夹\\1\\1981-01-12.nc");
        //删除30天前的格点的数据();
        //cuace格点数据是否存在(myDate);
        //readNCfile(myDate);
        //System.out.println(compressCUACE("E:\\1.nc","E:\\123.nc"));

    }

    public static  void 京津冀测试(Date date){
       try{
           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
           String format1 = df.format(date);
           SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd-HH");
           String format2 = df2.format(date);
           SimpleDateFormat df3= new SimpleDateFormat("yyyyMMddHH");
           String format3 = df3.format(date);
           String resourPath = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/jjj/"+ format1 + "/";
           if(!FileUtil.exist(resourPath)){
               FileUtil.mkdir(resourPath);
               return;
           }
           resourPath+=format3+".nc";
           if(FileUtil.exist(resourPath)){
               NetCDFDataInfo netCDFDataInfo = new NetCDFDataInfo(resourPath,true);
               netCDFDataInfo.readDataInfo();
               String myDirNameSaveBase = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/格点数据/huanbao/jjj/";
               for (Variable var1 : netCDFDataInfo.ncVariables){
                   try{
                       if(var1.getRank()==2){
                           String myName= var1.getFullName();
                           String myFileName1 = myDirNameSaveBase + myName + "/" + format1 + "/";
                           String myFileName =myFileName1+ "jjj_hb_" + myName + "_" + format2 + ".txt";
                           if (!FileUtil.exist(myFileName)){
                               double[] data = (double[]) var1.read().copyTo1DJavaArray();
                               for (int k = 0; k < data.length; k++) {
                                   if (Double.isNaN(data[k])) {
                                       data[k] = -99999;
                                   }

                               }
                               JSONObject json1 = JSONUtil.createObj()
                                       .set("datetime", DateUtil.format(date, "yyyy/MM/dd HH:mm:ss"))
                                       .set("paramType", myName)
                                       .set("projection", "LongLat")
                                       .set("startLat", netCDFDataInfo.yDim.getMinValue())
                                       .set("startLon", netCDFDataInfo.xDim.getMinValue())
                                       .set("endLat", netCDFDataInfo.yDim.getMaxValue())
                                       .set("endLon", netCDFDataInfo.xDim.getMaxValue())
                                       .set("latCount", netCDFDataInfo.yDim.getValues().length)
                                       .set("lonCount", netCDFDataInfo.xDim.getValues().length)
                                       .set("latStep", netCDFDataInfo.yDim.getStep())
                                       .set("lonStep", netCDFDataInfo.xDim.getStep())
                                       .set("data", data);

                               File myFile = FileUtil.touch(myFileName);
                               FileUtil.appendUtf8String(JSONUtil.toJsonStr(json1), myFile);

                           }

                       }else if(var1.getRank()==3){
                           List<Double> mytimes=netCDFDataInfo.tDim.getDimValue();
                           String myName= var1.getFullName();
                           String myFileName1 = myDirNameSaveBase + myName + "/" + format1 + "/";

                           for(int i=0;i<mytimes.size();i++){
                               String myFileName =myFileName1+ "jjj_hb_" + myName + "_" + format2+"_"+String.format("%04d", mytimes.get(i).intValue()) + ".txt";
                               List<Dimension> dims = var1.getDimensions();
                               List ranges = new ArrayList();
                               for (var dimLs : dims
                               ) {
                                   switch (dimLs.getName()) {
                                       case "latp":
                                           ranges.add(new ucar.ma2.Range(0, netCDFDataInfo.yDim.getValues().length-1));
                                           break;
                                       case "lonp":
                                           ranges.add(new ucar.ma2.Range(0, netCDFDataInfo.xDim.getValues().length-1));
                                           break;
                                       case "ltime":
                                           ranges.add(new ucar.ma2.Range(i, i));
                                           break;
                                       default:
                                           break;
                                   }
                               }
                               double[] data = (double[]) var1.read(ranges).copyTo1DJavaArray();
                               for (int k = 0; k < data.length; k++) {
                                   if (Double.isNaN(data[k])) {
                                       data[k] = -99999;
                                   }
                               }
                               JSONObject json1 = JSONUtil.createObj()
                                       .set("datetime", DateUtil.format(date, "yyyy/MM/dd HH:mm:ss"))
                                       .set("forecastTime", DateUtil.format(DateUtil.offsetHour(date,mytimes.get(i).intValue()), "yyyy/MM/dd HH:mm:ss"))
                                       .set("paramType", myName)
                                       .set("projection", "LongLat")
                                       .set("startLat", netCDFDataInfo.yDim.getMinValue())
                                       .set("startLon", netCDFDataInfo.xDim.getMinValue())
                                       .set("endLat", netCDFDataInfo.yDim.getMaxValue())
                                       .set("endLon", netCDFDataInfo.xDim.getMaxValue())
                                       .set("latCount", netCDFDataInfo.yDim.getValues().length)
                                       .set("lonCount", netCDFDataInfo.xDim.getValues().length)
                                       .set("latStep", netCDFDataInfo.yDim.getStep())
                                       .set("lonStep", netCDFDataInfo.xDim.getStep())
                                       .set("data", data);

                               File myFile = FileUtil.touch(myFileName);
                               FileUtil.appendUtf8String(JSONUtil.toJsonStr(json1), myFile);
                           }
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               netCDFDataInfo.close();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }

    }
    public static double[][] tfunction(double[][] test){
        int m=test.length;
        int n=test[0].length;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                if (Double.isNaN(test[j][i])) {
                    test[i][j] = -99999;
                }
            }
        }
        return  test;
    }



}
