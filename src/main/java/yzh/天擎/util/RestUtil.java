package yzh.天擎.util;

import cma.music.Apiinterface;
import cma.music.DataFormatChange;
import cma.music.RequestInfo;
import cma.music.RetGetway;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class RestUtil {
  
  /* 
   * TODO:REST服务地址，依具体环境设置
   */
	//private final String host = "10.40.17.54:80";
	private final String host = "10.28.104.47:8008";
	private final int timeoutInMilliSeconds =  1000 * 60 * 2 ; //2 MINUTE
	static String getwayFlag = "\"flag\":\"slb\""; //网关返回错误标识

  /*
   * REST请求服务，获取数据
   */
	public String getRestData(String params) {
		StringBuilder retStr = new StringBuilder();
		URI uri = null;
		URL url = null;
		BufferedReader reader = null;
		URLConnection con;
		try {
			uri = new URI("http", this.host, "/music-ws/api", params, "");
			url = uri.toURL();
			con = url.openConnection();
			con.setConnectTimeout(this.timeoutInMilliSeconds);
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				retStr.append(line).append("\r\n");
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retStr.toString();
	}

  /*
   * REST请求服务，回写数据
   */
	public String setRestData(String params, String inString) {
		byte[] value = null;
		URI uri = null;
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse response=null;
		String retStr = "";
		try {
			//写入字符串
			String inputData[][] = new String[1][1];
			inputData[0][0] = inString;
			byte[] storeData = DataFormatChange.getPbStoreArray2DBytes(inputData, 0, null, 0, "", "");
			
			// 创建默认的httpClient实例.
			uri = new URI("http", this.host, "/music-ws/write", params, "");
			httpClient = HttpClients.createDefault();  
			// 创建HttpPost    
			HttpPost httpPost = new HttpPost(uri);
			 
			ByteArrayEntity reqEntity =new ByteArrayEntity(storeData);// EntityBuilder.create().build().getContent().read();
			reqEntity.setContentEncoding("UTF-8");
			httpPost.setEntity(reqEntity);
	        //setConnectTimeout：设置连接超时时间，单位毫秒。
			RequestConfig requestConfig =RequestConfig.custom().setConnectTimeout(this.timeoutInMilliSeconds).build();
			httpPost.setConfig(requestConfig);
			//执行请求
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();// 获取响应实体
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != 200) {
				retStr = "-10001:executing request error, StatusCode:" + stateCode;
			} else {
				// 解析返回结果
				value = EntityUtils.toByteArray(entity);
				if ((value != null) && (value.length > 1)) {
		 			String retValStr = new String(value);
					if (retValStr.contains(getwayFlag)) { //负载均衡返回错误
						RetGetway retGetway = JSON.parseObject(retValStr, RetGetway.class);
						retStr = retGetway.getReturnCode() + ":" + retGetway.getReturnMessage();
					} else {
						// 反序列化为proto的结果
						Apiinterface.RequestInfo _pbResult = Apiinterface.RequestInfo.parseFrom(value);
						RequestInfo requestInfo = DataFormatChange.getRequestInfo(_pbResult);
						retStr = requestInfo.getErrorCode() + ":" + requestInfo.getErrorMessage();
					}
				} else {
					retStr = -10001 + ":获取写入数据返回结果失败";
				}
			}
		} catch (Exception ex1) {
			retStr = -10001 + ":" + ex1.getMessage();
			ex1.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if(response!=null) {
					response.close();
				}
				if(httpClient!=null) {
					httpClient.close();
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}
	
//	/*
//	 * REST请求服务，回写数据
//	 */
//	public String setRestData(String params, String inString) {
//		StringBuilder retStr = new StringBuilder();
//		URI uri = null;
//		URL url = null;
//		java.io.BufferedReader reader = null;
//		URLConnection con;
//		// params = params + "&instring=" + inString;
//		byte[] data = inString.getBytes();
//		try {
//			uri = new URI("http", this.host, "/music-ws/write", params, "");
//			url = uri.toURL();
//			con = url.openConnection();
//			con.setDoOutput(true);
//			OutputStream out = con.getOutputStream();
//			out.write(data, 0, data.length);
//			con.setConnectTimeout(this.timeoutInMilliSeconds);
//			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String line = reader.readLine();
//			while (line != null) {
//				retStr.append(line).append("\r\n");
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (Exception ex1) {
//			ex1.printStackTrace();
//		} finally {
//			if (null != reader) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return retStr.toString();
//	}
}
