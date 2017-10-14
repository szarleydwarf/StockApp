package utillity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	private Helper helper;
	private String defaultFolderPath;
	
	public Logger(String folderPath){
		helper = new Helper();
		this.defaultFolderPath = folderPath;
	}
	
	public void logError(String msg){
		String fileName = this.helper.getFormatedDate()+".txt";
		String filePathName = this.defaultFolderPath+ "\\"+fileName;
		
		System.out.println("Logger msg "+msg+"\n"+filePathName);
		boolean folderExist = this.helper.createFolderIfNotExist(defaultFolderPath);
		System.out.println("Logger folder "+folderExist);
		
		boolean fileExist = this.helper.createFileIfNotExist(filePathName);
		System.out.println("Logger file msg "+fileExist);
		
		try {
			FileWriter write = new FileWriter(filePathName, true);
			PrintWriter printLine = new PrintWriter(write);
			printLine.printf("%s"+"%n", msg);
			printLine.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
