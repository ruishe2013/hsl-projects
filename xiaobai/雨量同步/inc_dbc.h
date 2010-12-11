#ifndef INC_DBC_H_
#define INC_DBC_H_
#pragma warning(disable: 4786)
#pragma warning(disable: 4146)	

#define USE_DLL_CLASS __declspec(dllimport)

#include <afx.h>	//for CString
#include <comdef.h> //for _variant_t
#include <map>
using std::map;
using std::pair;

class CData;
typedef map<CString, CData> String2CData;

/*
* the exception 
* author: pister
*/
class USE_DLL_CLASS CDbcException  
{
private:
	CString m_strErrMsg;
public:
	CDbcException(LPCTSTR msg = _T(""));
	virtual ~CDbcException();
	LPCTSTR GetMessage() const;
	operator LPCTSTR() const;
};

/*
* the type which encap the basic types and the string type
* you can simply use it just like the type which you were encaped
* example:
*      1.string type:
*
*      CData str = _T("hello world!");
*      AfxMessageBox(str); 
*      
*      2.int type:
*     
*      CData intData = 12;
*      int b = intData;
*
*   if you want to know which data type were encaped,
*   you can use GetDataType() function to get the type
*   test with enum CData::DATA_TYPE
*
* author: pister
* email: pisting@163.com
* last modify date: 2007-3-5
*/
class USE_DLL_CLASS CData  
{
public:
	//enum data types
		enum DATA_TYPE { NULL_TYPE = 0,
				BOOL_TYPE = 1,
				INT_TYPE = 2,
				LONG_TYPE = 3,
				FLOAT_TYPE = 4,
				DOUBLE_TYPE = 5,
				STRING_TYPE = 6,
				DATE_TYPE = 7};
	CData();
	CData(bool boolData);
	CData(int intData);
	CData(long longData);
	CData(float floatData);
	CData(double doubleData);
	CData(LPCTSTR szStrData);
	CData(const CData& data);
	CData(const CString& strdata);
	CData(const SYSTEMTIME& date);
	CData& operator=(const CData& data);
	bool operator==(const CData& data) const;
	operator bool() const;
	operator int() const;
	operator long() const;
	operator float() const;
	operator double() const;
	operator LPCTSTR() const;
	operator SYSTEMTIME() const;
	DATA_TYPE GetDataType() const;
	virtual ~CData();
private:
	union _udata{
		bool	bData;
		int		iData;
		long	lData;
		float	fData;
		double	dData;
		char*	strData;
	} m_data;
	DATA_TYPE m_dataType;
};

/*
* the database connection object
* author: pister
*/
class USE_DLL_CLASS CDbConnection  
{
public:	
	CDbConnection();
	virtual ~CDbConnection();

	//add the property data with specify column name
	void AddProperty(LPCTSTR colName, const CData& data);

	//get the property data from specify column
	CData GetProperty(LPCTSTR colName) const;

	//set the database table name for the object
	void SetTableName(LPCTSTR tableName);

	//get the database table name for the object
	CString GetTableName() const;

	//set the primery key column and the primery key key for the object
	//it will change the m_bAutoId flag to false
	//this means when using CDbc::Insert() function will use
	//this value for primery, but not auto-increase-ment
	void SetKeyAndValue(LPCTSTR keyCol, const CData& data);

	//set the auto-increase-ment flag
	//it will effect the CDbc::Insert() behave
	//if true it will use auto-increase-ment with support by database
	//if false it will use the specify key value 
	//which CDbc::SetKeyData() or CDbc::SetKeyAndValue() sets
	void SetAutoId(bool autoId = true);

	//Get the auto-increase-ment flag
	bool IsAutoId() const;

	//Set the primery key column name
	void SetIdColName(LPCTSTR col = _T("id"));

	//Get the primery key column name
	CString GetIdColName() const;

	//Get the primery key data of this object
	CData GetKeyData() const;

	//Set the primery key data of this object
	void SetKeyData(const CData& data);

	//Set saving flag for this object
	void SetSaving(bool bIsSave);

	//Get saving flag for this object
	bool IsSaving() const;

	//Get the column count which stored this object
	//but not included primery id
	int GetColumnCount() const;

	//Get the const_iterator just as map::begin()
	String2CData::const_iterator begin() const;

	//Get the const_iterator just as map::end()
	String2CData::const_iterator end() const;

	//Get the iterator just as map::begin()
	String2CData::iterator begin();

	//Get the iterator just as map::end()
	String2CData::iterator end();

	
	virtual CString GetInsertSqlString() const;
	virtual CString GetUpdateSqlString() const;
	virtual CString GetDeleteSqlString() const;
	virtual CString GetSelectSqlString() const;

	//Clear the property datas and column names which stored this object
	void Clear();

private:	//privaste method
	//Get from sql's style data from CData object
	//such as: 'hello' or 123 or 34.55 (not contain 'or')
  	CString GetDataValStr(const CData& data) const;

	//Get the sql's style string separated by commas
	//just like: name, column1, column2 
	CString GetCommaSepCols() const;

	//Get pairs column and data value with an equal symbol
	//they are separated by commas
	//just like: name = 'hello', value1 = 123, value2 = 34.55
	CString GetPairedColAndValSepByComma() const;

	//Get the sql's style string separated by commas
	//just like: 'hello', 123, 34.55 
	CString GetCommaSepVals() const;
private:	//datas
	//save the column and property
	String2CData m_colData;

	//save table name
	CString m_tableName;

