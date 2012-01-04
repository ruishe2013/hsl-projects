package com.htc.ibatis;

import java.util.Map;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.ibatis.sqlmap.engine.datasource.DataSourceFactory;

public class ProxoolDataSourceFactory implements DataSourceFactory {

    /**  
     * 使用ProxoolDataSource数据源  
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
        //自动侦察各个连接状态的时间间隔(毫秒),侦察到空闲的连接就马上回收,超时的销毁，默认值为30秒
        dataSource.setHouseKeepingTestSql((String)map.get("housekeepingsleeptime"));
        //maximum-active-time:
        //线程最大存活时间，超过此时间的线程将被守护线程kill掉，默认值为5分钟
        dataSource.setMaximumActiveTime(Long.valueOf((String)map.get("maximumActiveTime")));
        //maximum-connection-count:
        //到数据库的最大连接数，超过了这个连接，再有请求时，就排在队列中等候，最大的等待请求数由simultaneous-build-throttle决定；默认值为15
        dataSource.setMaximumConnectionCount(Integer.valueOf((String)map.get("maximumConnectionCount")));
        //maximum-connection-lifetime:
        //连接最大存活时间，毫秒为单位，默认值为4小时
        dataSource.setMaximumConnectionLifetime((Integer.valueOf((String)map.get("maximumConnectionLifetime")) )*1000*3600);
        //minimum-connection-count:
        //不管是否被使用都保持开放的最小连接数，默认值为5
        dataSource.setMinimumConnectionCount(Integer.valueOf( (String) map.get("minimumConnectionCount")) );
        //overload-without-refusal-lifetime:
        //用来判断连接池状态，如果在此选项设置时间内（毫秒为单位）拒绝了连接，则认为过负载。默认值为60秒
        dataSource.setOverloadWithoutRefusalLifetime(Integer.valueOf((String) map.get("overloadWithoutRefusalLifetime"))*1000);
        //prototype-count:
        //最少保持的空闲连接数，注意与minimum-connection-count区分。默认值为0
        dataSource.setPrototypeCount(Integer.valueOf((String)map.get("prototypeCount")));
        //simultaneous-build-throttle:  
        //这是我们可一次建立的最大连接数。那就是新增的连接请求,但还没有可供使用的连接。由于连接可以使用多线程,
        //在有限的时间之间建立联系从而带来可用连接，但是我们需要通过一些方式确认一些线程并不是立即响应连接请求的，默认是10
        dataSource.setSimultaneousBuildThrottle(Integer.valueOf((String) map.get("simultaneousBuildThrottle")));
        //statistics:  连接池使用状况统计。 参数“10s,1m,1d”
        dataSource.setStatistics((String)map.get("statistics"));
        
	}

}
