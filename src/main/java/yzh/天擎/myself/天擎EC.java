package yzh.天擎.myself;

import cma.music.client.DataQueryClient;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class 天擎EC {
    @Test
    public void cs(){
        DateTime myDate = new DateTime("2021-05-31 08:00:00", DatePattern.NORM_DATETIME_FORMAT);
        EC地面下载(myDate,"TEM");
        EC地面(myDate,"TEM","1",0,0,24,"40.53/109.88,40.86/111.57,41.03/113.07,39.8/106.8,40.73/107.37,39.82/110.01,38.83/105.67");
    }
    //按起报时间、预报层次、预报时段、经纬度检索EC预报要素插值
    public static JSONArray EC高空(Date edate,String fcstEle,String levelType,Integer fcstLevel,Integer minVT,Integer maxVT,String latLons){
        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpEleAtPointByTimeAndLevelAndValidtimeRange" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_ECMF_C1D_ANEA_ANA");
        params.put("time", DateUtil.format(DateUtil.offsetHour(edate, -8),"yyyyMMddHHmmss"));
        params.put("fcstEle", fcstEle);
        params.put("levelType", levelType);
        params.put("fcstLevel", String.valueOf(fcstLevel));
        params.put("minVT", String.valueOf(minVT));
        params.put("maxVT", String.valueOf(maxVT));
        params.put("latLons", latLons);
        String dataFormat = "json";
        StringBuffer retStr = new StringBuffer() ;
        /* 3. 调用接口 */
        try {
            //初始化接口服务连接资源
            client.initResources() ;
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
            //输出结果
            if(rst == 0) { //正常返回
                if(JSONUtil.isJson(retStr.toString())){
                    JSONObject jsonObject = JSONUtil.parseObj(retStr);
                    myLists= (JSONArray) jsonObject.get("DS");
                }


            } else { //异常返回

            }
        } catch (Exception e) {
            //异常输出
            e.printStackTrace() ;
        } finally {
            //释放接口服务连接资源
            client.destroyResources() ;
        }
        return myLists;
    }
    public static JSONArray EC地面(Date edate,String fcstEle,String levelType,Integer fcstLevel,Integer minVT,Integer maxVT,String latLons){
        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpEleAtPointByTimeAndLevelAndValidtimeRange" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_ECMF_C1D_GLB_FOR");
        params.put("time", DateUtil.format(DateUtil.offsetHour(edate, -8),"yyyyMMddHHmmss"));
        params.put("fcstEle", fcstEle);
        params.put("levelType", levelType);
        params.put("fcstLevel", String.valueOf(fcstLevel));
        params.put("minVT", String.valueOf(minVT));
        params.put("maxVT", String.valueOf(maxVT));
        params.put("latLons", latLons);
        String dataFormat = "json";
        StringBuffer retStr = new StringBuffer() ;
        /* 3. 调用接口 */
        try {
            //初始化接口服务连接资源
            client.initResources() ;
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
            //输出结果
            if(rst == 0) { //正常返回
                JSONObject jsonObject = JSONUtil.parseObj(retStr);
                myLists= (JSONArray) jsonObject.get("DS");

            } else { //异常返回

            }
        } catch (Exception e) {
            //异常输出
            e.printStackTrace() ;
        } finally {
            //释放接口服务连接资源
            client.destroyResources() ;
        }
        return myLists;
    }

    //按经纬范围、起报时间、预报层次、预报时效检索预EC报要素场
    public static JSONArray EC高空经纬度范围(Date edate,String fcstEle,String levelType,Integer fcstLevel,Integer minVT,Integer maxVT,String latLons){
        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpEleAtPointByTimeAndLevelAndValidtimeRange" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_ECMF_C1D_ANEA_ANA");
        params.put("time", DateUtil.format(DateUtil.offsetHour(edate, -8),"yyyyMMddHHmmss"));
        params.put("fcstEle", fcstEle);
        params.put("levelType", levelType);
        params.put("fcstLevel", String.valueOf(fcstLevel));
        params.put("minVT", String.valueOf(minVT));
        params.put("maxVT", String.valueOf(maxVT));
        params.put("latLons", latLons);
        String dataFormat = "json";
        StringBuffer retStr = new StringBuffer() ;
        /* 3. 调用接口 */
        try {
            //初始化接口服务连接资源
            client.initResources() ;
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
            //输出结果
            if(rst == 0) { //正常返回
                JSONObject jsonObject = JSONUtil.parseObj(retStr);
                myLists= (JSONArray) jsonObject.get("DS");

            } else { //异常返回

            }
        } catch (Exception e) {
            //异常输出
            e.printStackTrace() ;
        } finally {
            //释放接口服务连接资源
            client.destroyResources() ;
        }
        return myLists;
    }
    public static String EC地面下载(Date myDate,String fcstEle){
        try{

            JSONArray myFileLists=getNafpFileByElementAndTime(myDate,fcstEle,"1");
            String format2 = new SimpleDateFormat("yyyyMMdd").format(myDate);
            if(myFileLists!=null&&myFileLists.size()>0){
                for (Object myList:myFileLists
                ) {
                    try{
                        JSONObject myfile=(JSONObject)myList;
                        Date date = DateUtil.parse(myfile.get("Datetime").toString(), "yyyyMMddHHmmss");
                        long filesize= Convert.toLong(myfile.get("FILE_SIZE").toString(),999999L);
                        String file_nameILE_NAME=myfile.get("FILE_NAME").toString();
                        String file_url=myfile.get("FILE_URL").toString();
                        String myName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/ECMWF/tlogp/" + format2 + "/surface/ls/" +file_nameILE_NAME;
                        if(!判断指定日期要素EC文件是否存在(filesize,myName)){
                            HttpUtil.downloadFile(file_url, FileUtil.file(myName), new StreamProgress(){
                                double jd=0;
                                @Override
                                public void start() {
                                    Console.log("{}  开始下载  {}", DateUtil.formatDateTime(new Date()),file_nameILE_NAME);
                                }
                                @Override
                                public void progress(long progressSize) {
                                    double jdls=(progressSize/(double)filesize)*100;
                                    if(jdls-jd>3){
                                        Console.log("{}  {}已下载：{}  {}%", DateUtil.formatDateTime(new Date()),file_nameILE_NAME,FileUtil.readableFileSize(progressSize), NumberUtil.round(jdls,1));
                                        jd=jdls;
                                    }
                                }
                                @Override
                                public void finish() {
                                    Console.log("{}下载完成  {}",DateUtil.formatDateTime(new Date()),file_nameILE_NAME);
                                }
                            });
                        }
                        return myName;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static boolean 判断指定日期要素EC文件是否存在(long size,String fileName){
        if(FileUtil.exist(fileName)){
            File myfile= FileUtil.file(fileName);
            return FileUtil.size(myfile)==size;
        }else{
            return false;
        }


    }
    public static JSONArray 根据起报时间获取天擎下载EC地面列表(Date myDate){
        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpFileByTime" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_ECMF_C1D_GLB_FOR");
        params.put("time", DateUtil.format(DateUtil.offsetHour(myDate, -8),"yyyyMMddHHmmss"));
        params.put("elements", "ELE_CODE,Datetime,FILE_SIZE,FILE_NAME,File_URL");
        String dataFormat = "json";
        StringBuffer retStr = new StringBuffer() ;
        /* 3. 调用接口 */
        try {
            //初始化接口服务连接资源
            client.initResources() ;
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
            //输出结果
            if(rst == 0) { //正常返回
                JSONObject jsonObject = JSONUtil.parseObj(retStr);
                myLists= (JSONArray) jsonObject.get("DS");

            } else { //异常返回

            }
        } catch (Exception e) {
            //异常输出
            e.printStackTrace() ;
        } finally {
            //释放接口服务连接资源
            client.destroyResources() ;
        }
        return  myLists;
    }

    /**
     * 根据起报时间预报要素获取天擎下载EC地面列表
     * @param myDate 起报时间
     * @param fcstEle 预报要素
     * @return
     */
    public static JSONArray getNafpFileByElementAndTime(Date myDate,String fcstEle,String levelType){

        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpFileByElementAndTime" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_ECMF_C1D_GLB_FOR");
        params.put("fcstEle", fcstEle);
        params.put("levelType", levelType);
        params.put("time", DateUtil.format(DateUtil.offsetHour(myDate, -8),"yyyyMMddHHmmss"));
        params.put("elements", "ELE_CODE,Datetime,FILE_SIZE,FILE_NAME,File_URL");
        String dataFormat = "json";
        StringBuffer retStr = new StringBuffer() ;
        /* 3. 调用接口 */
        try {
            //初始化接口服务连接资源
            client.initResources() ;
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
            //输出结果
            if(rst == 0) { //正常返回
                JSONObject jsonObject = JSONUtil.parseObj(retStr);
                myLists= (JSONArray) jsonObject.get("DS");

            } else { //异常返回

            }
        } catch (Exception e) {
            //异常输出
            e.printStackTrace() ;
        } finally {
            //释放接口服务连接资源
            client.destroyResources() ;
        }
        return  myLists;
    }
}
