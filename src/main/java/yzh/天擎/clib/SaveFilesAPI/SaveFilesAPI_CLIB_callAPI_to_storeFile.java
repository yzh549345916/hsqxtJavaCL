package yzh.天擎.clib.SaveFilesAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class SaveFilesAPI_CLIB_callAPI_to_storeFile {

	/**
	 * 利用文件存储方法，存储用户提交的文件以及属性信息：callAPI_to_storeFile
	 * 输入要素采用二维数组形式
	 * @param args
	 */
	public static void main(String[] args) {
		
		/* 1.接口调用客户端类 */
		DataStoreClient client = new DataStoreClient();
		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名、密码 */
		String userId = "USR_YZHWQ";
		String pwd = "YZHHGDjm3";
		/* 2.2 接口id */
		String interfaceId = "";
		// 接口ID赋值
		interfaceId = "saveFiles";
		/* 2.3 接口参数 */
		HashMap<String, String> params = new HashMap<String, String>();
		// 写入资料代码
		params.put("DataCode", "SEVP_CIPAS_TEM_ANOM");
		// 写入文件属性要素信息字段定义
		params.put("Elements",
				"Datetime,PUBLISH_TIME,FILE_NAME,DATA_CONTENT,DATA_ID,FORMAT,AREA,PRODUCER,DATA_SOURCE,FILE_SIZE");

		/* 2.4 写入文件属性要素数据值 */
		//文件属性信息
		String inArray2D[][] = new String[][] {
				{ "20151110000000", "20151114060000",
						"MOP_CHCC_NCEPRA_WMFS_TMP_NH_0_PM_20151110_0000.png",
						"-", "TEM", "PNG", "NHE", "NCC", "CIPAS", "1222" },
				{ "20151111000000", "20151114060000",
						"MOP_CHCC_NCEPRA_WMFS_TMP_NH_0_PM_20151111_0000.png",
						"-", "TEM", "PNG", "NHE", "NCC", "CIPAS", "1222" } };

		int num = inArray2D.length;
		// 写入文件对应路径（本地）
		String[] write_filenames = new String[num];
		String path = "F:/temp";
		for (int i = 0; i < num; i++) {
			write_filenames[i] = path + "/" + inArray2D[i][2];
		}

		/* 3. 调用接口 */
		try {
			// 3.1 初始化接口服务连接资源
			client.initResources();
			// 3.2调用写入资料方法
			RequestInfo r = client.callAPI_to_storeFile(userId, pwd,
					interfaceId, params, inArray2D, write_filenames);
			
			if (r != null) {
				System.out.println("return code=" + r.errorCode);
				System.out.println("return message=" + r.errorMessage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} finally {
			// 3.3 释放服务连接以及资源回收
			client.destroyResources();
		}
	}

}
