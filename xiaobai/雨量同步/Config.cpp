// Config.cpp: implementation of the CConfig class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "dbsynch.h"
#include "Config.h"
#import "msxml3.dll"

using namespace MSXML2;


#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CConfig::CConfig() : m_ndelay(1000), m_nReConnectDb(10000)
{
	if(!Init()) {
		AfxMessageBox(_T("ÅäÖÃÎÄ¼þ¼ÓÔØÊ§°Ü£¡"));
		exit(0);
	}
}

CConfig::~CConfig()
{

}

UINT CConfig::GetDelay() const {
	return m_ndelay;
}

UINT CConfig::GetStartDelay() const {
	return this->m_nStartDelay;
}

const CString& CConfig::GetLogFileDir() const {
	return this->m_strLogFileDir;
}

CDbConfig& CConfig::GetSrcDbConfig() {
	return this->m_srcDBConfig;
}

CDbConfig& CConfig::GetDestDbConfig() {
	return this->m_destDBConfig;
}

UINT CConfig::GetReConnectDb() const {
	return this->m_nReConnectDb;
}

bool CConfig::Init() {
	CString configFile = _T(".\\db_config.xml");
	const CString& SRC = _T("src");
	const CString& DEST = _T("dest");
	try {
		CoInitialize(NULL);
		IXMLDOMDocumentPtr ptrDoc;
		ptrDoc.CreateInstance("msxml2.domdocument");
		_variant_t xmlFile(configFile);
		_variant_t outRes(true);
		outRes = ptrDoc->load(xmlFile);
		if((bool)outRes == false) {
			IXMLDOMParseErrorPtr err = ptrDoc->GetparseError();
			AfxMessageBox(err->reason);
			return false;
		}
		IXMLDOMNodePtr root = ptrDoc->GetlastChild();
		IXMLDOMNamedNodeMapPtr rootAttr = root->attributes;
		const CString& logDir = (LPCTSTR)rootAttr->getNamedItem(_T("logDir"))->GetnodeValue().operator _bstr_t();
		this->m_ndelay = 1000 * (long)rootAttr->getNamedItem(_T("scanDelay"))->GetnodeValue();
		this->m_nReConnectDb = 1000 * (long)rootAttr->getNamedItem(_T("reConnectDb"))->GetnodeValue();
		this->m_nStartDelay =  (long)rootAttr->getNamedItem(_T("startDelay"))->GetnodeValue();
		this->m_strLogFileDir = logDir;
		IXMLDOMNodeListPtr dbConfigs = ptrDoc->getElementsByTagName(_T("db_config"));
		for(int i = 0; i < dbConfigs->Getlength(); i++) {
			IXMLDOMNodePtr dbConfig = dbConfigs->Getitem(i);
			IXMLDOMNamedNodeMapPtr attrs = dbConfig->attributes;
			IXMLDOMNodePtr idAttr = attrs->getNamedItem(_T("id"));
			const CString& idVal = (LPCTSTR)idAttr->GetnodeValue().operator _bstr_t();
			const CString& strIp = (LPCTSTR)attrs->getNamedItem(_T("ip"))->GetnodeValue().operator _bstr_t();
			const CString& strDbName = (LPCTSTR)attrs->getNamedItem(_T("dbName"))->GetnodeValue().operator _bstr_t();
			const CString& strUser = (LPCTSTR)attrs->getNamedItem(_T("user"))->GetnodeValue().operator _bstr_t();
			const CString& strPwd = (LPCTSTR)attrs->getNamedItem(_T("pwd"))->GetnodeValue().operator _bstr_t();
			if(SRC == idVal) {
				//src
				m_srcDBConfig.m_strUser = strUser;
				m_srcDBConfig.m_strPwd = strPwd;
				m_srcDBConfig.m_strIp = strIp;
				m_srcDBConfig.m_strDbName = strDbName;
				IXMLDOMNodeListPtr tables = dbConfig->childNodes;
				for(int tIndex = 0; tIndex < tables->Getlength(); ++tIndex) {
					IXMLDOMNodePtr tableNode = tables->Getitem(tIndex);
					IXMLDOMNamedNodeMapPtr tableAttrs = tableNode->attributes; 
					const CString& tableName = (LPCTSTR)tableAttrs->getNamedItem(_T("name"))->GetnodeValue().operator _bstr_t();
					const CString& tableId = (LPCTSTR)tableAttrs->getNamedItem(_T("id"))->GetnodeValue().operator _bstr_t();
					if(tableName.GetLength() < 1)
						continue;
					CDbTable dbTable(tableName);
					IXMLDOMNodeListPtr columns = tableNode->childNodes;
					for(int cIndex = 0; cIndex < columns->Getlength(); ++cIndex) {
						IXMLDOMNodePtr colNode = columns->Getitem(cIndex);
						IXMLDOMNamedNodeMapPtr colAttrs = colNode->attributes;
						const CString& colId = (LPCTSTR)colAttrs->getNamedItem(_T("id"))->GetnodeValue().operator _bstr_t();
						const CString& colName = (LPCTSTR)colAttrs->getNamedItem(_T("name"))->GetnodeValue().operator _bstr_t();
						CDbColumn dbcol(colId, colName);
						dbTable.AddColumn(dbcol);
					}
					m_srcDBConfig.AddTable(tableId, dbTable);
				}
			} else if(DEST == idVal) {
				//dest
				m_destDBConfig.m_strUser = strUser;
				m_destDBConfig.m_strPwd = strPwd;
				m_destDBConfig.m_strIp = strIp;
				m_destDBConfig.m_strDbName = strDbName;
				IXMLDOMNodeListPtr tables = dbConfig->childNodes;
				for(int tIndex = 0; tIndex < tables->Getlength(); ++tIndex) {
					IXMLDOMNodePtr tableNode = tables->Getitem(tIndex);
					IXMLDOMNamedNodeMapPtr tableAttrs = tableNode->attributes; 
					const CString& tableName = (LPCTSTR)tableAttrs->getNamedItem(_T("name"))->GetnodeValue().operator _bstr_t();
					const CString& tableId = (LPCTSTR)tableAttrs->getNamedItem(_T("id"))->GetnodeValue().operator _bstr_t();
					if(tableName.GetLength() < 1)
						continue;
					CDbTable dbTable(tableName);
					IXMLDOMNodeListPtr columns = tableNode->childNodes;
					for(int cIndex = 0; cIndex < columns->Getlength(); ++cIndex) {
						IXMLDOMNodePtr colNode = columns->Getitem(cIndex);
						IXMLDOMNamedNodeMapPtr colAttrs = colNode->attributes;
						const CString& colId = (LPCTSTR)colAttrs->getNamedItem(_T("id"))->GetnodeValue().operator _bstr_t();
						const CString& colName = (LPCTSTR)colAttrs->getNamedItem(_T("name"))->GetnodeValue().operator _bstr_t();
						CDbColumn dbcol(colId, colName);
						dbTable.AddColumn(dbcol);
					}
					m_destDBConfig.AddTable(tableId, dbTable);
				}
			}
		}
	}catch(...) {
		//cout << "error" << endl;
		return false;
	}
	CoUninitialize();
	return true;
}
