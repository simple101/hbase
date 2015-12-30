package action;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import services.CarService;
import util.HbaseConstants;

import com.opensymphony.xwork2.ActionSupport;
import common.CarRecod;
import common.QueryResult;

public class CarAction extends ActionSupport implements ServletRequestAware{

    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;


	private static Logger logger = Logger.getLogger(CarAction.class);
	private static Map<String, String> rowMap=new LinkedHashMap<String, String>();//存开始和结束rowkey
	
	private HttpServletRequest request;

    private CarService carService;

    private QueryResult qr;

    private List<CarRecod> result;

    private String methodType;

    private String hphm;

    private String hphmreg;

    private String gcxh;

    private String cllx;

    private String hpzl;

    private String kkbh;

    private String kkbhArr;

    private String startTime;

    private String endTime;

    private boolean showAll = false;

    private long count;

    private long time ;
    
    private long total;
    
	private  Integer rows;//每页数量
	
	private  Integer page;//页码
	private  Integer firstPage;//开始页
	private  Integer endPage;//结束页

    
    private String mark;//分页标记
    private String lastRow;//最后一个rowkey
    private String oneRow;//第一个rowkey
    private String rowKey;



	public String search(){
        try {
       		// 获取最大返回结果数量
       		if (rows == null || rows == 0L) {
       			rows = 10;
       		}
       		if (page == null || page == 0L) {
       			page = 1;
       		}
       		// 计算起始页和结束页
       	     firstPage = (page - 1) * rows;
       	     endPage = firstPage + rows;
         	  if ("p".equals(mark)) {
    			for (Map.Entry<String, String> m:rowMap.entrySet()) {
    			  String pRowKey=m.getKey();//获取key
    			  if (String.valueOf(page).equals(pRowKey)) {//判断是哪页
    				  String pRowValue=m.getValue();
    				   rowKey=pRowValue;
						}
					}
    			}
         	  String sTime=null;
         	  String eTime=null;
         	  long startTimes=0;
         	  long endTimes=0;
         	  //把时间转化成毫秒
         	  if (StringUtils.isNotEmpty(startTime)&&StringUtils.isNotEmpty(endTime)) {
         		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         		  startTimes = formatter.parse(startTime).getTime();
             	  endTimes = formatter.parse(endTime).getTime();
             	 sTime=String.valueOf(startTimes);
             	 eTime=String.valueOf(endTimes);
			}
         	String staDate=StringUtils.isEmpty(startTime)?null:sTime;
         	String endDate=StringUtils.isEmpty(endTime)?null:eTime;
            if(HbaseConstants.QUERY_METHOD_CLLX.equals(methodType)){
            	qr = carService.searchCarByCllx(staDate, endDate, cllx,rows,mark,rowKey,rowMap,page);
            }else if(HbaseConstants.QUERY_METHOD_GCXH.equals(methodType)){
                qr = carService.searchCarByGcxh(gcxh);
            }else if(HbaseConstants.QUERY_METHOD_HPHM.equals(methodType)){
                qr = carService.searchCarByHmhp(staDate, endDate, hphm,rows,mark,rowKey,rowMap,page);
            }else if(HbaseConstants.QUERY_METHOD_HPZL.equals(methodType)){
                qr = carService.searchCarByHpzl(staDate, endDate, hpzl,rows,mark,rowKey,rowMap,page);
            }else if(HbaseConstants.QUERY_METHOD_KKBH.equals(methodType)){
                qr = carService.searchCarByKkbh(staDate, endDate, kkbh,rows,mark,rowKey,rowMap,page);
            }else if(HbaseConstants.QUERY_METHOD_HPHM_REG.equals(methodType)){
            	qr = carService.searchCarByHmhpReg(staDate,endDate, hphmreg,rows,mark,rowKey,rowMap,page);
            }else if(HbaseConstants.QUERY_METHOD_RRBH_ARR.equals(methodType)){//卡口编号列表查询
            	if (StringUtils.isNotEmpty(kkbhArr)) {//把卡口编号set到页面
            		request.setAttribute("kkbhArrs",kkbhArr); 
				}
            	//分页的时候获取卡口编号
            	if (StringUtils.isNotEmpty(mark)) {
            		kkbhArr=request.getParameter("kkbhArrs");
            		request.setAttribute("kkbhArrs",kkbhArr); //把卡口编号set到页面
				}
            	qr=carService.searchCarByKkbhArr(staDate,endDate, rows,mark,rowKey,rowMap,page,kkbhArr);
            	
            }
            if(qr != null){
                result = qr.list;
                count = qr.count;
                time = qr.time;
                qr.page=page;
                lastRow=qr.lastRowKey;
                oneRow=qr.oneRow;
            }
        } catch (Exception e) {
            if(HbaseConstants.QUERY_METHOD_CLLX.equals(methodType)){
                logger.error("根据车辆类型【"+cllx+"】查询失败，开始时间【"+startTime+"】，结束时间【"+endTime+"】",e);
            }else if(HbaseConstants.QUERY_METHOD_GCXH.equals(methodType)){
                logger.error("根据过车序号【"+gcxh+"】查询失败",e);
            }else if(HbaseConstants.QUERY_METHOD_HPHM.equals(methodType)){
                logger.error("根据号码号牌【"+hphm+"】查询失败，开始时间【"+startTime+"】，结束时间【"+endTime+"】",e);
            }else if(HbaseConstants.QUERY_METHOD_HPZL.equals(methodType)){
                logger.error("根据号牌种类【"+hpzl+"】查询失败，开始时间【"+startTime+"】，结束时间【"+endTime+"】",e);
            }else if(HbaseConstants.QUERY_METHOD_KKBH.equals(methodType)){
                logger.error("根据卡口编号【"+kkbh+"】查询失败，开始时间【"+startTime+"】，结束时间【"+endTime+"】",e);
            }else if(HbaseConstants.QUERY_METHOD_HPHM_REG.equals(methodType)){
                logger.error("根据号码号牌【"+hphmreg+"】模糊查询失败，开始时间【"+startTime+"】，结束时间【"+endTime+"】",e);
            }

        }

        return "success";
    }

