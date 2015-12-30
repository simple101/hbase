package services;

import impl.HbaseDaoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import util.ReadThread;

import common.CarRecod;
import common.QueryResult;

public class CarService {

	private HbaseDaoImpl hbaseDao;
    //all_test_hbase_05  3,all_test_201409_hbase 2
	private String tableName = "test_500";

	/**
	 * 根据过车序号查询过车记录
	 * 
	 * @param gcxh
	 *            过车需要
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByGcxh(String gcxh) throws Exception {
		QueryResult qr = new QueryResult();
		long st = System.currentTimeMillis();
		Result r = hbaseDao.getOneRecordByRowKey(tableName, gcxh);
		long et = System.currentTimeMillis();
		if (r != null) {
			qr.list.add(initCarRecode(r));
			qr.count++;

		}
		qr.time = et - st;

		return qr;
	}

	/**
	 * 时间查询
	 * 
	 * @param filters
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<FilterBase> searchDate(List<FilterBase> filters,
			String startTime, String endTime) throws Exception{
		// 时间格式为 yyyy-MM-dd HH:mm:ss
		if (startTime != null && startTime.length() == 13) {
			filters.add(new SingleColumnValueFilter("data".getBytes(), "gcsj"
					.getBytes(), CompareOp.GREATER_OR_EQUAL, startTime
					.getBytes()));
		}
		if (endTime != null && endTime.length() == 13) {
			filters.add(new SingleColumnValueFilter("data".getBytes(), "gcsj"
					.getBytes(), CompareOp.LESS_OR_EQUAL, endTime.getBytes()));
		}
		return filters;
	}
    /**
     * 添加rowkey
     * @param qr
     * @param list
     * @param rMap
     * @param page
     * @return
     * @throws Exception
     */
	public QueryResult addRowKey(QueryResult qr, List<String> list,
			Map<String, String> rMap, Integer page) throws Exception {
		if (list.size() > 0 && list != null) {
			String lastRows = list.get(list.size() - 1).toString();// 获取最后一个值
			String oneRow = list.get(0);// 获取第一个值
			rMap.put(String.valueOf(page), oneRow);// key 页数 value 第一个rowkey
			qr.lastRowKey = lastRows;
			qr.oneRow = oneRow;
		}
		return qr;

	}

