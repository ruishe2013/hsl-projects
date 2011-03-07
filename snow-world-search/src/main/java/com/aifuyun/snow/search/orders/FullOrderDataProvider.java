package com.aifuyun.snow.search.orders;

public class FullOrderDataProvider extends BaseDataProvider {

	@Override
	protected String getSqlCondition() {
		return " deleted = 0 ";
	}

	public static void main(String[] args) {
		FullOrderDataProvider dp = new FullOrderDataProvider();
		dp.init();
		while(dp.hasNext()) {
			System.out.println(dp.next());
		}
		dp.close();
	}
	
}
