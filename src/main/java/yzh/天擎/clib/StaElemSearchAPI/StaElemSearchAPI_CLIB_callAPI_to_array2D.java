package yzh.天擎.clib.StaElemSearchAPI;

import cma.music.RetArray2D;
import cma.music.client.DataQueryClient;
import yzh.天擎.util.ClibUtil;

import java.util.HashMap;

/*
 * 客户端调取，站点资料检索，返回RetArray2D对象
 */
public class StaElemSearchAPI_CLIB_callAPI_to_array2D {

	/*
	 * main方法，程序入口
	 * 如：按时间检索地面数据要素 getSurfEleByTime
	 */
	public static void main(String[] args) {
		
		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient() ;
		
		/* 2. 调用方法的参数定义，并赋值 */
		/* 2.1 用户名&密码 */
		String userId = "USR_YZHWQ" ;
		String pwd = "YZHHGDjm3" ;
		/* 2.2  接口ID */
		String interfaceId = "getSurfEleByTimeRange" ;		
		/* 2.3  接口参数，多个参数间无顺序 */
		HashMap<String, String> params = new HashMap<String, String>();
		//必选参数
		params.put("dataCode", "SURF_CHN_MUL_HOR") ; //资料代码
		params.put("elements", "Station_ID_C,PRE_1h,PRS,RHU,VIS,WIN_S_Avg_2mi,WIN_D_Avg_2mi,Q_PRS") ;//检索要素：站号、站名、小时降水、气压、相对湿度、能见度、2分钟平均风速、2分钟风向
		params.put("timeRange", "(20190601000000,20190601010000]") ; //检索时间
		//可选参数
		params.put("orderby", "Station_ID_C:ASC") ; //排序：按照站号从小到大
		params.put("staLevels", "011,012") ; //排序：按照站号从小到大
		 params.put("limitCnt", "100") ; //返回最多记录数：100
		/* 2.4 返回对象 */
		RetArray2D retArray2D = new RetArray2D() ;
		
		/* 3. 调用接口 */
		try {
			//初始化接口服务连接资源
			client.initResources() ;
			//调用接口
			int rst = client.callAPI_to_array2D(userId, pwd, interfaceId, params, retArray2D) ;
			//输出结果
			if(rst == 0) { //正常返回
			  ClibUtil clibUtil = new ClibUtil() ;
			  clibUtil.outputRst( retArray2D ) ;
			} else { //异常返回
				System.out.println( "[error] StaElemSearchAPI_CLIB_callAPI_to_array2D." ) ;				
				System.out.printf( "\treturn code: %d. \n", rst ) ;
				System.out.printf( "\terror message: %s.\n", retArray2D.request.errorMessage ) ;
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
