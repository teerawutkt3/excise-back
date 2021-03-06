
package th.go.excise.ims.oa.persistence.entity;

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
@Table(name = "OA_HYDROCARB")
public class OaHydrocarb
    extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OA_HYDROCARB_GEN")
    @SequenceGenerator(name = "OA_HYDROCARB_GEN", sequenceName = "OA_HYDROCARB_SEQ", allocationSize = 1)
    @Column(name = "OA_HYDROCARB_ID")
    private BigDecimal oaHydrocarbId;
    @Column(name = "OA_PLAN_ID")
    private BigDecimal oaPlanId;
    @Column(name = "LICENSE_NO")
    private String licenseNo;
    @Column(name = "LICENSE_ID")
    private BigDecimal licenseId;

    public BigDecimal getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(BigDecimal licenseId) {
		this.licenseId = licenseId;
	}

	public BigDecimal getOaHydrocarbId() {
        return oaHydrocarbId;
    }

    public void setOaHydrocarbId(BigDecimal oaHydrocarbId) {
        this.oaHydrocarbId = oaHydrocarbId;
    }

    public BigDecimal getOaPlanId() {
        return oaPlanId;
    }

    public void setOaPlanId(BigDecimal oaPlanId) {
        this.oaPlanId = oaPlanId;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

}
