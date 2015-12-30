import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseTest extends Thread {
	private static Configuration hbaseConfig = null;
	public static HTablePool pool = null;
	/**
	 * 初始化配置
	 */
	static {
//		hbaseConfig = new Configuration();
//		hbaseConfig.set("hbase.zookeeper.quorum", "master,slave1");
//		hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
//		hbaseConfig.set("zookeeper.znode.parent", "/hbase");
		 Configuration HBASE_CONFIG = new Configuration();
		 HBASE_CONFIG = new Configuration();
		 HBASE_CONFIG.set("hbase.zookeeper.quorum", "master,slave1");
		 HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
		 HBASE_CONFIG.set("zookeeper.znode.parent", "/hbase");
		 hbaseConfig = HBaseConfiguration.create(HBASE_CONFIG);
		 pool = new HTablePool(hbaseConfig, 90);
	}

	public static Configuration configuration() {
		hbaseConfig = new Configuration();
		hbaseConfig.set("hbase.zookeeper.quorum", "master,slave1");
		hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
		hbaseConfig.set("zookeeper.znode.parent", "/hbase");
		return hbaseConfig;
	}

	/**
	 * 创建一张表
	 */
	public static void creatTable(String tableName, String family)
			throws Exception {
		HBaseAdmin admin = new HBaseAdmin(hbaseConfig);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(family));
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
	}

	/**
	 * 删除表
	 */
	public static void deleteTable(String tableName) throws Exception {
		try {
			HBaseAdmin admin = new HBaseAdmin(hbaseConfig);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
			}
		
			System.out.println("delete table " + tableName + " ok.");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入一行记录
	 */
	public static void addRecord(String tableName, String rowKey,
			String family, String[] column, String[] value, HTableInterface table,int sum)
			throws Exception {
		try {
			// HTableInterface table = null;
			// HTable table = new HTable(hbaseConfig, tableName);
			// table = pool.getTable(tableName);
			table.setAutoFlush(false);
//			table.setWriteBufferSize(1024*1024*12);
//			 byte[] buffer = new byte[350];
//			List<Put> list = new ArrayList<Put>();
			Put put = new Put(Bytes.toBytes(rowKey.replace("\"", "")));
			for (int i = 0; i < column.length; i++) {//循环key  19
//					System.out.println("column:"+column[i].replace("\"", "")+" value: "+value[i].replace("\"", ""));
				
					//					System.out.println(" value: "+value[i].replace("\"", ""));
							put.add(Bytes.toBytes(family),
							Bytes.toBytes(column[i].replace("\"", "")),
							Bytes.toBytes(value[i+1].replace("\"", "")));//值 一共20
				
				put.setWriteToWAL(false);
//				list.add(put);

			}
//			if (sum%1000==0) {
				table.put(put);
//				list.clear();
//			}
			
//			 table.flushCommits();
			System.out.println("insert recored " + rowKey + " to table "+ tableName + " ok.");
			// table.;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除一行记录
	 */
	public static void delRecord(String tableName, String rowKey)
			throws IOException {
		HTable table = new HTable(hbaseConfig, tableName);
		List list = new ArrayList();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		System.out.println("del recored " + rowKey + " ok.");
	}

	/**
	 * 查找一行记录
	 */
	public static void getOneRecord(String tableName, String rowKey)
			throws IOException {
		HTable table = new HTable(hbaseConfig, tableName);
		Get get = new Get(rowKey.getBytes());
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			System.out.print(new String(kv.getRow()) + " ");
			System.out.print(new String(kv.getFamily()) + ":");
			System.out.print(new String(kv.getQualifier()) + " ");
			System.out.print(kv.getTimestamp() + " ");
			System.out.println(new String(kv.getValue()));
		}
	}

	/**
	 * 显示所有数据
	 */
	public static void getAllRecord(String tableName) {
		try {
			HTable table = new HTable(hbaseConfig, tableName);
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				for (KeyValue kv : r.raw()) {
					System.out.print(new String(kv.getRow()) + " ");
					System.out.print(new String(kv.getFamily()) + ":");
					System.out.print(new String(kv.getQualifier()) + " ");
					System.out.print(kv.getTimestamp() + " ");
					System.out.println(new String(kv.getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	


	public static void init(String tablename, String[] dataArr, String[] column) {
		try {
			// String tablename = "test2";
			// deleteTable(tablename);
			// String cols = loadFile("resourse/rowkey.txt");
			// String[] column = cols.split(",");
			// String datas = loadFile("resourse/car.txt");
			// String[] dataArr = datas.split("/n");
			// HBaseTest.creatTable(tablename, "data");
			// add record zkb
//		/	HTable table = new HTable(hbaseConfig, tablename);
			HTableInterface table=null;
			table=pool.getTable(tablename);
			int sum=0;
			for (int j = 0; j < dataArr.length; j++) {
				System.out.println("sum:"+sum++);
				String[] coldatas = dataArr[j].split(",");
				addRecord(tablename, coldatas[0], "data", column,
						coldatas, table,sum);

			}
			pool.putTable(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 多线程导入
	 * 
	 * @throws InterruptedException
	 */
	public static void MuitThreads() throws InterruptedException {
		long start = System.currentTimeMillis();
		int threadNumber =3;
		Thread[] threads = new Thread[threadNumber];
		try {
			String tablename = "test2";
			deleteTable(tablename);
			String cols = loadFile("resourse/rowkey.txt");
			String[] column = cols.split(",");
			String datas = loadFile("resourse/car.txt");
			String[] dataArr = datas.split("/n");
			HBaseTest.creatTable(tablename, "data");
			init(tablename, dataArr, column);
//			for (int i = 0; i < threads.length; i++) {
//				threads[i] = new importThread(tablename, dataArr, column);
//				threads[i].start();
//			}
//			for (int j = 0; j < threads.length; j++) {
//				System.out.println("threads2 : " + threads[j]);
//				(threads[j]).join();
//			}
			long endTime = System.currentTimeMillis();
			System.out.println("MultThreadInsert：" + threadNumber
					+ "共耗时：" + (endTime - start) * 1.0 / 1000 + "s");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
   
  
   
   private static String loadFile(String filepath) {
		StringBuffer sb = new StringBuffer("");
		try {
			FileReader reader = new FileReader(filepath);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			int i = 0;
			System.out.println(br.readLine());
			while ((str = br.readLine()) != null) {
				if (i<=2) {
					if (i > 0) {
						sb.append("/n");
					}
					sb.append(str);
				}
				
				i++;
//				System.out.println(i);
				continue;
				
			}
//			System.out.println(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	public static void main(String[] agrs) {
		try {
			loadFile("E:\\大数据平台\\car.txt");;
//			randomReadFile();
//			p();
//			MuitThreads();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
