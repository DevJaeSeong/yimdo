package egovframework.yimdoSystem.YimdoSystemController.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractConditionChecker {

	public boolean isAppropriateRainfall(String baseRainFall, String compareRainFall) {
		
		double doubleBaseRainFall = Double.valueOf(baseRainFall != null ? baseRainFall : "0");
		double doubleCompareRainFall = Double.valueOf(compareRainFall);
		boolean result = doubleBaseRainFall >= doubleCompareRainFall;
		
		log.trace("(기준) 강수량: {}, (대조군) 강수량:{}, 기준적합:{}", doubleBaseRainFall, doubleCompareRainFall, result);
		return result;
	}
	
	public boolean isAppropriateWindspeed(String baseWindSpeed, String compareWindSpeed) {
		
		double doubleBaseWindSpeed = Double.valueOf(baseWindSpeed != null ? baseWindSpeed : "0");
		double doubleCompareWindSpeed = Double.valueOf(compareWindSpeed);
		boolean result = doubleBaseWindSpeed >= doubleCompareWindSpeed;
		
		log.trace("(기준) 풍속: {}, (대조군) 풍속:{}, 기준적합:{}", doubleBaseWindSpeed, doubleCompareWindSpeed, result);
		return result;
	}

	public boolean isAppropriateHumidity(String baseHumidity, String compareHumidity) {
		
		double doubleBaseHumidity = Double.valueOf(baseHumidity != null ? baseHumidity : "0");
		double doubleCompareHumidity = Double.valueOf(compareHumidity);
		boolean result = doubleBaseHumidity <= doubleCompareHumidity;
		
		log.trace("(기준) 습도: {}, (대조군) 습도:{}, 기준적합:{}", doubleBaseHumidity, doubleCompareHumidity, result);
		return result;
	}

	public boolean isAfterSunrise(String baseSunrise, String sunriseTime) {
		
		int hour = Integer.parseInt(sunriseTime.substring(0, 2));
        int minute = Integer.parseInt(sunriseTime.substring(2, 4));
        int minutesToAdd = Integer.parseInt(baseSunrise);
		
        minute += minutesToAdd;
        
        if (minute >= 60) {
        	
            hour += minute / 60;
            minute %= 60;
        }
        
        LocalTime currentTime = LocalTime.now();
        LocalTime targetTime = LocalTime.of(hour, minute);
        boolean result = false;
        
        if (currentTime.compareTo(targetTime) >= 0) {
        	
        	result = true;
        }
        
        log.trace("(기준) 일출이후: {}, (대조군) 현재시각:{}, 기준적합:{}", targetTime, currentTime.format(DateTimeFormatter.ofPattern("HH:mm")), result);
		return result;
	}
	
	public boolean isBeforeSunset(String baseSunset, String sunsetTime) {
		
		int hour = Integer.parseInt(sunsetTime.substring(0, 2));
        int minute = Integer.parseInt(sunsetTime.substring(2, 4));
        int totalMinutes = hour * 60 + minute;
        int minutesToSubtract = Integer.parseInt(baseSunset);
        
        totalMinutes -= minutesToSubtract;
        
        if (totalMinutes < 0) {
        	
            totalMinutes += 24 * 60;
        }
        
        hour = totalMinutes / 60;
        minute = totalMinutes % 60;
        
        LocalTime currentTime = LocalTime.now();
        LocalTime targetTime = LocalTime.of(hour, minute);
        boolean result = false;
        
        if (currentTime.compareTo(targetTime) <= 0) {
        	
        	result = true;
        }
        
        log.trace("(기준) 일몰이전: {}, (대조군) 현재시각:{}, 기준적합:{}", targetTime, currentTime.format(DateTimeFormatter.ofPattern("HH:mm")), result);
		return result;
	}
}
