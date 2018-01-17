package com.casicloud.monitor;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import net.sf.json.JSONObject;

public class HttpClientUtil {
	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 360000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 360000;

	public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=utf-8";
	
	
	public static final String CONTENT_TYPE_JSON_CHARSET_UTF8 = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET_UTF8 = "application/xml;charset=utf-8";


	/**
	 * httpclient读取内容时使用的字符集
	 */
	public static final String CHARSET_GBK = "GBK";

	public static final String CHARSET_UTF8 = "UTF-8";
	
	public static final Charset UTF_8 = Charset.forName(CHARSET_UTF8);

	public static final Charset GBK = Charset.forName(CHARSET_GBK);
	
	public static String JsonPostInvoke(String url, Map<String, Object> params, String charsets) 
			throws Exception{  
        System.out.println("post方式提交json数据开始......");
        // 接收参数json列表  
        JSONObject jsonParam = new JSONObject();  
        // http客户端
        HttpClient httpClient = buildHttpClient(false);
        // post请求
        HttpPost method = new HttpPost(url);  
        if(null != params){
        	for(String key : params.keySet()){
        		jsonParam.put(key, params.get(key));
        	}
        	// 参数实体
        	StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
        	entity.setContentEncoding(CHARSET_UTF8);    
            entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
            method.setEntity(entity);  
        }
        // 执行响应操作
        HttpResponse result = httpClient.execute(method);  
        // 请求结束，返回结果  
        String data = EntityUtils.toString(result.getEntity()); 
        System.out.println(data);
        System.out.println("post方式提交json数据结束......");
        method.releaseConnection();
        return data;
	} 
	public static String JsonGetInvoke(String url, String charsets) 
			throws ClientProtocolException, IOException{  
		System.out.println("get方式提交json数据开始......");
		// 接收参数json列表  
		JSONObject jsonParam = new JSONObject();  
		// http客户端
		HttpClient httpClient = buildHttpClient(false);
		// post请求
		HttpGet method = new HttpGet(url);  
		
		// 执行响应操作
		HttpResponse result = httpClient.execute(method);  
		// 请求结束，返回结果  
		String data = EntityUtils.toString(result.getEntity()); 
		System.out.println(data);
		System.out.println("get方式提交json数据结束......");
		return data;
	} 
	
	/***
	 * 只传入url和参数不需要编码
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String JsonPostInvoke(String url, Map<String, Object> params) 
			throws ClientProtocolException, IOException{  
        System.out.println("post方式提交json数据开始......");
        // 接收参数json列表  
        JSONObject jsonParam = new JSONObject();  
        // http客户端
        HttpClient httpClient = buildHttpClient(false);
        // post请求
        HttpPost method = new HttpPost(url);  
        if(null != params){
        	for(String key : params.keySet()){
        		jsonParam.put(key, params.get(key));
        	}
        	// 参数实体
        	StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
        	entity.setContentEncoding(CHARSET_UTF8);    
            entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
            method.setEntity(entity);  
        }
        // 执行响应操作
        HttpResponse result = null;
        String data="";
		try {
			result = httpClient.execute(method);
			data= EntityUtils.toString(result.getEntity()); 
		} catch (Exception e) {
			data="请求错误";
			e.printStackTrace();
		}  finally{
			method.releaseConnection();
		}
        // 请求结束，返回结果  
       
        System.out.println(data);
        System.out.println("post方式提交json数据结束......");
        return data;
	} 
	
	
	
	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		return simpleGetInvoke(url, params, CHARSET_UTF8);
	}

	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient client = buildHttpClient(false);
		HttpGet get = buildHttpGet(url, params);
		HttpResponse response = client.execute(get);
		assertStatus(response);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String returnStr = EntityUtils.toString(entity, charset);
			return returnStr;
		}
		return null;
	}

	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params)
			throws URISyntaxException, ClientProtocolException, IOException {
		return simplePostInvoke(url, params, CHARSET_UTF8);
	}

	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params, String charset)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = buildHttpClient(false);
		HttpPost postMethod = buildHttpPost(url, params, charset);
		HttpResponse response = client.execute(postMethod);
		assertStatus(response);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String returnStr = EntityUtils.toString(entity, charset);
			return returnStr;
		}
		return null;
	}

	/**
	 * 创建HttpClient
	 * 
	 * @param isMultiThread
	 * @return
	 */
	public static HttpClient buildHttpClient(boolean isMultiThread) {
		CloseableHttpClient client;
		if (isMultiThread)
			client = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
		else
			client = HttpClientBuilder.create().build();
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		return client;
	}

