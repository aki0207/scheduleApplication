package model;

import java.util.Calendar;

import sun.text.IntHashtable;

public class Month {

	public int year;
	public int month;
	public Calendar cal = Calendar.getInstance();
	public int count;
	public int return_int_value;
	public String return_string_value;

	// パラメータの確認int型ver
	public int intParameterCheck(String parameter) {

		if (parameter == null || parameter.length() == 0) {

			return_int_value = -999;

		} else {

			try {

				return_int_value = Integer.parseInt(parameter);

			} catch (NumberFormatException e) {

				return_int_value = -999;

			}
		}

		return return_int_value;
	}

	public String stringParameterCheck(String parameter) {

		if (parameter == null || parameter.length() == 0) {

			return_string_value = "";

		} else {

			try {

				return_string_value = parameter;

			} catch (NumberFormatException e) {

				return_string_value = "";

			}
		}

		return return_string_value;
	}
	
	public int dayParameterCheck (int year_parameter,int month_parameter,String day_parameter) {
		
		if (day_parameter == null || day_parameter.length() == 0) {

			return_int_value = -999;

		} else {

			try {

				//変換に失敗すれば数字じゃない
				return_int_value = Integer.parseInt(day_parameter);
				
				
				//当月の最終日を取得
				Calendar theDay = Calendar.getInstance();
				theDay.clear();
				theDay.set(year_parameter,month_parameter - 1,1);
				int end_of_month_day;
				end_of_month_day = theDay.getActualMaximum(Calendar.DATE);
				System.out.println("月末は" + end_of_month_day);
				
				//0日以下or月末日越えてたらおかしい
				if (return_int_value < 1 || return_int_value > end_of_month_day) {
					
					return_int_value = -999;
				}
				

			} catch (NumberFormatException e) {

				return_int_value = -999;

			} 
		}

		return return_int_value;
		
		
		
	}
}
