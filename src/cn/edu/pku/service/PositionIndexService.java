package cn.edu.pku.service;

import org.apache.lucene.index.IndexWriter;

public interface PositionIndexService {
	
	public void create();
	
	public void indexMysql(final IndexWriter writer);
}
