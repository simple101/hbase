package batch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapred.Task;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

public class UserPointDriver {
    // 定义job名称
    static final String NAME = "MRimport";
    
	public static long input = 0;
	public static long output = 0;
	public static long errput = 0;
	
	private static Configuration hbaseConfig = null;
	public static HTablePool pool = null;
	/**
	 * 初始化配置
	 */
	static {
		 Configuration HBASE_CONFIG = new Configuration();
		 HBASE_CONFIG = new Configuration();
		 HBASE_CONFIG.set("hbase.zookeeper.quorum", "master,slave1");
		 HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
		 HBASE_CONFIG.set("zookeeper.znode.parent", "/hbase");
		 hbaseConfig = HBaseConfiguration.create(HBASE_CONFIG);
		 pool = new HTablePool(hbaseConfig, 90);
	}
	
	private static String loadFile(String filepath) {
		StringBuffer sb = new StringBuffer("");
		try {
			FileReader reader = new FileReader(filepath);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			int i = 0;
			while ((str = br.readLine()) != null) {
				if (i > 0) {
					sb.append("/n");
				}
				sb.append(str);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	

    // 日志
    static final Log log = LogFactory.getLog(UserPointDriver.class);
    // 配置文件
//    private static Properties pro = HbaseConfigUtils.getConfigs();

    public static void main(String[] args) throws IOException,
            InterruptedException, ClassNotFoundException, ParseException {
        long l1 = System.currentTimeMillis();
        // 输入参数必须大于等于2
//        if (args.length < 2) {
//            log.error("error args size,must 3....");
//            System.exit(1);
//        }
        // 截取时间长度
//        String day = checkDay(args[0]);
        SimpleDateFormat mahoutdate = new SimpleDateFormat("yyyyMMdd");
        String day=mahoutdate.format(new Date());

        // String day = args[0];
        // 获取参数
//        String quorum = pro.getProperty("zookeeper_quorum");
//        String clientPort = pro
//                .getProperty("zookeeper_property_clientPor");
//        String table = pro.getProperty("table.userpoint.name");
//        String family = pro.getProperty("table.userpoint.family");
//        String column = pro.getProperty("table.userpoint.column");
        
        String tablename = "test2";
		String cols = loadFile("resourse/rowkey.txt");
		String[] column = cols.split(",");
		String datas = loadFile("resourse/car.txt");
		String[] dataArr = datas.split("/n");

        String tableName="test1";
        // 设置HBase配置参数
//        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", quorum);
//        conf.set("hbase.zookeeper.property.clientPort", clientPort);
//        conf.set("table.userpoint.name", table);
//        conf.set("table.userpoint.family", family);
//        conf.set("table.userpoint.column", column);
        dropTable(hbaseConfig,tableName);//删除

        checkTable(hbaseConfig,tableName, day,dataArr);
        //args[1]路径
        Job job = CreateSubmitTableJob(hbaseConfig, day, args[1]);

        long starttime = System.currentTimeMillis();
        boolean succ = job.waitForCompletion(true);
        org.apache.hadoop.mapreduce.Counters counters = job.getCounters();
        long total = job.getCounters().findCounter(HbaseCounter.TOTAL_LINE).getValue();
		input = counters.findCounter(Task.Counter.MAP_INPUT_RECORDS).getValue();
        long error = job.getCounters().findCounter(HbaseCounter.ERROR_LINE).getValue();
        log.info("total:" + total + ",error:" + error + ",cost:"
                + (System.currentTimeMillis() - l1));
		
		long endtime = System.currentTimeMillis();
		String status = "fail";
		if (succ) {
			status = "success";
		}
		String datatag = day;
		System.out.println(UserPointDriver.class.getName() + "input:" + input
				+ ",output:" + output + ",errput:" + errput + ",starttime:"
				+ starttime + ",endtime" + endtime + ",datatag:" + datatag
				+ ",status:" + status);
//		DataCount.save("warehouse", "dm_a_user_point", datatag, input,
//				output, errput, status, starttime, endtime);
    }

    /**
     * 截取日期格式
     *
     * @param d
     * @return
     */
    public static String checkDay(String d) {
        if (null == d || "".equals(d)) {
            log.error("input time " + d + "error ,must:yyyymmddhh24mi");
            System.exit(1);
        }
        return d.substring(0, 8);
    }

    /**
     * 自定义job
     *
     * @param inputDir
     * @param day
     * @param conf
     * @return
     * @throws IOException
     */
    private static Job CreateSubmitTableJob(Configuration conf, String day,
                                            String inputDir) throws IOException {
        // 创建job实例
        Job job = new Job(conf, NAME + "_" + day);
        job.setJarByClass(PointMapper.class);

        Path path = new Path(inputDir);
        FileInputFormat.addInputPath(job, path);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(PointMapper.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(NullOutputFormat.class);
        TableMapReduceUtil.addDependencyJars(job);
        return job;
    }

    /**
     * 组织Path
     *
     * @param conf
     * @param filevals
     * @param fss
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static ArrayList<Path> buildFs(Configuration conf,
                                           ArrayList<Path> filevals, FileStatus[] fss) throws IOException {
        for (FileStatus status : fss) {
            Path p = status.getPath();
            String rawPath = p.toUri().getPath();
            System.out.println("FilePath:" + p.toUri().getPath());
            if (status.isDir()) {// 是目录
                // FileSystem fs = p.getFileSystem(conf);
                // filevals = buildFs(conf, filevals, fs.listStatus(p));
            } else {// 不是目录
                if (rawPath.indexOf("_") > 0) {

                } else {
                    filevals.add(p);
                    System.out.println(p.toString());
                }

            }
        }
        return filevals;
    }

    /**
     * 检查表是否存在，不在则创建
     *
     * @param conf
     * @param mainTable
     * @param day
     * @throws IOException
     */
    private static void checkTable(Configuration conf, String mainTable,
                                   String day,String[] dataArr) {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            if (!admin.tableExists(mainTable)) {
                log.info("table not create,now create table:" + mainTable);
                Spliter.createUserPointTable(day,conf,dataArr);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dropTable(Configuration conf, String mainTable) {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            if (admin.tableExists(mainTable)) {
                log.info("table disable:" + mainTable);
                admin.disableTable(mainTable);
                log.info("table drop:" + mainTable);
                admin.deleteTable(mainTable);
                System.out.println("table drop:" + mainTable);
                //Spliter.createDealTable(day);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
