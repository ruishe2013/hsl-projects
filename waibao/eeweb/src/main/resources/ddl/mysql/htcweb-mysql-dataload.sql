use htcweb;

-- 正式数据
insert into tuser(name,password,power,useless,placeAStr,placeBStr,personStr) values('htc_htc_htc','htc',0,1,'0','0','');
insert into tuser(name,password,power,useless,placeAStr,placeBStr,personStr) values('admin','admin',2,1,'0','0','');

insert into tpower(powerId,powerName) values(1,'管理员');
insert into tpower(powerId,powerName) values(2,'高级管理员');

insert into tequitype (tyepId,typename) values(1,'温湿度');
insert into tequitype (tyepId,typename) values(2,'单温度');
insert into tequitype (tyepId,typename) values(3,'单湿度');

--tworkplace useless定义: 2:是"未定义"使用的,是系统默认的,不显示在CRUD界面,但是会出现在仪器添加选项中.
--1:表示这个用户正在使用 0:表示这个账户已经被删掉了
insert into tworkplace(placeName,useless) values('未定义',2);

--open_short_message(短信报警	默认关闭=1)
--open_pcsound			(声卡报警	默认开启=2)
--open_access_store	(药监信息	默认关闭=1)
insert into tsysparam(argsKey, argsValue) values('company_name','公司名称'); 
insert into tsysparam(argsKey, argsValue) values('alarm_playfile','alarmHtc1.mp3'); 
insert into tsysparam(argsKey, argsValue) values('open_short_message','1'); 
insert into tsysparam(argsKey, argsValue) values('open_pcsound','2'); 
insert into tsysparam(argsKey, argsValue) values('data_flashtime','20'); 
insert into tsysparam(argsKey, argsValue) values('high_colordef','#ff0000'); 
insert into tsysparam(argsKey, argsValue) values('normal_colordef','#0000ff'); 
insert into tsysparam(argsKey, argsValue) values('low_colordef','#ff00ff'); 
insert into tsysparam(argsKey, argsValue) values('y_axes_addvalue','5'); 
insert into tsysparam(argsKey, argsValue) values('temp_show_type','1'); 
insert into tsysparam(argsKey, argsValue) values('max_select_count_bar','50'); 
insert into tsysparam(argsKey, argsValue) values('max_select_count_line','5'); 
insert into tsysparam(argsKey, argsValue) values('max_select_count_table','5'); 
insert into tsysparam(argsKey, argsValue) values('max_select_count_flash','5'); 
insert into tsysparam(argsKey, argsValue) values('max_select_count_alarm','50'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_line','100'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_table_now','2000'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_table_day','1000'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_table_month','60'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_flash_now','2000'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_flash_day','1000'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_flash_month','60'); 
insert into tsysparam(argsKey, argsValue) values('max_rs_count_alarm','100'); 
insert into tsysparam(argsKey, argsValue) values('baudrate_number','9600'); 
insert into tsysparam(argsKey, argsValue) values('backup_path','d:/eefield/backup'); 
insert into tsysparam(argsKey, argsValue) values('open_access_store','1'); 
insert into tsysparam(argsKey, argsValue) values('seri_retry_time','5'); 
insert into tsysparam(argsKey, argsValue) values('seri_addition_time','100');
insert into tsysparam(argsKey, argsValue) values('sms_center_number','13800571500'); 
insert into tsysparam(argsKey, argsValue) values('sms_store_type','2'); 