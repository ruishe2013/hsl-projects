// dbsynchDlg.cpp : implementation file
//

#include "stdafx.h"
#include "dbsynch.h"
#include "dbsynchDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDbsynchDlg dialog

CDbsynchDlg::CDbsynchDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CDbsynchDlg::IDD, pParent), m_log(CLogger::GetInstance())
{
	//{{AFX_DATA_INIT(CDbsynchDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_bIsStartSyn = false;
	m_uCurrThread = 0;
	m_hSemaphore = 0;
	m_bIsStartDelay = FALSE;
}
CDbsynchDlg::~CDbsynchDlg() {

}

void CDbsynchDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CDbsynchDlg)
	DDX_Control(pDX, IDC_DO_SYNCH, m_btnCtrl);
	DDX_Control(pDX, IDC_LIST_INFO, m_listBoxInfo);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CDbsynchDlg, CDialog)
	//{{AFX_MSG_MAP(CDbsynchDlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_DO_SYNCH, OnDoSynch)
	ON_COMMAND(IDM_DBS_EXIT, OnDbsExit)
	ON_WM_CLOSE()
	ON_COMMAND(IDM_DBS_HELP_ABOUT, OnDbsHelpAbout)
	ON_WM_TIMER()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDbsynchDlg message handlers

BOOL CDbsynchDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here

	m_menu.LoadMenu(IDR_MENU1);
	this->SetMenu(&m_menu);

	//IDR_TOOLBAR1

	CConfig& config = CConfig::GetInstance();
	if(config.GetStartDelay() > 0 ){
		CString msg;
		msg.Format(_T("开始同步（%d秒后自动开始）"), config.GetStartDelay());
		SetDlgItemText(IDC_DO_SYNCH, msg);
		SetTimer(1, 1000, NULL);
	}
	m_startTime = CTime::GetCurrentTime();
	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CDbsynchDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CDbsynchDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CDbsynchDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}


void CDbsynchDlg::OnDoSynch() 
{
	// TODO: Add your control notification handler code here
	m_bIsStartSyn = !m_bIsStartSyn;	
	KillTimer(1);
	if(m_bIsStartSyn) {
		m_btnCtrl.SetWindowText(_T("停止同步"));
		doSynch();
	} else {
		_beginthreadex(NULL, 0, StopSynch, this, 0, 0);
		
	}
}

void CDbsynchDlg::OnDbsExit() 
{
	// TODO: Add your command handler code here
	this->SendMessage(WM_CLOSE);
}

void CDbsynchDlg::OnClose() 
{
	// TODO: Add your message handler code here and/or call default
	if(MessageBox(_T("是否确定退出!"), _T("退出"), MB_YESNO) != IDYES)
		return;
	CDialog::OnClose();
}

void CDbsynchDlg::OnOK() 
{
	// TODO: Add extra validation here
	return;
	CDialog::OnOK();
}



BOOL CDbsynchDlg::PreTranslateMessage(MSG* pMsg) 
{
	// TODO: Add your specialized code here and/or call the base class
	//VK_ESCAPE
	switch(pMsg->message) {
	case WM_KEYDOWN:
	case WM_KEYUP:
		if(LOWORD(pMsg->wParam) == VK_ESCAPE) {
			return FALSE;
		}
		break;
	default:
		break;
	}
	return CDialog::PreTranslateMessage(pMsg);
}

