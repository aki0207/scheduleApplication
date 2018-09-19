package model;

import java.util.Calendar;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import sun.text.IntHashtable;

public class Month {

	public int year;
	public int month;
	public Calendar cal = Calendar.getInstance();
	public int count;
	public int return_int_value;
	public String return_string_value;

	// パラメータの確認int型ver
	public int idParameterCheck(String id_parameter) {
		
		int return_id;

		if (id_parameter == null || id_parameter.length() == 0) {

			return_id = -999;

		} else {

			try {

				return_id = Integer.parseInt(id_parameter);

			} catch (NumberFormatException e) {

				return_id = -999;

			}
		}

		return return_id;
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

	public int yearParameterCheck(String year_parameter) {

		if (year_parameter == null || year_parameter.length() == 0) {

			return_int_value = -999;

		} else {

			try {

				// 変換に失敗すれば数字じゃない
				return_int_value = Integer.parseInt(year_parameter);
				// 21世紀の範囲内のみ有効とする(2000年~2100年まで)
				if (return_int_value < 2000 || return_int_value > 2100) {

					return_int_value = -999;
				}

			} catch (NumberFormatException e) {

				return_int_value = -999;

			}
		}

		return return_int_value;

	}

	public int monthParameterCheck(String month_parameter) {

		if (month_parameter == null || month_parameter.length() == 0) {

			return_int_value = -999;

		} else {

			try {

				// 変換に失敗すれば数字じゃない
				return_int_value = Integer.parseInt(month_parameter);
				// 0は12月,13は1月としているがその範囲を越えた数値は認めない
				if (return_int_value < 0 || return_int_value > 13) {

					return_int_value = -999;
				}

			} catch (NumberFormatException e) {

				return_int_value = -999;

			}
		}

		return return_int_value;

	}

	public int dayParameterCheck(int year_parameter, int month_parameter, String day_parameter) {

		if (day_parameter == null || day_parameter.length() == 0) {

			return_int_value = -999;

		} else {

			try {

				// 変換に失敗すれば数字じゃない
				return_int_value = Integer.parseInt(day_parameter);

				// 当月の最終日を取得
				Calendar theDay = Calendar.getInstance();
				theDay.clear();
				theDay.set(year_parameter, month_parameter - 1, 1);
				int end_of_month_day;
				end_of_month_day = theDay.getActualMaximum(Calendar.DATE);

				// 0日以下or月末日越えてたらおかしい
				if (return_int_value < 1 || return_int_value > end_of_month_day) {

					return_int_value = -999;
				}

			} catch (NumberFormatException e) {

				return_int_value = -999;

			}
		}

		return return_int_value;

	}

	public String totaleTimeParameterCheck(String totale_time_parameter) {

		// 型確認用
		int mold_check = 0;

		// 11桁固定
		if (totale_time_parameter == null || totale_time_parameter.length() != 11) {

			totale_time_parameter = "";

			// 本来なら指定の位置に記号があるはず
		} else if (!(totale_time_parameter.substring(2, 3).equals(":"))
				|| (!totale_time_parameter.substring(5, 6).equals("-"))
				|| (!totale_time_parameter.substring(8, 9).equals(":"))) {

			totale_time_parameter = "";

		} else {

			try {

				// 片っ端から変換。変換に失敗すれば数字じゃない
				for (int i = 0; i <= 10; i++) {

					// 記号のところは除く
					if (i == 2 || i == 5 || i == 8) {
						continue;
					}

					mold_check = Integer.parseInt(totale_time_parameter.substring(i, i + 1));
					System.out.println(mold_check);
				}
			} catch (NumberFormatException e) {

				totale_time_parameter = "";

			}
		}

		return totale_time_parameter;
	}

	public String indexNumberParameterCheck(String index_number_parameter) {

		if (index_number_parameter == null || index_number_parameter.length() == 0) {

			index_number_parameter = "";

		} else {

			try {

				// 変換に失敗すれば数字じゃない
				return_int_value = Integer.parseInt(index_number_parameter);

				// 配列の大きさは24
				if (return_int_value < 0 || return_int_value > 23) {

					index_number_parameter = "";

				}

			} catch (NumberFormatException e) {

				index_number_parameter = "";

			}
		}

		return index_number_parameter;
	}
}
