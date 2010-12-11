// DbConfig.cpp: implementation of the CDbConfig class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "dbsynch.h"
#include "DbConfig.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CDbConfig::CDbConfig()
{

}

CDbConfig::~CDbConfig()
{

}

void CDbConfig::AddTable(const CString& id, const CDbTable &table)
{
	this->m_mapTables[id] = table;
}

CDbTable& CDbConfig::GetIdedTable(const CString &name)
{
	return m_mapTables[name];
}

CString CDbConfig::GetSqlConnStr() const {
	CString connStr;
	connStr.Format(_T("Provider=SQLOLEDB.1;Persist Security Info=False;User ID=%s;Password=%s;Initial Catalog=%s;Data Source=%s"),
		m_strUser, m_strPwd, m_strDbName, m_strIp);
	return connStr;
}
