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
import utils.FileInput;
import utils.FileOutput;
import utils.HttpClientUtil;
import utils.TimeUtil;

public class Zhilian {
	
	public Zhilian() {
		
	}
	
	public void makeDirs(String path) {
		File file = new File(path);
		System.out.println("make dir : " + file.getAbsolutePath());
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();    
		}
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
	
	public void getRecruitPageBatch(ZhilianConf zc) {
		for (int i = 0; i < zc.getIndustryDir().size(); i ++) {
			String curTime = TimeUtil.getCurrentTime();
			String dataDir = zc.getDataDir() + "/" + zc.getIndustryDir().get(i)
				+ "/" + curTime;
			makeDirs(dataDir);
			for (int j = 1; j <= zc.getMaxPageNumber(); j ++) {
				String content = getIndexPage(zc.getIndustryUrl().get(i) + String.valueOf(j));
				parseIndexPage(content, dataDir);
			}
		}
	}
	
	public void parseRecruitPage(String inputPath, String outputPath) {
		FileInput fi = new FileInput(inputPath);
		FileOutput fo = new FileOutput(outputPath);
		
		String content = new String(), line = new String();
		try {
			while ((line = fi.reader.readLine()) != null) {
				content += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(content);
		Document doc = Jsoup.parse(content);
		Elements divs = doc.select(".tab-cont-box");
		if (divs != null) {
			Element div = divs.first();
			if (div != null) {
				divs = div.select(".tab-inner-cont");
				if (divs != null) {
					div = divs.first();
					if (div != null) {
						try {
							fo.t3.write(div.text());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		fo.closeOutput();
		fi.closeInput();
	}
	
	public void parseRecruitPageBatch(ZhilianConf zc) {
		for (int i = 0; i < zc.getIndustryDir().size(); i ++) {
			String curTime = TimeUtil.getCurrentTime();
			String descriptionDir = zc.getDescriptionDir()
					+ "/" + zc.getIndustryDir().get(i)
					+ "/" + curTime;
			makeDirs(descriptionDir);
			String dataDir = zc.getDataDir()
					+ "/" + zc.getIndustryDir().get(i)
					+ "/" + curTime;
			makeDirs(dataDir);
			File file = new File(dataDir);
			for (File f : file.listFiles()) {
				System.out.println("input file : " + f.getAbsolutePath());
				parseRecruitPage(f.getAbsolutePath(), descriptionDir + "/" + f.getName());
			}
		}
	}
	
	public static void main(String [] args) throws IOException {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		zl.getRecruitPageBatch(zc);
	}
}