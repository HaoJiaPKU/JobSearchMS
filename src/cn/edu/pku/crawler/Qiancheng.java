package cn.edu.pku.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.conf.QianchengConf;
import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.object.QianchengObj;
import cn.edu.pku.util.FileInput;
import cn.edu.pku.util.FileOutput;
import cn.edu.pku.util.HttpClientUtil;
import cn.edu.pku.util.TimeUtil;

public class Qiancheng {
	
	public static HashSet<String> stopword = new HashSet<String> ();
	
	public Qiancheng() {
		
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
		Elements tables = doc.select(".el");
		if (tables == null) {
			return;
		}
		for (Element table : tables) {
			Elements ps = table.select("p");
			if (ps == null) {
				continue;
			}
			Element p = ps.first();
			if (p == null) {
				continue;
			}
			Elements spans = p.select("span");
			if (spans == null) {
				continue;
			}
			Element span = spans.first();
			if (span == null) {
				continue;
			}
			Elements as = span.select("a");
			if (as == null) {
				continue;
			}
			Element a = span.select("a").first();
			if (a == null) {
				continue;
			}
			Elements inputs = p.select("input");
			if (inputs == null) {
				continue;
			}
			Element input = inputs.first();
			if (input == null) {
				continue;
			}
			String value = input.attr("value").toString();
			String link = a.attr("href").toString();
			if (link.indexOf("jobs.51job.com") == -1) {
				continue;
			}
			crawlPositionPage(link, saveDir + "/" + value, encoding);
		}
	}
	
	public String getRcruitPage(String url, String encoding) {
		return HttpClientUtil.httpGetRequest(url, encoding);
	}
	
