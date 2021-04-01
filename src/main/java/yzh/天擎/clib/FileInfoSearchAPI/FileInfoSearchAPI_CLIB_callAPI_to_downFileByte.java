package yzh.天擎.clib.FileInfoSearchAPI;

import cma.music.RetFilesInfo;
import cma.music.client.DataQueryClient;

import java.util.HashMap;

/*
 * 客户端调取，获取文件字节流
 */
public class FileInfoSearchAPI_CLIB_callAPI_to_downFileByte {

  /*
   * main方法
   * 如：按时间段、站号检索雷达文件 getRadaFileByTimeRangeAndStaId
   */
  public static void main(String[] args) {
    
    /* 1. 定义client对象 */
    DataQueryClient client = new DataQueryClient() ;
    
    /* 2. 调用方法的参数定义，并赋值 */
    /* 2.1 用户名&密码 */
    String userId = "USR_YZHWQ" ;
    String pwd = "YZHHGDjm3" ;
    /* 2.2  接口ID */
    String interfaceId = "getRadaFileByTimeRangeAndStaId" ;
    /* 2.3  接口参数，多个参数间无顺序 */
    HashMap<String, String> params = new HashMap<String, String>();
    //必选参数
    params.put("dataCode", "RADA_L2_UFMT"); //资料：质控前原始格式多普勒雷达基数据
    params.put("timeRange", "[20190601000000,20190601000600)"); //时间段，前闭后开
    params.put("staIds", "Z9859,Z9852,Z9856,Z9851,Z9855"); //雷达站
    //可选参数
    /* 2.4 文件的本地保持目录 */
    String saveDir = "e:/tmp/" ;
    /* 2.5 返回文件的描述对象 */
    RetFilesInfo retFilesInfo = new RetFilesInfo() ;
    
    //字节流
    byte[] bytes=null;
    String fileURL="";
    
    /* 3. 调用接口 */
    try {
      //初始化接口服务连接资源
      client.initResources() ;
      //调用接口获取文件url
		int rst = client.callAPI_to_fileList(userId, pwd, interfaceId, params, retFilesInfo) ;
		//输出结果
		if(rst == 0) {
			if(retFilesInfo.fileInfos.length>0) {
				fileURL=retFilesInfo.fileInfos[0].fileUrl;
				//获取服务器上文件字节流
				bytes=client.callAPI_to_downFileByte(fileURL);
			} else {
				 System.out.println( "[error] retFilesInfo is NULL." ) ; 
			}
		}
     else { //异常返回
        System.out.println( "[error] FileInfoSearchAPI_CLIB_callAPI_to_fileList." ) ;       
        System.out.printf( "\treturn code: %d. \n", rst ) ;
        System.out.printf( "\terror message: %s.\n", retFilesInfo.request.errorMessage ) ;
      }
    } catch (Exception e) {
      //异常输出
      e.printStackTrace() ;
    } finally {
      //释放接口服务连接资源
      client.destroyResources() ;
    }
  }
  
}
