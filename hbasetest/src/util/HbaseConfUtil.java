package util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取hbase配置文件hbase-conf.properties文件配置信息工具类
 * 
 * @author wangxiaohui
 * @date 2015-3-11
 */
public class HbaseConfUtil {
	/**
	 * 日志器
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(HbaseConfUtil.class);
	/**
	 * 配置信息
	 */
	private final static Properties props = new Properties();

	private static HbaseConfUtil hbaseUtil = null;

	private HBaseConfiguration hbaseConfig = null;

	// 读取配置文件信息
	static {
		InputStream is = null;
		try {
			logger.info("hbase-conf.properties文件目录【"
					+ HbaseConstants.HBASE_CONF_FILE + "】");
			is = HbaseConfUtil.class.getClassLoader()
					.getResource(HbaseConstants.HBASE_CONF_FILE).openStream();
			props.load(is);
		} catch (Exception e) {
			logger.error(
					"HbaseConfUtil加载hbase配置文件【hbase-conf.properties】时发生异常", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private HbaseConfUtil() {
		hbaseConfig = new HBaseConfiguration();

		hbaseConfig.set("hbase.zookeeper.quorum",
				props.getProperty(HbaseConstants.HBASE_ZOOKEEPER_QUORUM));
		hbaseConfig
				.set("hbase.zookeeper.property.clientPort",
						props.getProperty(HbaseConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT));
		hbaseConfig.set("zookeeper.znode.parent",
				props.getProperty(HbaseConstants.ZOOKEEPER_ZNODE_PARENT));

	}

	/**
	 * 获取hbaseconfig实例
	 * 
	 * @return
	 */
	public static synchronized HbaseConfUtil getInstance() {
		if (hbaseUtil == null)
			hbaseUtil = new HbaseConfUtil();
		return hbaseUtil;
	}

	/**
	 * 根据提供的key获取配置文件中的相关配置数据
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public HBaseConfiguration getHbaseConfig() {
		return hbaseConfig;
	}
	
//	public static void ver() {
//		String date="2014-02-01";
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		long now=131003000000l;
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		 Calendar calendar = Calendar.getInstance();
//		 calendar.setTimeInMillis(now);
//
//		 System.out.println(now + " = " + formatter.format(calendar.getTime()));
//		long millionSeconds = 0;
//		try {
////			millionSeconds = sdf.parse(date).getTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}// 毫秒
//
//		System.out.println(millionSeconds);
//	}


}
