package action;

import java.util.List;

import org.apache.log4j.Logger;

import services.HDService;

import com.opensymphony.xwork2.ActionSupport;

public class HDAction extends ActionSupport {

	private static Logger logger = Logger.getLogger(HDAction.class);
	
	
	private HDService hdservice;
	
	


	private String phoneNum ;
	
	public List<String> result;
	
	
	public String search(){
		
		try {
			result = hdservice.searchByPhoneNum(phoneNum);
		} catch (Exception e) {
			logger.error("根据电话号码【"+phoneNum+"】查询失败",e);
		}
		
		return "success";
	}
	




	public String getPhoneNum() {
		return phoneNum;
	}



	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}



	public List<String> getResult() {
		return result;
	}



	public void setResult(List<String> result) {
		this.result = result;
	}





	public HDService getHdservice() {
		return hdservice;
	}





	public void setHdservice(HDService hdservice) {
		this.hdservice = hdservice;
	}
}
