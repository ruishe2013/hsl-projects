// DbTable.cpp: implementation of the CDbTable class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "dbsynch.h"
#include "DbTable.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CDbTable::CDbTable()
{

}

CDbTable::~CDbTable()
{

}

CDbTable::CDbTable(const CString& name)
	: m_strName(name)
{
	
}

const CString& CDbTable::GetColumnNameById(const CString& id) {
	return m_ctrColumns[id].m_strName;
}

void CDbTable::AddColumn(const CDbColumn& col) {
	m_ctrColumns[col.m_strId] = col;
}

void CDbTable::SetName(const CString &name)
{
	this->m_strName = name;
}

const CString& CDbTable::GetName() const
{
	return this->m_strName;
}

//DEL void CDbTable::AddTable(const CDbTable &table)
//DEL {
//DEL 	
//DEL }

//DEL const CDbTable& CDbTable::GetNamedTable(const CString &name)
//DEL {
//DEL 
//DEL }
