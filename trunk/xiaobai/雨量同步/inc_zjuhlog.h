/* 
	Copyright (c) 2006-2007  bbs.zjuh.com
	zjuh_log library: the library of logging
	You can use this code freely!
	but you must leave these comments here!
	author: pister
	email: pisting@163.com
	QQ: 54025853
	http://bbs.zjuh.com
*/

#ifndef INC_ZJUHLOG_
#define INC_ZJUHLOG_
#include <windows.h>

#ifdef ZJUH_GENERATE
#define EXT_FUN 
#else
#define EXT_FUN __declspec(dllimport)
#endif

//#define CALL_TYPE
//if you want to use stdcall, 
//use the follow macro, not above

#define CALL_TYPE __stdcall


#define FUN_PRE EXT_FUN CALL_TYPE

#ifndef __cplusplus
#define		EXTERN_TYPE 
#else
#define		EXTERN_TYPE extern "C"
#endif

typedef enum tag_ZJUH_LOG_ERROR_CODE {
	INVALID_LOG_OBJECT			= -1,					/*object is invalid*/
	INVALID_FILE_APPENDER_OPT	= -2,					/*file append option is invalid*/
	INVALID_FILE_LOG_TYPE		= -3,					/*log file type is invalid*/
	INVALID_FILE_READ_WRITE		= -4					/*file can not read or write*/
}ZJUH_LOG_ERROR_CODE;

/*log appender types*/
typedef enum tag_LOG_APPENDER_TYPE {
	APPENDER_TYPE_CONSOLE		= 1,		/*for console*/
	APPENDER_TYPE_FILE			= 2			/*for file */
}LOG_APPENDER_TYPE;

/*log level*/
typedef enum tag_LOG_LEVEL {
	LOG_LEVEL_INFO				= 1,		/*info level*/
	LOG_LEVEL_DEBUG				= 2,		/*debug level*/
	LOG_LEVEL_ERROR				= 3			/*error level*/
}LOG_LEVEL;

typedef enum tag_FILE_LOG_TYPE {
	/*when the file reach the size, then create new file*/
	CHANGE_FILE_WHILE_MAX_SIZE	= 1,		
	/*every day create a new file to log */
	CHANGE_FILE_EVERY_DAY		= 2
}FILE_LOG_TYPE;

typedef struct tag_LOG_FORMAT {
	void* pFmt;						/*the field reserved*/
} LOG_FORMAT;

typedef struct tag_FILE_APPENDER_OPT {
	/*
	File name to log:
		if fileLogType is CHANGE_FILE_EVERY_DAY
		the log-file-name will be like as c:\aa\abc.txt -> c:\aa\abc2007_3_27.txt
		if fileLogType is CHANGE_FILE_WHILE_MAX_SIZE
		the log-file-name will add two chars '_' and '0' append the filename but before the ext-name
		when the log file reach the max-size
		the log-file-name will be increase 1 append the filename but before the ext-name
		such as:
			c:\aa\abc_0.txt
			c:\aa\abc_1.txt
			c:\aa\abc_2.txt
			...
	*/
	LPTSTR				szFileName;	
	/*
		File log type: CHANGE_FILE_WHILE_MAX_SIZE or CHANGE_FILE_EVERY_DAY
	*/
	FILE_LOG_TYPE		fileLogType;
	/*
		max file size: if fileLogType is CHANGE_FILE_EVERY_DAY,
		this field will be ignored
		unit: Kbyte
	*/
	UINT				uMaxSize;
}LOG_APPENDER_OPT;
/*
typedef struct tag_LOG_APPENDER {
	LOG_APPENDER_TYPE	appendType;		//	/*append type*/
//	LOG_LEVEL			logLevel;	//		/*log level */
//}LOG_APPENDER;
//*/
typedef unsigned int LOG_OBJECT;

/*
	Create the LOG_OBJECT
	
	the default log levle is INFO

	you can change the level with function SetLogLevel
	  
	once you create the LOG_OBJECT, you must call CloseLog
	when you do not use it never

	

	parameters:
		ppLogObject - the LOG_OBJECT pointer you want to create
		appenderTypes - APPENDER_TYPE_CONSOLE or APPENDER_TYPE_FILE
						you can use '|' to use more than one 
		pFileAppenderOpt - the pointer of LOG_APPENDER_OPT
							if APPENDER_TYPE_FILE flag was not set
							the function will ignore this parameter
	return value:
		if call successful return zero
		if call failed return none-zero code cantains error

	example:

		LOG_OBJECT *pLogObject = NULL;
		if(CreateLog(&pLogObject) != 0) {
			//error occurs
			return -1;
		}
		//do some operator with LOG_OBJECT

		CloseLog(pLogObject);

*/
EXTERN_TYPE int FUN_PRE CreateLog(LOG_OBJECT **ppLogObject,
								  int appenderTypes,
								  const LOG_APPENDER_OPT *pFileAppenderOpt,
								  LOG_FORMAT *pLogFmt);

/*
	Close the LOG_OBJECT

	parameters:
		pLogObject - the LOG_OBJECT you want to close
	return value:
		if call successful return zero
		if call failed return none-zero code cantains error
*/
EXTERN_TYPE int FUN_PRE CloseLog(LOG_OBJECT *pLogObject);

/*
	Log the info-level to this log object

	parameters:
		pLogObject - the  the LOG_OBJECT you want log info with
		szInfo - zero-end char or wchar array string
	return value:
		if call successful return zero
		if call failed return none-zero code cantains error
*/
EXTERN_TYPE int FUN_PRE LogInfo(LOG_OBJECT *pLogObject, LPCTSTR szInfo);

/*
	Log the error-level to this log object

	parameters:
		pLogObject - the  the LOG_OBJECT you want log error with
		szError - zero-end char or wchar array string
	return value:
		if call successful return zero
		if call failed return none-zero code cantains error
*/
EXTERN_TYPE int FUN_PRE LogError(LOG_OBJECT *pLogObject, LPCTSTR szError);

/*
	Log the debug-level to this log object

	parameters:
		pLogObject - the  the LOG_OBJECT you want log debug with
		szDebug - zero-end char or wchar array string
	return value:
		if call successful return zero
		if call failed return none-zero code cantains error
*/
EXTERN_TYPE int FUN_PRE LogDebug(LOG_OBJECT *pLogObject, LPCTSTR szDebug);

/*
	Set the log-object's level
	parameters:
		pLogObject - the  the LOG_OBJECT you want log debug with
		lvl - new level
	return value:
		if it is a negative value, the called is failed
			and this value is the error value
		else called successful , the value is the old level		
*/
EXTERN_TYPE int FUN_PRE SetLogLevel(LOG_OBJECT *pLogObject, LOG_LEVEL lvl);


#endif