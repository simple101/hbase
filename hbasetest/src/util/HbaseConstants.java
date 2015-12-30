package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * 常量工具类
 * 
 * @author wangxiaohui
 * @date 2015-3-11
 */
public class HbaseConstants {

	/**
	 * 配置文件hbase_conf.properties
	 */
	public static final String HBASE_CONF_FILE = "hbase-conf.properties";

	/**
	 * hbase.zookeeper.quorum，对应conf/hbase-conf.properties文件中的zookeeper_quorum
	 */
	public static final String HBASE_ZOOKEEPER_QUORUM = "zookeeper_quorum";
	/**
	 * hbase.zookeeper.property.clientPort，对应conf/hbase-conf.
	 * properties文件中的zookeeper_property_clientPort
	 */
	public static final String HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT = "zookeeper_property_clientPort";
	/**
	 * zookeeper.znode.parent，对应conf/hbase-conf.
	 * properties文件中的zookeeper_znode_parent
	 */
	public static final String ZOOKEEPER_ZNODE_PARENT = "zookeeper_znode_parent";

	/**
	 * 查询方式-过车序号
	 */
	public static final String QUERY_METHOD_GCXH = "GCXH";
	/**
	 * 查询方式-车辆类型
	 */
	public static final String QUERY_METHOD_CLLX = "CLLX";
	/**
	 * 查询方式-号码号牌
	 */
	public static final String QUERY_METHOD_HPHM = "HPHM";
	/**
	 * 查询方式-号牌种类
	 */
	public static final String QUERY_METHOD_HPZL = "HPZL";
	/**
	 * 查询方式-卡口编号
	 */
	public static final String QUERY_METHOD_KKBH = "KKBH";
	/**
	 * 查询方式-卡口编号列表
	 */
	public static final String QUERY_METHOD_KKBHIN = "KKBHIN";
	/**
	 * 查询方式-模糊号码号牌
	 */
	public static final String QUERY_METHOD_HPHM_REG = "HPHM_REG";
	/**
	 * 查询方式-卡口编号列表
	 */
	public static final String QUERY_METHOD_RRBH_ARR = "KKBHARR";
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		Calendar calendar = Calendar.getInstance();// 把毫秒转化成时间
		calendar.setTimeInMillis(131003000000L);
		System.out.println(sdf.format(calendar.getTime()));
	}

}
