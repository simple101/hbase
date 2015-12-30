package common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 过车记录信息
 * @author wangxiaohui
 * @date 2015-3-17
 */
public class CarRecod {
    //过车序号
    public String gcxh;
    //卡口编号
    public String kkbh;
    //方向类型
    public String fxlx;
    //车道号
    public String cdh;
    //过车时间
    public String gcsj;
    //号牌种类
    public String hpzl;
    //号牌号码
    public String hphm;
    //号牌颜色
    public String hpys;
    //车外廓长
    public String cwkc;
    //车辆速度
    public String clsd;
    //车身颜色
    public String csys;
    //车辆品牌
    public String clpp;
    //车辆类型
    public String cllx;
    //辅助号牌种类
    public String fzhpzl;
    //辅助号牌号码
    public String fzhphm;
    //辅助号牌颜色
    public String fzhpys;
    //车辆编号
    public String clbh;
    //车辆图片
    public String cltp;
    //时间
    public String sj;
    
    public String xxly;

    public String zwrksj;

    public String nwrksj;

    public String bz;
    
    public String cs;
    public static void main(String[] args) {
//		0000000003041
		StringBuilder sRow=new StringBuilder("0000000003041");
	    String s= sRow.reverse().toString();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		Calendar calendar = Calendar.getInstance();// 把毫秒转化成时间
		calendar.setTimeInMillis(Long.valueOf(s));
		System.out.println(sdf.format(calendar.getTime()));
	}

}