	/**
	 * 根据卡口编号查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param kkbh
	 *            卡口编号
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByCllx(String startTime, String endTime,
			String clpp,Integer pageSum, String mark,
			String RowKeys, Map<String, String> rMap, Integer page)
			throws Exception {
		QueryResult qr = new QueryResult();

		List<FilterBase> filters = new ArrayList<FilterBase>();
		// 时间格式为 yyyy-MM-dd HH:mm:ss
		searchDate(filters, startTime, endTime);// 时间查询
		if (clpp != null && StringUtils.isNotEmpty(clpp)) {
			filters.add(new SingleColumnValueFilter("data".getBytes(), "clpp"
					.getBytes(), CompareOp.EQUAL, clpp.getBytes()));
		}
		long st = System.currentTimeMillis();
		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,pageSum, mark, RowKeys);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		
		for (Result r : rs) {
			String row = toStr(r.getRow());// 获取rowkey
			qr.list.add(initCarRecode(r));
			list.add(row);
			qr.count++;
		}
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		return qr;

	}
	
	
	/**
	 * 根据车牌号码查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param hmhp
	 *            车牌号码
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByHmhp(String startTime, String endTime,String hphm, 
			Integer pageSum, String mark, String RowKeys,Map<String, String> rMap, 
			Integer page) throws Exception {
	
		QueryResult qr = new QueryResult();
		List<FilterBase> filters = new ArrayList<FilterBase>();
//		searchDate(filters, startTime, endTime);// 时间查询
		// 时间格式为 yyyy-MM-dd HH:mm:ss
		if (hphm != null && StringUtils.isNotEmpty(hphm)) {
			RegexStringComparator reg = new RegexStringComparator("" + hphm
					+ "");
			filters.add(new SingleColumnValueFilter("data".getBytes(), "hphm"
					.getBytes(), CompareOp.EQUAL, reg));
		}

		long st = System.currentTimeMillis();
		
//		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,pageSum, mark, RowKeys);
		ResultScanner rs = hbaseDao.queryByRowTest(tableName, startTime, endTime,filters);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		
//		Result[] rarray = rs.next(20);
//		for (Result r : rarray) {
//			String row = toStr(r.getRow());// 获取rowkey
//			qr.list.add(initCarRecode(r));// 初始化数据
//			list.add(row);
//			qr.count++;
//		}
//		
		int threadNumber =9;
		Thread[] threads = new Thread[threadNumber];
		for (int i = 0; i < threads.length; i++) {
		threads[i] = new ReadThread(rs, list, qr);
		threads[i].start();
	    }
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		return qr;

	}

	/**
	 * 根据车牌号码模糊查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param hmhp
	 *            车牌号码
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByHmhpReg(String startTime, String endTime,
			String hmhp, Integer pageSum, String mark,
			String RowKeys, Map<String, String> rMap, Integer page)
			throws Exception {
		QueryResult qr = new QueryResult();

		List<FilterBase> filters = new ArrayList<FilterBase>();
		searchDate(filters, startTime, endTime);// 时间查询
		if (hmhp != null && hmhp.trim() != "") {
			RegexStringComparator reg = new RegexStringComparator(".*" + hmhp
					+ ".*");
			filters.add(new SingleColumnValueFilter("data".getBytes(), "hphm"
					.getBytes(), CompareOp.EQUAL, reg));
		}

		long st = System.currentTimeMillis();
//		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,
//				pageSum, mark, RowKeys);
		ResultScanner rs = hbaseDao.queryByRowTest(tableName, startTime, endTime,filters);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		Result[] rarray = rs.next(20);
		for (Result r : rarray) {
			String row = toStr(r.getRow());// 获取rowkey
			qr.list.add(initCarRecode(r));// 初始化数据
			list.add(row);
			qr.count++;
		}
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		rs.close();
		return qr;

	}
	
	/**
	 * 根据号牌种类查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param hpzl
	 *            号牌种类
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByHpzl(String startTime, String endTime,
			String hpzl,  Integer pageSum, String mark,
			String RowKeys, Map<String, String> rMap, Integer page)
			throws Exception {
		QueryResult qr = new QueryResult();

		List<FilterBase> filters = new ArrayList<FilterBase>();
		searchDate(filters, startTime, endTime);// 时间查询
		if (hpzl != null && StringUtils.isNotEmpty(hpzl)) {
			filters.add(new SingleColumnValueFilter("data".getBytes(), "hpzl"
					.getBytes(), CompareOp.EQUAL, hpzl.getBytes()));
		}

		long st = System.currentTimeMillis();
		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,
				pageSum, mark, RowKeys);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		for (Result r : rs) {
			String row = toStr(r.getRow());// 获取rowkey
			qr.list.add(initCarRecode(r));
			list.add(row);
			qr.count++;
		}
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		return qr;

	}

	/**
	 * 根据卡口编号列表查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param hmhp
	 *            车牌号码
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByKkbhArr(String startTime, String endTime, Integer pageSum, String mark,
			String RowKeys,Map<String, String> rMap, Integer page,String kkbhArr) 
					throws Exception {
		QueryResult qr = new QueryResult();

		List<FilterBase> filters = new ArrayList<FilterBase>();
		searchDate(filters, startTime, endTime);// 时间查询
		if (kkbhArr != null && StringUtils.isNotEmpty(kkbhArr)) {
			String[] kkA = kkbhArr.split(",");
			String regStr = "";
			for (String kk : kkA) {
				regStr += "|" + kk;
			}
			regStr = regStr.substring(1);
			RegexStringComparator reg = new RegexStringComparator("" + regStr+ "");
			filters.add(new SingleColumnValueFilter("data".getBytes(), "kkbh".getBytes(), CompareOp.EQUAL, reg));
		}

		long st = System.currentTimeMillis();
		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,pageSum, mark, RowKeys);//过滤数据
//		ResultScanner rs = hbaseDao.queryByRowTest(tableName, startTime, endTime,filters);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
//		for (Result r : rs) {
//			String row = toStr(r.getRow());// 获取rowkey
//			qr.list.add(initCarRecode(r));
//			list.add(row);
//			qr.count++;
//		}
		int threadNumber =7;
		Thread[] threads = new Thread[threadNumber];
		for (int i = 0; i < threads.length; i++) {
		threads[i] = new ReadThread(rs, list, qr);
		threads[i].start();
	    }
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		return qr;

	}

	

	/**
	 * 根据卡口编号查询指定时间段的过车记录
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param kkbh
	 *            卡口编号
	 * @return
	 * @throws Exception
	 */
	public QueryResult searchCarByKkbh(String startTime, String endTime,
			String kkbh, Integer pageSum, String mark,
			String RowKeys, Map<String, String> rMap, Integer page)
			throws Exception {
		QueryResult qr = new QueryResult();
		List<FilterBase> filters = new ArrayList<FilterBase>();
		searchDate(filters, startTime, endTime);// 时间查询

		if (kkbh != null && StringUtils.isNotEmpty(kkbh)) {
			filters.add(new SingleColumnValueFilter("data".getBytes(), "kkbh"
					.getBytes(), CompareOp.EQUAL, kkbh.getBytes()));
		}
		long st = System.currentTimeMillis();
		ResultScanner rs = hbaseDao.getRecordsByFilters(tableName, filters,
				pageSum, mark, RowKeys);
//		ResultScanner rs = (ResultScanner) hbaseDao.queryByRowKey(tableName,  kkbh);
//		Result rs =  hbaseDao.queryByRowKey(tableName,  hphm);
		long et = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		int threadNumber =3;//三个线程
		Thread[] threads = new Thread[threadNumber];
		for (int i = 0; i < threads.length; i++) {
		threads[i] = new ReadThread(rs, list, qr);
		threads[i].start();
	    }
		rs.close();
		addRowKey(qr,list,rMap,page);//添加rowkey
		qr.time = et - st;
		return qr;

	}

