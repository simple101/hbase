package batch;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;


public class batchImpo {
	static final Log logger = LogFactory.getLog(batchImpo.class);
	//获取配置文件参数
//	private static  Properties pro = HbaseConfigUtils.getConfigs();
	//获取zookeeper的主机
//	static String quorum = pro.getProperty("hbase.zookeeper.quorum");
	static String quorum = "slave1";
	//获取zookeeper的主机端口
//	static String clientPort = pro.getProperty("hbase.zookeeper.property.clientPort");
	static String clientPort = "2222";
	//获取列族
//	static String famliy = pro.getProperty("table.exposure.family");
	static String famliy = "data";
	
	//分发处理器类
//	 static String[] coprocessor_class = pro.getProperty("table.userpoint.coprocessor.class").split(",");
//	 //分发处理器地址
//	 static String[] coprocessor_path = pro.getProperty("table.userpoint.coprocessor.path").split(",");
	 //压缩方式
//	 static String compression = pro.getProperty("table.exposure.compression");
	 static String compression = "SNAPPY";
	//表描述方式
//	 static String tableDescriptor = pro.getProperty("table.userpoint.descriptor");
	 
	 //Region数量
//	 static int sprangsize = Integer.parseInt(pro.getProperty("table.exposure.region.size"));
	 static int sprangsize = 10;
	 
	 //key的长度
	 // ExceptionInInitializerError
//	 static int keylength = Integer.parseInt(pro.getProperty("table.exposure.key.length"));
	 static int keylength = 32;
	
	public static void spliter(String table,Configuration conf,String[] dataArr) throws IOException, ParseException{
//		//配置HBase参数
//		Configuration conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.quorum", "slave1");
//		conf.set("hbase.zookeeper.property.clientPort", "2181");
		
		try {
			//按","切分
			String[] fs = famliy.split(",");
			logger.info("..."+table+"  "+fs+" "+conf);
			//拆分表
			createPresplitTable(table,fs,conf,dataArr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	static void createPresplitTable(String tableName, String[] columnFamilies,
			Configuration conf,String[] dataArr) throws IOException, InterruptedException , ParseException {
		
		//预分Region数量
		final int splitCount = sprangsize;
		//输出建表日志
		logger.debug("Creating table" + tableName + " with " + columnFamilies.length + " column families.  Presplitting to " +splitCount + "regions" );
		
		//定义HTableDescriptor对象，用来分发 coprocessor
		HTableDescriptor desc = new HTableDescriptor(tableName);
       
		
		//遍历列族的列  实现压缩
		for(String cf:columnFamilies){
			HColumnDescriptor hcd = new HColumnDescriptor(Bytes.toBytes(cf));
			if(compression != null){
				if(compression.toUpperCase().equals("SNAPPY")){
					hcd.setCompressionType(Algorithm.SNAPPY);
				}
				if(compression.toUpperCase().equals("LZO")){
					hcd.setCompressionType(Algorithm.LZO);
				}
				if(compression.toUpperCase().equals("GZ")){
					hcd.setCompressionType(Algorithm.GZ);
				}
			}
			
			//添加压缩后的列
			desc.addFamily(hcd);
		}
		
		HBaseAdmin admin = new HBaseAdmin(conf);
		
//		int offset = 16777215 / splitCount;
//		int base = offset;
		String split[] = new String[splitCount - 1];//分为10个
		
		for (int i = 0; i < split.length; i++) {
//			System.out.println("base:" + base);
			
//			split[i] = formatKey2(base);
//			base = base + offset;
		}
		
		byte[][] splitKeys = new byte[split.length][];
		for(int i = 0;i < split.length; i++){
			splitKeys[i] = Bytes.toBytes(split[i]);
		}
		
		
		//创建表
		admin.createTable(desc, splitKeys);
		//验证已创建的region总数，如果小于规定的region数量，继续等待region创建，直到创建完成为止
		if(!conf.getBoolean("split.verify", true)){
			HTable table = new HTable(conf, tableName);
			int onlineRegions = 0;
			while(onlineRegions <splitCount){
				//获取已创建的Region数量
				onlineRegions = table.getRegionLocations().size();
				logger.debug(onlineRegions + " of " + splitCount + " regions online... ");
				if(onlineRegions < splitCount){
					Thread.sleep(10 * 1000);  //等待10秒
				}
			}
		}
			logger.debug("Finished creating table with " + splitCount + "regions");
	}
	
	// 格式化RowKey
	static String formatKey2(long base) {
		
		String v = Long.toHexString(base);
		if(v.length() < 6){
			for(int i = 0; i < 6-v.length(); i++){
				v = "0" + v;
			}
		}
		String all = v + fzero("", 32 - 6);
		System.out.println(all);
		return all;
	}
	//格式化RowKey
	static byte[] formatKey(long base) {                
		byte[] b = new byte[6];
		long d = Math.abs(base);
		for (int i = b.length - 1; i >= 0; i--) {
			b[i] = (byte) ((d % 10) + '0');
			d /= 10;
		}
		System.out.println("key length is "+keylength);
		String all = new String(b)+fzero("",keylength-6);
		System.out.println(all);
		return all.getBytes();
	}
	static String fzero(String zz,int length){
		if(zz.length()<length){
			return fzero(zz+"0",length);
		}else{
			return zz;
		}	
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		int offset = 999999/10;
		int base = offset;
		byte[][] splitrange = new byte[9][];
		for (int i = 0; i < splitrange.length; i++) {
			splitrange[i] = formatKey(base);
			base = base + offset;
		}
	}
}
