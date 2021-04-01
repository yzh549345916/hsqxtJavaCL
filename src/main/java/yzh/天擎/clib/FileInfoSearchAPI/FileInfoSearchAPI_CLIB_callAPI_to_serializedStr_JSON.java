package yzh.天擎.clib.FileInfoSearchAPI;

import cma.music.client.DataQueryClient;
import yzh.天擎.util.FormatUtil;

import java.util.HashMap;

/*
 * 客户端调取，文件信息列表检索，返回json格式的文件列表信息
 */
public class FileInfoSearchAPI_CLIB_callAPI_to_serializedStr_JSON {
	/**
	 * main方法 
	 * 如：按时间段、站号检索雷达文件 getRadaFileByTimeRangeAndStaId
	 **/
	public static void main(String[] args) {

		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "getRadaFileByTimeRangeAndStaId";
		/* 2.3 接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		// 必选参数
	    params.put("dataCode", "RADA_L2_UFMT"); //资料：质控前原始格式多普勒雷达基数据
		// 时间段，前闭后开 
	    params.put("timeRange", "[20190601000000,20190601000600)"); //时间段，前闭后开
		params.put("staIds", "Z9859,Z9852,Z9856,Z9851,Z9855"); // 雷达站
		// 可选参数

		/* 2.4 返回文件的格式 */
		String dataFormat = "json";
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
				formatUtil.outputRstJson(retStr.toString());
			} else { // 异常返回
				System.out
						.println("[error] FileInfoSearchAPI_CLIB_callAPI_to_serializedStr_JSON.");
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
