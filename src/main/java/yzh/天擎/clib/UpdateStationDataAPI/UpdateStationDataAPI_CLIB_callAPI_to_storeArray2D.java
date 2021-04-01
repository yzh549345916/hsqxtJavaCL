package yzh.天擎.clib.UpdateStationDataAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class UpdateStationDataAPI_CLIB_callAPI_to_storeArray2D {

	public static void main(String[] args) {
		/* 1. 调用类初始化 */
		DataStoreClient client = new DataStoreClient();

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口ID */
		String interfaceId = "updateStationData";
		/* 2.3 接口参数定义 */
		HashMap<String, String> params = new HashMap<String, String>();
		
		//  写入资料代码(城市天气预报)
		params.put("DataCode", "SEVP_WEFC_ACPP_STORE");
		//  更新要素：最低气温
		params.put("valueEles","TEM_Min_2m");
		// 更新要素条件要素
		params.put("KeyEles", "Datetime,Station_Id_C,Validtime");

		/* 2.4 输入更新站点数据要素值 */
		// 更新数据要素值，先key要素值，后更新值，更新20150114060000,54324站，预报时效为24小时的最低气温值为-30.02
		String inArray2D[][] = new String[][] {
				{ "20150114060000","54324","24","-30.02" },
				{ "20150114060000","54324","72","-30.02" }
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
