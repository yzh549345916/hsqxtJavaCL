package yzh.天擎.clib.StaElemStatAPI;

import cma.music.RetFilesInfo;
import cma.music.client.DataQueryClient;
import yzh.天擎.util.ClibUtil;

import java.util.HashMap;

/*
 * 客户端调取，站点要素统计，存储为html格式
 */
public class StaElemStatAPI_CLIB_callAPI_to_saveAsFile_HTML {

	/*
	 * main方法 如：按时间段统计中国地面逐小时降水量 statSurfPre
	 */
	public static void main(String[] args) {

		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "statSurfPre";
		/* 2.3 接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		// 必选参数
		params.put("elements", "Station_ID_C,Station_Name");//统计分组：站号，站名
		params.put("timeRange", "(20190601000000,20190601060000]"); // 时间范围，前开后闭
		// 可选参数
		params.put("orderby", "SUM_PRE_1h:desc"); // 排序：按照累计降水从大到小
		// params.put("statEleValueRanges", "SUM_PRE_1h:(50,)");
		// //统计结果过滤：累计降水值大于50mm的记录
		// params.put("limitCnt", "10") ; //返回最多记录数：10
		// params.put("staLevels", "011,012,013") ; //台站级别：国家站（基准站、基本站、一般站）
		 /* 2.4 返回文件的格式 */
	    String dataFormat = "html" ;
	    /* 2.5 文件的本地全路径 */
	    String savePath = "F:/1/StaElemStatAPI_CLIB_callAPI_to_saveAsFile_HTML.html" ;
	    /* 2.6 返回文件的描述对象 */
	    RetFilesInfo retFilesInfo = new RetFilesInfo() ;
	    
	    /* 3. 调用接口 */
	    try {
	      //初始化接口服务连接资源
	      client.initResources() ;
	      //调用接口
	      int rst = client.callAPI_to_saveAsFile(userId, pwd, interfaceId, params, dataFormat, savePath, retFilesInfo) ;
	      //输出结果
	      if(rst == 0) { //正常返回
	    	  ClibUtil clibUtil = new ClibUtil() ;
	    	  clibUtil.outputRst( retFilesInfo ) ;
	      } else { //异常返回
	    	  System.out.println( "[error] StaElemStatAPI_CLIB_callAPI_to_saveAsFile_HTML." ) ;       
	    	  System.out.printf( "\treturn code: %d. \n", rst ) ;
	    	  System.out.printf( "\terror message: %s.\n", retFilesInfo.request.errorMessage ) ;
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
