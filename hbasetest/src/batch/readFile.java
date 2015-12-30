package batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class readFile {
	public static void readFileByLine(int bufSize, FileChannel fcin,
			ByteBuffer rBuffer, FileChannel fcout, ByteBuffer wBuffer) {
		String enterStr = "\n";
		try {
			byte[] bs = new byte[bufSize];

			int size = 0;
			StringBuffer strBuf = new StringBuffer("");
			// while((size = fcin.read(buffer)) != -1){
			while (fcin.read(rBuffer) != -1) {
				int rSize = rBuffer.position();
				rBuffer.rewind();
				rBuffer.get(bs);
				rBuffer.clear();
				String tempString = new String(bs, 0, rSize);
				// System.out.print(tempString);
				// System.out.print("<200>");

				int fromIndex = 0;
				int endIndex = 0;
				while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
					String line = tempString.substring(fromIndex, endIndex);
					line = new String(strBuf.toString() + line);
					// System.out.print(line);
					// System.out.print("</over/>");
					// write to anthone file
					writeFileByLine(fcout, wBuffer, line);

					strBuf.delete(0, strBuf.length());
					fromIndex = endIndex + 1;
				}
				if (rSize > tempString.length()) {
					strBuf.append(tempString.substring(fromIndex,
							tempString.length()));
				} else {
					strBuf.append(tempString.substring(fromIndex, rSize));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeFileByLine(FileChannel fcout, ByteBuffer wBuffer,
			String line) {
		try {
			// write on file head
			// fcout.write(wBuffer.wrap(line.getBytes()));
			// wirte append file on foot
			fcout.write(wBuffer.wrap(line.getBytes()), fcout.size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test()  {
		int bufSize = 3;
		File fin = new File("E:\\大数据平台\\car.txt");
		File fout = new File("E:\\201404_01.txt");

		FileChannel fcin=null;
		FileChannel fcout = null;
		ByteBuffer rBuffer = null;
		try {
			fcin = new RandomAccessFile(fin, "r").getChannel();
			 rBuffer = ByteBuffer.allocate(bufSize);
			fcout = new RandomAccessFile(fout, "rws").getChannel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteBuffer wBuffer = ByteBuffer.allocateDirect(bufSize);

	
		readFileByLine(bufSize, fcin, rBuffer, fcout, wBuffer);

		System.out.print("OK!!!");

	}
	
	public static void p() throws Exception{
		  FileChannel read = new FileInputStream("E:\\大数据平台\\car.txt").getChannel();  
	        FileChannel writer = new RandomAccessFile("E:\\201404_01.txt","rw").getChannel();  
	        long i = 0;  
	        long size = read.size()/10;  
	        ByteBuffer bb,cc = null;  
	        while(i<read.size()&&(read.size()-i)>size){  
	            bb = read.map(FileChannel.MapMode.READ_ONLY, i, size);  
	            cc = writer.map(FileChannel.MapMode.READ_WRITE, i, size);  
	            cc.put(bb);  
	            i+=size;  
	            bb.clear();  
	            cc.clear();  
	        }  
	        bb = read.map(FileChannel.MapMode.READ_ONLY, i, read.size()-i);  
	        cc.put(bb);  
	        bb.clear();  
	        cc.clear();  
	        read.close();  
	        writer.close();  

	}

	public static void main(String[] args) {
//		test();
		try {
			p();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
