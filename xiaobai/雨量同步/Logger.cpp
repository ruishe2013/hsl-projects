// Logger.cpp: implementation of the CLogger class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "dbsynch.h"
#include "Logger.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CLogger::CLogger()
{
	m_pLogObject = 0;
	init();
}

CLogger::~CLogger()
{
	CloseLog(m_pLogObject);
}

void CLogger::init()
{
	TCHAR logFile[512] = {0};
	_tcscpy(logFile, CConfig::GetInstance().GetLogFileDir().operator LPCTSTR());
	LOG_APPENDER_OPT opt;
	opt.szFileName = logFile;
	opt.fileLogType = CHANGE_FILE_WHILE_MAX_SIZE;
	opt.uMaxSize = 5 * 1024;	//5 * 1024 kbyte
	CreateLog(&m_pLogObject,  APPENDER_TYPE_FILE, &opt, NULL);
	SetLogLevel(m_pLogObject, LOG_LEVEL_ERROR);
}

void CLogger::Info(const CString &info)
{
	::LogInfo(m_pLogObject, info);
}


void CLogger::Debug(const CString &debug)
{
	::LogDebug(m_pLogObject, debug);
}

void CLogger::Error(const CString &error)
{
	::LogError(m_pLogObject, error);
}
