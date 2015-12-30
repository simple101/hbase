package dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.FilterBase;

public interface IHbaseDao {

    /**
     * 获取指定表中的所有记录
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public ResultScanner getAllRecords(String tableName) throws Exception;

    /**
     * 获取指定表中的指定主键的一行数据
     * @param tableName 表名
     * @param rowKey 主键值
     * @return
     * @throws Exception
     */
    public Result getOneRecordByRowKey(String tableName,String rowKey)
            throws Exception;


    /**
     * 根据指定的主键值获取指定列簇中的所有值
     * @param tableName 表名
     * @param rowKey 主键值
     * @param family 列簇
     * @param qualifier 列名
     * @return
     * @throws Exception
     */
    public Result getOneRecordByRowKeyColumn(String tableName, String rowKey,
            String family,String qualifier) throws Exception ;

    /**
     * 根据过滤条件获取所有记录
     * @param tableName 表名
     * @param filters 过滤过滤器列表
     * @return
     * @throws Exception
     */
    public ResultScanner getRecordsByFilter(String tableName,
            List<FilterBase> filters)throws Exception;
}
