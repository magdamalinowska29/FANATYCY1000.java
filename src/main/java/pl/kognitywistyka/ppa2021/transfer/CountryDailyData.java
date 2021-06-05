package pl.kognitywistyka.ppa2021.transfer;

import java.time.LocalDate;

public class CountryDailyData {
	
	protected LocalDate date;
	protected Integer totalCases;
	protected Integer totalDeaths;
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Integer getTotalCases() {
		return totalCases;
	}
	public void setTotalCases(Integer totalCases) {
		this.totalCases = totalCases;
	}
	public Integer getTotalDeaths() {
		return totalDeaths;
	}
	public void setTotalDeaths(Integer totalDeaths) {
		this.totalDeaths = totalDeaths;
	}

}
