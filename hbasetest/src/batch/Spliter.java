package batch;

import impo.RegionUserDealSplitter;
import impo.UserPointSplitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;


/**
 * 建表入口
 * @author gaosong
 * @date 2013-7-1
 */
public class Spliter {
	//初始化日期
	static String day = "19700101";
	//获取配置文件
//	private static Properties pro = HbaseConfigUtils.getConfigs();
	
//	public static void main(String[] args) throws Exception {
//		if(args != null && args.length >0){
//			day = args[0];
//		}
//		createTable(day);
//	}
	
//	public static void createDealTable(String day) throws ParseException, IOException{
//		//获取配置文件中的表名并替换$DAY$
//		String table = pro.getProperty("table.wap.name").replace("$DAY$", day);
//		//预分Region
//		RegionUserDealSplitter.spliter(table);
//	}
	
	
	public static void createUserPointTable(String day,Configuration conf,String[] dataArr) throws ParseException, IOException{
		//获取配置文件中的表名并替换$DAY$
//		String table = pro.getProperty("table.userpoint.name").replace("$DAY$", day);
		String table = "test1";
		//预分Region
		batchImpo.spliter(table,conf,dataArr);
	}
	
//	public static void createExposureTable(String day) throws ParseException, IOException{
//		String table = "test2";
//		//预分Region
//		batchImpo.spliter(table);
//	}
}
