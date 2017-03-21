package cn.edu.pku.conf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.edu.pku.util.FileInput;

public class QianchengConf {
	
	public static final String HostUrl = "http://jobs.51job.com/beijing";
	public static final String Source = "前程无忧";
	public static final String ConfFile = "../../../git/jrs-backend/conf/qiancheng.conf";
		
	public int MaxPageNumber = 10;
	public String dataDir = Source;
	public ArrayList<String> industryUrl = new ArrayList<String>();
	public ArrayList<String> industryDir = new ArrayList<String>();
	
	public QianchengConf() {
		makeDirs(getDataDir());
		
		industryDir.clear();
		industryUrl.clear();
		industryDir.add("计算机软件");
		industryUrl.add("http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=010000&industrytype=01&keywordtype=2&lang=c&stype=2&postchannel=0000&fromType=1&confirmdate=9&curr_page=");
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
		QianchengConf qc = new QianchengConf();
		qc.run();
	}
}
