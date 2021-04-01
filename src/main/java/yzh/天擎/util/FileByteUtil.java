package yzh.天擎.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileByteUtil {

	/*
	 * http下载
	 * 
	 * @throws Exception
	 */
	public static byte[] httpDownloadByte(String httpUrl) throws Exception {
		int byteread = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = null;
		try {
			url = new URL(httpUrl);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			throw new Exception("Get file error", e1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw new Exception("Get file error", e);
		}
		return out.toByteArray();
	}

}
