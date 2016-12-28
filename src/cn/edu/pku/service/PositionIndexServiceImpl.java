package cn.edu.pku.service;

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
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.util.TimeUtil;

@Service
public class PositionIndexServiceImpl implements PositionIndexService{
	
	@Override
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

	@Override
	public void indexMysql(final IndexWriter writer) {

		try {
			String url = DatabaseConf.getDatebaseurl();
			Class.forName(DatabaseConf.getClassname());
			Connection conn = DriverManager.getConnection(url);

			String sql = "select "
					+ "* "
					+ " from "
					+ DatabaseConf.getPositiontable()
					+ " where "
					+ "pos_publish_date > '"
					+ TimeUtil.getDate(DatabaseConf.getExpiredate()) + "';";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new LongField("id", Long.parseLong(rs.getString("id")), Field.Store.YES));
				doc.add(new StringField("source", rs.getString("source"),
						Field.Store.YES));
				doc.add(new TextField("display_content", rs.getString("display_content"),
						Field.Store.NO));
				String content = rs.getString("pos_title")
						+ " " + rs.getString("pos_location")
						+ " " + rs.getString("pos_type")
						+ " " + rs.getString("pos_degree")
						+ " " + rs.getString("pos_category")
						+ " " + rs.getString("pos_description")
						+ " " + rs.getString("com_type")
						+ " " + rs.getString("com_industry")
						+ " " + rs.getString("com_location");
				doc.add(new TextField("content", content, Field.Store.NO));
				writer.addDocument(doc);
			}

			sql = "select * from recruitment";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new TextField("id", rs.getString("id"), Field.Store.YES));
				doc.add(new TextField("source", "jobpopo", Field.Store.YES));
				String content = rs.getString("business")
						+ " " + rs.getString("city")
						+ " " + rs.getString("company")
						+ " " + rs.getString("degree")
						+ " " + rs.getString("description")
						+ " " + rs.getString("position")
						+ " " + rs.getString("type");
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
