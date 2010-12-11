// DbColumn.cpp: implementation of the CDbColumn class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "dbsynch.h"
#include "DbColumn.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////


CDbColumn::CDbColumn(const CString& strId, const CString& strName)
	: m_strId(strId), m_strName(strName) 
{
}

CDbColumn::CDbColumn()
{

}

CDbColumn::~CDbColumn()
{

}
