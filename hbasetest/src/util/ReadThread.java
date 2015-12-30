package util;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import services.CarService;

import common.QueryResult;

public class ReadThread extends Thread {
	private ResultScanner rs;
	private List<String> list;
	private QueryResult qr;

	public ReadThread(ResultScanner rs, List<String> list, QueryResult qr) {
		this.rs = rs;
		this.list = list;
		this.qr = qr;
		initData();
	}

	public void initData() {
		try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void run(){
		try {
			for (Result r : rs) {
				String row = CarService.toStr(r.getRow());// 获取rowkey
				qr.list.add(CarService.initCarRecode(r));
				list.add(row);
				qr.count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}