	public static CarRecod initCarRecode(Result r) {
		CarRecod cr = null;
		if (r == null || r.list() == null || r.list().size() == 0)
			return cr;
		cr = new CarRecod();
		cr.gcxh = Bytes.toString(r.getRow());
		List<KeyValue> kv = r.list();
		for (KeyValue k : kv) {
			if ("kkbh".equals(Bytes.toString(k.getQualifier()))) {
				cr.kkbh = Bytes.toString(k.getValue());
			} else if ("fxlx".equals(Bytes.toString(k.getQualifier()))) {
				cr.fxlx = Bytes.toString(k.getValue());
			} else if ("cdh".equals(Bytes.toString(k.getQualifier()))) {
				cr.cdh = Bytes.toString(k.getValue());
			} else if ("gcsj".equals(Bytes.toString(k.getQualifier()))) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Calendar calendar = Calendar.getInstance();// 把毫秒转化成时间
				String carDate = Bytes.toString(k.getValue());
				calendar.setTimeInMillis(Long.valueOf(carDate));
				cr.gcsj = sdf.format(calendar.getTime());
				// cr.gcsj = Bytes.toString(k.getValue());
			} else if ("hpzl".equals(Bytes.toString(k.getQualifier()))) {
				cr.hpzl = Bytes.toString(k.getValue());
			} else if ("hphm".equals(Bytes.toString(k.getQualifier()))) {
				cr.hphm = Bytes.toString(k.getValue());
			} else if ("hpys".equals(Bytes.toString(k.getQualifier()))) {
				cr.hpys = Bytes.toString(k.getValue());
			} else if ("cwkc".equals(Bytes.toString(k.getQualifier()))) {
				cr.cwkc = Bytes.toString(k.getValue());
			} else if ("clsd".equals(Bytes.toString(k.getQualifier()))) {
				cr.clsd = Bytes.toString(k.getValue());
			} else if ("csys".equals(Bytes.toString(k.getQualifier()))) {
				cr.csys = Bytes.toString(k.getValue());
			} else if ("clpp".equals(Bytes.toString(k.getQualifier()))) {
				cr.clpp = Bytes.toString(k.getValue());
			} else if ("cllx".equals(Bytes.toString(k.getQualifier()))) {
				cr.cllx = Bytes.toString(k.getValue());
			} else if ("fzhpzl".equals(Bytes.toString(k.getQualifier()))) {
				cr.fzhpzl = Bytes.toString(k.getValue());
			} else if ("fzhphm".equals(Bytes.toString(k.getQualifier()))) {
				cr.fzhphm = Bytes.toString(k.getValue());
			} else if ("fzhpys".equals(Bytes.toString(k.getQualifier()))) {
				cr.fzhpys = Bytes.toString(k.getValue());
			}
		}
		return cr;
	}

	/* 转换byte数组 */
	public static byte[] getBytes(String str) {
		if (str == null)
			str = "";

		return Bytes.toBytes(str);
	}

	private static Scan getScan() {
		Scan scan = new Scan();
		return scan;
	}

	/**
	 * 封装查询条件
	 */
	private static FilterList packageFilters(boolean isPage) {
		FilterList filterList = null;
		// MUST_PASS_ALL(条件 AND) MUST_PASS_ONE（条件OR）
		filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		if (isPage) {
			filterList.addFilter(new FirstKeyOnlyFilter());
		}
		return filterList;
	}

	public static String toStr(byte[] bt) {
		return Bytes.toString(bt);
	}

	public HbaseDaoImpl getHbaseDao() {
		return hbaseDao;
	}

	public void setHbaseDao(HbaseDaoImpl hbaseDao) {
		this.hbaseDao = hbaseDao;
	}
}
