package com.zjuh.waibao.pxsearch.dataprovider.qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.zjuh.waibao.pxsearch.dataprovider.CommonDataProvider;
import com.zjuh.waibao.pxsearch.doc.Types;
import com.zjuh.waibao.pxsearch.util.HtmlUtil;
import com.zjuh.waibao.pxsearch.util.WordUtil;

public class QABaseDataProvider extends CommonDataProvider {

	private File baseWordDir = new File("D:/wwwroot/SIKAOLI.COM");
	
	@Override
	protected String getSql() {
		String sql = "select PID, Name, Keywords, Details, WordFilename from c_textbook where [State] = 2 ";
		return sql;
	}

	@Override
	protected String getSqlCondition() {
		return "";
	}
	
	private String getWordContent(String wordFilename) {
		if (wordFilename == null) {
			return null;
		}
		//D:\wwwroot\SIKAOLI.COM\docs
		FileInputStream is = null;
		try {
			File wordFile = new File(baseWordDir, wordFilename);
			is = new FileInputStream(wordFile);
			return WordUtil.getWordText(is);
		} catch (Exception e) {
			log.error("∂¡»°word ß∞‹", e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	@Override
	protected Map<String, String> mappingResult(ResultSet rs) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			String pid = rs.getString("PID");
			String name = rs.getString("name");
			String tags = rs.getString("Keywords");
			String details = rs.getString("Details");
			String wordFilename = rs.getString("WordFilename");
			String type = Types.QA;
			
			ret.put("id", makeId(pid, type));
			ret.put("docId", pid);
			ret.put("title", name);
			ret.put("tags", tags);
			ret.put("type", type);
			
			details = HtmlUtil.removeHtmlTags(details);
			String wordContent = getWordContent(wordFilename);
			
			StringBuilder sbContent = new StringBuilder();
			if (details != null) {
				sbContent.append(details);
			}
			if (wordContent != null) {
				sbContent.append(wordContent);
			}
			ret.put("content", sbContent.toString());
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}

	
	protected String getWordContent(InputStream is) {
		return WordUtil.getWordText(is);
	}

}
