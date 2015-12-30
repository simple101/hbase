package services;

import impl.HbaseDaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HDService {

	private HbaseDaoImpl hbaseDao;
	
	
	public List<String> searchByPhoneNum(String phoneNum) throws Exception{
		List<String> result = new ArrayList<String>();
		
		Result r = hbaseDao.getOneRecordByRowKey("pull", phoneNum);
		
		for(KeyValue k:r.raw()){
			result.add(new String(Bytes.toString(k.getValue())));
		}
		
		return result;
	}
	
	
	

	public HbaseDaoImpl getHbaseDao() {
		return hbaseDao;
	}

	public void setHbaseDao(HbaseDaoImpl hbaseDao) {
		this.hbaseDao = hbaseDao;
	}
}
