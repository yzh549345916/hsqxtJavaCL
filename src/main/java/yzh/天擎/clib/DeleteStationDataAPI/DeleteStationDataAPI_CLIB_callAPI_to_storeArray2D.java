package yzh.天擎.clib.DeleteStationDataAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class DeleteStationDataAPI_CLIB_callAPI_to_storeArray2D {

	/**
	 * 利用客户端调用删除存储的站点资料方法：callAPI_to_storeArray2D
     * 输入记录采用二维数据
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
		/* 2.3 接口参数定义 */
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("DataCode", "SEVP_WEFC_ACPP_STORE");//  写入资料代码
		params.put("KeyEles", "Datetime,Station_Id_C");//  删除条件要素，时间、站号

		/* 2.4 输入删除站点资料记录的条件值*/
		String inArray2D[][] = new String[][] { 
				{ "20150114060000","54323"} ////要素值信息，删除20150114060000的,54323的记录
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
