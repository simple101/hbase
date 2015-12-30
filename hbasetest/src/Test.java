import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


public class Test {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		HTablePool hTablePool = null;
		try {
			Configuration hbaseConfig = new Configuration();
			hbaseConfig.set("hbase.zookeeper.quorum", "slave1");
			hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
			hbaseConfig.set("zookeeper.znode.parent", "/hbase");
			
			byte[] rGcxh=Bytes.toBytes("gcxh");//列
		    hTablePool=new HTablePool(hbaseConfig, 500);
			HTable table = (HTable)hTablePool.getTable("kakou");
//			HTable table = new HTable(hbaseConfig, "car");
			Get get = new Get(rGcxh);
			get.setMaxVersions(); 
			Result r = table.get(get);
			
			List<KeyValue> kv = r.list();
			for(KeyValue k : kv){
//			System.out.println(new String(k.getKey())+"========"+new String(k.getValue()));
				System.out.println("row======"+Bytes.toString(k.getRow()));
				System.out.println("family======"+Bytes.toString(k.getFamily()));
				System.out.println("qualifier======"+Bytes.toString(k.getQualifier()));
				System.out.println("value======"+Bytes.toString(k.getValue()));
				System.out.println("timestamp======"+k.getTimestamp());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			hTablePool.closeTablePool("kakou");
		}
	}
}
