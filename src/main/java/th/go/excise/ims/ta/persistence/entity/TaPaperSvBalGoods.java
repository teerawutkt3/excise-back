
package th.go.excise.ims.ta.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import th.co.baiwa.buckwaframework.common.persistence.entity.BaseEntity;

@Entity
@Table(name = "TA_PAPER_SV_BAL_GOODS")
public class TaPaperSvBalGoods extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4674573838096815820L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TA_PAPER_SV_BAL_GOODS_GEN")
	@SequenceGenerator(name = "TA_PAPER_SV_BAL_GOODS_GEN", sequenceName = "TA_PAPER_SV_BAL_GOODS_SEQ", allocationSize = 1)
	@Column(name = "PAPER_SV_BAL_GOODS_ID")
	private Long paperSvBalGoodsId;
	@Column(name = "PLAN_NUMBER")
	private String planNumber;
	@Column(name = "PAPER_NUMBER")
	private String paperNumber;
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	@Column(name = "NEW_REG_ID")
	private String newRegId;
	@Column(name = "START_DATE")
	private Date startDate;
	@Column(name = "END_DATE")
	private Date endDate;
	@Column(name = "UPLOAD_NUMBER")
	private String uploadNumber;
	@Column(name = "UPLOAD_DATE")
	private Date uploadDate;
	@Column(name = "SEQ_NO")
	private String seqNo;
	@Column(name = "GOODS_DESC")
	private String goodsDesc;
	@Column(name = "BALANCE_GOODS_QTY")
	private BigDecimal balanceGoodsQty;
	@Column(name = "AUDIT_BALANCE_GOODS_QTY")
	private BigDecimal auditBalanceGoodsQty;
	@Column(name = "DIFF_BALANCE_GOODS_QTY")
	private BigDecimal diffBalanceGoodsQty;

	public Long getPaperSvBalGoodsId() {
		return paperSvBalGoodsId;
	}

	public void setPaperSvBalGoodsId(Long paperSvBalGoodsId) {
		this.paperSvBalGoodsId = paperSvBalGoodsId;
	}

	public String getPlanNumber() {
		return planNumber;
	}

	public void setPlanNumber(String planNumber) {
		this.planNumber = planNumber;
	}

	public String getPaperNumber() {
		return paperNumber;
	}

	public void setPaperNumber(String paperNumber) {
		this.paperNumber = paperNumber;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getNewRegId() {
		return newRegId;
	}

	public void setNewRegId(String newRegId) {
		this.newRegId = newRegId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUploadNumber() {
		return uploadNumber;
	}

	public void setUploadNumber(String uploadNumber) {
		this.uploadNumber = uploadNumber;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public BigDecimal getBalanceGoodsQty() {
		return balanceGoodsQty;
	}

	public void setBalanceGoodsQty(BigDecimal balanceGoodsQty) {
		this.balanceGoodsQty = balanceGoodsQty;
	}

	public BigDecimal getAuditBalanceGoodsQty() {
		return auditBalanceGoodsQty;
	}

	public void setAuditBalanceGoodsQty(BigDecimal auditBalanceGoodsQty) {
		this.auditBalanceGoodsQty = auditBalanceGoodsQty;
	}

	public BigDecimal getDiffBalanceGoodsQty() {
		return diffBalanceGoodsQty;
	}

	public void setDiffBalanceGoodsQty(BigDecimal diffBalanceGoodsQty) {
		this.diffBalanceGoodsQty = diffBalanceGoodsQty;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
