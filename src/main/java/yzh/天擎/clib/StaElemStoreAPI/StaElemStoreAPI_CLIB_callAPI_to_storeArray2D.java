package yzh.天擎.clib.StaElemStoreAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class StaElemStoreAPI_CLIB_callAPI_to_storeArray2D {

	/* 1. 调用类初始化 */
	DataStoreClient client = new DataStoreClient();

	/* 2. 调用方法的参数定义，并赋值 */
	/* 2.1 用户名&密码 */
	String userId = "USR_YZHWQ";
	String pwd = "YZHHGDjm3";
	/* 2.2 接口ID */
	String interfaceId = "saveSurfEleData";
	/* 接口参数定义 */
	HashMap<String, String> params = new HashMap<String, String>();

	public static void main(String[] args) {

	}

	private void saveEleData() {
		// 接口id
		interfaceId = "saveSurfEleData";
		params = new HashMap<String, String>();
		// 2.3 写入资料代码
		params.put("DataCode", "TEST_SURF_WEA_CHN_HOR");
		// 2.4 写入要素：区站号、测站名、观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params.put("Elements",
				"Station_Id_C,Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h,Datetime");

		// 写入数据要素值
		String inArray2D[][] = new String[][] {
				{ "54511", "2015", "09", "21", "10", "999.8", "1056", "56",
						"20150921100000" },
				{ "54512", "2015", "09", "21", "10", "999.9", "1056", "44",
						"20150921100000" },
				{ "54513", "2015", "09", "21", "10", "999.8", "1056", "77",
						"20150921100000" } };

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void updateEleData() {
		// 接口id
		interfaceId = "updateSurfEleData";
		params = new HashMap<String, String>();
		// 2.3 写入资料代码
		params.put("DataCode", "TEST_SURF_WEA_CHN_HOR");
		// 2.4 更新要素：观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params.put("Elements",
				"Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h,Datetime");
		// 2.5 更新要素条件要素
		params.put("KeyEles", "Station_Id_C,Datetime");

		// 写入数据要素值
		String inArray2D[][] = new String[][] {
				{ "54511", "20150921100000", "2014", "09", "21", "10", "999.8",
						"1056", "56", "20150921100000" },
				{ "54512", "20150921100000", "2013", "09", "21", "10", "999.9",
						"1056", "44", "20150921100000" },
				{ "54513", "20150921100000", "2012", "09", "21", "10", "999.8",
						"1056", "77", "20150921100000" } };

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void saveOrUpdateEleData() {
		// 接口id
		interfaceId = "saveOrUpdateSurfEleData";
		params = new HashMap<String, String>();
		// 2.3 写入资料代码
		params.put("DataCode", "TEST_SURF_WEA_CHN_HOR");
		// 2.4 更新或写入要素：观测年、观测月、观测日、观测时、气压、海平面气压、3小时变压、观测时间
		params.put("Elements",
				"Year,Mon,Day,Hour,PRS,PRS_Sea,PRS_Change_3h,Datetime");

		// 2.5 更新要素条件要素，
		params.put("KeyEles", "Station_Id_C,Datetime");

		String inArray2D[][] = new String[][] {
				{ "54511", "20150926100000", "2014", "08", "21", "10", "999.8",
						"1056", "56" },
				{ "54512", "20150927100000", "2013", "08", "21", "10", "999.9",
						"1056", "44" },
				{ "54513", "20150921100000", "2012", "08", "21", "10", "999.8",
						"1056", "77" } };

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

	private void deleteEleData() {
		// 接口id
		interfaceId = "deleteSurfEleData";
		params = new HashMap<String, String>();
		// 2.3 写入资料代码
		params.put("DataCode", "TEST_SURF_WEA_CHN_HOR");

		// 2.4 删除条件要素
		params.put("KeyEles", "Station_Id_C,Datetime");

		//要素值信息
		String inArray2D[][] = new String[][] { { "54514", "20150921100000" },
				{ "54512", "20150921100000" }, { "54513", "20150921100000" } };

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
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}

}
