package pl.kognitywistyka.ppa2021.data;

public class DailyData {
	
	// Bean
	// POJO = Plain Old Java Object
	
	protected Integer totalCases;
	protected Integer totalDeaths;
	
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
