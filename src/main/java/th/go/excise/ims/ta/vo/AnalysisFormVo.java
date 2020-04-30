package th.go.excise.ims.ta.vo;

import th.co.baiwa.buckwaframework.common.bean.DataTableRequest;

public class AnalysisFormVo extends DataTableRequest {

	private static final long serialVersionUID = -4962250628414975351L;

	private String paperBaNumber;
	private String newRegId;
	private String dutyGroupId;
	private String startDate;
	private String endDate;

	public String getPaperBaNumber() {
		return paperBaNumber;
	}

	public void setPaperBaNumber(String paperBaNumber) {
		this.paperBaNumber = paperBaNumber;
	}

	public String getNewRegId() {
		return newRegId;
	}

	public void setNewRegId(String newRegId) {
		this.newRegId = newRegId;
	}

	public String getDutyGroupId() {
		return dutyGroupId;
	}

	public void setDutyGroupId(String dutyGroupId) {
		this.dutyGroupId = dutyGroupId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
