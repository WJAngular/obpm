package cn.myapps.km.disk.ejb;

public class SearchNFile extends NFile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4698647317430528834L;
	/**
	 * 页数
	 */
	private String totalPages;
	
	/**
	 * 评价次数
	 */
	private int evaluateSum;
	
	/**
	 * 评分
	 */
	private double searchScore;

	public double getSearchScore() {
		return searchScore;
	}

	public void setSearchScore(double searchScore) {
		this.searchScore = searchScore;
	}

	public int getEvaluateSum() {
		return evaluateSum;
	}

	public void setEvaluateSum(int evaluateSum) {
		this.evaluateSum = evaluateSum;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
}
