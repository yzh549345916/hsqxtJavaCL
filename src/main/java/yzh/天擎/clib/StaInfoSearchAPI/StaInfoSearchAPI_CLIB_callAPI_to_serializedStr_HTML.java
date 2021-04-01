package yzh.天擎.clib.StaInfoSearchAPI;

import cma.music.client.DataQueryClient;
import yzh.天擎.util.FormatUtil;

import java.util.HashMap;

/*
 * 客户端调取，台站信息检索，返回HTML格式字符串
 */
public class StaInfoSearchAPI_CLIB_callAPI_to_serializedStr_HTML {
	/*
	 * main方法，程序入口 
	 * 如：按照经纬度范围检索台站信息 getStaInfoinRect
	 */
	public static void main(String[] args) {

		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ"; // 测试使用，后续会关闭
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "getStaInfoInRect";
		/* 2.3 接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		// 必选参数
		params.put("dataCode", "STA_INFO_SURF_CHN"); // 资料代码：中国地名台站信息
		params.put("elements", "Station_ID_C,Station_Name,Lat,Lon,Alti"); // 检索要素：站号、站名、纬度、经度、高度
		params.put("minLat", "39"); // 经纬度范围：北京及周边（纬度39-42，经度115-117）
		params.put("maxLat", "42");
		params.put("minLon", "115");
		params.put("maxLon", "117");
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
						.println("[error] StaInfoSearchAPI_CLIB_callAPI_to_serializedStr_HTML.");
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
