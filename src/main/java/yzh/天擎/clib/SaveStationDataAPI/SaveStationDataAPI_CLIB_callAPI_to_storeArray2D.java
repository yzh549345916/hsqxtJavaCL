package yzh.天擎.clib.SaveStationDataAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class SaveStationDataAPI_CLIB_callAPI_to_storeArray2D {
	/**
	 * 利用站点、指数资料存储方法：callAPI_to_storeArray2D，保存用户提交的信息
	 * 输入要素采用二维数组形式
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
		HashMap<String, String> params = new HashMap<String, String>();
		// 写入资料代码
		params.put("DataCode", "SEVP_WEFC_ACPP_STORE");
		// 写入要素名称：观测时间(yyyymmddhh24miss),年，月，日，时，预报时效，风向.....
		params.put("Elements",
				"Datetime,Station_Id_C,Year,Mon,Day,Hour,Validtime,WIN_D,WIN_Turn,WINF,WINF_Turn,TEM_Max_2m,TEM_Min_2m");

		/* 2.4 写入记录要素值*/
		String inArray2D[][] = new String[][] {
				{"20150114060000","54323","2015","1","14","6","24","1","1","1","1","-50.0000","-50.0000"},
				{"20150114060000","54324","2015","1","14","6","48","8","8","1","1","-50.0000","-50.0000"},
				{"20150114060000","54325","2015","1","14","6","144","7","7","0","0","5.0000","-50.0000"},
				{"20150114060000","54326","2015","1","14","6","168","7","7","1","1","1.0000","-50.0000"},
				{"20150114060000","54327","2015","1","14","6","24","1","1","1","1","-50.0000","-50.0000"},
				{"20150115060000","54323","2015","1","15","6","120","3","3","1","1","1.0000","-50.0000"}
				};

		/* 3. 调用接口 */
		try {
			// 3.1 初始化接口服务连接资源
			client.initResources();
			// 3.2 调用接口
			RequestInfo rst = client.callAPI_to_storeArray2D(userId, pwd,
					interfaceId, params, inArray2D);
			// 输出结果
			if (rst != null) { // 正常返回
				System.out.println("return code=" + rst.errorCode);
				System.out.println("return message=" + rst.errorMessage);
			}
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
		} finally {
			// 3.3 释放接口服务连接资源
			client.destroyResources();
		}
	}
	
}
