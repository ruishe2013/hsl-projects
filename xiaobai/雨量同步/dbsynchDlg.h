// dbsynchDlg.h : header file
//

#if !defined(AFX_DBSYNCHDLG_H__9F592964_0D13_4CBA_BD45_584418650A1C__INCLUDED_)
#define AFX_DBSYNCHDLG_H__9F592964_0D13_4CBA_BD45_584418650A1C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CDbsynchDlg dialog
#include "Config.h"
#include "Logger.h"

class CDbsynchDlg : public CDialog
{
// Construction
public:
	struct CSessesAndDlg {
		CSession *m_pSession1;
		CSession *m_pSession2;
		CDbsynchDlg *m_dlg;
	};

	CDbsynchDlg(CWnd* pParent = NULL);	// standard constructor
	~CDbsynchDlg();
// Dialog Data
	//{{AFX_DATA(CDbsynchDlg)
	enum { IDD = IDD_DBSYNCH_DIALOG };
	CButton	m_btnCtrl;
	CListBox	m_listBoxInfo;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDbsynchDlg)
	public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	//{{AFX_MSG(CDbsynchDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnDoSynch();
	afx_msg void OnDbsExit();
	afx_msg void OnClose();
	virtual void OnOK();
	afx_msg void OnDbsHelpAbout();
	afx_msg void OnTimer(UINT nIDEvent);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
private:
	BOOL m_bIsStartDelay;
	CTime m_startTime;
	void doSynch();
	CMenu m_menu;
	bool m_bIsStartSyn;
	UINT m_uCurrThread;
	CCriticalSection m_sec;
	CCriticalSection m_secDealRains;

	HANDLE m_hSemaphore;

	CLogger& m_log;
	static void DealTables(const CString& sql1, const CString& sql2, const CString& sql3,CSessesAndDlg &sad);
	static UINT PASCAL ThreadProc(PVOID lpParam);
	static UINT PASCAL StopSynch(PVOID lpParam);
	void PushInfo(const CString& info);
	static void DealResult1(CResult* pRes, PVOID pParam);
	static void DealResult2(CResult* pRes, PVOID pParam);
	static void DealResult3(CResult* pRes, PVOID pParam);

	static void UpdateFailedFlag(CSessesAndDlg *pSad,
					  CSession* pSession1, 
					  LPCTSTR tableName, 
					  LPCTSTR col_flag, LPCTSTR col_id,
					  LPCTSTR id, LPCTSTR col_time, 
					  LPCTSTR strTime);
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DBSYNCHDLG_H__9F592964_0D13_4CBA_BD45_584418650A1C__INCLUDED_)
