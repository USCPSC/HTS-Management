package gov.cpsc.hts.itds.ui.domain.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.cpsc.hts.itds.ui.domain.entity.CpscHtsMgmtHistEntity;

public interface CpscHtsMgmtHistRepository extends JpaRepository<CpscHtsMgmtHistEntity,Long> {

	public CpscHtsMgmtHistEntity findFirstByCategoryOrderByEventDateDesc(String category);
	
	@Query("select eventDate from CpscHtsMgmtHistEntity chmhe where chmhe.category = :category and chmhe.eventDate = (select max(eventDate) from CpscHtsMgmtHistEntity rhm where rhm.stateTransition = 'FINALIZE_END') ")
	public List<Date> obtainLatestFinalizeEventTimeStamp(@Param("category") String category);

	
	

}
