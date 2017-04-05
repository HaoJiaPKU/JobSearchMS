package cn.edu.pku.conf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.edu.pku.util.FileInput;

public class BdwmConf {
	
	public static final String HostUrl = "http://bbs.pku.edu.cn/";
	public static final String Source = "北大未名";
	public static final String ConfFile = "../../../../workspace/jrs-backend/conf/bdwm.conf";
		
	public int MaxPageNumber = 10;
	public String dataDir = Source;
	public ArrayList<String> industryUrl = new ArrayList<String>();
	public ArrayList<String> industryDir = new ArrayList<String>();
	
	public BdwmConf() {
		makeDirs(getDataDir());
		
		industryDir.clear();
		industryUrl.clear();
		industryDir.add("招聘版");
		industryUrl.add("https://bbs.pku.edu.cn/v2/thread.php?bid=845&mode=topic&page=");
		makeDirs(getDataDir() + "/" + industryDir.get(0));
	}
	
	public void makeDirs(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();    
		}
	}
	
	public void run() {
		FileInput fi = new FileInput(ConfFile);
		if (fi.reader != null) {
			String line = new String();
			try {
				line = fi.reader.readLine();
				String workspace = line.trim();
				
				setDataDir(workspace + "/" + dataDir);
				makeDirs(getDataDir());
				
				industryDir.clear();
				industryUrl.clear();
				while((line = fi.reader.readLine()) != null) {
					String args[] = line.trim().split("	+");
					makeDirs(getDataDir() + "/" + args[0].trim());
					industryDir.add(args[0].trim());
					industryUrl.add(args[1].trim());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getMaxPageNumber() {
		return MaxPageNumber;
	}
	
	public void setMaxPageNumber(int maxPageNumber) {
		MaxPageNumber = maxPageNumber;
	}
	
	public String getDataDir() {
		return dataDir;
	}
	
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	
	public ArrayList<String> getIndustryUrl() {
		return industryUrl;
	}
	
	public void setIndustryUrl(ArrayList<String> industryUrl) {
		this.industryUrl = industryUrl;
	}
	
	public ArrayList<String> getIndustryDir() {
		return industryDir;
	}
	
	public void setIndustryDir(ArrayList<String> industryDir) {
		this.industryDir = industryDir;
	}
	
	public static String getHosturl() {
		return HostUrl;
	}

	public static String getSource() {
		return Source;
	}

	public static String getConffile() {
		return ConfFile;
	}

	public static void main(String []args) {
		BdwmConf bd = new BdwmConf();
		//qc.run();
	}
}
