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
	
	public Logger() {
		
	}


	public void logError(String msg){
		String fileName = this.helper.getFormatedDate()+".txt";
		String filePathName = this.defaultFolderPath+ "\\"+fileName;
		
		boolean folderExist = this.helper.createFolderIfNotExist(defaultFolderPath);
		
		boolean fileExist = this.helper.createFileIfNotExist(filePathName);
		
		try {
			FileWriter write = new FileWriter(filePathName, true);
			PrintWriter printLine = new PrintWriter(write);
			printLine.printf("%s"+"%n", msg);
			printLine.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getDefaultFolderPath() {
		return defaultFolderPath;
	}

	public void setFolderPath(String path) {
		this.defaultFolderPath = path;
	}
}
