package com.zjuh.waibao.pxsearch.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.aifuyun.search.util.StringUtil;

public class SearchUtil {

	private static final String ESCAPING = "+,-,&,|,!,(,),{,},[,],^,\",~,*,?,:,\\";
	
	private static final String[] CHINSE_WORDS = {"啊", "阿", "的", "了", "是","但", "并", "没", 
		"在", "不", "这", "之", "否", "啥", "吗", "也", "与", "得", "从"};
	
	
	private static final String[] ENGLISH_WORDS = { "a", "an", "and", "are", "as", "at", "be", "but", "by",
	      "for", "if", "in", "into", "is", "it",
	      "no", "not", "of", "on", "or", "such",
	      "that", "the", "their", "then", "there", "these",
	      "they", "this", "to", "was", "will", "with"};

	private static Analyzer analyzer;
	
	private static final Set<Character> ESCAPING_CHARS = new HashSet<Character>();
	
	private static Set<String> STOP_WORDS;

	static {
		String[] tokens = ESCAPING.split(",");
		for (String token : tokens) {
			if (token.length() == 0) {
				continue;
			}
			ESCAPING_CHARS.add(token.charAt(0));
		}
		
		Set<String> words = new HashSet<String>(Arrays.asList(CHINSE_WORDS));
		words.addAll(Arrays.asList(ENGLISH_WORDS));
		
		STOP_WORDS = Collections.unmodifiableSet(words);
		
		analyzer = new StandardAnalyzer(Version.LUCENE_29, STOP_WORDS);
	}

	public static String filter(String input) {
		if (StringUtil.isEmpty(input)) {
			return input;
		}
		StringBuilder sb = new StringBuilder();
		char[] chars = input.toCharArray();
		for (char c : chars) {
			if (ESCAPING_CHARS.contains(c)) {
				sb.append("\\");
			}
			sb.append(c);
		}
		return sb.toString();
	}

	static final int MAX_WORD_LENGTH = 64;
	
	
	
	public static String parseKeywords(String fieldName, String keyword, Analyzer analyzer) {
		if (keyword.length() > MAX_WORD_LENGTH) {
			keyword = keyword.substring(0, MAX_WORD_LENGTH);
		}
		try {
			StringBuilder sb = new StringBuilder();
			TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(keyword));
			TermAttribute charTermAttribute = (TermAttribute)tokenStream.addAttribute(TermAttribute.class);
			while (tokenStream.incrementToken()) {
				String term = charTermAttribute.term();
				//String term = new String(charTermAttribute.buffer(), 0, charTermAttribute.length());
				sb.append("+");
				sb.append(fieldName);
				sb.append(":");
				sb.append(term);
				sb.append(" ");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String parseItemKeywords(String name, String tags, String content, String keyWords) {
		return parseItemKeywords(name, tags, content, keyWords, analyzer);
	}
	
	public static String parseItemKeywords(String title, String tags, String content, String keyWords, Analyzer analyzer) {
		StringBuilder sb = new StringBuilder();
		
		try {
			StringBuilder sbTitle = new StringBuilder();
			StringBuilder sbTags =  new StringBuilder();
			StringBuilder sbContent =  new StringBuilder();
			sbTitle.append("(");
			sbTags.append("(");
			sbContent.append("(");
			TokenStream tokenStream = analyzer.tokenStream(title, new StringReader(keyWords));
			TermAttribute charTermAttribute = (TermAttribute)tokenStream.addAttribute(TermAttribute.class);
			while (tokenStream.incrementToken()) {
				String term = charTermAttribute.term();
				//String term = new String(charTermAttribute.buffer(), 0, charTermAttribute.length());
				sbTitle.append("+");
				sbTitle.append(title);
				sbTitle.append(":");
				sbTitle.append(term);
				sbTitle.append("^100.0");
				sbTitle.append(" ");
				
				sbTags.append("+");
				sbTags.append(tags);
				sbTags.append(":");
				sbTags.append(term);
				sbTags.append("^10.0");
				sbTags.append(" ");
				
				sbContent.append("+");
				sbContent.append(content);
				sbContent.append(":");
				sbContent.append(term);
				sbContent.append(" ");
				
			}
			sbTitle.append(")");
			sbTags.append(")");
			sbContent.append(")");
			
			sb.append(sbTitle.toString());
			sb.append(" OR ");
			sb.append(sbTags.toString());
			sb.append(" OR ");
			sb.append(sbContent.toString());
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String parseKeywords(String fieldName, String keyword) {
		return parseKeywords(fieldName, keyword, analyzer);
	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		String a = parseKeywords("aa", "你好+的啊 heheh HI is 12");
		System.out.println(a);
	}
}
