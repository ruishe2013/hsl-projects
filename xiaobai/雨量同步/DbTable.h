// DbTable.h: interface for the CDbTable class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_DBTABLE_H__62A7442F_1942_4860_9C17_CD41CACFA269__INCLUDED_)
#define AFX_DBTABLE_H__62A7442F_1942_4860_9C17_CD41CACFA269__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include "DbColumn.h"
#include <map>
using std::map;


class CDbTable  
{
public:
	typedef map<CString, CDbColumn> Id2Column;
	CDbTable();
	CDbTable(const CString& name);
	virtual ~CDbTable();
public:	
	const CString& GetName() const;
	void SetName(const CString& name);
	const CString& GetColumnNameById(const CString& id);
	void AddColumn(const CDbColumn& col);
private:
	Id2Column m_ctrColumns;
	CString m_strName;
};

#endif // !defined(AFX_DBTABLE_H__62A7442F_1942_4860_9C17_CD41CACFA269__INCLUDED_)
