package cn.edu.pku.service;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.index.IndexWriter;

public interface ResumeIndexService {

	public void create();
	
	public void indexDoc(IndexWriter writer, Path file) throws IOException;
	
	public void indexMysql(final IndexWriter writer);
}
