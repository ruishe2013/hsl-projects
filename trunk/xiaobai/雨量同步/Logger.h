// Logger.h: interface for the CLogger class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_LOGGER_H__971B68FF_EC69_47F4_BD17_5DDACDB34038__INCLUDED_)
#define AFX_LOGGER_H__971B68FF_EC69_47F4_BD17_5DDACDB34038__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Config.h"

class CLogger  
{
public:
	virtual ~CLogger();
private:
	void init();
	CLogger();
	LOG_OBJECT *m_pLogObject;
	CLogger(const CLogger&) {}
	CLogger& operator=(const CLogger&) { return *this; }
public:
	void Error(const CString& error);
	void Debug(const CString& debug);
	void Info(const CString& info);
	static CLogger &GetInstance() {
	static CLogger log;
	return log;
	}
};

#endif // !defined(AFX_LOGGER_H__971B68FF_EC69_47F4_BD17_5DDACDB34038__INCLUDED_)
