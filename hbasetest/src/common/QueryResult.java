package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResult {

	public List<CarRecod> list = new ArrayList<CarRecod>();
	
	public  Map<String, String> rowMap=new HashMap<String, String>();//存开始和结束rowkey

	public long time = 0l;

	public long count = 0l;

	public double total;
	
	public long pageCount;
	
	public Integer page;
	
	public Integer rows;
	
	public String lastRowKey;
	
	public String oneRow;
}
