package yzh.天擎.clib.StaElemStoreAPI;

import cma.music.client.DataStoreClient;

public class StaElemStoreAPI_CLIB_callAPI_to_storeSerializedStr {

	/* 1. 调用类初始化 */
	DataStoreClient client = new DataStoreClient();

	/* 2. 调用方法的参数定义，并赋值 */
	/* 2.1 用户名&密码 */
	String userId = "USR_YZHWQ";
	String pwd = "YZHHGDjm3";
	/* 2.2 . 接口ID */
	String interfaceId = "saveSurfEleData";
	/* 接口参数定义 */
	String params = "";

	public static void main(String[] args) {

	}

	private void saveEleSerializedStr() {
		//接口ID
		interfaceId = "saveSurfEleData";
		String params = "";
		// 2.3 资料代码
		params = "DataCode=TEST_SURF_WEA_CHN_HOR";
		// 2.4 写入要素：区站号、观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params = "&"
				+ "Elements=Station_Id_C,Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h,Datetime";
		
		//写入要素序列化数据值，字段之间用逗号（,）分隔，行之间用分号(;)分开,前面的为key要素的值，后面为value要素的值
		String inArrayString = "545111,2015,09,21,10,999.8,1056,56,20150921100000;"
				+ "545112,2015,09,21,10,999.8,1056,56,20150921100000";

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void updateEleSerializedStr() {
		//接口ID
		interfaceId = "updateSurfEleData";
		params = "";
		// 2.3 资料代码
		params = "DataCode=TEST_SURF_WEA_CHN_HOR";
		// 2.4 更新要素字段：观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params = params + "&"
				+ "valueEles=Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h";
		// 2.5 更新条件要素字段
		params = params + "&" + "keyEles=Station_Id_C,Datetime";

		// 序列化数据，字段之间用逗号（,）分隔，行之间用分号(;)分开,前面的为key要素的值，后面为value要素的值
		String inArrayString = "54511,20150926100000,2014,08,21,10,999.8,1056,56";
		inArrayString = inArrayString + ";"
				+ "54512,20150926100000,2014,08,21,10,995.8,1056,56";
		inArrayString = inArrayString + ";"
				+ "54513,20150926130000,2014,08,21,10,995.8,1056,46";

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void saveOrUpdateEleSerializedStr() {
		//接口ID
		interfaceId = "saveOrUpdateSurfEleData";
		params = "";
		// 2.3 资料代码
		params = "DataCode=TEST_SURF_WEA_CHN_HOR";
		// 2.4 更新要素字段：观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params = params + "&"
				+ "valueEles=Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h";
		// 2.5 更新条件要素字段
		params = params + "&" + "keyEles=Station_Id_C,Datetime";

		// 字段之间用逗号（,）分隔，行之间用分号(;)分开,前面的为key要素的值，后面为value要素的值
		String inArrayString = "54511,20150926100000,2014,08,21,10,999.8,1056,56";
		inArrayString = inArrayString + ";"
				+ "54512,20150926100000,2014,08,21,10,995.8,1056,56";
		inArrayString = inArrayString + ";"
				+ "54513,20150926130000,2014,08,21,10,995.8,1056,46";

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void deleteEleSerializedStr() {
		//接口ID
		interfaceId = "deleteSurfEleData";
		params = "";
		// 2.3 资料代码
		params = "DataCode=TEST_SURF_WEA_CHN_HOR";
		// 2.4 删除记录条件要素名称
		params = params + "&" + "KeyEles=Station_Id_C,Datetime";

		// 条件要素值
		String inArrayString = "54514,20150921100000";
		inArrayString = inArrayString + ";" + "54512,20150921100000";
		inArrayString = inArrayString + ";" + "54513,20150921100000";

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

}
