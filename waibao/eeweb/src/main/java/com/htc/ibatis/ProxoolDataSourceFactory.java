package com.htc.ibatis;

import java.util.Map;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.ibatis.sqlmap.engine.datasource.DataSourceFactory;

public class ProxoolDataSourceFactory implements DataSourceFactory {

    /**  
     * ʹ��ProxoolDataSource����Դ  
     */  
    private ProxoolDataSource dataSource; 	
	
	public DataSource getDataSource() {
		return dataSource;
	}

	@SuppressWarnings("unchecked")
	public void initialize(Map map) {
        dataSource = new ProxoolDataSource();   
        dataSource.setDriver((String)map.get("driver"));   
        dataSource.setDriverUrl((String)map.get("driverUrl"));   
        dataSource.setUser((String)map.get("user"));   
        dataSource.setPassword((String)map.get("password"));   
        dataSource.setAlias("alias");
        
        //house-keeping-sleep-time:
        //�Զ�����������״̬��ʱ����(����),��쵽���е����Ӿ����ϻ���,��ʱ�����٣�Ĭ��ֵΪ30��
        dataSource.setHouseKeepingTestSql((String)map.get("housekeepingsleeptime"));
        //maximum-active-time:
        //�߳������ʱ�䣬������ʱ����߳̽����ػ��߳�kill����Ĭ��ֵΪ5����
        dataSource.setMaximumActiveTime(Long.valueOf((String)map.get("maximumActiveTime")));
        //maximum-connection-count:
        //�����ݿ�������������������������ӣ���������ʱ�������ڶ����еȺ����ĵȴ���������simultaneous-build-throttle������Ĭ��ֵΪ15
        dataSource.setMaximumConnectionCount(Integer.valueOf((String)map.get("maximumConnectionCount")));
        //maximum-connection-lifetime:
        //���������ʱ�䣬����Ϊ��λ��Ĭ��ֵΪ4Сʱ
        dataSource.setMaximumConnectionLifetime((Integer.valueOf((String)map.get("maximumConnectionLifetime")) )*1000*3600);
        //minimum-connection-count:
        //�����Ƿ�ʹ�ö����ֿ��ŵ���С��������Ĭ��ֵΪ5
        dataSource.setMinimumConnectionCount(Integer.valueOf( (String) map.get("minimumConnectionCount")) );
        //overload-without-refusal-lifetime:
        //�����ж����ӳ�״̬������ڴ�ѡ������ʱ���ڣ�����Ϊ��λ���ܾ������ӣ�����Ϊ�����ء�Ĭ��ֵΪ60��
        dataSource.setOverloadWithoutRefusalLifetime(Integer.valueOf((String) map.get("overloadWithoutRefusalLifetime"))*1000);
        //prototype-count:
        //���ٱ��ֵĿ�����������ע����minimum-connection-count���֡�Ĭ��ֵΪ0
        dataSource.setPrototypeCount(Integer.valueOf((String)map.get("prototypeCount")));
        //simultaneous-build-throttle:  
        //�������ǿ�һ�ν�����������������Ǿ�����������������,����û�пɹ�ʹ�õ����ӡ��������ӿ���ʹ�ö��߳�,
        //�����޵�ʱ��֮�佨����ϵ�Ӷ������������ӣ�����������Ҫͨ��һЩ��ʽȷ��һЩ�̲߳�����������Ӧ��������ģ�Ĭ����10
        dataSource.setSimultaneousBuildThrottle(Integer.valueOf((String) map.get("simultaneousBuildThrottle")));
        //statistics:  ���ӳ�ʹ��״��ͳ�ơ� ������10s,1m,1d��
        dataSource.setStatistics((String)map.get("statistics"));
        
	}

}
