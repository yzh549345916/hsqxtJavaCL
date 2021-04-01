package yzh.天擎.clib.UpdateFilesAPI;

import cma.music.RequestInfo;
import cma.music.client.DataStoreClient;

import java.util.HashMap;

public class UpdateFilesAPI_CLIB_callAPI_to_storeFile {

	/**
	 * 根据用户输入的条件更新存储在服务端的文件方法：callAPI_to_storeFile
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
		interfaceId = "updateFiles";// 接口ID赋值
		// 2.3 参数定义
		HashMap<String, String> params = new HashMap<String, String>();
		// 写入资料名称
		params.put("dataCode", "SEVP_CIPAS_TEM_ANOM");
		// 保存或更新文件属性要素信息字段定义，更新发布时间与区域
		params.put("valueEles",	"PUBLISH_TIME,AREA");
		// 更新文件属性条件关键要素
		params.put("KeyEles", "Datetime");

		/* 2.4 输入更新文件条件要素值 */
		String inArray2D[][] = new String[][] {
				{ "20151111000000", "20151114120000", "NHE" }
		};

		int num=inArray2D.length;
		// 保存或更新文件对应路径（本地）
		String[] write_filenames = new String[num];
		String path = "./CIPAS产品";
		write_filenames[0] = path + "/" + "MOP_CHCC_NCEPRA_WMFS_TMP_NH_0_PM_20151111_0000.png";	

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
			System.out.println(ex.getMessage());
		} finally {
			// 3.3 释放服务连接以及资源回收
			client.destroyResources();
		}
	}
}
