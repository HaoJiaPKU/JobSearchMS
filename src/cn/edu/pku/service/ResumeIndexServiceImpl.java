package cn.edu.pku.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

import cn.edu.pku.conf.DatabaseConf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ResumeIndexServiceImpl implements ResumeIndexService {

	public void create() {
		String indexPath = "ResumeIndex";

		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			// 以文件的形式创建索引
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			// 中文分词器
			Analyzer analyzer = new SimpleAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			// 在文件夹内创建新的文件索引，并移除掉之前的索引文件
			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter writer = new IndexWriter(dir, iwc);
			// indexDocs(writer, docDir);
			String path = "../webapps/fileUpload/resume";
			File dirFile = new File(path);
			String[] files = dirFile.list();
			System.out.println(files.length);
			for (String p : files) {
				if (p.endsWith("html")) {
					indexDoc(writer, Paths.get(path + "/" + p));
				}
			}
			indexMysql(writer);
			writer.close();
			System.out.println("简历索引构建完成");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}

	public void indexDoc(IndexWriter writer, Path file) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {
			// 建立一个新的空文档
			Document doc = new Document();

			doc.add(new StringField("path",
					file.toString().split("webapps")[1], Field.Store.YES));
			doc.add(new StringField("source", "51job", Field.Store.YES));
			doc.add(new TextField("contents", new BufferedReader(
					new InputStreamReader(stream, StandardCharsets.UTF_8))));

			// 建立新的索引文件
			writer.addDocument(doc);
			
		}

	}

	public void indexMysql(final IndexWriter writer) {

		try {
			String url = DatabaseConf.getDatebaseurl();
			Class.forName(DatabaseConf.getClassname());
			Connection conn = DriverManager.getConnection(url);

			String sql = "select * from resume_jobpopo";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new StringField("path", rs.getString("employee_id"),
						Field.Store.YES));
				doc.add(new StringField("source", "jobpopo", Field.Store.YES));
				String content = "";
				for (int i = 1; i < 23; i++) {
					content += rs.getString(i) + " ";
				}
				Statement stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1
						.executeQuery("select * from education where employee_id='"
								+ rs.getString("employee_id") + "'");
				while (rs1.next()) {
					for (int i = 1; i < 10; i++) {
						content += rs1.getString(i);
					}
				}
				ResultSet rs2 = stmt1
						.executeQuery("select * from work_experience where employee_id='"
								+ rs.getString("employee_id") + "'");
				while (rs2.next()) {
					for (int i = 1; i < 10; i++) {
						content += rs2.getString(i);
					}
				}
				doc.add(new TextField("contents", content, Field.Store.NO));
				writer.addDocument(doc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
