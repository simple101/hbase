package impo;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import batch.HbaseConfigUtils;

/**
 * 该类主要用来初始化hbase的相关配置，用indicate标明所属的配置。例如table.userpoint.family=f的indicate是userpoint。
 * 将作为构造函数的参数插入。
 * Created with IntelliJ IDEA
 * User: YangJunLin
 * Date: 2015/1/27
 * Time: 13:34
 */
public class HbaseProperties {
    private String hbase_job_name;
    private String hbase_zookeeper_quorum;
    private String hbase_zookeeper_property_clientPort;
    private String hbase_master;
    private String server_monitor_inteval;
    private String server_listen_port;
    private String server_threadpool_size;
    private String table_name;
    private int table_key_length;
    private int table_region_size;
    private String table_descriptor;
    private String table_compression;
    private String table_family;
    private String table_column;
    private String table_coprocessor_class;
    private String table_coprocessor_path;
    private int table_lifecycle;
    private Configuration configuration;

    public HbaseProperties(String indicate) {
        Properties property = HbaseConfigUtils.getConfigs();
        this.hbase_zookeeper_quorum = property.getProperty("hbase." + indicate + ".zookeeper.quorum");
        this.hbase_zookeeper_property_clientPort = property
                .getProperty("hbase." + indicate + ".zookeeper.property.clientPort");
        this.table_name = property.getProperty("table." + indicate + ".name");
        this.table_family = property.getProperty("table." + indicate + ".family");
        this.table_column = property.getProperty("table." + indicate + ".column");
        this.table_lifecycle = Integer.parseInt(property
                .getProperty("table." + indicate + ".lifecycle"));
        this.hbase_job_name = indicate;
        this.table_coprocessor_class = property.getProperty("table." + indicate + ".coprocessor.class");
        this.table_coprocessor_path = property.getProperty("table." + indicate + ".coprocessor.path");
        this.table_compression = property.getProperty("table." + indicate + ".compression");
        this.table_descriptor = property.getProperty("table." + indicate + ".descriptor");
        this.table_region_size = Integer.parseInt(property.getProperty("table." + indicate + ".region.size"));
        this.table_key_length = Integer.parseInt(property.getProperty("table." + indicate + ".key.length"));
        this.hbase_master = property.getProperty("hbase." + indicate + ".master");
        this.server_monitor_inteval = property.getProperty("server." + indicate + ".monitor.inteval");
        this.server_listen_port = property.getProperty("server." + indicate + ".listen.port");
        this.server_threadpool_size = property.getProperty("server." + indicate + ".threadpool.size");
        this.setConfiguration();
    }

    public void setConfiguration() {
        Configuration conf = HBaseConfiguration.create();
//        conf.set(Constants.HBASE_ZOOKEEPER_QUORUM, this.getHbase_zookeeper_quorum());
//        conf.set(Constants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT, this.getHbase_zookeeper_property_clientPort());
//        conf.set(Constants.SERVER_MONITOR_INTEVAL, this.getServer_monitor_inteval());
//        conf.set(Constants.SERVER_LISTEN_PORT, this.getServer_listen_port());
//        conf.set(Constants.SERVER_THREADPOOL_SIZE, this.getServer_threadpool_size());
//        conf.set(Constants.HBASE_MASTER, this.getHbase_master());
//        conf.set(Constants.HBASE_INDICATE_NAME, this.getHbase_job_name());
        this.configuration = conf;
    }

    public String getHbase_job_name() {
        return hbase_job_name;
    }

    public String getHbase_zookeeper_quorum() {
        return hbase_zookeeper_quorum;
    }

    public String getHbase_zookeeper_property_clientPort() {
        return hbase_zookeeper_property_clientPort;
    }

    public String getHbase_master() {
        return hbase_master;
    }

    public String getServer_monitor_inteval() {
        return server_monitor_inteval;
    }

    public String getServer_listen_port() {
        return server_listen_port;
    }

    public String getServer_threadpool_size() {
        return server_threadpool_size;
    }

    public String getTable_name() {
        return table_name;
    }

    public int getTable_key_length() {
        return table_key_length;
    }

    public int getTable_region_size() {
        return table_region_size;
    }

    public String getTable_descriptor() {
        return table_descriptor;
    }

    public String getTable_compression() {
        return table_compression;
    }

    public String getTable_family() {
        return table_family;
    }

    public String getTable_column() {
        return table_column;
    }

    public String getTable_coprocessor_class() {
        return table_coprocessor_class;
    }

    public String getTable_coprocessor_path() {
        return table_coprocessor_path;
    }

    public int getTable_lifecycle() {
        return table_lifecycle;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