void CDbsynchDlg::OnDbsHelpAbout() 
{
	// TODO: Add your command handler code here
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

void CDbsynchDlg::doSynch()
{
	m_uCurrThread = _beginthreadex(NULL, 0, ThreadProc, this, 0, 0);
}

void CDbsynchDlg::PushInfo(const CString &info)
{
	const static int MAX_COUNT = 50;
	m_sec.Lock();

	if(m_listBoxInfo.GetCount() >= MAX_COUNT) {
		m_listBoxInfo.DeleteString(0);
	}
	m_listBoxInfo.InsertString(-1, info);
	m_log.Info(info);
	m_sec.Unlock();
}

UINT PASCAL CDbsynchDlg::StopSynch(PVOID lpParam) {
		CDbsynchDlg *pDlg = (CDbsynchDlg*)lpParam;	
		pDlg->m_btnCtrl.SetWindowText(_T("正在停止..."));
		pDlg->m_btnCtrl.EnableWindow(FALSE);
		if(pDlg->m_uCurrThread) {
			::WaitForSingleObject(pDlg->m_hSemaphore, INFINITE);
			::TerminateThread((void*)pDlg->m_uCurrThread, 1);
			::CloseHandle((void*)pDlg->m_uCurrThread);
		}
		pDlg->m_btnCtrl.SetWindowText(_T("开始同步"));
		pDlg->m_btnCtrl.EnableWindow(TRUE);
		return 0;
}

UINT PASCAL CDbsynchDlg::ThreadProc(PVOID lpParam)
{
	CDbsynchDlg *pDlg = (CDbsynchDlg*)lpParam;	
	CConfig& config = CConfig::GetInstance();

	CDbc& srcDbc = CDbcFactory::GetAdoDbc(config.GetSrcDbConfig().GetSqlConnStr());
	CDbc& destDbc = CDbcFactory::GetAdoDbc1(config.GetDestDbConfig().GetSqlConnStr());
	CSession *pSrcSession = NULL;
	CSession *pDestSession = NULL;
	try {
		pDlg->PushInfo(_T("正在连接源数据库..."));
		pSrcSession = srcDbc.CreateSession();
		pDlg->PushInfo(_T("源数据库连接成功."));
		pDlg->PushInfo(_T("正在连接目标数据库..."));
		pDestSession = destDbc.CreateSession();
		pDlg->PushInfo(_T("目标数据库连接成功."));

		CConfig& config = CConfig::GetInstance();
		CDbConfig& srcConfig = config.GetSrcDbConfig();
		CDbTable& srcTable1 = srcConfig.GetIdedTable(_T("table1"));
		CDbTable& srcTable2 = srcConfig.GetIdedTable(_T("table2"));
		CString sql1, sql2, sql3;
		sql1.Format(_T("select * from [%s] where [%s] = 0 "), 
			srcTable1.GetName(), srcTable1.GetColumnNameById(_T("flag")));
		sql2.Format(_T("select * from [%s] where [%s] = 0 and [%s] >0 "), 
			srcTable2.GetName(), srcTable2.GetColumnNameById(_T("flag")), 
			srcTable2.GetColumnNameById(_T("rain2")));
		sql3.Format(_T("select * from [%s] where [%s] = 0 "), 
			srcTable2.GetName(), srcTable2.GetColumnNameById(_T("flag2")));
		CSessesAndDlg cad;
		while(pDlg->m_bIsStartSyn) {
			try {
				if(pSrcSession == NULL || pDestSession == NULL)
					throw CDbcException(_T("连接失败..."));
				//这里开始处理业务关系
				if(pDlg->m_hSemaphore)
					CloseHandle(pDlg->m_hSemaphore);
				
				pDlg->m_hSemaphore = ::CreateSemaphore(NULL, 0, 1, "aaa");				
				//
				if(NULL == pDlg->m_hSemaphore) {
					AfxMessageBox(_T("创建系统资源失败:CreateSemaphore"));
					exit(1);
				}
				cad.m_dlg = pDlg;
				cad.m_pSession1 = pSrcSession;
				cad.m_pSession2 = pDestSession;				
				DealTables(sql1, sql2, sql3, cad);
				//
				::ReleaseSemaphore(pDlg->m_hSemaphore, 1, NULL);

				//处理业务关系结束
				::Sleep(config.GetDelay());
			} catch(CDbcException &e) {
				pDlg->PushInfo(e.GetMessage());
				CString reConnStr;
				reConnStr.Format(_T("%d秒之后重新连接数据库..."), config.GetReConnectDb() / 1000);
				pDlg->PushInfo(reConnStr);
				::Sleep(config.GetReConnectDb());
				try {
					pDlg->PushInfo(_T("正在重新连接数据库..."));
					pSrcSession = srcDbc.CreateSession();
					pDestSession = destDbc.CreateSession();
					pDlg->PushInfo(_T("重新连接数据库成功."));	
				} catch(CDbcException &e1) {
					pSrcSession = NULL;
					pDestSession = NULL;
					pDlg->PushInfo(e1.GetMessage());
				}
			}
		}
		if(pSrcSession)
			pSrcSession->Destroy();
		if(pDestSession)
			pDestSession->Destroy();
	} catch(CDbcException &e0) {
		pDlg->PushInfo(e0.GetMessage());
	}
	
	return 0;
}

void CDbsynchDlg::DealTables(const CString& sql1, const CString& sql2, const CString& sql3, CSessesAndDlg &sad)
{	
	try {
		sad.m_pSession1->ExecuteQuery(sql1, DealResult1, &sad);
		sad.m_pSession1->ExecuteQuery(sql2, DealResult2, &sad);
		sad.m_pSession1->ExecuteQuery(sql3, DealResult3, &sad);
	} catch(...) {
		CString errMsg(_T("未知异常3"));
		sad.m_dlg->PushInfo(errMsg);
	}
}

void CDbsynchDlg::UpdateFailedFlag(CSessesAndDlg *pSad,
					  CSession* pSession1, 
					  LPCTSTR tableName, 
					  LPCTSTR col_flag, LPCTSTR col_id,
					  LPCTSTR id, LPCTSTR col_time, 
					  LPCTSTR strTime) {
	try {
		CString updateFlagSql;
		updateFlagSql.Format(_T("update [%s] set [%s] = 2 where [%s] = '%s' and [%s] = '%s'"), 
			tableName, col_flag, col_id, id, col_time, strTime);
		pSession1->ExecuteUpdate(updateFlagSql);
	} catch(...) {
		CString errMsg(_T("未知异常2"));
		pSad->m_dlg->PushInfo(errMsg);
	}
}

void CDbsynchDlg::DealResult1(CResult *pRes, PVOID pParam)
{
	//从源数据库中获取数据
	CSessesAndDlg *pCad = (CSessesAndDlg*)pParam;
	CDbsynchDlg* pDlg = pCad->m_dlg;
	CSession* pSession1 = pCad->m_pSession1;
	CSession* pSession2 = pCad->m_pSession2;
	CConfig& config = CConfig::GetInstance();
	CDbConfig& srcConfig = config.GetSrcDbConfig();
	CDbConfig& destConfig = config.GetDestDbConfig();
	CDbTable& srcTable1 = srcConfig.GetIdedTable(_T("table1"));
	CDbTable& destTable1 =destConfig.GetIdedTable(_T("table1"));
	CDbTable& destWaterTable1 =destConfig.GetIdedTable(_T("water1"));

	LPCTSTR col_id = srcTable1.GetColumnNameById(_T("id"));
	LPCTSTR col_water = srcTable1.GetColumnNameById(_T("water"));
	LPCTSTR col_time = srcTable1.GetColumnNameById(_T("time"));
	LPCTSTR col_flag = srcTable1.GetColumnNameById(_T("flag"));

	LPCTSTR col_id_dest = destTable1.GetColumnNameById(_T("id"));
	LPCTSTR col_time_dest = destTable1.GetColumnNameById(_T("time"));
	LPCTSTR col_water_dest = destTable1.GetColumnNameById(_T("water"));

	LPCTSTR col_id_dest2 = destWaterTable1.GetColumnNameById(_T("id"));
	LPCTSTR col_time_dest2 = destWaterTable1.GetColumnNameById(_T("time"));
	LPCTSTR col_water_dest2 = destWaterTable1.GetColumnNameById(_T("water"));

	CString id = (LPCTSTR)pRes->GetCollect(col_id);
	float water = (float)pRes->GetCollect(col_water);
	SYSTEMTIME sysTime = (SYSTEMTIME)pRes->GetCollect(col_time);
	CTime time(sysTime);
	CString strTime = time.Format(_T("%Y-%m-%d %H:%M:%S"));
	
	try {
		//写到目标数据库	
		CString insertSql;
		insertSql.Format(_T("insert into [%s] ([%s], [%s], [%s]) values ('%s','%s', %.2f)"), 
			destTable1.GetName(),col_id_dest, col_time_dest, col_water_dest, id, strTime, water);
		pSession2->ExecuteUpdate(insertSql);

		CString insertSql2;
		insertSql2.Format(_T("insert into [%s] ([%s], [%s], [%s]) values ('%s','%s', %.2f)"), 
			destWaterTable1.GetName(),col_id_dest2, col_time_dest2, col_water_dest2, id, strTime, water);
		pSession2->ExecuteUpdate(insertSql2);
		//先到这里

		//把源数据库的flag设置为1		
		CString updateFlagSql;
		updateFlagSql.Format(_T("update [%s] set [%s] = 1 where [%s] = '%s' and [%s] = '%s'"), 
			srcTable1.GetName(), col_flag, col_id, id, col_time, strTime);
		pSession1->ExecuteUpdate(updateFlagSql);


		CString msg;
		msg.Format(_T("[%s]表%s为%s的记录已同步."),srcTable1.GetName(), col_id, id);
		pDlg->PushInfo(msg);
	} catch(CDbcException& e) {		
		CString errMsg;
		errMsg.Format(_T("操作失败：%s"), e.GetMessage());
		pDlg->PushInfo(errMsg);
		try {
			UpdateFailedFlag(pCad, pSession1, srcTable1.GetName(), col_flag, col_id, id, col_time, strTime);
			CString msg;
			msg.Format(_T("[%s]修改标志为2."), srcTable1.GetName());
			pDlg->PushInfo(msg);
		} catch(CDbcException& e2) {
			CString errMsg2;
			errMsg2.Format(_T("操作失败：%s"), e2.GetMessage());
			pDlg->PushInfo(errMsg2);
		}
	} catch(... ) {
		CString errMsg(_T("未知错误"));
		pDlg->PushInfo(errMsg);
		try {
			UpdateFailedFlag(pCad, pSession1, srcTable1.GetName(), col_flag, col_id, id, col_time, strTime);
			CString msg;
			msg.Format(_T("[%s]修改标志为2."), srcTable1.GetName());
			pDlg->PushInfo(msg);
		} catch(CDbcException& e2) {
			CString errMsg2;
			errMsg2.Format(_T("操作失败：%s"), e2.GetMessage());
			pDlg->PushInfo(errMsg2);
		}
	}
}

void CDbsynchDlg::DealResult2(CResult *pRes, PVOID pParam)
{
	CSessesAndDlg *pCad = (CSessesAndDlg*)pParam;
	CDbsynchDlg* pDlg = pCad->m_dlg;
	CSession* pSession1 = pCad->m_pSession1;
	CSession* pSession2 = pCad->m_pSession2;
	CConfig& config = CConfig::GetInstance();
	CDbConfig& srcConfig = config.GetSrcDbConfig();
	CDbConfig& destConfig = config.GetDestDbConfig();
	CDbTable& srcTable2 = srcConfig.GetIdedTable(_T("table2"));
	CDbTable& destTable2 = destConfig.GetIdedTable(_T("table2"));

	LPCTSTR col_id = srcTable2.GetColumnNameById(_T("id"));
	LPCTSTR col_time = srcTable2.GetColumnNameById(_T("time"));
	LPCTSTR col_rain1 = srcTable2.GetColumnNameById(_T("rain1"));
	LPCTSTR col_flag = srcTable2.GetColumnNameById(_T("flag"));

	LPCTSTR col_id_dest2 = destTable2.GetColumnNameById(_T("id"));
	LPCTSTR col_time_dest2 = destTable2.GetColumnNameById(_T("time"));
	LPCTSTR col_rain1_dest2 = destTable2.GetColumnNameById(_T("rain1"));

	CString id = (LPCTSTR)pRes->GetCollect(col_id);
	float rain1 = (float)pRes->GetCollect(col_rain1);
	SYSTEMTIME sysTime = (SYSTEMTIME)pRes->GetCollect(col_time);
	CTime time(sysTime);
	CString strTime = time.Format(_T("%Y-%m-%d %H:%M:%S"));

	try {
		//写到目标数据库	
		CString insertSql2;
		
		insertSql2.Format(_T("insert into [%s] ([%s], [%s], [%s]) values ('%s','%s', %.1f)"), 
			destTable2.GetName(), col_id_dest2, col_time_dest2, col_rain1_dest2, id, strTime, rain1);
		pSession2->ExecuteUpdate(insertSql2);

		//把源数据库的flag设置为1		
		CString updateFlagSql;
		updateFlagSql.Format(_T("update [%s] set [%s] = 1 where [%s] = '%s' and [%s] = '%s'"), 
			srcTable2.GetName(), col_flag, col_id, id, col_time, strTime);
		pSession1->ExecuteUpdate(updateFlagSql);
		CString msg;
		msg.Format(_T("[%s]表%s为%s的记录已同步."), srcTable2.GetName(), col_id, id);
		pDlg->PushInfo(msg);
	} catch(CDbcException& e) {
		CString errMsg;
		errMsg.Format(_T("操作失败：%s"), e.GetMessage());
		pDlg->PushInfo(errMsg);
		try {
			UpdateFailedFlag(pCad, pSession1, srcTable2.GetName(), col_flag, col_id, id, col_time, strTime);
			CString msg;
			msg.Format(_T("[%s]修改标志为2."), srcTable2.GetName());
			pDlg->PushInfo(msg);
		} catch(CDbcException& e2) {
			CString errMsg2;
			errMsg2.Format(_T("操作失败：%s"), e2.GetMessage());
			pDlg->PushInfo(errMsg2);
		}
	} catch(...) {
		CString errMsg(_T("未知错误"));
		pDlg->PushInfo(errMsg);
		try {
			UpdateFailedFlag(pCad, pSession1, srcTable2.GetName(), col_flag, col_id, id, col_time, strTime);
			CString msg;
			msg.Format(_T("[%s]修改标志为2."), srcTable2.GetName());
			pDlg->PushInfo(msg);
		} catch(CDbcException& e2) {
			CString errMsg2;
			errMsg2.Format(_T("操作失败：%s"), e2.GetMessage());
			pDlg->PushInfo(errMsg2);
		}
	}
}


void CDbsynchDlg::DealResult3(CResult *pRes, PVOID pParam) {
	CSessesAndDlg *pCad = (CSessesAndDlg*)pParam;
	CDbsynchDlg* pDlg = pCad->m_dlg;
	CSession* pSession1 = pCad->m_pSession1;
	CSession* pSession2 = pCad->m_pSession2;
	CConfig& config = CConfig::GetInstance();
	CDbConfig& srcConfig = config.GetSrcDbConfig();
	CDbConfig& destConfig = config.GetDestDbConfig();
	CDbTable& srcTable2 = srcConfig.GetIdedTable(_T("table2"));
	CDbTable& destTable3 = destConfig.GetIdedTable(_T("table3"));

	LPCTSTR col_id = srcTable2.GetColumnNameById(_T("id"));
	LPCTSTR col_time = srcTable2.GetColumnNameById(_T("time"));
	LPCTSTR col_rain2 = srcTable2.GetColumnNameById(_T("rain2"));
	LPCTSTR col_flag2 = srcTable2.GetColumnNameById(_T("flag2"));

	LPCTSTR col_id_dest3 = destTable3.GetColumnNameById(_T("id"));
	LPCTSTR col_time_dest3 = destTable3.GetColumnNameById(_T("time"));
	LPCTSTR col_rain3_dest3 = destTable3.GetColumnNameById(_T("rain3"));

	CString id = (LPCTSTR)pRes->GetCollect(col_id);
	float rain2 = (float)pRes->GetCollect(col_rain2);

	SYSTEMTIME sysTime = (SYSTEMTIME)pRes->GetCollect(col_time);
	CTime time(sysTime);
	CTime oldTime(time);	
	static CTimeSpan oneSecond(0, 0, 0, 1);
	static CTimeSpan oneHour(0, 1, 0, 0);
	time -= oneSecond;
	CTime afterHour(time);
	afterHour += oneHour;
	CString strOldTime = oldTime.Format(_T("%Y-%m-%d %H:%M:%S"));
	CString strDate = afterHour.Format(_T("%Y-%m-%d %H:00:00"));
	try {
		//写到目标数据库

		if(rain2 > 0 ) {
			CString updateSql1;
			updateSql1.Format(_T("update [%s] set [%s] = [%s] + %.1f where [%s] = '%s' and [%s] = '%s'"),
				destTable3.GetName(), col_rain3_dest3, col_rain3_dest3, rain2, col_id_dest3, id,
													col_time_dest3, strDate);

			if(pSession2->ExecuteUpdate(updateSql1) < 1) {
				CString insertSql2;		
				insertSql2.Format(_T("insert into [%s] ([%s], [%s], [%s]) values ('%s','%s', %.1f)"), 
					destTable3.GetName(), col_id_dest3, col_time_dest3, col_rain3_dest3,
						id, strDate, rain2);
				pSession2->ExecuteUpdate(insertSql2);
			}
		}
		//把源数据库的flag设置为1		
		CString updateFlagSql;
		updateFlagSql.Format(_T("update [%s] set [%s] = 1 where [%s] = '%s' and [%s] = '%s'"), 
			srcTable2.GetName(), col_flag2, col_id, id, col_time, strOldTime);
		pSession1->ExecuteUpdate(updateFlagSql);

		CString msg;
		msg.Format(_T("[%s]表%s为%s的记录已同步."), srcTable2.GetName(), col_id, id);
		pDlg->PushInfo(msg);
	} catch(CDbcException& e) {
		CString errMsg;
		errMsg.Format(_T("操作失败：%s"), e.GetMessage());
		pDlg->PushInfo(errMsg);
	} catch(...) {
		CString errMsg(_T("未知错误"));
		pDlg->PushInfo(errMsg);
	}
	
}



void CDbsynchDlg::OnTimer(UINT nIDEvent) 
{
	// TODO: Add your message handler code here and/or call default
		CConfig& config = CConfig::GetInstance();
	static CTimeSpan span(0,0,0, config.GetDelay());
	if(m_bIsStartDelay) {
		KillTimer(1);		
		return;
	}
	CTime currTime = CTime::GetCurrentTime();
	//
	int leftTime = config.GetStartDelay() - (currTime - m_startTime).GetSeconds();
	CString msg;
	msg.Format(_T("开始同步（%d秒后自动开始）"), leftTime);
	SetDlgItemText(IDC_DO_SYNCH, msg);
	switch(nIDEvent) {
	case 1:
	{
		if(leftTime <= 0) {
			//start
			OnDoSynch();
			m_bIsStartDelay = TRUE;
		}		 
	}
	break;
	default:
		break;
	}	
	CDialog::OnTimer(nIDEvent);
}
