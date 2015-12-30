package impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import util.HbaseConfUtil;
import dao.IHbaseDao;

public class HbaseDaoImpl implements IHbaseDao {

	/**
	 * 获取指定表中的所有记录
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public ResultScanner getAllRecords(String tableName) throws Exception {

		ResultScanner rs = null;

		// HTablePool pool = new
		// HTablePool(HbaseConfUtil.getInstance().getHbaseConfig(),Integer.MAX_VALUE);
		// HTable table = (HTable) pool.getTable(tableName);
		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
				tableName);
		Scan scan = new Scan();

		rs = table.getScanner(scan);

		return rs;
	}
	
	
	/**
	 * 获取指定表中的指定主键的一行数据
	 * 
	 * @param tableName
	 *            表名
	 * @param rowKey
	 *            主键值
	 * @return
	 * @throws Exception
	 */
	public ResultScanner queryByRowKey(String tableName, String rowKey,String endKey)throws Exception {
		ResultScanner r = null;
		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
				tableName);
		if (StringUtils.isNotEmpty(rowKey)) {
//			 Get get = new Get(Bytes.toBytes(s+rowKey));
//			 get.setMaxVersions();
//			 r = table.get(get);
			Scan scan=new Scan();
			scan.setCacheBlocks(true);
			scan.setBatch(300);
			scan.setStartRow(rowKey.getBytes());
			scan.setStopRow(endKey.getBytes());
			r=table.getScanner(scan);
//			 System.out.println("raw: "+r.next());
//			 for (KeyValue key : r.raw()) {
//				System.out.println("列："+new String(key.getFamily())+" 值："+ new String(key.getValue()));
//			}
		}
		return r;
	}

	/**
	 * 获取指定表中的指定主键的一行数据
	 * 
	 * @param tableName
	 *            表名
	 * @param rowKey
	 *            主键值
	 * @return
	 * @throws Exception
	 */
	public Result getOneRecordByRowKey(String tableName, String rowKey)
			throws Exception {
		Result r = null;
		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
				tableName);
		
		if (StringUtils.isNotEmpty(rowKey)) {
			 Get get = new Get(rowKey.getBytes());
			 get.setMaxVersions();
			 r = table.get(get);
		}
		
		
		return r;
	}

	/**
	 * 根据指定的主键值获取指定列簇中的所有值
	 * 
	 * @param tableName
	 *            表名
	 * @param rowKey
	 *            主键值
	 * @param family
	 *            列簇
	 * @param qualifier
	 *            列
	 * @return
	 * @throws Exception
	 */
	public Result getOneRecordByRowKeyColumn(String tableName, String rowKey,
			String family, String qualifier) throws Exception {
		Result r = null;
		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
				tableName);
		Get get = new Get(rowKey.getBytes());
		get.addColumn(family.getBytes(), qualifier.getBytes());

		r = table.get(get);
		return r;
	}
	
	public ResultScanner queryByRowTest(String tableName, String rowKey,String endKey,List<FilterBase> filters
			)throws Exception {
		ResultScanner r = null;
		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
				tableName);
		Scan scan = new Scan();
		if (StringUtils.isNotEmpty(rowKey)&&StringUtils.isNotEmpty(endKey)) {
//			StringBuilder sRow;
//			StringBuilder eRow;
//			sRow=new StringBuilder(rowKey);
//			eRow=new StringBuilder(endKey);
//			String sKey=sRow.reverse().toString();
//			String eKey=eRow.reverse().toString();
			System.out.println("reverse: "+rowKey+"  "+endKey);
			scan.setStartRow(Bytes.toBytes(rowKey));
 		    scan.setStopRow(Bytes.toBytes(endKey));
		}
		FilterList filterList = new FilterList();
		Filter pageFilter=new PageFilter(20);//返回数量
		for (FilterBase filter : filters) {
			filterList.addFilter(filter);
		}
		filterList.addFilter(pageFilter);
		scan.setFilter(filterList);
		scan.addFamily("data".getBytes());
		// 缓存100条数据
		scan.setCaching(20);
		scan.setCacheBlocks(false);
		r = table.getScanner(scan);
		return r;
	}

	/**
	 * 根据过滤条件获取所有记录
	 * 
	 * @param tableName
	 *            表名
	 * @param filters
	 *            过滤过滤器列表
	 * @return
	 * @throws Exception
	 */
	public ResultScanner getRecordsByFilters(String tableName,
			List<FilterBase> filters,Integer pageSum,String mark,String RowKeys) throws Exception {
		ResultScanner rs = null;
		HTablePool htable=new HTablePool(HbaseConfUtil.getInstance().getHbaseConfig(), 50);
		HTableInterface table = htable.getTable(tableName);
//		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
//				tableName);
		Scan scan = new Scan();
		if("n".equals(mark)&&StringUtils.isNotEmpty(RowKeys)){//下一页
			StringBuffer sd=new StringBuffer();
			String rowK=sd.append(RowKeys).append(",").append(RowKeys).toString();
			scan.setStartRow(rowK.getBytes());
		}else if("p".equals(mark)&&StringUtils.isNotEmpty(RowKeys)){// 上一页
			scan.setStartRow(RowKeys.getBytes());
		}
		FilterList filterList = new FilterList();
		Filter pageFilter=new PageFilter(20);//返回数量
		for (FilterBase filter : filters) {
			filterList.addFilter(filter);
		}
		filterList.addFilter(pageFilter);
		scan.setFilter(filterList);
		scan.addFamily("data".getBytes());
		// 缓存100条数据
		scan.setCaching(20);
//		scan.setBatch(100);
		scan.setCacheBlocks(false);
		rs = table.getScanner(scan);
		return rs;
	}
	
	
	/**
	 * 根据过滤条件获取所有记录
	 * 
	 * @param tableName
	 *            表名
	 * @param filters
	 *            过滤过滤器列表
	 * @return
	 * @throws Exception
	 */
	public ResultScanner getRecordsByFilter(String tableName,
			List<FilterBase> filters) throws Exception {
		ResultScanner rs = null;
		HTablePool htable=new HTablePool(HbaseConfUtil.getInstance().getHbaseConfig(), 50);
		HTableInterface table = htable.getTable(tableName);
//		HTable table = new HTable(HbaseConfUtil.getInstance().getHbaseConfig(),
//				tableName);
		Scan scan = new Scan();
		FilterList filterList = new FilterList();
		//分页过滤器
		for (FilterBase filter : filters) {
			filterList.addFilter(filter);
		}
		scan.setFilter(filterList);
		
		// 缓存100条数据
		scan.setCaching(100);
		scan.setCacheBlocks(false);
		rs = table.getScanner(scan);
		return rs;
	}

	public static void main(String[] args) throws Exception {
		HbaseDaoImpl dao = new HbaseDaoImpl();

		// dao.PUt("pull", "15210269305",1298529542218L+200,
		// "fffffffffffffffff");
		// dao.PUt("pull", "15210269305",1298529542218L+300,
		// "gggggggggggggggggg");
		//
		//

		Result r = dao.getOneRecordByRowKey("kakou", "0000001");
		List<KeyValue> kv = r.list();
		for (KeyValue k : kv) {
			// System.out.println(new String(k.getKey())+"========"+new
			// String(k.getValue()));
			System.out.println("row======" + Bytes.toString(k.getRow()));
			System.out.println("family======" + Bytes.toString(k.getFamily()));
			System.out.println("qualifier======"
					+ Bytes.toString(k.getQualifier()));
			System.out.println("value======" + Bytes.toString(k.getValue()));
			System.out.println("timestamp======" + k.getTimestamp());
		}
	}

}
