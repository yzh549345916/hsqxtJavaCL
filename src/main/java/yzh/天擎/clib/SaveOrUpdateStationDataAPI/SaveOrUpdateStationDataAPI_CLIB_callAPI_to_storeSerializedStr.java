package yzh.天擎.clib.SaveOrUpdateStationDataAPI;
import cma.music.client.DataStoreClient;

public class SaveOrUpdateStationDataAPI_CLIB_callAPI_to_storeSerializedStr {

	/**
	 * 利用站点、指数资料存储方法：callAPI_to_storeSerializedStr，保存或更新用户提交的信息
	 * 输入要素采用序列化字符串形式
	 * @param args
	 */
	public static void main(String[] args) {
		/* 1. 调用类初始化 */
		DataStoreClient client = new DataStoreClient();

		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2  接口ID */
		String interfaceId = "saveOrUpdateStationData";
		/* 2.3 接口参数定义，字符串格式，不同参数之间用&连接 */
		String params = "";
		//  资料代码
		params = "DataCode=SEVP_WEFC_ACPP_STORE";
		// 更新要素字段：年、月、日、时、预报时效，风向......
		params = params + "&"
				+ "valueEles=Year,Mon,Day,Hour,Validtime,WIN_D,WIN_Turn,WINF,WINF_Turn,TEM_Max_2m,TEM_Min_2m";
		// 更新条件要素字段
		params = params + "&" + "keyEles=Datetime,Station_Id_C";

		/* 2.4 保存或更新要素记录值 */
		// 记录值之间用逗号（,）分隔，记录之间用分号(;)分开,前面的为keyEles要素的值，后面为valueEles要素的值
		String inArrayString = "20150114060000,54323,2015,1,14,6,168,7,7,1,1,0.0000,-50.0000";
		inArrayString = inArrayString + ";"
				+ "20150114060000,54326,2015,1,14,6,24,1,1,1,1,-50.0000,-50.0000";
		inArrayString = inArrayString + ";"
				+ "20150114060000,54326,2015,1,14,6,48,8,8,1,1,-50.0000,-50.0000";

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