    public CarService getCarService() {
        return carService;
    }




    public void setCarService(CarService carService) {
        this.carService = carService;
    }




    public List<CarRecod> getResult() {
        return result;
    }




    public void setResult(List<CarRecod> result) {
        this.result = result;
    }




    public String getMethodType() {
        return methodType;
    }




    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }




    public String getHphm() {
        return hphm;
    }




    public void setHphm(String hphm) {
        this.hphm = hphm;
    }




    public String getHphmreg() {
        return hphmreg;
    }




    public void setHphmreg(String hphmreg) {
        this.hphmreg = hphmreg;
    }




    public String getGcxh() {
        return gcxh;
    }




    public void setGcxh(String gcxh) {
        this.gcxh = gcxh;
    }




    public String getCllx() {
        return cllx;
    }




    public void setCllx(String cllx) {
        this.cllx = cllx;
    }




    public String getHpzl() {
        return hpzl;
    }




    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }




    public String getKkbh() {
        return kkbh;
    }




    public void setKkbh(String kkbh) {
        this.kkbh = kkbh;
    }




    public String getKkbhArr() {
        return kkbhArr;
    }




    public void setKkbhArr(String kkbhArr) {
        this.kkbhArr = kkbhArr;
    }




    public String getStartTime() {
        return startTime;
    }




    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }




    public String getEndTime() {
        return endTime;
    }




    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }




    public boolean isShowAll() {
        return showAll;
    }




    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }




    public long getCount() {
        return count;
    }




    public void setCount(long count) {
        this.count = count;
    }




    public long getTime() {
        return time;
    }




    public void setTime(long time) {
        this.time = time;
    }




	public Integer getRows() {
		return rows;
	}




	public void setRows(Integer rows) {
		this.rows = rows;
	}




	public Integer getPage() {
		return page;
	}




	public void setPage(Integer page) {
		this.page = page;
	}




	public long getTotal() {
		return total;
	}




	public void setTotal(long total) {
		this.total = total;
	}




	public void setServletRequest(HttpServletRequest request) {
	
		this.request=request;
	}


	public String getMark() {
		return mark;
	}




	public void setMark(String mark) {
		this.mark = mark;
	}




	public String getLastRow() {
		return lastRow;
	}




	public void setLastRow(String lastRow) {
		this.lastRow = lastRow;
	}




	public String getOneRow() {
		return oneRow;
	}




	public void setOneRow(String oneRow) {
		this.oneRow = oneRow;
	}




	public String getRowKey() {
		return rowKey;
	}




	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}













}
