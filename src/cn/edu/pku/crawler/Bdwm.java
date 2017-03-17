package cn.edu.pku.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.conf.BdwmConf;
import cn.edu.pku.object.BdwmObj;
import cn.edu.pku.util.FileInput;
import cn.edu.pku.util.FileOutput;
import cn.edu.pku.util.HttpClientUtil;
import cn.edu.pku.util.TimeUtil;

public class Bdwm {
	
	public static HashSet<String> stopword = new HashSet<String> ();
	
	public Bdwm() {
		
	}
	
	public void makeDirs(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();    
		}
	}
	
	public String getIndexPage(String url, String encoding) {
		return HttpClientUtil.httpGetRequest(url, encoding);
	}
	
	public void parseIndexPage(String content, String saveDir, String encoding) {
		Document doc = Jsoup.parse(content);
		Elements tables = doc.select(".list-item-topic");
		if (tables == null) {
			return;
		}
		for (Element table : tables) {
			Elements titles = table.select(".title");
			if (titles == null) {
				continue;
			}
			Element title = titles.first();
			if(title == null) {
				continue;
			}
			String name = title.text().trim();
			if (name.indexOf("恢复") != -1 && name.indexOf("发文权力") != -1) {
				continue;
			}
			if (name.indexOf("封禁") != -1 && name.indexOf("发文权力") != -1) {
				continue;
			}
			Elements as = table.select("a");
			if (as == null) {
				continue;
			}
			Element a = as.first();
			if (a == null) {
				continue;
			}
			String link = "https://bbs.pku.edu.cn/v2/" + a.attr("href").toString().replace("&amp;", "");
			String itemid = link.substring(link.indexOf("threadid=") + 9);
			crawlPositionPage(link, saveDir + "/"  + itemid, encoding);
		}
	}
	
	public String getPositionPage(String url, String encoding) {
		return HttpClientUtil.httpGetRequest(url, encoding);
	}
	
	public void crawlPositionPage(String url, String path, String encoding) {
		String content = getPositionPage(url, encoding);
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
	
	public void crawlPositionPageBatch(BdwmConf bd, String encoding) {
		String date = TimeUtil.getDate(DatabaseConf.getCrawldate());
		String dataDir = bd.getDataDir() + "/" + bd.getIndustryDir().get(0)
			+ "/" + date;
		System.out.println(dataDir);
		makeDirs(dataDir);
		for (int j = 1; j <= bd.getMaxPageNumber(); j ++) {
			String content = getIndexPage(bd.getIndustryUrl().get(0) + String.valueOf(j), encoding);
			parseIndexPage(content, dataDir, encoding);
		}
	}
	
	public void parsePositionPage(String inputPath, String outputPath) {
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
		
		BdwmObj bobj = new BdwmObj();
		
		Document doc = Jsoup.parse(content);
		Elements divs = doc.select(".post-body");
		if(divs != null){
			Element div = divs.first();
			Elements tits = div.select("header");
			if(tits != null) {
				Element tit = tits.first();
				Elements h3s = tit.select("h3");
				if(h3s != null) {
					Element h1 = h3s.first();
					if(h1 != null){
						bobj.setPostitle(h1.text().replace("&nbsp;", "").trim());
					}
				}
			}
			Elements dess = div.select(".file-read");
			if (dess != null) {
				Element des = dess.first();
				if(des !=null){
					String description = des.text().trim();
					description.replace("&nbsp;", "");
					if (description == null
							|| description.length() == 0
							|| description.equals("")) {
						fo.closeOutput();
						fi.closeInput();
						return;
					}
					bobj.setPosDescription(description);
					Elements ps = des.select("p");
					if(ps != null){
						for(Element p : ps) {
							if(p != null){
								String text = p.text().trim();
								if (text.contains("职位月薪：")) {
									bobj.setPosSalary(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("工作地点：")) {
									bobj.setPosLocation(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("工作经验：")) {
									bobj.setPosExperience(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("最低学历：")) {
									bobj.setPosDegree(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("招聘人数：")) {
									bobj.setPosRecruitNum(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("职位类别：")) {
									bobj.setPosCategory(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("公司规模：")) {
									bobj.setComScale(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("公司性质：")) {
									bobj.setComType(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("公司行业：")) {
									bobj.setComIndustry(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("公司主页：")) {
									bobj.setComHost(text.replace("&nbsp;", "").substring(5).trim());
								} else if (text.contains("公司地址：")) {
									bobj.setComLocation(text.replace("&nbsp;", "").substring(5).trim());
								} else {
									continue;
								}
							}
						}
					}
				}
			}
//			Elements times = div.select(".down-list");
//			if(times != null) {
//				Element time = times.first();
//				if(time != null) {
//					Elements lis = time.select("li");
//					if(lis != null) {
//						Element li = lis.first();
//						if(li != null) {
//							Elements spans = li.select("span");
//							if(spans != null) {
//								Element span = spans.first();
//								String pub = span.text().trim();
//								bobj.setPosPublishDate(pub.substring(3,13));
//							}
//						}
//					}
//				}
//				else {
				Elements times = div.select(".sl-triangle-container");
				if(times != null) {
					Element time = times.first();
					if(time != null) {
						Elements lis = time.select(".title");
						if(lis != null) {
							Element li = lis.first();
							if(li != null) {
								Elements spans = li.select("span");
								if(spans != null) {
									Element span = spans.first();
									String pub = span.text().trim();
									bobj.setPosPublishDate(pub.substring(pub.indexOf("201"),pub.indexOf("201")+10));
								}
							}
						}
					}
				}
		}

		
		
		
		String posDescription = bobj.getPosDescription();
		int descriptionLen = posDescription.length();
		descriptionLen = (descriptionLen >= 100 ? 100 : descriptionLen);
		//缩略文字部分
		bobj.setDisplayContent(bobj.getPosDescription().substring(0, descriptionLen) + "...");
		//职位链接部分
		bobj.setPosUrl(BdwmConf.HostUrl + "v2/post-read.php?bid=845&threadid=" 
				+ new File(inputPath).getName());
		
		bobj.setPosType("全职");
		//存储到数据库视图缓存
		bobj.preStore();
//		System.out.println(bobj.getComName());
		
		try {
			fo.t3.write(bobj.getPostitle()
					+ "	" + bobj.getPosPublishDate()
					+ "	" + bobj.getPosCategory()
					+ "	" + bobj.getPosDescription());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fo.closeOutput();
		fi.closeInput();
	}
		
	public void parsePositionPageBatch(BdwmConf bd) {
		BdwmObj.loadVirtualView();
		for (int i = 0; i < bd.getIndustryDir().size(); i ++) {
			System.out.println("parsing into " + bd.getIndustryDir().get(i));
			String date = TimeUtil.getDate(DatabaseConf.getParsedate());
			String descriptionDir = bd.getDescriptionDir()
					+ "\\" + bd.getIndustryDir().get(i)
					+ "\\" + date;
			makeDirs(descriptionDir);
			String dataDir = bd.getDataDir()
					+ "\\" + bd.getIndustryDir().get(i)
					+ "\\" + date;
			makeDirs(dataDir);
			File file = new File(dataDir);
			for (File f : file.listFiles()) {
				if (f.getName().contains(".DS_Store")) {
					continue;
				}
				parsePositionPage(f.getAbsolutePath(),
						descriptionDir + "/" + f.getName());
			}
		}
		BdwmObj.excuteStore();
		BdwmObj.clearVirtualView();
	}
	
	//过期不一定要删除数据
	public void expirePositionObject() {
//		String date = TimeUtil.getDate(DatabaseConf.getExpiredate());
//		BdwmObj.deleteBdwmObjs("pos_publish_date", date);
	}
	
	public void removeDuplicate() {
		BdwmObj.removeDuplicate();
	}
	
	public static void main(String [] args) throws IOException {}
}