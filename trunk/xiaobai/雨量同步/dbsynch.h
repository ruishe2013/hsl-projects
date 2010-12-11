// dbsynch.h : main header file for the DBSYNCH application
//

#if !defined(AFX_DBSYNCH_H__95DBE5B6_BA0D_41D5_B998_4AB92BA9FF36__INCLUDED_)
#define AFX_DBSYNCH_H__95DBE5B6_BA0D_41D5_B998_4AB92BA9FF36__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CDbsynchApp:
// See dbsynch.cpp for the implementation of this class
//

class CDbsynchApp : public CWinApp
{
public:
	CDbsynchApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDbsynchApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CDbsynchApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DBSYNCH_H__95DBE5B6_BA0D_41D5_B998_4AB92BA9FF36__INCLUDED_)
