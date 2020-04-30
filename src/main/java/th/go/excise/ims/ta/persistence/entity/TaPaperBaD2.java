package th.go.excise.ims.ta.persistence.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import th.co.baiwa.buckwaframework.common.persistence.entity.BaseEntity;

@Entity
@Table(name = "TA_PAPER_BA_D2")
public class TaPaperBaD2 extends BaseEntity {

	private static final long serialVersionUID = -8020302393317955991L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TA_PAPER_BA_D2_GEN")
	@SequenceGenerator(name = "TA_PAPER_BA_D2_GEN", sequenceName = "TA_PAPER_BA_D2_SEQ", allocationSize = 1)
	@Column(name = "PAPER_BA_D2_SEQ")
	private Long paperBaD2Seq;
	@Column(name = "PAPER_BA_CODE")
	private String paperBaCode;
	@Column(name = "REC_NO")
	private String recNo;
	@Column(name = "GOODS_DESC")
	private String goodsDesc;
	@Column(name = "TAX_INFORM_PRICE")
	private BigDecimal taxInformPrice;
	@Column(name = "INFORM_PRICE")
	private BigDecimal informPrice;
	@Column(name = "DIFF_TAX_INFORM_PRICE")
	private BigDecimal diffTaxInformPrice;

	public Long getPaperBaD2Seq() {
		return paperBaD2Seq;
	}

	public void setPaperBaD2Seq(Long paperBaD2Seq) {
		this.paperBaD2Seq = paperBaD2Seq;
	}

	public String getPaperBaCode() {
		return paperBaCode;
	}

	public void setPaperBaCode(String paperBaCode) {
		this.paperBaCode = paperBaCode;
	}

	public String getRecNo() {
		return recNo;
	}

	public void setRecNo(String recNo) {
		this.recNo = recNo;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public BigDecimal getTaxInformPrice() {
		return taxInformPrice;
	}

	public void setTaxInformPrice(BigDecimal taxInformPrice) {
		this.taxInformPrice = taxInformPrice;
	}

	public BigDecimal getInformPrice() {
		return informPrice;
	}

	public void setInformPrice(BigDecimal informPrice) {
		this.informPrice = informPrice;
	}

	public BigDecimal getDiffTaxInformPrice() {
		return diffTaxInformPrice;
	}

	public void setDiffTaxInformPrice(BigDecimal diffTaxInformPrice) {
		this.diffTaxInformPrice = diffTaxInformPrice;
	}

}
