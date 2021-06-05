package pl.kognitywistyka.ppa2021.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryData {

	protected Map<LocalDate, DailyData> dailyDataByDate;

	public CountryData() {
		dailyDataByDate = new HashMap<>();
	}
	
	public void setDataForDate(LocalDate date, DailyData data) {
		dailyDataByDate.put(date, data);
	}
	
	public DailyData getDataForDate(LocalDate date) {
		return dailyDataByDate.get(date);
	}
	
	public LocalDate getMinDate() {
		return dailyDataByDate.keySet().stream().min(Comparator.naturalOrder()).orElse(null);	
	}
	
	public LocalDate getMaxDate() {
		return dailyDataByDate.keySet().stream().max(Comparator.naturalOrder()).orElse(null);	
	}
	
	public List<DailyData> getDataFromTo(LocalDate min, LocalDate max) {
		List<DailyData> ret = new ArrayList<>();
		if (min != null && max != null && !min.isAfter(max)) {
			for (LocalDate cur = min; !cur.isAfter(max); cur = cur.plusDays(1)) {
				ret.add(dailyDataByDate.get(cur));
			}
		}
		return ret;
	}
	
}
