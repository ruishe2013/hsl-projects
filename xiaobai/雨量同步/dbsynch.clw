; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CDbsynchDlg
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "dbsynch.h"

ClassCount=3
Class1=CDbsynchApp
Class2=CDbsynchDlg
Class3=CAboutDlg

ResourceCount=4
Resource1=IDD_DBSYNCH_DIALOG
Resource2=IDR_MAINFRAME
Resource3=IDD_ABOUTBOX
Resource4=IDR_MENU1

[CLS:CDbsynchApp]
Type=0
HeaderFile=dbsynch.h
ImplementationFile=dbsynch.cpp
Filter=N

[CLS:CDbsynchDlg]
Type=0
HeaderFile=dbsynchDlg.h
ImplementationFile=dbsynchDlg.cpp
Filter=D
BaseClass=CDialog
VirtualFilter=dWC
LastObject=IDC_DO_SYNCH

[CLS:CAboutDlg]
Type=0
HeaderFile=dbsynchDlg.h
ImplementationFile=dbsynchDlg.cpp
Filter=D

[DLG:IDD_ABOUTBOX]
Type=1
Class=CAboutDlg
ControlCount=5
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308480
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373889
Control5=IDC_STATIC,static,1342308352

[DLG:IDD_DBSYNCH_DIALOG]
Type=1
Class=CDbsynchDlg
ControlCount=2
Control1=IDC_DO_SYNCH,button,1342242816
Control2=IDC_LIST_INFO,listbox,1353777417

[MNU:IDR_MENU1]
Type=1
Class=?
Command1=IDM_DBS_EXIT
Command2=IDM_DBS_HELP_ABOUT
CommandCount=2