	//saving or loading
	bool m_bSaving;

	//primery key column name
	CString m_keyCol;

	//primery key data
	CData m_keyData;

	//is auto generate primery key value by database
	bool m_bAutoId;
};

/*
* all database objects must inherit the class 
* author: pister
*/
class USE_DLL_CLASS CDbObject  
{
public:
	//you must override this function for storing or loading data
	virtual void DealObject(CDbConnection& dbConn);
	CDbObject();
	virtual ~CDbObject();
};



/*
* the Result of one row info
* author: pister
*/
class USE_DLL_CLASS CResult  
{
public:
	CResult();
	virtual ~CResult();
	virtual CData GetCollect(const _variant_t& index) const = 0;
};

/*
* the connection session
* since version 1.3
* author:pister
*/
class USE_DLL_CLASS CSession  
{
public:
	CSession();
	typedef void (*PTRANS_FUN)(CResult* pRes, PVOID pParam);
	//Save the object to the database
	//if the object not exist in database, use CDbc::Insert()
	//otherwise use CDbc::Update()
	//it will throw DbcException if the excepton occurs
	virtual int Save(CDbObject& ob) const = 0;
	//Insert the object to the database
	//it will throw DbcException if the excepton occurs
	virtual int Insert(CDbObject& ob) const  = 0;
	//Update the object to the database
	//it will throw DbcException if the excepton occurs
	virtual int Update(CDbObject& ob) const  = 0;
	//Delete the object from the database
	//it will throw DbcException if the excepton occurs
	virtual int Delete(CDbObject& ob) const  = 0;
	//the object must set the key value
	//it will throw DbcException if the excepton occurs
	virtual int Load(CDbObject& ob) const = 0;
	//Send a insert/update/delete sql query to the database 
	//it will throw DbcException if the excepton occurs
	virtual int ExecuteUpdate(LPCTSTR sql) const = 0;
	//execute a select sql and invoke the TRANS_FUN function
	//you can set a void* pointer to this function
	//and you will receive the void* parameter when invoke TRANS_FUN function
	//return the resultset count
	virtual int ExecuteQuery(LPCTSTR sql, PTRANS_FUN pFun, PVOID pParam) const = 0;
	//destroy this object
	virtual void Destroy() = 0;
	//begin the transact
	virtual void BeginTransact() = 0;
	//commit
	virtual void Commit() = 0;
	//rollback
	virtual void Rollback() = 0;
	virtual ~CSession();
};

/*
* the database connection
* you must get instance by CDbcFactory::GetXXXDbc() function
* author: pister
*/

class USE_DLL_CLASS CDbc 
{
public:
	typedef void (*PTRANS_FUN)(CResult* pRes, PVOID pParam);
	//Save the object to the database
	//if the object not exist in database, use CDbc::Insert()
	//otherwise use CDbc::Update()
	//it will throw DbcException if the excepton occurs
	virtual int Save(CDbObject& ob) const = 0;
	//Insert the object to the database
	//it will throw DbcException if the excepton occurs
	virtual int Insert(CDbObject& ob) const  = 0;
	//Update the object to the database
	//it will throw DbcException if the excepton occurs
	virtual int Update(CDbObject& ob) const  = 0;
	//Delete the object from the database
	//it will throw DbcException if the excepton occurs
	virtual int Delete(CDbObject& ob) const  = 0;
	//the object must set the key value
	//it will throw DbcException if the excepton occurs
	virtual int Load(CDbObject& ob) const = 0;
	//Send a insert/update/delete sql query to the database 
	//it will throw DbcException if the excepton occurs
	virtual int ExecuteUpdate(LPCTSTR sql) const = 0;
	//execute a select sql and invoke the TRANS_FUN function
	//you can set a void* pointer to this function
	//and you will receive the void* parameter when invoke TRANS_FUN function
	//return the resultset count
	virtual int ExecuteQuery(LPCTSTR sql, PTRANS_FUN pFun, PVOID pParam) const = 0;
	//create a new session
	//when you do not use it, you must delete object!
	//since version 1.3
	virtual CSession* CreateSession() const = 0;
	virtual ~CDbc();
protected:
	CDbc(LPCTSTR dbConnStr, LPCTSTR strDbUser, LPCTSTR strDbPwd);
	CString m_strDbConnStr;
	CString m_strDbUser;
	CString m_strDbPwd;
};

/*
* the factory which create CDbc object
* author: pister
*/
class USE_DLL_CLASS CDbcFactory  
{
private:
	CDbcFactory();
	virtual ~CDbcFactory();
public:
	static CDbc& GetAdoDbc(LPCTSTR strDbConnStr = "");
	static CDbc& GetAdoDbc1(LPCTSTR strDbConnStr = _T(""));
	static CDbc& GetAdoDbc2(LPCTSTR strDbConnStr = _T(""));
	static CDbc& GetAdoDbc3(LPCTSTR strDbConnStr = _T(""));
	//support use muli-databases,
	//because the vc++ 6.0 not support define static object in case: -_-!!
	//you can alse define more function to support more database support;
	/*
	static CDbc& GetAdoDbc1(LPCTSTR strDbConnStr = "") {
		static CAdoDbPool pool(strDbConnStr);
		return pool;
	}

	static CDbc& GetAdoDbc2(LPCTSTR strDbConnStr = "") {
		static CAdoDbPool pool(strDbConnStr);
		return pool;
	}
	*/
};


#endif //INC_DBC_H_