	/**
	 * 构建httpPost对象
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildHttpPost(String url, Map<String, String> params, String charset)
			throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		HttpEntity he = null;
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
			he = new UrlEncodedFormEntity(formparams, Charset.forName(charset));
			post.setEntity(he);
		}
		// 在RequestContent.process中会自动写入消息体的长度，自己不用写入，写入反而检测报错
		// setContentLength(post, he);
		return post;

	}

	/**
	 * 构建httpGet对象
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws URISyntaxException
	 */
	public static HttpGet buildHttpGet(String url, Map<String, String> params) throws URISyntaxException {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		HttpGet get = new HttpGet(buildGetUrl(url, params));
		return get;
	}

	/**
	 * build getUrl str
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static String buildGetUrl(String url, Map<String, String> params) {
		StringBuffer uriStr = new StringBuffer(url);
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				ps.add(new BasicNameValuePair(key, params.get(key)));
			}
			uriStr.append("?");
			uriStr.append(URLEncodedUtils.format(ps, UTF_8));
		}
		return uriStr.toString();
	}

	/**
	 * 设置HttpMethod通用配置
	 * 
	 * @param httpMethod
	 */
	public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, CHARSET_UTF8);// setting
		// contextCoding
		// httpMethod.setHeader(HTTP.CHARSET_PARAM, CONTENT_CHARSET);
		httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
		// httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_XML_CHARSET);
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 * 
	 * @param httpMethod
	 * @param he
	 */
	public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he) {
		if (he == null) {
			return;
		}
		httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
	}

	/**
	 * 构建公用RequestConfig
	 * 
	 * @return
	 */
	public static RequestConfig buildRequestConfig() {
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT_MS)
				.setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
		return requestConfig;
	}

	/**
	 * 强验证必须是200状态否则报异常
	 * 
	 * @param res
	 * @throws HttpException
	 */
	static void assertStatus(HttpResponse res) throws IOException {
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		switch (res.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:
			// case HttpStatus.SC_CREATED:
			// case HttpStatus.SC_ACCEPTED:
			// case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
			// case HttpStatus.SC_NO_CONTENT:
			// case HttpStatus.SC_RESET_CONTENT:
			// case HttpStatus.SC_PARTIAL_CONTENT:
			// case HttpStatus.SC_MULTI_STATUS:
			break;
		default:
			throw new IOException("服务器响应状态异常,失败.");
		}
	}

	private HttpClientUtil() {
	}
	public static String getContent(String url) throws Exception{  
        String backContent = null;  
        //先建立一个客户端实例，将模拟一个浏览器  
        HttpClient httpclient = null;  
        HttpGet httpget = null;
        HttpPost httpPost=null;
        try {  
            //************************************************************  
            // 设置超时时间  
            // 创建 HttpParams 以用来设置 HTTP 参数  
            HttpParams params = new BasicHttpParams();  
            // 设置连接超时和 Socket 超时，以及 Socket 缓存大小  
            HttpConnectionParams.setConnectionTimeout(params, 180 * 1000);  
            HttpConnectionParams.setSoTimeout(params, 180 * 1000);  
            HttpConnectionParams.setSocketBufferSize(params, 8192);  
            // 设置重定向，缺省为 true  
            HttpClientParams.setRedirecting(params, false);  
            //************************************************************     
            httpclient = new DefaultHttpClient(params);  
//          httpclient = new DefaultHttpClient();  
            // 建立一个get方法请求，提交刷新  
            httpget = new HttpGet(url);       
              
            HttpResponse response = httpclient.execute(httpget);   
            //HttpStatus.SC_OK(即:200)服务器收到并理解客户端的请求而且正常处理了  
//          if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {  
//              //对象呼叫中止  
//              httpget.abort();  
//              backContent = "获取不到";  
//          }  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {              
                //start 读取整个页面内容  
                InputStream is = entity.getContent();  
                BufferedReader in = new BufferedReader(new InputStreamReader(is));   
                StringBuffer buffer = new StringBuffer();   
                String line = "";  
                while ((line = in.readLine()) != null) {  
                    buffer.append(line);  
                }  
                //end 读取整个页面内容  
                backContent = buffer.toString();  
            }  
        } catch (Exception e) {  
            httpget.abort();  
            backContent = "有异常,获取不到";     
            System.out.println("-------------异常开始");  
            e.printStackTrace();  
            System.out.println("-------------异常结束");  
        }finally{  
            //HttpClient的实例不再需要时，降低连接，管理器关闭，以确保立即释放所有系统资源  
            if(httpclient != null)  
                httpclient.getConnectionManager().shutdown();  
        }          
        //返回结果  
        return backContent;  
    }  
	public static void main(String[] args) 
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", "我们测试");
		params.put("tsn", "6590300");
		params.put("tname", "千万千万有限公司");
		params.put("uopenId", "cc4b6be78725463ab6511ebbfb1510d2");
		params.put("uname", "uuuu");
		params.put("pwd", "jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=");
		params.put("mobile", "13552438933");
		params.put("email", null);
		params.put("state", 0);
		params.put("systemId", "10000048130074");
		params.put("fullName", "21321");
		
	Map<String, Object> map=new HashMap<String, Object>();
	map.put("systemId", "10000055300000");
	map.put("uri", "");
	Map<String, Object> data=new HashMap<String, Object>();
	data.put("account", "uctest");
	map.put("data", data);
	
		System.out.println(JsonPostInvoke("http://172.17.11.38:8080/api/getUserInfoByAcc.ht",map));
		//System.out.println(JsonPostInvoke("http://182.50.1.8:15380/platform/system/api/tenantAdd.ht",params));
		
		/*	Map<String, Object> params=new HashMap<String, Object>();
			params.put("data", "{pwd=htG1exdb5F1HnAAfZ9CvZ7JsoiEd8ClOGH7bZn48lPs=, email=null, topenId=0d78e1dff6af4b21a8b28c3d8f2f960b, uname=hbzxcl, state=0, tname=湖北正信齿轮制造有限公司, systemId=10000048280000, fullName=管理员, uopenId=9ac9ad6b9aa64298bb3cbe0bc26a567c, tsn=6690301, mobile=13907294647}");
			ThreadUtil threadUtil=new ThreadUtil( params, "http://kangju.casicloud.com:9090/platform/system/api/tenantAdd.ht");
			ThreadUtil threadUtil2=new ThreadUtil( params, "http://kangju.casicloud.com:9090/platform/system/api/tenantAdd.ht");
		    Sync sync=new Sync(threadUtil);
		    Sync sync2=new Sync(threadUtil2);
		    sync.start();
		    sync2.start();*/
		    

	}
	
	
	/**
	 * 用urlconnect  json post 传递参数
	 */
	public static String urlPost(String url,String jsonString){
		if (url==null || StringUtil.isEmpty(url)){
			return "";
		}
		if (jsonString==null || StringUtil.isEmpty(jsonString)){
			return "";
		}
		String returnLine = "";
		try {
			System.out.println("**************开始http通讯**************");
			System.out.println("**************调用的接口地址为**************");
			System.out.println(url);
			System.out.println("**************请求发送的数据为**************");
			System.out.println(jsonString);
			URL my_url = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			byte[] content = jsonString.getBytes("utf-8");
			out.write(content, 0, content.length);
			out.flush();
			out.close(); // flush and close
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			//StringBuilder builder = new StringBuilder();
			String line = "";
			System.out.println("Contents of post request start");
			while ((line = reader.readLine()) != null) {
				// line = new String(line.getBytes(), "utf-8");
				returnLine += line;
			}
			System.out.println("Contents of post request ends");
			reader.close();
			connection.disconnect();
			System.out.println("**************返回的结果为**************");
			System.out.println(returnLine);
			System.out.println("**************http通讯结束**************");

			return returnLine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String JsonPostInvoke(String url, String jsonParam) 
			throws ClientProtocolException, IOException{  
        System.out.println("post方式提交json数据开始......");
        // 接收参数json列表  
        // http客户端
        HttpClient httpClient = buildHttpClient(false);
        // post请求
        HttpPost method = new HttpPost(url);  
      
        	// 参数实体
        	StringEntity entity = new StringEntity(jsonParam, CHARSET_UTF8);//解决中文乱码问题    
        	entity.setContentEncoding(CHARSET_UTF8);    
            entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
            method.setEntity(entity);  
        // 执行响应操作
        HttpResponse result = null;
        String data="";
		try {
			result = httpClient.execute(method);
			data= EntityUtils.toString(result.getEntity()); 
		} catch (Exception e) {
			data="请求错误";
			e.printStackTrace();
		}  finally{
			method.releaseConnection();
		}
        // 请求结束，返回结果  
       
        System.out.println(data);
        System.out.println("post方式提交json数据结束......");
        return data;
	} 
}