	public void crawlPositionPage(String url, String path, String encoding) {
		String content = getRcruitPage(url, encoding);
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
	
	public void crawlPositionPageBatch(QianchengConf qc, String encoding) {
		for (int i = 0; i < qc.getIndustryDir().size(); i ++) {
			String date = TimeUtil.getDate(DatabaseConf.getCrawldate());
			String dataDir = qc.getDataDir() + "/" + qc.getIndustryDir().get(i)
				+ "/" + date;
			System.out.println(dataDir);
			makeDirs(dataDir);
			for (int j = 1; j <= qc.getMaxPageNumber(); j ++) {
				String content = getIndexPage(qc.getIndustryUrl().get(i) + String.valueOf(j), encoding);
				parseIndexPage(content, dataDir, encoding);
			}
		}
	}
	
	public void parsePositionPage(String inputPath) {
		FileInput fi = new FileInput(inputPath);
		
		String content = new String(), line = new String();
		try {
			while ((line = fi.reader.readLine()) != null) {
				content += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		QianchengObj qcobj = new QianchengObj();
		
		Document doc = Jsoup.parse(content);
		Elements divs = doc.select(".job_msg");
		if (divs != null) {
			Element div = divs.first();
			if (div != null) {
				String description = div.text().replace("&nbsp;","").trim();
				
				if (description == null
						|| description.length() == 0
						|| description.equals("")) {
					fi.closeInput();
					return;
				}
				int index1 = description.indexOf("实习");
				int index2 = description.indexOf("兼职");
				if (index1 != -1) {
					qcobj.setPosType("实习");
				} else if (index2 != -1) {
					qcobj.setPosType("兼职");
				} else {
					qcobj.setPosType("全职");
				}
				//设置职位描述信息
				qcobj.setPosDescription(description);
				}
		}
		divs = doc.select(".bmsg");
		if (divs != null) {
			for(Element div : divs) {
				if(div == null) {
					continue;
				}
				if(div.select(".fp") == null) {
					continue;
				}
				Elements fps = div.select(".fp");
				if (fps == null) {
					continue;
				}
				Element fp = fps.first();
				if (fp == null) {
					continue;
				}
				if (fp.text().replace("　", "").trim().length() > 5) {
					String location = fp.text().replace("　", "").substring(5).trim();
					qcobj.setPosLocation(location);
				}
			}
		}
		divs = doc.select(".tmsg");
		if (divs != null) {
			Element div = divs.first();
			if (div != null) {
				Elements brs = div.select("br");
				if (brs != null) {
					for (Element br : brs) {
						if(br != null) {
							String text = br.text().replace("&nbsp;", "").trim();
							if (text.contains("公司规模：")) {
								qcobj.setComScale(text.substring(5).trim());
							} else if (text.contains("公司性质：")) {
								qcobj.setComType(text.substring(5).trim());
							} else if (text.contains("公司行业：")) {
								qcobj.setComIndustry(text.substring(5).trim());
							} else if (text.contains("公司主页：")) {
								qcobj.setComHost(text.substring(5).trim());
							} else if (text.contains("公司地址：")) {
								qcobj.setComLocation(text.substring(5).trim());
							} else if (text.contains("总部地址：")) {
								qcobj.setComLocation(text.substring(5).trim());
							} else {
								continue;
							}
						}
					}
				}
			}
		}
		//职位标题，公司名称
		Elements cns = doc.select(".cn");
		if (cns != null) {
			Element cn = cns.first();
			if (cn != null) {
				Elements h1s = cn.select("h1");
				if (h1s != null) {
					Element h1 = h1s.first();
					if (h1 != null) {
						qcobj.setPostitle(h1.text().trim());
					}
				}
				Elements strongs = cn.select("strong");
				if (strongs != null) {
					Element strong = strongs.first();
					if (strong != null) {
						qcobj.setPosSalary(strong.text().trim());
					}
				}
				Elements ps = cn.select("p");
				if (ps != null) {
					for (Element p : ps) {
						if (p != null){
							Elements as = p.select("a");
							if (as != null) {
								Element a = as.first();
								if (a != null) {
									//System.out.println(a.text().trim());
									qcobj.setComName(a.text().trim());
									continue;
								}
							}
							String text = p.text().replace("&nbsp;", "").replace("  ", "").trim();
							String[] list = text.split("\\|");
							for (String index : list) {
								if (index.indexOf("公司") != -1 || index.indexOf("合资") != -1 || index.indexOf("外资") != -1) {
									qcobj.setComType(index);
								} else if (index.indexOf("人") != -1) {
									qcobj.setComScale(index);
								} else {
									qcobj.setComIndustry(index);
								}
							}
						}
					}
				}
			}
		}
		
		Elements jtags = doc.select(".jtag");
		if (jtags != null) {
			if(jtags.first() != null) {
				Elements t1s = jtags.first().select(".t1");
				if (t1s != null) {
					if (t1s.first() != null) {
						Elements spans = t1s.first().select("span");
						if (spans != null) {
							for (Element span : spans) {
								if(span != null) {
									if (span.select(".i1") != null) {
										if (span.select(".i1").first() != null) {
											//System.out.println(span.text().trim());
											qcobj.setPosExperience(span.text().trim());
										} 
									}
									if (span.select(".i2") != null) {
										if (span.select(".i2").first() != null) {
											//System.out.println(span.text().trim());
											qcobj.setPosDegree(span.text().trim());
										}
									}
									if (span.select(".i3") != null) {
										if (span.select(".i3").first() != null) {
											//System.out.println(span.text().trim());
											qcobj.setPosRecruitNum(span.text().trim());
										}
									}
									if (span.select(".i4") != null) {
										if (span.select(".i4").first() != null) {
											qcobj.setPosPublishDate("2017-" + span.text().trim().substring(0,5));
										}
									}
								}
							}
						}
					}
				}
			}
			Elements t2s = jtags.select(".t2");
			if (t2s != null) {
				Element t2 = t2s.first();
				if(t2 != null) {
					//System.out.println(t2.text().trim());
					qcobj.setPosKeyword(t2.text().trim());
				}
			}
		}
		
		String posDescription = qcobj.getPosDescription();
		int descriptionLen = posDescription.length();
		descriptionLen = (descriptionLen >= 100 ? 100 : descriptionLen);
		//缩略文字部分
		qcobj.setDisplayContent(qcobj.getPosDescription().substring(0, descriptionLen) + "...");
		//职位链接部分
		qcobj.setPosUrl(QianchengConf.HostUrl
				+ "/" + new File(inputPath).getName() + ".html");
		
		//存储到数据库视图缓存
		qcobj.preStore();
		
		fi.closeInput();
	}
		
	public void parsePositionPageBatch(QianchengConf qc) {
		QianchengObj.loadVirtualView();
		for (int i = 0; i < qc.getIndustryDir().size(); i ++) {
			System.out.println("parsing into " + qc.getIndustryDir().get(i));
			String date = TimeUtil.getDate(DatabaseConf.getParsedate());
			String dataDir = qc.getDataDir()
					+ "/" + qc.getIndustryDir().get(i)
					+ "/" + date;
			makeDirs(dataDir);
			File file = new File(dataDir);
			for (File f : file.listFiles()) {
				if (f.getName().contains(".DS_Store")) {
					continue;
				}
				parsePositionPage(f.getAbsolutePath());
			}
		}
		QianchengObj.excuteStore();
		QianchengObj.clearVirtualView();
	}
	
	//过期不一定要删除数据
	public void expirePositionObject() {
//		String date = TimeUtil.getDate(DatabaseConf.getExpiredate());
//		ZhilianObj.deleteZhilianObjs("pos_publish_date", date);
	}
	
	public void removeDuplicate() {
		QianchengObj.removeDuplicate();
	}
	
	public static void main(String [] args) throws IOException {}
}