package apps;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.FileOutput;
import utils.HttpClientUtil;

public class Zhillian {
	
	public static final String IndustryUrl = "http://sou.zhaopin.com/jobs/searchresult.ashx?bj=160000&in=210500&jl=%E5%8C%97%E4%BA%AC&sm=0&sg=ab99a48943284cd0a2ca8acac91b00d7&p=";
	public static final String HostUrl = "http://jobs.zhaopin.com/";
	public static final int MaxPageNumber = 90;
	
	public static final String DataDir = "recruit data/";
	public static String saveDir = new String();
	
	public static String getIndexPage(String url) {
		return HttpClientUtil.httpGetRequest(url);
	}
	
	public static void parseIndexPage(String content) {
//		System.out.println(content);
		Document doc = Jsoup.parse(content);
		Elements tables = doc.select(".newlist");
		if (tables == null) {
			return;
		}
		for (Element table : tables) {
			Elements tbodys = table.select("tbody");
			if (tbodys == null) {
				continue;
			}
			Element tbody = tbodys.first();
			if (tbody == null) {
				continue;
			}
			Elements trs = tbody.select("tr");
			if (trs == null) {
				continue;
			}
			Element tr = trs.first();
			if (tr == null) {
				continue;
			}
			Elements tds = tr.select(".zwmc");
			if (tds == null) {
				continue;
			}
			Element td = tds.first();
			if (td == null) {
				continue;
			}
			Elements divs = td.select("div");
			if (divs == null) {
				continue;
			}
			Element div = divs.first();
			if (div == null) {
				continue;
			}
			Elements as = div.select("a");
			if (as == null) {
				continue;
			}
			Element a = div.select("a").first();
			if (a == null) {
				continue;
			}
//			String title = a.text().toString();
			String link = a.attr("href").toString();
			tds = tr.select("td");
			if (tds == null) {
				continue;
			}
			td = tds.last();
			if (td == null) {
				continue;
			}
			String date = td.text().toString();
//			System.out.println(date);
			getRecruitPage(link, saveDir + "/" + date + "-" + link.substring(HostUrl.length()));
		}
	}
	
	public static void getRecruitPage(String url, String path) {
//		System.out.println(url);
		String content = HttpClientUtil.httpGetRequest(url);
		FileOutput fo = new FileOutput(path);
		try {
			fo.t3.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fo.closeOutput();
	}
	
	public static void batch() {
		String curTime = getCurrentTime();
		saveDir = DataDir + curTime;
		File file = new File(saveDir);  
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();    
		}
		for (int i = 31; i <= MaxPageNumber; i ++) {
//			System.out.println(IndustryUrl + String.valueOf(i));
			String content = getIndexPage(IndustryUrl + String.valueOf(i));
//			System.out.println(content);
			parseIndexPage(content);
		}
	}
	
	public static String getCurrentTime() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }
	
	public static void main(String [] args) throws IOException {
		batch();
	}
}