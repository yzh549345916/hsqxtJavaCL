package yzh.天擎.clib.GridElemPointSearchAPI;

import cma.music.RetFilesInfo;
import cma.music.client.DataQueryClient;
import yzh.天擎.util.ClibUtil;

import java.util.HashMap;

/*
 * 客户端调取，格点要素抽取，存储json文件对象
 */
public class GridElemPointSearchAPI_CLIB_callAPI_to_saveAsFile_JSONP {
	/*
	 * main方法 
	 * 如：按起报时间、预报层次、预报时段、经纬度检索预报要素插值 getNafpEleAtPointByTimeAndLevelAndValidtimeRange
	 */
	public static void main(String[] args) {

		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "getNafpEleAtPointByTimeAndLevelAndValidtimeRange";
		/* 2.3 接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		//必选参数
		params.put("dataCode", "NAFP_FOR_FTM_LOW_EC_GLB"); //资料：欧洲中心数值预报产品-低分辨率-全球
		params.put("time", "20190601000000"); //起报时间
		params.put("minVT", "0"); //起始预报时效
		params.put("maxVT", "12"); //终止预报时效
		params.put("latLons", "39.8/116.4667,31.2/121.4333"); //经纬度点，北京（纬度39.8，经度116.4667）、上海（纬度31.2，经度121.4333）
		params.put("fcstEle", "TEM"); //预报要素（单个)：气温	
		params.put("levelType", "100"); //层次类型
		params.put("fcstLevel", "850"); //预报层次（单个)：850hpa
		
		//jsonp必选
		params.put("callbackName", "apiData");
		
		/* 2.4 返回文件的格式 */
		String dataFormat = "jsonp";
		/* 2.5 文件的本地全路径 */
		String savePath = "e:/temp/GridElemPointSearchAPI_CLIB_callAPI_to_saveAsFile_JSONP.jsonp";
		/* 2.6 返回文件的描述对象 */
		RetFilesInfo retFilesInfo = new RetFilesInfo();

		/* 3. 调用接口 */
		try {
			// 初始化接口服务连接资源
			client.initResources();
			// 调用接口
			int rst = client.callAPI_to_saveAsFile(userId, pwd, interfaceId,
					params, dataFormat, savePath, retFilesInfo);
			// 输出结果
			if (rst == 0) { // 正常返回
				ClibUtil clibUtil = new ClibUtil();
				clibUtil.outputRst(retFilesInfo);
			} else { // 异常返回
				System.out
						.println("[error] GridElemPointSearchAPI_CLIB_callAPI_to_saveAsFile_JSONP.");
				System.out.printf("\treturn code: %d. \n", rst);
				System.out.printf("\terror message: %s.\n",
						retFilesInfo.request.errorMessage);
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
