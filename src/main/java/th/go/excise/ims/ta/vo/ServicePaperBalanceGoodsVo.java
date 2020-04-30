package th.go.excise.ims.ta.vo;

import org.springframework.web.multipart.MultipartFile;

public class ServicePaperBalanceGoodsVo {
	private String goodsDesc; //รายการ
	private String balanceGoodsQty; //ยอดคงเหลือตามบัญชี
	private String auditBalanceGoodsQty; //ยอดสินค้าคงเหลือจากการตรวจนับ
	private String 	diffBalanceGoodsQty;  //ผลต่าง
	private MultipartFile file;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getBalanceGoodsQty() {
		return balanceGoodsQty;
	}
	public void setBalanceGoodsQty(String balanceGoodsQty) {
		this.balanceGoodsQty = balanceGoodsQty;
	}
	public String getAuditBalanceGoodsQty() {
		return auditBalanceGoodsQty;
	}
	public void setAuditBalanceGoodsQty(String auditBalanceGoodsQty) {
		this.auditBalanceGoodsQty = auditBalanceGoodsQty;
	}
	public String getDiffBalanceGoodsQty() {
		return diffBalanceGoodsQty;
	}
	public void setDiffBalanceGoodsQty(String diffBalanceGoodsQty) {
		this.diffBalanceGoodsQty = diffBalanceGoodsQty;
	}
	
}
