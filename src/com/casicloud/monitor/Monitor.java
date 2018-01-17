package com.casicloud.monitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casicloud.mail.SendMail;
import com.google.gson.Gson;

import net.sf.json.JSONObject;


public class Monitor {
	
	private static Log log = LogFactory.getLog(Monitor.class);
	private static String url1;
	private static String url2;
	private static String url3;
	private static String url4;
	private static String url5;
	private static String filePath;
	static{
		//测试获取设备24小时状态的接口(2.7.4)
		url1 ="http://ds.casicloud.com/api/getEnterpriseRealTimeDataByOrgIdAndKey.ht";
		//测试根据设备id,key,时间范围获得采集数据(2.7.5)
		url2 ="http://ds.casicloud.com/api/getDatasByDevIdColIdTimesPage.ht";
		//测试根据设备id获得采集点列表(2.4.2)
		url3 ="http://ds.casicloud.com/api/getCollectionByDevId.ht";
		//测试根据设备id,key获得最新采集数据(2.7.2)
		url4 ="http://ds.casicloud.com/api/v1/getLastDatasByDevIdColId.ht";
		//测试根据设备id,key获得最新采集数据(2.7.2)
		url5 ="http://ds.casicloud.com/api/getDevDatasByDevIdPage.ht";
		//日志保存路径
		filePath ="D:/recordLog.txt";
	}
	public static void main(String[] args) throws Exception {
		while(true){
			//测试获取设备24小时状态的接口(2.7.4)
			getDeviceAllDayStatus();
			//测试根据设备id,key,时间范围获得采集数据(2.7.5)
			getDatasByDevIdColIdTimesPage();
			//测试根据设备id获得采集点列表(2.4.2)
			getCollectionByDevId();
			//测试根据设备id,key获得最新采集数据(3.8.1)
			getLastDatasByDevIdColId();
			//测试根据设备id,key获得最新采集数据(2.7.2)
			getDevDatasByDevIdPage();
			//线程休眠30分钟
			threadSleepFor30Min();
		}
		
	}
	
	//测试获取设备24小时状态的接口(2.7.4)
	public static void getDeviceAllDayStatus() throws Exception{
		log.info("测试获取设备24小时状态的接口");
		String result = "";
		Map params=new HashMap<>();
		params.put("orgId", "8421746");
		params.put("key", "yxs");
		params.put("pageSize", "1");
		params.put("sort", "1");
		try {
			result = HttpClientUtil.JsonPostInvoke(url1, params);
			Gson gson = new Gson();
			Map map = gson.fromJson(result, Map.class);
			List list = (List) map.get("data");
			if(list.isEmpty()){
				String message = "调用API---"+url1+"获取的数据为空,折线图没有数据展示!!!";
				recordLog(filePath,message);
				SendMail.deliverMail(message);
			}else{
				JSONObject jsob = JSONObject.fromObject(result);
				Object object = jsob.get("data");
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "调用API---"+url1+"抛异常。异常原因为"+e.getMessage();
			log.error(e.getMessage());
			recordLog(filePath,message);
			SendMail.deliverMail(message);
		}
	}
	
	//测试根据设备id,key,时间范围获得采集数据(2.7.5)
	public static void getDatasByDevIdColIdTimesPage() throws Exception{
		log.info("测试根据设备id,key,时间范围获得采集数据");
		String result = "";
		Map params=new HashMap<>();
		params.put("devId", "10000050640212");
		params.put("colId", "feed");
		params.put("currentPage", 1);
		params.put("pagesize", 10);
		try {
			result = HttpClientUtil.JsonPostInvoke(url2, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "调用API---"+url2+"抛异常。异常原因为"+e.getMessage();
			log.error(e.getMessage());
			recordLog(filePath,message);
			SendMail.deliverMail(message);
		}
	}
	
	//测试根据设备id获得采集点列表(2.4.2)
	public static void getCollectionByDevId() throws Exception{
		log.info("测试根据设备id获得采集点列表");
		String result = "";
		Map params=new HashMap<>();
		params.put("devId", "10000050640212");
		try {
			result = HttpClientUtil.JsonPostInvoke(url3, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "调用API---"+url3+"抛异常。异常原因为"+e.getMessage();
			log.error(e.getMessage());
			recordLog(filePath,message);
			SendMail.deliverMail(message);
		}
	}
	
	
	//测试根据设备id,key获得最新采集数据(3.8.1)
	public static void getLastDatasByDevIdColId() throws Exception{
		log.info("测试根据设备id,key获得最新采集数据");
		String result = "";
		Map params=new HashMap<>();
		params.put("devId", "10000050640212");
		params.put("colId", "feed");
		try {
			result = HttpClientUtil.JsonPostInvoke(url4, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "调用API---"+url4+"抛异常。异常原因为"+e.getMessage();
			log.error(e.getMessage());
			recordLog(filePath,message);
			SendMail.deliverMail(message);
		}
	}
	
	
	//根据设备id获得采集数据(分页)(2.7.2)
	public static void getDevDatasByDevIdPage() throws Exception{
		log.info("根据设备id获得采集数据(分页)");
		String result = "";
		String url = "http://ds.casicloud.com/api/getDevDatasByDevIdPage.ht";
		Map params=new HashMap<>();
		params.put("devId", "10000050640212");
		params.put("currentPage", 1);
		params.put("pagesize", 10);
		try {
			result = HttpClientUtil.JsonPostInvoke(url5, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "调用API---"+url5+"抛异常。异常原因为"+e.getMessage();
			log.error(e.getMessage());
			recordLog(filePath,message);
			SendMail.deliverMail(message);
		}
	}
	
	//线程休眠3分钟
	public static void threadSleepFor3Min(){
		Thread thread = Thread.currentThread();
		try {
			thread.sleep(180000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//线程休眠30分钟
	public static void threadSleepFor30Min(){
		Thread thread = Thread.currentThread();
		try {
			thread.sleep(1800000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//将日志写入本地文件
	public static void recordLog(String filePath,String code){
		FileWriter fw = null;
		try {
		//如果文件存在，则追加内容；如果文件不存在，则创建文件
		File f=new File(filePath);
		fw = new FileWriter(f, true);
		} catch (IOException e) {
		e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		Date date=new Date();//Java-取得服务器当前的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sdfdate = sdf.format(date);
		pw.println(sdfdate+":"+code);
		pw.flush();
		try {
		fw.flush();
		pw.close();
		fw.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}

}
