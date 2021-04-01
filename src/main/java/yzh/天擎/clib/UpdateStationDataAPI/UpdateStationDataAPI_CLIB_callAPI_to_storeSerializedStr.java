package yzh.天擎.clib.UpdateStationDataAPI;

import cma.music.client.DataStoreClient;

public class UpdateStationDataAPI_CLIB_callAPI_to_storeSerializedStr {

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
		String params = "";
		params = "DataCode=SEVP_WEFC_ACPP_STORE";//  资料代码
		params = params + "&"
				+ "valueEles=TEM_Min_2m";// 更新要素名称：最低气温	
		params = params + "&" + "keyEles=Datetime,Station_Id_C,Validtime";// 更新条件要素名称

		/* 2.4 输入更新站点数据要素值 */
		// 序列化数据，字段之间用逗号（,）分隔，行之间用分号(;)分开,前面的为key要素的值，后面为value要素的值
		String inArrayString = "20150114060000,54324,24,-30.02";
		inArrayString = inArrayString + ";"
				+ "20150114060000,54324,72,-30.02";

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
			// 3.3释放接口服务连接资源
			client.destroyResources();
		}
	}

}
