package cn.edu.pku.conf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.edu.pku.util.FileInput;

public class ZhilianConf {
	
	public static final String HostUrl = "http://jobs.zhaopin.com/";
	public static final String Source = "智联招聘";
	public static final String ConfFile = "../../../../workspace/JobSearchMS/conf/zhilian.conf";
		
	public int MaxPageNumber = 90;
	public String dataDir = new String();
	public String descriptionDir = new String();
	public ArrayList<String> industryUrl = new ArrayList<String>();
	public ArrayList<String> industryDir = new ArrayList<String>();
	
	public ZhilianConf() {
		MaxPageNumber = 90;
		dataDir = "data";
		descriptionDir = "description";
		makeDirs(getDataDir());
		makeDirs(getDescriptionDir());
		
		industryDir.clear();
		industryUrl.clear();
		industryDir.add("互联网&电子商务");
		industryUrl.add("http://sou.zhaopin.com/jobs/searchresult.ashx?bj=160000&in=210500&jl=%E5%8C%97%E4%BA%AC&sm=0&sg=ab99a48943284cd0a2ca8acac91b00d7&p=");
		makeDirs(getDataDir() + "/" + industryDir.get(0));
		makeDirs(getDescriptionDir() + "/" + industryDir.get(0));
	}
	
	public void makeDirs(String path) {
		File file = new File(path);
		System.out.println("make dir : " + file.getAbsolutePath());
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
				setDescriptionDir(workspace + "/" + descriptionDir);
				makeDirs(getDataDir());
				makeDirs(getDescriptionDir());
				
				industryDir.clear();
				industryUrl.clear();
				while((line = fi.reader.readLine()) != null) {
					String args[] = line.trim().split("	+");
					makeDirs(getDataDir() + "/" + args[0].trim());
					makeDirs(getDescriptionDir() + "/" + args[0].trim());
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
	
	public String getDescriptionDir() {
		return descriptionDir;
	}

	public void setDescriptionDir(String descriptionDir) {
		this.descriptionDir = descriptionDir;
	}

	public static String getSource() {
		return Source;
	}

	public static String getConffile() {
		return ConfFile;
	}

	public static void main(String []args) {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
	}
}
