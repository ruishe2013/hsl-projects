// Config.h: interface for the CConfig class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_CONFIG_H__C8507B19_5B64_49A7_B4F7_9E25795C5B3C__INCLUDED_)
#define AFX_CONFIG_H__C8507B19_5B64_49A7_B4F7_9E25795C5B3C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include "DbConfig.h"

class CConfig  
{
public:
	virtual ~CConfig();
	static CConfig& GetInstance() {
		static CConfig config;
		return config;
	}
private:
	bool Init();
	CConfig();
	CConfig(const CConfig&) {}
	CConfig& operator=(const CConfig&) { return *this;}
	CDbConfig m_srcDBConfig;
	CDbConfig m_destDBConfig;
	CString m_strLogFileDir;
	UINT	m_ndelay;
	UINT	m_nReConnectDb;
	UINT	m_nStartDelay;
public:
	const CString& GetLogFileDir() const;
	CDbConfig& GetSrcDbConfig();
	CDbConfig& GetDestDbConfig();
	UINT GetDelay() const;
	UINT GetReConnectDb() const;
	UINT GetStartDelay() const;
};

#endif // !defined(AFX_CONFIG_H__C8507B19_5B64_49A7_B4F7_9E25795C5B3C__INCLUDED_)
