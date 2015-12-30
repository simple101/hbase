public class importThread extends Thread {
	protected String tablename;
	protected String[] column;
	protected String[] dataArr;

	public importThread(String tablename, String[] dataArr , String[] column) {
		this.tablename = tablename;
		this.dataArr = dataArr;
		this.column = column;
	}

	public void run() {
		try {
//			int i=0;
//			System.out.println(".................................................................");
//			System.out.println("................tablename:"+tablename+" dataArr: "+dataArr.length+" column: "+column.length);
			HBaseTest.init(tablename, dataArr, column);
//			System.out.println("threads1 : " + i++);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
	}

}
