package com.ttw.itds.ui.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ttw.itds.ui.domain.entity.HtsMgmtLookupEntity;
import com.ttw.itds.ui.domain.entity.CpscHtsManagementEntity;

public interface HtsMgmtLookupRepository extends JpaRepository<HtsMgmtLookupEntity,Long> {

	static final String recentStartDateClause = "rha.startDate = (select max(sib.startDate) from HtsMgmtLookupEntity sib where sib.htsCode = rha.htsCode)";
	static final String recentStartDateClauseViaSeq = "rha.startDate = (select max(sib.startDate) from HtsMgmtLookupEntity sib where sib.htsCode = rha.htsCode and sib.sequenceId = :sequenceId)";
	static final String unexpiredClause = "(rha.endDate is null or rha.endDate >= :today)";
	static final String sunsetClause = "rha.endDate >= :today and rha.targeted = true";
	static final String minHtsLengthClause = "length(rha.htsCode) >= :minLengthOfCode";

    @Query("select count(rha) from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNum(@Param("today") Date today, @Param("minLengthOfCode") Integer minLengthOfCode);
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.sequenceId = :sequenceId and " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId, @Param("minLengthOfCode") Integer minLengthOfCode);
	
    @Query("select count(rha) from HtsMgmtLookupEntity rha where " + sunsetClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumOfSunsets(@Param("today") Date today, @Param("minLengthOfCode") Integer minLengthOfCode);
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.sequenceId = :sequenceId and " + sunsetClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumOfSunsetsViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId, @Param("minLengthOfCode") Integer minLengthOfCode);
	
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.jurisdiction = :jurisdiction and " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumUsingJurisdiction(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("minLengthOfCode") Integer minLengthOfCode);
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.jurisdiction = :jurisdiction and rha.sequenceId = :sequenceId and " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumUsingJurisdictionViaSeq(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("sequenceId") Integer sequenceId, @Param("minLengthOfCode") Integer minLengthOfCode);
	
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.jurisdiction = :jurisdiction and rha.targeted = :targeted and " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumUsingJurisdictionAndTargeted(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("targeted") Boolean targeted, @Param("minLengthOfCode") Integer minLengthOfCode);
    @Query("select count(rha) from HtsMgmtLookupEntity rha where rha.jurisdiction = :jurisdiction and rha.targeted = :targeted and rha.sequenceId = :sequenceId and " + unexpiredClause + " and " + recentStartDateClause + " and " + minHtsLengthClause)
	public Long obtainNumUsingJurisdictionAndTargetedViaSeq(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("targeted") Boolean targeted, @Param("sequenceId") Integer sequenceId, @Param("minLengthOfCode") Integer minLengthOfCode);

    @Query("select htsCode from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause)
	public List<String> obtainHtsCodesIncludingDuplicates(@Param("today") Date today);
    @Query("select rha.htsCode from HtsMgmtLookupEntity rha where rha.sequenceId = :sequenceId and " + unexpiredClause + " and " + recentStartDateClause)
	public List<String> obtainHtsCodesIncludingDuplicatesViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId);

    @Query("select distinct htsCode from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause)
	public List<String> obtainDistinctHtsCodes(@Param("today") Date today);    

}