// DbColumn.h: interface for the CDbColumn class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_DBCOLUMN_H__4E4E2618_4F6F_4702_ADB5_6FCE76E4B7C7__INCLUDED_)
#define AFX_DBCOLUMN_H__4E4E2618_4F6F_4702_ADB5_6FCE76E4B7C7__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CDbColumn  
{
public:
	CDbColumn();
	CDbColumn(const CString& strId, const CString& strName);
	virtual ~CDbColumn();
public:
	CString m_strId;
	CString m_strName;
private:

};

#endif // !defined(AFX_DBCOLUMN_H__4E4E2618_4F6F_4702_ADB5_6FCE76E4B7C7__INCLUDED_)
