package th.go.excise.ims.preferences.persistence.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import th.co.baiwa.buckwaframework.common.constant.CommonConstants.FLAG;
import th.co.baiwa.buckwaframework.common.persistence.repository.CommonJpaCrudRepository;
import th.go.excise.ims.preferences.persistence.entity.ExciseHoliday;

public interface ExciseHolidayRepository extends CommonJpaCrudRepository<ExciseHoliday, Long>, ExciseHolidayRepositoryCustom {
	
	@Modifying
	@Query(
		value = "UPDATE EXCISE_HOLIDAY SET IS_DELETED = '" + FLAG.Y_FLAG + "'",
		nativeQuery = true
	)
	public void queryUpdateIsDeletedY();
	
}
