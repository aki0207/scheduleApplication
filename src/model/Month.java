package model;

import java.util.Calendar;

import sun.text.IntHashtable;

public class Month {

	public int year;
	public int month;
	public Calendar cal = Calendar.getInstance();
	public int count;

	// パラメータの年度確認
	public int yearParameterCheck(String year_parameter) {

		if (year_parameter == null || year_parameter.length() == 0) {

			year = -999;

		} else {

			try {

				year = Integer.parseInt(year_parameter);

			} catch (NumberFormatException e) {

				year = -999;

			}
		}

		return year;
	}

	
	//月バージョン
	public int monthParameterChaeck(String month_parameter) {

		if (month_parameter == null || month_parameter.length() == 0) {
			
			month = -999;

		} else {
			
			try {
				
				month = Integer.parseInt(month_parameter);
				
			} catch (NumberFormatException e) {
				
				month = -999;
				
			}
		}
		
		return month;

	}
	
	 
	
	
	

}
