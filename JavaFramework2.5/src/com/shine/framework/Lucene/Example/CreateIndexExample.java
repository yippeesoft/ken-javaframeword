package com.shine.framework.Lucene.Example;

import com.shine.framework.Lucene.LuceneManager;

/**
 * 生成lucene索引
 * 
 * @author Ken
 * 
 */
public class CreateIndexExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LuceneManager
				.getManager()
				.createIndex(
						"E:\\JavaWorkSpace\\JavaFramework2.5\\src\\com\\shine\\framework\\Lucene\\data\\index",
						"E:\\JavaWorkSpace\\JavaFramework2.5\\src\\com\\shine\\framework\\Lucene\\data\\documents");
	}

}
