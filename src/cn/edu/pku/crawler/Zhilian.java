package cn.edu.pku.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.object.ZhilianObj;
import cn.edu.pku.util.FileInput;
import cn.edu.pku.util.FileOutput;
import cn.edu.pku.util.HttpClientUtil;
import cn.edu.pku.util.TimeUtil;

public class Zhilian {
	
	public static HashSet<String> stopword = new HashSet<String> ();
	
	public Zhilian() {
		
	}
	
	public static void loadStopword() {
		FileInput fi = new FileInput("stopwords.txt");
		String line = new String ();
		try {
			while ((line = fi.reader.readLine()) != null) {
				stopword.add(line.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fi.closeInput();
	}
	
	public static void proData() {
		FileOutput fo = new FileOutput("../lda/model/lda.dat");
		try {
			fo.t3.write(String.valueOf(41917));
			fo.t3.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File("../../myFile/智联招聘 训练集_pro");
		for (File f : file.listFiles()) {
			if (f.getName().contains(".DS_Store")) {
				continue;
			}
			for (File subFile : f.listFiles()) {
				if (subFile.getName().contains(".DS_Store")) {
					continue;
				}
				FileInput fi = new FileInput(subFile.getAbsolutePath());
				String line = new String (), ret = new String();
				try {
					while ((line = fi.reader.readLine()) != null) {
						String token[] = line.split(" +");
						for (int i = 0; i < token.length; i ++) {
							token[i] = token[i].trim();
							if (!stopword.contains(token[i])) {
								ret += token[i] + " ";
							}
						}
					}
					fo.t3.write(ret);
					fo.t3.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fi.closeInput();
			}
		}
		fo.closeOutput();
	}
	
	public void makeDirs(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();    
		}
	}
	
	public String getIndexPage(String url) {
		return HttpClientUtil.httpGetRequest(url);
	}
	
	public void parseIndexPage(String content, String saveDir) {
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
			crawlPositionPage(link, saveDir + "/" + date + "-" + link.substring(ZhilianConf.getHosturl().length()));
		}
	}
	
	public String getPositionPage(String url) {
		return HttpClientUtil.httpGetRequest(url);
	}
	
	public void crawlPositionPage(String url, String path) {
		String content = getPositionPage(url);
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
	
	public void crawlPositionPageBatch(ZhilianConf zc) {
		for (int i = 0; i < zc.getIndustryDir().size(); i ++) {
			String date = TimeUtil.getDate(DatabaseConf.getCrawldate());
			String dataDir = zc.getDataDir() + "/" + zc.getIndustryDir().get(i)
				+ "/" + date;
			System.out.println(dataDir);
			makeDirs(dataDir);
			for (int j = 1; j <= zc.getMaxPageNumber(); j ++) {
				String content = getIndexPage(zc.getIndustryUrl().get(i) + String.valueOf(j));
				parseIndexPage(content, dataDir);
			}
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
		
		ZhilianObj zlobj = new ZhilianObj();
		
		Document doc = Jsoup.parse(content);
		Elements divs = doc.select(".tab-cont-box");
		if (divs != null) {
			Element div = divs.first();
			if (div != null) {
				divs = div.select(".tab-inner-cont");
				if (divs != null) {
					div = divs.first();
					if (div != null) {
						String description = div.text().trim();
						
						if (description == null
								|| description.length() == 0
								|| description.equals("")) {
							fo.closeOutput();
							fi.closeInput();
							return;
						}
						
						//删除职位描述部分的工作地址
						int index = description.indexOf("工作地址：");
						if (index != -1) {
							zlobj.setPosDescription(description.substring(0, index));
						} else {
							zlobj.setPosDescription(description);
						}
					}
				}
			}
		}
		
		Elements uls = doc.select("ul.terminal-ul");
		if (uls != null) {
			for (Element ul : uls) {
				if (ul != null) {
					Elements lis = ul.select("li");
					for (Element li : lis) {
						if (li != null) {
							String text = li.text().trim();
							if (text.contains("职位月薪：")) {
								zlobj.setPosSalary(text.replace("&nbsp;", "").substring(5).trim());
							} else if (text.contains("工作地点：")) {
								zlobj.setPosLocation(text.substring(5).trim());
							} else if (text.contains("发布日期：")) {
								zlobj.setPosPublishDate(text.substring(5).trim());
							} else if (text.contains("工作性质：")) {
								zlobj.setPosType(text.substring(5).trim());
							} else if (text.contains("工作经验：")) {
								zlobj.setPosExperience(text.substring(5).trim());
							} else if (text.contains("最低学历：")) {
								zlobj.setPosDegree(text.substring(5).trim());
							} else if (text.contains("招聘人数：")) {
								zlobj.setPosRecruitNum(text.substring(5).trim());
							} else if (text.contains("职位类别：")) {
								zlobj.setPosCategory(text.substring(5).trim());
							} else if (text.contains("公司规模：")) {
								zlobj.setComScale(text.substring(5).trim());
							} else if (text.contains("公司性质：")) {
								zlobj.setComType(text.substring(5).trim());
							} else if (text.contains("公司行业：")) {
								zlobj.setComIndustry(text.substring(5).trim());
							} else if (text.contains("公司主页：")) {
								zlobj.setComHost(text.substring(5).trim());
							} else if (text.contains("公司地址：")) {
								zlobj.setComLocation(text.substring(5).trim());
							} else {
								continue;
							}
						}
					}
				}
			}
		}
		
		
		divs = doc.select(".inner-left");
		if (divs != null) {
			Element div = divs.first();
			if (div != null) {
				//职位标题部分
				Elements h1s = div.select("h1");
				if (h1s != null) {
					Element h1 = h1s.first();
					if (h1 != null) {
						zlobj.setPostitle(h1.text().trim());
					}
				}
				//公司名称部分
				Elements h2s = div.select("h2");
				if (h2s != null) {
					Element h2 = h2s.first();
					if (h2 != null) {
						Elements as = h2.select("a");
						if (as != null) {
							Element a = as.first();
							if (a != null) {
								zlobj.setComName(a.text().trim());
							}
						}
					}
				}
			}
		}
		
		String posDescription = zlobj.getPosDescription();
		int descriptionLen = posDescription.length();
		descriptionLen = (descriptionLen >= 100 ? 100 : descriptionLen);
		//缩略文字部分
		zlobj.setDisplayContent(zlobj.getPosDescription().substring(0, descriptionLen) + "...");
		//职位链接部分
		zlobj.setPosUrl(ZhilianConf.HostUrl
				+ new File(inputPath).getName().substring(6));
		
		//存储到数据库视图缓存
		zlobj.preStore();
		
		try {
			fo.t3.write(zlobj.getPostitle()
					+ "	" + zlobj.getPosPublishDate()
					+ "	" + zlobj.getPosCategory()
					+ "	" + zlobj.getPosDescription());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fo.closeOutput();
		fi.closeInput();
	}
		
	public void parsePositionPageBatch(ZhilianConf zc) {
		ZhilianObj.loadVirtualView();
		for (int i = 0; i < zc.getIndustryDir().size(); i ++) {
			System.out.println("parsing into " + zc.getIndustryDir().get(i));
			String date = TimeUtil.getDate(DatabaseConf.getParsedate());
			String descriptionDir = zc.getDescriptionDir()
					+ "/" + zc.getIndustryDir().get(i)
					+ "/" + date;
			makeDirs(descriptionDir);
			String dataDir = zc.getDataDir()
					+ "/" + zc.getIndustryDir().get(i)
					+ "/" + date;
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
		ZhilianObj.excuteStore();
		ZhilianObj.clearVirtualView();
	}
	
	//过期不一定要删除数据
	public void expirePositionObject() {
//		String date = TimeUtil.getDate(DatabaseConf.getExpiredate());
//		ZhilianObj.deleteZhilianObjs("pos_publish_date", date);
	}
	
	public void removeDuplicate() {
		ZhilianObj.removeDuplicate();
	}
	
	public static void main(String [] args) throws IOException {}
}