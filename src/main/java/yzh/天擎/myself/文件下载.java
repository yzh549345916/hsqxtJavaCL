package yzh.天擎.myself;

import cma.music.client.DataQueryClient;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;


import java.util.Date;
import java.util.HashMap;

public class 文件下载 {
    @Test
    public void cs(){
        下载亚洲沙尘模式(new Date());
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
}
