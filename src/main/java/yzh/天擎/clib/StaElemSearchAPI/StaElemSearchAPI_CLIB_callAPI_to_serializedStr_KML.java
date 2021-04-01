package yzh.天擎.clib.StaElemSearchAPI;

import cma.music.client.DataQueryClient;
import yzh.天擎.util.FormatUtil;

import java.util.HashMap;

public class StaElemSearchAPI_CLIB_callAPI_to_serializedStr_KML {

	/*
	   * main方法，程序入口
	   * 如：按时间检索地面数据要素 getSurfEleByTime
	   */
	  public static void main(String[] args) {
	    
	    /* 1. 定义client对象 */
	    DataQueryClient client = new DataQueryClient() ;

	    /* 2. 调用方法的参数定义，并赋值 */
	    /* 2.1 用户名&密码 */
	    String userId = "USR_YZHWQ" ;
	    String pwd = "YZHHGDjm3" ;
	    /* 2.2  接口ID */
	    String interfaceId = "getSurfEleByTime" ;   
	    /* 2.3  接口参数，多个参数间无顺序 */
	    HashMap<String, String> params = new HashMap<String, String>();
		// 必选参数
		params.put("dataCode", "SURF_CHN_MUL_HOR"); // 资料代码
		params.put("elements","lat,lon,PRE_1h");// 检索要素：纬度、经度、小时降水
		params.put("times", "20190601000000"); // 检索时间
		
		params.put("ctLevel", "-10,0,1,5,100"); // 色标分级
		
		// 可选参数
		params.put("eleValueRanges", "PRE_1h:(,100)"); // 检索要素值范围
		
	    params.put("limitCnt", "10") ; //返回最多记录数：10
	    /* 2.4 返回文件的格式 */
	    String dataFormat = "kml-p" ;
	    /* 2.5 返回字符串 */
	    StringBuffer retStr = new StringBuffer() ;
	    
	    /* 3. 调用接口 */
	    try {
	      //初始化接口服务连接资源
	      client.initResources() ;
	      //调用接口
	      int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr) ;
	      //输出结果
	      if(rst == 0) { //正常返回
	        FormatUtil formatUtil = new FormatUtil() ;
	        formatUtil.outputRstText( retStr.toString() ) ;
	      } else { //异常返回
	        System.out.println( "[error] StaElemSearchAPI_CLIB_callAPI_to_serializedStr_KML." ) ;       
	        System.out.printf( "\treturn code: %d. \n", rst ) ;
	      }
	    } catch (Exception e) {
	      //异常输出
	      e.printStackTrace() ;
	    } finally {
	      //释放接口服务连接资源
	      client.destroyResources() ;
	    }
	  }

}
