package service;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

public class CreateRecruitmentService {
	
	public void create() {
		String indexPath = "RecruitmentIndex";
		// 以文件的形式创建索引
		Directory dir;
		try {
			dir = FSDirectory.open(Paths.get(indexPath));

			// 中文分词器
			Analyzer analyzer = new SimpleAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			// 在文件夹内创建新的文件索引，并移除掉之前的索引文件
			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter writer = new IndexWriter(dir, iwc);
			// indexDocs(writer, docDir);
			indexMysql(writer);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void indexMysql(final IndexWriter writer) {

		try {
			String url = "jdbc:mysql://162.105.30.30:3306/jobsearch?"
					+ "user=root&password=seke1726&useUnicode=true&characterEncoding=UTF-8";
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url);

			String sql = "select * from recruitment_bbs";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new LongField("id", Long.parseLong(rs.getString("id")), Field.Store.YES));
				doc.add(new StringField("source", rs.getString("source"),
						Field.Store.YES));
				doc.add(new TextField("content", rs.getString("content"),
						Field.Store.NO));
				writer.addDocument(doc);
			}

			sql = "select * from recruitment";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new TextField("id", rs.getString("id"), Field.Store.YES));
				doc.add(new TextField("source", "jobpopo", Field.Store.YES));
				String content = rs.getString("business") + " "
						+ rs.getString("city") + " " + rs.getString("company")
						+ " " + rs.getString("degree") + " "
						+ rs.getString("description") + " "
						+ rs.getString("position") + " " + rs.getString("type");
				doc.add(new TextField("content", content, Field.Store.NO));
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
