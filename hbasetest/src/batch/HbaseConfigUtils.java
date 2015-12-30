package batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import util.HbaseConfUtil;
import util.HbaseConstants;

public class HbaseConfigUtils {
	
	private static final String path = "/config/hbase.properties";	
	private static final String path_1 = "/config/hbase.properties";
	
	private static Properties prop;
	private static InputStream inputStream;
	
	// 返回配置对象
	public static Properties getConfigs() {
		if(prop==null){
			init();
		}
		return prop;
	}
	public static void init(){
		prop = new Properties();
		try {
//			inputStream = HbaseConfUtil.class.getClassLoader().getResource(path).openStream();
			inputStream = HbaseConfUtil.class.getResourceAsStream(HbaseConstants.HBASE_CONF_FILE);
			if(inputStream==null){
				inputStream =  HbaseConfUtil.class.getResourceAsStream(HbaseConstants.HBASE_CONF_FILE);
			}
			prop.load(inputStream);
		} catch (Exception e) {
			System.out.println("init properties error: " + path);
			e.printStackTrace();
		}finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("can't close the inputstream!");
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		Enumeration e = getConfigs().keys();
		while(e.hasMoreElements()){
			String k = e.nextElement().toString();
			System.out.println(k+"----"+prop.get(k));
		}
	}
}
