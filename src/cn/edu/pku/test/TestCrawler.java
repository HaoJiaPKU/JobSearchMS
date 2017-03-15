package cn.edu.pku.test;

import java.io.IOException;

import javax.print.Doc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.pku.util.HttpClientUtil;
import cn.edu.pku.util.UrlUtil;

public class TestCrawler {

	public static void main(String [] args) {
		try {
			Document doc = Jsoup.connect("https://bbs.pku.edu.cn/v2/post-read.php?bid=845&threadid=16038054").get();
			System.out.println(doc.text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String str = HttpClientUtil.httpGetRequest("https://bbs.pku.edu.cn/v2/post-read.php?bid=845&threadid=16038054", "utf-8");
		System.out.println(str);
		
		Document doc;
		try {
			doc = Jsoup.parse(UrlUtil.getHTML("https://bbs.pku.edu.cn/v2/post-read.php?bid=845&threadid=16038054"));
			System.out.println(doc.text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
