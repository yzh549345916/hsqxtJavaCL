package yzh.天擎.clib.GridElemRectSearchAPI;

import cma.music.client.DataQueryClient;
import yzh.天擎.util.FormatUtil;

import java.util.HashMap;

/*
 * 客户端调取，
 * 格点场要素获取（切块），写HTML格式字符串
 */
public class GridElemRectSearchAPI_CLIB_callAPI_to_serializedStr_HTML {
	/*
    * main方法
    * 如：按经纬范围、起报时间、预报层次、预报时效检索预报要素场 getNafpEleGridInRectByTimeAndLevelAndValidtime
    */
	public static void main(String[] args) {

		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "getNafpEleGridInRectByTimeAndLevelAndValidtime";
		/* 2.3 接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		// 必选参数
		params.put("dataCode", "NAFP_FOR_FTM_LOW_EC_GLB"); //资料：欧洲中心数值预报产品-低分辨率-全球
		params.put("time", "20200601000000"); //起报时间
		params.put("validTime", "24"); // 预报时效：24	
		params.put("fcstEle", "TEM"); //预报要素（单个)：气温	
		params.put("levelType", "100"); //层次类型
		params.put("fcstLevel", "850"); //预报层次（单个)：850hpa
		params.put("minLat", "39"); // 经纬度范围：北京及周边（纬度39-42，经度115-117）
		params.put("maxLat", "42"); //结束纬度
		params.put("minLon", "115"); //开始经度
		params.put("maxLon", "117"); //结束经度
		
		// 可选参数

		/* 2.4 返回文件的格式 */
		String dataFormat = "html";
		/* 2.5 返回字符串 */
		StringBuffer retStr = new StringBuffer();

		/* 3. 调用接口 */
		try {
			// 初始化接口服务连接资源
			client.initResources();
			// 调用接口
			int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId,
					params, dataFormat, retStr);
			// 输出结果
			if (rst == 0) { // 正常返回
				FormatUtil formatUtil = new FormatUtil();
				formatUtil.outputRstHtml(retStr.toString());
			} else { // 异常返回
				System.out
						.println("[error] GridElemRectSearchAPI_CLIB_callAPI_to_serializedStr_HTML.");
				System.out.printf("\treturn code: %d. \n", rst);
			}
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
		} finally {
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}
	
}
