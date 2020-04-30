
package th.go.excise.ims.ia.persistence.repository;

import java.util.List;

import th.co.baiwa.buckwaframework.common.persistence.repository.CommonJpaCrudRepository;
import th.go.excise.ims.ia.persistence.entity.IaAuditIncD2;

public interface IaAuditIncD2Repository extends CommonJpaCrudRepository<IaAuditIncD2, Long> , IaAuditIncD2RepositoryCustom {
	
	public List<IaAuditIncD2> findByAuditIncNoOrderByReceiptDate(String auditIncNo);
	
}
