package th.go.excise.ims.ta.vo;

import java.util.List;

import th.go.excise.ims.preferences.vo.ExcisePersonVoSelect;

public class PlanWorksheetDatatableVo {

	private String cusFullname;
	private String facFullname;
	private String facAddress;
	private String officeCodeR4000;
	private String dutyCode;
	private String dutyDesc;
	private String secCode;
	private String secDesc;
	private String areaCode;
	private String areaDesc;
	private String planNumber;
	private String analysisNumber;
	private String newRegId;
	private String condMainGrp;
	private String auditStatus;
	private String auditStatusDesc;
	private String auditType;
	private String auditDate;
	private String auSubdeptCode;
	private String auJobResp;
	private String officeCode;
	private Long planWorksheetDtlId;
    private String deptShortName;
    private String subdeptShortName;
    private String personName;
    private List<ExcisePersonVoSelect> personAssingList;
    
   
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Long getPlanWorksheetDtlId() {
		return planWorksheetDtlId;
	}

	public void setPlanWorksheetDtlId(Long planWorksheetDtlId) {
		this.planWorksheetDtlId = planWorksheetDtlId;
	}

	public String getAuSubdeptCode() {
		return auSubdeptCode;
	}

	public void setAuSubdeptCode(String auSubdeptCode) {
		this.auSubdeptCode = auSubdeptCode;
	}

	public String getAuJobResp() {
		return auJobResp;
	}

	public void setAuJobResp(String auJobResp) {
		this.auJobResp = auJobResp;
	}

	public String getCusFullname() {
		return cusFullname;
	}

	public void setCusFullname(String cusFullname) {
		this.cusFullname = cusFullname;
	}

	public String getFacFullname() {
		return facFullname;
	}

	public void setFacFullname(String facFullname) {
		this.facFullname = facFullname;
	}

	public String getFacAddress() {
		return facAddress;
	}

	public void setFacAddress(String facAddress) {
		this.facAddress = facAddress;
	}

	public String getOfficeCodeR4000() {
		return officeCodeR4000;
	}

	public void setOfficeCodeR4000(String officeCodeR4000) {
		this.officeCodeR4000 = officeCodeR4000;
	}

	public String getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(String dutyCode) {
		this.dutyCode = dutyCode;
	}

	public String getDutyDesc() {
		return dutyDesc;
	}

	public void setDutyDesc(String dutyDesc) {
		this.dutyDesc = dutyDesc;
	}

	public String getSecCode() {
		return secCode;
	}

	public void setSecCode(String secCode) {
		this.secCode = secCode;
	}

	public String getSecDesc() {
		return secDesc;
	}

	public void setSecDesc(String secDesc) {
		this.secDesc = secDesc;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getPlanNumber() {
		return planNumber;
	}

	public void setPlanNumber(String planNumber) {
		this.planNumber = planNumber;
	}

	public String getAnalysisNumber() {
		return analysisNumber;
	}

	public void setAnalysisNumber(String analysisNumber) {
		this.analysisNumber = analysisNumber;
	}

	public String getNewRegId() {
		return newRegId;
	}

	public void setNewRegId(String newRegId) {
		this.newRegId = newRegId;
	}

	public String getCondMainGrp() {
		return condMainGrp;
	}

	public void setCondMainGrp(String condMainGrp) {
		this.condMainGrp = condMainGrp;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getDeptShortName() {
		return deptShortName;
	}

	public void setDeptShortName(String deptShortName) {
		this.deptShortName = deptShortName;
	}

	public String getSubdeptShortName() {
		return subdeptShortName;
	}

	public void setSubdeptShortName(String subdeptShortName) {
		this.subdeptShortName = subdeptShortName;
	}

	public List<ExcisePersonVoSelect> getPersonAssingList() {
		return personAssingList;
	}

	public void setPersonAssingList(List<ExcisePersonVoSelect> personAssingList) {
		this.personAssingList = personAssingList;
	}
	
	

}
