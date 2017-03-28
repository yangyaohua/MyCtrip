package com.example.utils;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.example.activity.WelcomeActivity;
import com.example.activity.WelcomeActivity.VISITOR_TYPE;
import com.example.bean.Ticket;
import com.example.data.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class HttpUtil {

	public static String URL = "http://172.23.176.52:8080/MyCtrip/";
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8";

	// 此方法用于公共连接服务器的接口方法。
	public static String testUrlConnection(Context context, String servletName,
			Map<String, String> map) {
		try {
			URL url = getUrl(servletName, map);
			;// new URL(URL + servletName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 取得sessionId;
			String sessionId = (String) SpUtil.get(context,
					Constants.PC_SESSIONID, "");
			if (WelcomeActivity.getType() == VISITOR_TYPE.ANDROID) {
				sessionId = (String) SpUtil.get(context,
						Constants.ANDROID_SESSIONID, "");
			}
			if (sessionId != null && !sessionId.equals("") && !sessionId.equals("JSESSIONID=null")) {
				// 已经存在session
				conn.addRequestProperty("Cookie", sessionId);
			} else {
				// 不存在session,第一次访问服务器，存储sessionId;
				String cookie = conn.getHeaderField("set-cookie");
				if (cookie != null && !conn.equals("")) {
					sessionId = cookie.substring(0, cookie.indexOf(";"));
					SpUtil.put(context, Constants.ANDROID_SESSIONID, sessionId);
				}
			}
			conn.setConnectTimeout(5000);
			// conn.setRequestMethod("POST");
			conn.connect();
			InputStream is = conn.getInputStream();
			String str = "";
			StringBuffer sb = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}
			}
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 此方法在于首先访问服务器，创造cookie
	public static String loginConnection(Context context, String servletName,
			Map<String, String> map) {
		try {
			URL url = getUrl(servletName, map);
			;// new URL(URL + servletName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 取得sessionId;
			String cookie = conn.getHeaderField("set-cookie");
			String sessionId = null;
			if (cookie != null) {
				sessionId = cookie.substring(0, cookie.indexOf(";"));
				SpUtil.put(context, Constants.PC_SESSIONID, sessionId);
			}
			conn.setConnectTimeout(5000);
			// conn.setRequestMethod("POST");
			conn.connect();
			InputStream is = conn.getInputStream();
			String str = "";
			StringBuffer sb = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}
			}
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 根据传入的map，创建url,
	private static URL getUrl(String servletName, Map<String, String> map) {
		StringBuffer sb = new StringBuffer(URL + servletName);
		if (map != null && !map.isEmpty()) {
			sb.append("?");
			for (String s : map.keySet()) {
				sb.append(s + "=" + map.get(s) + "&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		URL url = null;
		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String login(Context context, String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", username);
		map.put("password", password);
		map.put("type", "ANDROID");
		String result = testUrlConnection(context, "LoginServlet", map);
		return result;
	}

	/*
	 * public static Map<String, Hotel> queryHotels(Context context){ String
	 * result = testUrlConnection(context,"QueryHotelsServlet", null); Gson gson
	 * = new Gson(); Map<String, Hotel> hotels = gson.fromJson(result, new
	 * TypeToken<Map<String, Hotel>>(){}.getType()); //解析数组形式 List<Hotel>
	 * hotelList = new ArrayList<Hotel>(); Type type = new
	 * TypeToken<ArrayList<Hotel>>(){}.getType(); hotelList =
	 * gson.fromJson(result, type); return hotels; }
	 */

	// 查询车票
	public static List<Ticket> searchTicket(Context context,
			String originalAddress, String destinationAddress, Date startDate) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oaddress", originalAddress);
		map.put("daddress", destinationAddress);
		map.put("sday", String.valueOf(startDate));
		String result = testUrlConnection(context, "QueryTicketServlet", map);
		Gson gson = new GsonBuilder().setDateFormat(1).create();
		Map<String, TicketHolder> ticketholders = gson.fromJson(result,
				new TypeToken<Map<String, TicketHolder>>() {
				}.getType());
		List<Ticket> tickets = change2Tickets(ticketholders);
		return tickets;
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param oldP
	 * @param newP
	 * @return
	 */
	public static boolean changePassword(Context context, String oldP,
			String newP) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("old", oldP);
		map.put("new", newP);
		String result = testUrlConnection(context, "ChangePasswordServlet", map);
		return Boolean.valueOf(result);
	}

	public static boolean onTicketItemClicked(Context context, int tid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tid", String.valueOf(tid));
		String result = testUrlConnection(context, "BookStateServlet", map);
		return Boolean.valueOf(result);
	}

	/**
	 * 预订车票
	 * 
	 * @param context
	 * @param cnumber
	 * @param tid
	 */
	public static boolean reverseTicket(Context context) {
		String result = testUrlConnection(context, "BookTicketServlet", null);
		return Boolean.valueOf(result);
	}

	public static boolean payforTicket(Context context, String phoneNumber,
			String payPassword) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone_number", phoneNumber);
		map.put("pay_password", payPassword);
		String result = testUrlConnection(context, "PayForTicketServlet", map);
		return Boolean.valueOf(result);
	}

	public static String getSessionUsername(String sessionId) {
		try {
			URL url = getUrl("SessionIdServlet", null);
			;// new URL(URL + servletName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (sessionId != null) {
				conn.addRequestProperty("Cookie", sessionId);
			}
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.connect();
			InputStream is = conn.getInputStream();
			String str = "";
			StringBuffer sb = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}
			}
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String downloadPcSessionId(Context context) {
		String result = testUrlConnection(context, "SessionIdServlet", null);
		return result;
	}

	public static String downloadSessionPiece(Context context) {
		String result = testUrlConnection(context, "SessionPieceServlet", null);
		return result;
	}

	public static String getTicketPrice(Context context) {
		String result = testUrlConnection(context, "GetTicketPriceServlet",
				null);
		return result;
	}

	private static List<Ticket> change2Tickets(
			Map<String, TicketHolder> ticketholders) {
		List<Ticket> tickets = new ArrayList<Ticket>();
		if (ticketholders == null || ticketholders.isEmpty()) {
			return null;
		}
		for (String key : ticketholders.keySet()) {
			Ticket ticket = new Ticket();
			TicketHolder holder = ticketholders.get(key);
			ticket.setConsume(holder.consume);
			ticket.setDesAddress(holder.desAddress);
			ticket.setEndTime(new Timestamp(holder.endTime));
			ticket.setNumbers(holder.numbers);
			ticket.setOriginAddress(holder.originAddress);
			ticket.setPrice(holder.price);
			ticket.setSerialNumber(holder.serialNumber);
			ticket.setStartDay(new Timestamp(holder.startDay));
			ticket.setStartTime(new Timestamp(holder.startTime));
			ticket.setTid(holder.tid);
			tickets.add(ticket);
		}
		return tickets;
	}

	private class TicketHolder {
		public int tid;
		public String serialNumber;
		public String originAddress;
		public String desAddress;
		public long startDay;
		public long startTime;// 具体时间，几点几分
		public long endTime;
		public double price;
		public String consume;// 耗时
		public int numbers;
	}

	/*
	 * public static void saveData(Context context, String data) { Map<String,
	 * String> map = new HashMap<String, String>(); map.put("data", data);
	 * testUrlConnection(context, "UploadDataServlet", map); }
	 */

	public static void uploadData(Context context, String filePath) {
		File file = new File(FileHelper.DOWNLOAD_PATH + filePath);
		try {
			URL url = getUrl("UploadDataServlet", null);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", CHARSET);
			connection.setRequestProperty("connection", "keep-alive");
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			InputStream inputStream = new FileInputStream(file);
			
			int numberRead = 0;
			byte[] buffer = new byte[1024];
			while((numberRead = inputStream.read(buffer)) != -1){
				dos.write(buffer, 0, numberRead);
			}
			inputStream.close();
			dos.close();
			if (connection.getResponseCode() == 200) {
				Log.e("TAG","ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * String BOUNDARY = UUID.randomUUID().toString(); String PREFIX =
		 * "--",LINE_END = "\r\n"; String CONTENT_TYPE = "multipart/form-data";
		 * String result = null;
		 * 
		 * try { HttpURLConnection connection = (HttpURLConnection)
		 * url.openConnection(); connection.setReadTimeout(TIME_OUT);
		 * connection.setConnectTimeout(TIME_OUT); connection.setDoInput(true);
		 * connection.setDoOutput(true); connection.setUseCaches(false);
		 * connection.setRequestMethod("POST");
		 * connection.setRequestProperty("Charset", CHARSET);
		 * connection.setRequestProperty("connection", "keep-alive");
		 * connection.setRequestProperty("Content-Type", CONTENT_TYPE +
		 * ",boundary=" + BOUNDARY); if (file != null) { DataOutputStream dos =
		 * new DataOutputStream(connection.getOutputStream()); StringBuffer sb =
		 * new StringBuffer(); sb.append(PREFIX); sb.append(BOUNDARY);
		 * sb.append(LINE_END);
		 *//**
		 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
		 * filename是文件的名字，包含后缀名的 比如:abc.png
		 */
		/*
		 * sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+
		 * file.getName()+"\""+LINE_END); sb.append(LINE_END);
		 * dos.write(sb.toString().getBytes()); InputStream is = new
		 * FileInputStream(file); byte[] bytes = new byte[1024]; int len = 0;
		 * while((len = is.read(bytes)) != -1){ dos.write(bytes,0,len); }
		 * is.close(); dos.write(LINE_END.getBytes()); byte[] end_data = (PREFIX
		 * + BOUNDARY + PREFIX + LINE_END).getBytes(); dos.write(end_data);
		 * dos.flush();
		 * 
		 * int res = connection.getResponseCode(); if (res == 200) { InputStream
		 * input = connection.getInputStream(); StringBuffer sb1 = new
		 * StringBuffer(); int ss; while((ss = input.read()) != -1){
		 * sb1.append((char)ss); } result = sb1.toString(); } } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

}
