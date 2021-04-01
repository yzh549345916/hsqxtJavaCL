package yzh.天擎.clib.DeleteStationDataAPI;

import cma.music.client.DataStoreClient;

public class DeleteStationDataAPI_CLIB_callAPI_to_storeSerializedStr {

	/**
	 * 利用客户端调用删除存储的站点资料方法：callAPI_to_storeSerializedStr
	 * 输入记录采用序列化字符串
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
		String interfaceId = "deleteStationData";
		/* 2.3 接口参数定义，参数之间用&连接 */
		String params = "";
		// 资料代码
		params = "DataCode=SEVP_WEFC_ACPP_STORE";
		// 删除站点记录条件要素名称，时间、站号
		params = params + "&" + "keyEles=Datetime,Station_Id_C";

		/* 2.4 输入删除记录条件要素值 */
		//不同记录之间用分号（;）分割
		String inArrayString = "20150114060000,54323";
		inArrayString = inArrayString + ";" + "20150114060000,54326";

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
