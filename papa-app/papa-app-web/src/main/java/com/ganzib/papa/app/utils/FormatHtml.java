package com.ganzib.papa.app.utils;


import java.math.BigDecimal;

/**
 * 用户页面调用
 * 格式化日期为几分钟前，几小时前~~~~
 */

public class FormatHtml {
	/*public String formatTime(String dateStr){
		try{
			return DateUtils.dateDiff(DateUtils.DatePattern.PATTERN_ALL_NOSECOND.getDateStr(DateUtils.DatePattern.PATTERN_ALL.getDate(dateStr)), 
					DateUtils.DatePattern.PATTERN_ALL_NOSECOND.getDateStr(new Date()));
		}catch(Exception e){
			return dateStr;
		}
	}
	
	public String formatTime(Date date){
		return DateUtils.dateDiff(DateUtils.DatePattern.PATTERN_ALL_NOSECOND.getDateStr(date),
				DateUtils.DatePattern.PATTERN_ALL_NOSECOND.getDateStr(new Date()));
	}*/

    /**
     * 四舍五入
     */
    public String formatFloat(Double num) {
        return String.format("%.3f", num);
    }

    /**
     * 向下进位
     */
    public String formatFloatFloor(Double num, Integer pos) {
        BigDecimal big = new BigDecimal(num);
        Double amount = big.setScale(pos, BigDecimal.ROUND_DOWN).doubleValue();
        return String.valueOf(amount);
    }
}
