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
}
