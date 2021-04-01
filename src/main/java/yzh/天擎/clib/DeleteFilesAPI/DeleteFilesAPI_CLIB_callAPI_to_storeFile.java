package yzh.天擎.clib.DeleteFilesAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class DeleteFilesAPI_CLIB_callAPI_to_storeFile {
	/**
	 * 根据用户输入的条件删除存储在服务端的文件方法：callAPI_to_storeFile
	 * 输入要素采用二维数组形式
	 * @param args
	 */
	public static void main(String[] args) {
		/* 1.接口调用客户端类 */
		DataStoreClient client = new DataStoreClient();
		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名、密码 */
		String userId = "USR_YZHWQ";//API账号名
		String pwd = "YZHHGDjm3";//密码
		/* 2.2 接口id */
		String interfaceId = "deleteFiles";
		// 2.3 参数定义
		HashMap<String, String> params = new HashMap<String, String>();	
		params.put("DataCode", "SEVP_CIPAS_TEM_ANOM");//资料名称
		params.put("KeyEles", "Datetime");//更新文件属性条件关键要素

		/* 2.4 输入删除文件条件要素值 */
		String inArray2D[][] = new String[][] {
				{ "20151110000000" },
				{ "20151111000000" }, 
				{ "20151124010000" } };
		/* 3. 调用接口 */
		try {
			// 3.1 初始化接口服务连接资源
			client.initResources();
			// 3.2调用写入资料方法
			RequestInfo r = client.callAPI_to_storeFile(userId, pwd,
					interfaceId, params, inArray2D, null);
			if (r != null) {
				System.out.println("return code=" + r.errorCode);
				System.out.println("return message=" + r.errorMessage);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			// 3.3 释放服务连接以及资源回收
			client.destroyResources();
		}
	}

}
