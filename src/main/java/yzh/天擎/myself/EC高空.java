package yzh.天擎.myself;

import cma.music.client.DataQueryClient;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

public class EC高空 {
    @Test
    public void cs(){
        DateTime myDate = new DateTime("2021-04-07 20:00:00", DatePattern.NORM_DATETIME_FORMAT);
        EC高空(myDate,"GPH","100",500,0,24,"40.53/109.88,40.86/111.57,41.03/113.07,39.8/106.8,40.73/107.37,39.82/110.01,38.83/105.67");
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
}
