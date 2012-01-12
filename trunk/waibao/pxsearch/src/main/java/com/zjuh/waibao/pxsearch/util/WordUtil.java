package com.zjuh.waibao.pxsearch.util;

import java.io.FileInputStream;
import java.io.InputStream;

import org.textmining.text.extraction.WordExtractor;

public class WordUtil {
	
	public static String getWordText(InputStream is)  {
		WordExtractor  wordExtractor = new WordExtractor();
		try {
			return wordExtractor.extractText(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(getWordText(new FileInputStream("d:/EEWEBÐÞ¸Ä.doc")));
	}

}
