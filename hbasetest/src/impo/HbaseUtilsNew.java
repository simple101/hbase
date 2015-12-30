package impo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;


/**
 * @author yangjunlin
 */
public class HbaseUtilsNew {
    static final Log log = LogFactory.getLog(HbaseUtilsNew.class);
    private HbaseProperties properties;
    private Configuration conf;

    public HbaseUtilsNew(HbaseProperties hbaseProperties) {
        this.properties = hbaseProperties;
        conf = hbaseProperties.getConfiguration();
    }


    /**
     * 检查表是否存在，不存在则创建。
     *
     * @param
     * @return
     */
    public boolean checkTable() {
        HBaseAdmin hBaseAdmin;
        try {
            log.info("################# begin to cheke tableexists###############");
            hBaseAdmin = new HBaseAdmin(properties.getConfiguration());
            log.info("###############hbaseadmin new success#############");
            if (!hBaseAdmin.tableExists(properties.getTable_name())) {
                log.info("###################### table isnot exist !##########################");
//                HbaseCRUD hbaseCRUD = new HbaseCRUD(properties);
//                hbaseCRUD.create();
                log.info("###################### create sucess !##########################");
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//         catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return true;
    }

    /**
     * 自定义job.
     *
     * @param
     * @param day
     * @param inputDir
     * @return
     * @throws java.io.IOException
     */
    public Job createSubmitTableJob(String day,
                                    String inputDir) throws IOException {
        Configuration conf = properties.getConfiguration();
        Job job = new Job(conf, properties.getHbase_job_name() + "_" + day);
        log.info("################job new success ###############");
//        job.setJarByClass(UserPointMapperNew.class);
        Path path = new Path(inputDir);
        FileInputFormat.addInputPath(job, path);
        job.setInputFormatClass(TextInputFormat.class);
//        job.setMapperClass(UserPointMapperNew.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(NullOutputFormat.class);
//        TableMapReduceUtil.addDependencyJars(job);
        return job;
    }

    /**
     * 截取日期格式
     *
     * @param d
     * @return
     */
    public String checkDay(String d) {
        if (null == d || "".equals(d)) {
            log.error("input time " + d + "error ,must:yyyymmddhh24mi");
            System.exit(1);
        }
        return d.substring(0, 8);
    }

    /**
     * @param filevals
     * @param fss
     * @return
     * @throws java.io.IOException
     */
    @SuppressWarnings("deprecation")
    public ArrayList<Path> buildFs(
            ArrayList<Path> filevals, FileStatus[] fss) throws IOException {
        for (FileStatus status : fss) {
            Path p = status.getPath();
            if (status.isDir()) {// 是目录
                FileSystem fs = p.getFileSystem(conf);
                filevals = buildFs(filevals, fs.listStatus(p));
            } else {// 不是目录
                if (p.toString().indexOf("_") > 0) {

                } else {
                    filevals.add(p);
                    System.out.println(p.toString());
                }

            }
        }
        return filevals;
    }

}
