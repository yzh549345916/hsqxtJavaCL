package yzh.天擎.myself;

import cma.music.client.DataQueryClient;
import cn.hutool.core.convert.Convert;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class 文件下载 {
    @Test
    public void cs(){
        EC下载();
    }
    public static JSONArray 下载亚洲沙尘模式(Date edate){
        JSONArray myLists=null;
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient() ;

        /* 2. 调用方法的参数定义，并赋值 */
        /* 2.1 用户名&密码 */
        String userId = "USR_YZHWQ" ;
        String pwd = "YZHHGDjm3" ;
        /* 2.2  接口ID */
        String interfaceId = "getNafpFileByTimeRange" ;
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = new HashMap<String, String>();
        //必选参数
        params.put("dataCode", "NAFP_WMO_CUACE_DUST");
        params.put("timeRange", "["+ DateUtil.format(DateUtil.offsetDay(edate, -3),"yyyyMMddHHmmss")+","+DateUtil.format(edate,"yyyyMMddHHmmss")+"]");
        params.put("elements", "Datetime,FILE_SIZE");
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
    public static JSONArray 下载EC(){
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
        params.put("dataCode", "NAFP_ECMF_C1D_ANEA_ANA");
        params.put("time", "20210530000000");
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
    public static void EC下载(){
        try{
            JSONArray myFileLists=下载EC();
            if(myFileLists!=null&&myFileLists.size()>0){
                for (Object myList:myFileLists
                ) {
                    try{
                        JSONObject myfile=(JSONObject)myList;
                        Date date = DateUtil.parse(myfile.get("Datetime").toString(), "yyyyMMddHHmmss");
                        long filesize= Convert.toLong(myfile.get("FILE_SIZE").toString(),999999L);
                        String file_nameILE_NAME=myfile.get("FILE_NAME").toString();
                        String file_url=myfile.get("FILE_URL").toString();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String format1 = df.format(date);
                        String myName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/ECHeight/" + format1 + "/"+file_nameILE_NAME;
                        HttpUtil.downloadFile(file_url, FileUtil.file(myName), new StreamProgress(){
                            double jd=0;
                            @Override
                            public void start() {
                                Console.log("{}  开始下载  {}",new Date(),file_nameILE_NAME);
                            }

                            @Override
                            public void progress(long progressSize) {
                                double jdls=(progressSize/(double)filesize)*100;
                                if(jdls-jd>3){
                                    Console.log("{}  {}已下载：{}  {}%", new Date(),file_nameILE_NAME,FileUtil.readableFileSize(progressSize), NumberUtil.round(jdls,1));
                                    jd=jdls;
                                }

                            }

                            @Override
                            public void finish() {
                                Console.log("{}下载完成  {}",new Date(),file_nameILE_NAME);

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
