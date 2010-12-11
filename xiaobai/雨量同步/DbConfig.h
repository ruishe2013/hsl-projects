// DbConfig.h: interface for the CDbConfig class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_DBCONFIG_H__CC0E647E_2730_4233_8662_2876B1D56B61__INCLUDED_)
#define AFX_DBCONFIG_H__CC0E647E_2730_4233_8662_2876B1D56B61__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include "dbtable.h"
#include <map>
using std::map;


class CDbConfig  
{
public:
	typedef map<CString, CDbTable> Name2Table;
	CDbConfig();
	virtual ~CDbConfig();
public:
	CDbTable& GetIdedTable(const CString& id);
	void AddTable(const CString& id, const CDbTable& table);
	CString m_strUser;
	CString m_strPwd;
	CString m_strIp;
	CString m_strDbName;
	CString GetSqlConnStr() const;
private:
	Name2Table m_mapTables;
};

#endif // !defined(AFX_DBCONFIG_H__CC0E647E_2730_4233_8662_2876B1D56B61__INCLUDED_)
