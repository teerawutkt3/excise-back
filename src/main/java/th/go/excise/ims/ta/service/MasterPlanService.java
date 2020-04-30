package th.go.excise.ims.ta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.go.excise.ims.ta.persistence.entity.TaPlanMas;
import th.go.excise.ims.ta.persistence.repository.TaPlanMasRepository;

@Service
public class MasterPlanService {

	@Autowired
	private TaPlanMasRepository planMasRepository;

	public void insertPlan(List<TaPlanMas> form) {
		TaPlanMas planMas = null;
		List<TaPlanMas> list = new ArrayList<TaPlanMas>();

		for (TaPlanMas obj : form) {
			if (obj.getPlanMasId() == null) {
				planMas = new TaPlanMas();
				planMas.setOfficeCode(UserLoginUtils.getCurrentUserBean().getOfficeCode());
				planMas.setBudgetYear(obj.getBudgetYear());
				planMas.setMonth(obj.getMonth());
				planMas.setFacNum(obj.getFacNum());
				list.add(planMas);
			} else {
				planMas = planMasRepository.findById(obj.getPlanMasId()).get();
				planMas.setFacNum(obj.getFacNum());
				list.add(planMas);
			}
		}

		planMasRepository.saveAll(list);
	}
	
	public List<TaPlanMas> getPlan(TaPlanMas form) {
		List<TaPlanMas> list = new ArrayList<>();
		list = planMasRepository.findByBudgetYearAndOfficeCode(form.getBudgetYear(), UserLoginUtils.getCurrentUserBean().getOfficeCode());
		return list;
	}
}
