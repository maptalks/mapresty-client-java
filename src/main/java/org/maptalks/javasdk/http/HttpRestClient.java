package org.maptalks.javasdk.http;

import com.alibaba.fastjson.JSON;
import org.maptalks.javasdk.exceptions.RestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class HttpRestClient {

	public static String doGet(final String url,
                               final Map<String, String> data, final boolean useGzip)
			throws IOException, RestException {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		final StringBuffer sb = new StringBuffer();
		if (data != null) {
			sb.append("?");
			for (final Entry<String, String> e : data.entrySet()) {
				if (e.getValue() != null) {
					sb.append(e.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
					sb.append("&");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		// 尝试发送请求
		final String params = sb.toString();
		u = new URL(url + params);
		con = (HttpURLConnection) u.openConnection();
		if (useGzip)
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);

		// 读取返回内容
		// 读取返回内容
		final BufferedReader br = getConReader(con);
		int temp;
		StringBuilder resultBuilder = new StringBuilder();
		boolean nextIsData = false;
		while ((temp = br.read()) != -1) {
			resultBuilder.append((char)temp);
		}
		con.disconnect();
		return resultBuilder.toString();
	}

	/**
	 * 根据ContentEncoding返回不同的Reader
	 * 
	 * @param con
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static BufferedReader getConReader(final HttpURLConnection con)
			throws IOException {
		BufferedReader br = null;
		final String contentEncoding = con.getContentEncoding();
		if ("gzip".equals(contentEncoding)) {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					con.getInputStream()), "UTF-8"));
		} else {
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
		}
		return br;
	}

	public static List doGetList(final String url,
                                 final Map<String, String> data, final Class clazz,
                                 final boolean useGzip) throws IOException, RestException {
		final String resultData = doGet(url, data, useGzip);
		RestResult result = JSON.parseObject(resultData, RestResult.class);
		if (!result.isSuccess()) {
			throw new RestException(result.getErrCode(), result.getError());
		}
		final List ret = JSON.parseArray(result.getData(), clazz);
		return ret;
	}

	public static String doPost(final String url,
			final Map<String, String> data, final boolean useGzip)
			throws IOException, RestException {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		final StringBuilder sb = new StringBuilder();
		if (data != null) {
			Set<Entry<String, String>> entrySet = data.entrySet();
			for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator
					.hasNext();) {
				Entry<String, String> entry = iterator.next();
				if (entry.getValue() != null) {
					sb.append(entry.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					sb.append("&");
				}
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		// 尝试发送请求
		u = new URL(url);
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		if (useGzip) {
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Content-Encoding", "gzip");
        }
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
        OutputStreamWriter osw;
        if (useGzip) {
            //gzip压缩上传
            osw = new OutputStreamWriter(new GZIPOutputStream(con.getOutputStream()),"UTF-8");
        } else {
            osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        }
        osw.write(sb.toString());
		osw.flush();
		osw.close();

		// 读取返回内容
		final BufferedReader br = getConReader(con);

		StringBuilder resultBuilder = new StringBuilder();
		int temp;
		while ((temp = br.read()) != -1) {
			resultBuilder.append((char)temp);
		}
		con.disconnect();
		RestResult result = JSON.parseObject(resultBuilder.toString(), RestResult.class);

		if (!result.isSuccess()) {
			throw new RestException(result.getErrCode(), result.getError());
		}
		return result.getData();

	}
}
