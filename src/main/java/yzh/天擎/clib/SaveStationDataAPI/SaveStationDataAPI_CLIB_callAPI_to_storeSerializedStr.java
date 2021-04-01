package yzh.天擎.clib.SaveStationDataAPI;

import cma.music.client.DataStoreClient;

public class SaveStationDataAPI_CLIB_callAPI_to_storeSerializedStr {
	/**
	 * 利用站点、指数资料存储方法：callAPI_to_storeSerializedStr，保存用户提交的信息
	 * 输入要素采用字符串形式
	 * @param args
	 */
	public static void main(String[] args) {
		/* 1. 调用类初始化 */
		DataStoreClient client = new DataStoreClient();

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "saveStationData";
		/* 2.3 接口参数定义 */
		String params = "";
		// 资料代码
		params = "DataCode=SEVP_WEFC_ACPP_STORE";
		// 写入要素名称：时间，站号、年、月、日、时、预报时效、风向......
		params =params+"&"
				+ "Elements=Datetime,Station_Id_C,Year,Mon,Day,Hour,Validtime,WIN_D,WIN_Turn,WINF,WINF_Turn,TEM_Max_2m,TEM_Min_2m";

		/* 2.4 写入记录要素值*/
		// 写入要素序列化数据值，字段之间用逗号（,）分隔，行之间用分号(;)分开,前面的为key要素的值，后面为value要素的值
		String inArrayString = "20150114060000,54326,2015,1,14,6,72,5,5,0,0,2.0000,-50.0000;"
				+ "20150114060000,54327,2015,1,14,6,96,4,4,1,1,2.0000,-50.0000";

		/* 3. 调用接口 */
		try {
			// 3.1 初始化接口服务连接资源
			client.initResources();
			// 3.2 调用接口
			int rst = client.callAPI_to_storeSerializedStr(userId, pwd,
					interfaceId, params, inArrayString);
			// 输出结果
			System.out.println("return code=" + rst);
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
		} finally {
			// 3.3 释放接口服务连接资源
			client.destroyResources();
		}
	}
	
}
