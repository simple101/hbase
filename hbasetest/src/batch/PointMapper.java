package batch;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.*;


import com.mosol.util.Skeleton.Metadata;
import com.ultrapower.ultranms.fault.ec.jep.function.Random;

public class PointMapper extends
		Mapper<LongWritable, Text, NullWritable, NullWritable> {

	static final Log log = LogFactory.getLog(PointMapper.class);
	private static final byte spbyte = '|'; // value分隔符
	private Random random = new Random(); // 随机数

	private byte[] family;
	private byte[] column;
	private HTable table = null; // 基础汇总表
	private int errorcnt = 0; // 错误条数
	private int totalcnt = 0; // 成功入库记录条数
	private int keylength = 0;
	private static Pattern pattern2 = Pattern.compile("\\|");
	static Pattern pattern1 = Pattern.compile("[0-9]*");

	Put put;

	public PointMapper() {
	}

	/**
	 * 初始化参数
	 */
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		super.setup(context);
//		Metadata rc = new Metadata("/config/hbase.properties");
		// recom_item_num = Integer.parseInt(rc.getValue("recom_item_num"));

		// 获取参数
		Configuration conf = context.getConfiguration();
		String tableName = conf.get("table.userpoint.name");
		family = conf.get("table.userpoint.family").getBytes();
		column = conf.get("table.userpoint.column").getBytes();
//		keylength = Integer.parseInt(rc.getValue("table.userpoint.key.length"));
		table = new HTable(conf, tableName);
		System.out.println("table is " + table + " keylength is" + keylength);
		// 设置自动刷出为false
		table.setAutoFlush(false);
		// 设置写入缓存
		table.setWriteBufferSize(12 * 1024 * 1024);
	}

	public void map(LongWritable offset, Text value, Context context)
			throws InterruptedException {
		// 定义测试变量，用于测试map执行时间
		long l1 = System.currentTimeMillis();
		String value_str = value.toString();
		String[] gndata = pattern2.split(value_str);

		if (gndata.length >= 2) {
			String user_id = gndata[0];
			String dealids = value_str;

			if (user_id.length() < keylength
					&& pattern1.matcher(user_id).matches()) {
				user_id = reverse3(user_id);

			} else {
				user_id = Math.abs(user_id.hashCode()) + "";
				user_id = reverse3(user_id);
				user_id = user_id + "de";
			}

			user_id = fzero(user_id, keylength);
			byte[] key = user_id.getBytes();
			byte[] val = dealids.getBytes();
			try {
				long l2 = System.currentTimeMillis();
				// 向表中插入数据
				put = new Put(key);
				put.add(family, column, val);
				put.setWriteToWAL(false);
				table.put(put);
				totalcnt++;
				context.getCounter("WordMapper", "put table ").increment(
						System.currentTimeMillis() - l2);
				context.getCounter("WordMapper", "all operation ").increment(
						System.currentTimeMillis() - l1);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 重写cleanup方法
	 */
	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		try {
			table.flushCommits();
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 context.getCounter(HbaseCounter.ERROR_LINE).increment(errorcnt);
		 context.getCounter(HbaseCounter.SUCCESS_LINE).increment(totalcnt);
	};

	public static String reverse3(String s) {
		char[] array = s.toCharArray();
		String reverse = "";
		for (int i = array.length - 1; i >= 0; i--)
			reverse += array[i];
		return reverse;

	}

	static String fzero(String zz, int length) {
		if (zz.length() < length) {
			return fzero(zz + "0", length);
		} else {
			return zz;
		}
	}

}
