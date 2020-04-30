package th.go.excise.ims.ta.vo;

import org.springframework.web.multipart.MultipartFile;

public class ProductPaperOutputForeignGoodsVo {
	private Long id;
	private String goodsDesc;
	private String cargoDocNo;
	private String invoiceNo;
	private String outputDailyAccountQty;
	private String outputMonthStatementQty;
	private String outputAuditQty;
	private String taxReduceQty;
	private String diffOutputQty;
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getCargoDocNo() {
		return cargoDocNo;
	}

	public void setCargoDocNo(String cargoDocNo) {
		this.cargoDocNo = cargoDocNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getOutputDailyAccountQty() {
		return outputDailyAccountQty;
	}

	public void setOutputDailyAccountQty(String outputDailyAccountQty) {
		this.outputDailyAccountQty = outputDailyAccountQty;
	}

	public String getOutputMonthStatementQty() {
		return outputMonthStatementQty;
	}

	public void setOutputMonthStatementQty(String outputMonthStatementQty) {
		this.outputMonthStatementQty = outputMonthStatementQty;
	}

	public String getOutputAuditQty() {
		return outputAuditQty;
	}

	public void setOutputAuditQty(String outputAuditQty) {
		this.outputAuditQty = outputAuditQty;
	}

	public String getTaxReduceQty() {
		return taxReduceQty;
	}

	public void setTaxReduceQty(String taxReduceQty) {
		this.taxReduceQty = taxReduceQty;
	}

	public String getDiffOutputQty() {
		return diffOutputQty;
	}

	public void setDiffOutputQty(String diffOutputQty) {
		this.diffOutputQty = diffOutputQty;
	}

}
