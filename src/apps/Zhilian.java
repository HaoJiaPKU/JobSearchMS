package apps;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import conf.ZhilianConf;
import utils.FileOutput;
import utils.HttpClientUtil;

public class Zhilian {
	
	public Zhilian() {
		
	}
	
	public String getIndexPage(String url) {
		return HttpClientUtil.httpGetRequest(url);
	}
	
	public void parseIndexPage(String content, String saveDir) {
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
//			System.out.println(link);
			getRecruitPage(link, saveDir + "/" + date + "-" + link.substring(ZhilianConf.getHosturl().length()));
		}
	}
	
	public void getRecruitPage(String url, String path) {
//		System.out.println(url);
		String content = HttpClientUtil.httpGetRequest(url);
		FileOutput fo = new FileOutput(path);
		try {
			if (fo.t3 != null) {
				fo.t3.write(content);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fo.closeOutput();
	}
	
	public void batch(ZhilianConf zc) {
		for (int i = 0; i < zc.getIndustryDir().size(); i ++) {
			String curTime = getCurrentTime();
			String saveDir = zc.getDataDir() + "/" + zc.getIndustryDir().get(i)
				+ "/" + curTime;
			File file = new File(saveDir);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();    
			}
			for (int j = 1; j <= zc.getMaxPageNumber(); j ++) {
				String content = getIndexPage(zc.getIndustryUrl().get(i) + String.valueOf(j));
				parseIndexPage(content, saveDir);
			}
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
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		zl.batch(zc);
	}
}