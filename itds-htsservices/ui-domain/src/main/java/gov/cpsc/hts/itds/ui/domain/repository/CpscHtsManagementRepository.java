package gov.cpsc.hts.itds.ui.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.cpsc.hts.itds.ui.domain.entity.CpscHtsManagementEntity;
import gov.cpsc.hts.itds.ui.domain.entity.HtsMgmtLookupEntity;

public interface CpscHtsManagementRepository extends JpaRepository<CpscHtsManagementEntity,Long> {
	
	static final String recentStartDateClause = "rha.startDate = (select max(sib.startDate) from HtsMgmtLookupEntity sib where sib.htsCode = rha.htsCode)";
	static final String recentStartDateClauseViaSeq = "rha.startDate = (select max(sib.startDate) from HtsMgmtLookupEntity sib where sib.htsCode = rha.htsCode and sib.sequenceId = :sequenceId)";
	static final String unexpiredClause = "(rha.endDate is null or rha.endDate >= :today)";
	static final String rhaSearchClause = "(rha.cpscShortDescription like %:searchterm% or rha.itcDescription like %:searchterm% or rha.cpscDescription like %:searchterm% or rha.htsCode like :codesearchterm%)";
	static final String rhmeSearchClause = "(rhme.cpscShortDescription like %:searchterm% or rhme.description like %:searchterm% or rhme.cdescription like %:searchterm% or rhme.htsCode like :codesearchterm%)";

	// used in postBeanInjectionTasks() and debugCountRecords():
	public Long countByUsername(String username);
	
	public Long countByUsernameAndChangeStatus(String username, String changeStatus);
	public Long countByUsernameAndChangeStatusAndCodeType(String username, String changeStatus, Integer codeType);
	public Long countByUsernameAndChangeStatusAndCodeTypeAndJurisdiction(String username, String changeStatus, Integer codeType, Boolean jurisdiction);
	public Long countByUsernameAndChangeStatusAndCodeTypeAndTargeted(String username, String changeStatus, Integer codeType, Boolean targeted);
	public Long countByJurisdiction(Boolean jurisdiction);
	public Long countByJurisdictionAndCodeType(Boolean jurisdiction, Integer codeType);
	public Long countByJurisdictionAndTargeted(Boolean jurisdiction, Boolean targeted);
	public Long countByJurisdictionAndTargetedAndCodeType(Boolean jurisdiction, Boolean targeted, Integer codeType);
	
	public Page<CpscHtsManagementEntity> findByUsernameOrderByHtsCodeAsc(String username, Pageable pageable);
	public List<CpscHtsManagementEntity> findByUsernameOrderByHtsCodeAsc(String username);
	public List<CpscHtsManagementEntity> findByUsernameAndModifiedOrderByHtsCodeAsc(String username, Boolean modified);
	public List<CpscHtsManagementEntity> findByUsernameAndModifiedAndChangeStatusOrderByHtsCodeAsc(String username, Boolean modified, String changeStatus);
	public List<CpscHtsManagementEntity> findByUsernameAndModifiedAndInheritsChangeAndChangeStatusOrderByHtsCodeAsc(String username, Boolean modified, Boolean inheritsChange, String changeStatus);
	public List<CpscHtsManagementEntity> findByUsernameAndJurisdictionOrderByHtsCodeAsc(String username, Boolean jurisdiction);
	public List<CpscHtsManagementEntity> findByUsernameAndJurisdictionAndCodeTypeOrderByHtsCodeAsc(String username, Boolean jurisdiction, Integer codeType);
	public List<CpscHtsManagementEntity> findByUsernameAndTargetedOrderByHtsCodeAsc(String username, Boolean targeted);	
	public List<CpscHtsManagementEntity> findByUsernameAndTargetedAndCodeTypeOrderByHtsCodeAsc(String username, Boolean targeted, Integer codeType);
	public List<CpscHtsManagementEntity> findByUsernameAndCodeTypeOrderByHtsCodeAsc(String username, Integer codeType);	
	public List<CpscHtsManagementEntity> findBySource(String source);
	public List<CpscHtsManagementEntity> findByHtsCode(String htsCode);
	public List<CpscHtsManagementEntity> findByCodeType(Integer codeType);
	public List<CpscHtsManagementEntity> findByDescription(String description);
	public List<CpscHtsManagementEntity> findByJurisdiction(Boolean jurisdiction);
	public List<CpscHtsManagementEntity> findByTargeted(Boolean targeted);
	public List<CpscHtsManagementEntity> findByChangeStatus(String changeStatus);
	public List<CpscHtsManagementEntity> findByReviewStatus(String reviewStatus);
	public List<CpscHtsManagementEntity> findByNotes(String notes);
    public CpscHtsManagementEntity findOneByUsernameAndHtsCode(String username, String htsCode);
    public CpscHtsManagementEntity findOneByUsernameAndHtsCodeAndChangeStatus(String username, String htsCode, String changeStatus);

    @Query("select rhme from CpscHtsManagementEntity rhme where rhme.username = :username and rhme.codeType = :codeType and rhme.htsCode like :codePrefix%")
    public List<CpscHtsManagementEntity> obtainDescendantsOfSpecificLevelByCodePrefix(
    		@Param("username") String username, @Param("codeType") Integer codeType, @Param("codePrefix") String codePrefix);
    
    @Query("select distinct username from CpscHtsManagementEntity")
	public List<String> obtainDistinctUsernames();    
    
    // ----------------------------
    // START of dateful pairs
    // ----------------------------

    // START of signatures called only from CpscHtsManagementService.select where thePresentGlobalState == HtsGlobalStateEnum.FINALIZED_NO_WIP
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause)
    public List<HtsMgmtLookupEntity> obtainRefsOrderedDateful(@Param("today") Date today);

    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and length(rha.htsCode) = :lengthOfCode")
    public List<HtsMgmtLookupEntity> obtainRefsByLengthOfCodeOrderedDateful(@Param("today") Date today, @Param("lengthOfCode") Integer lengthOfCode);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.htsCode = :htsCode")
    public List<HtsMgmtLookupEntity> obtainRefByCodeOrderedDateful(@Param("today") Date today, @Param("htsCode") String htsCode);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsOrderedDatefulViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and length(rha.htsCode) = :lengthOfCode and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsByLengthOfCodeOrderedDatefulViaSeq(@Param("today") Date today, @Param("lengthOfCode") Integer lengthOfCode, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.htsCode = :htsCode and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefByCodeOrderedDatefulViaSeq(@Param("today") Date today, @Param("htsCode") String htsCode, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and " + rhaSearchClause)
    public List<HtsMgmtLookupEntity> obtainRefsBySearchtermDateful(@Param("today") Date today, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and " + rhaSearchClause + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsBySearchtermDatefulViaSeq(@Param("today") Date today, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.jurisdiction = :jurisdiction")
    public List<HtsMgmtLookupEntity> obtainRefsByJurisdictionOrderedDateful(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.jurisdiction = :jurisdiction and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsByJurisdictionOrderedDatefulViaSeq(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.jurisdiction = :jurisdiction and " + rhaSearchClause)
    public List<HtsMgmtLookupEntity> obtainRefsByJurisdictionAndSearchtermDateful(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.jurisdiction = :jurisdiction and " + rhaSearchClause + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsByJurisdictionAndSearchtermDatefulViaSeq(@Param("today") Date today, @Param("jurisdiction") Boolean jurisdiction, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.targeted = :targeted")
    public List<HtsMgmtLookupEntity> obtainRefsByTargetedOrderedDateful(@Param("today") Date today, @Param("targeted") Boolean targeted);	
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.targeted = :targeted and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsByTargetedOrderedDatefulViaSeq(@Param("today") Date today, @Param("targeted") Boolean targeted, @Param("sequenceId") Integer sequenceId);
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.targeted = :targeted and " + rhaSearchClause)
    public List<HtsMgmtLookupEntity> obtainRefsByTargetedAndSearchtermDateful(@Param("today") Date today, @Param("targeted") Boolean targeted, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.targeted = :targeted and " + rhaSearchClause + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsByTargetedAndSearchtermDatefulViaSeq(@Param("today") Date today, @Param("targeted") Boolean targeted, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm, @Param("sequenceId") Integer sequenceId);
    
    // END of signatures called only from CpscHtsManagementService.select where thePresentGlobalState == HtsGlobalStateEnum.FINALIZED_NO_WIP
    
    // signatures called only from CpscHtsManagementService.obtainRefsUsingCodeList:
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause + " and rha.htsCode in (:codelist)")
    public List<HtsMgmtLookupEntity> obtainRefsUsingALCodeListOrderedDateful(@Param("today") Date today, @Param("codelist") ArrayList<String> codelist);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.htsCode in (:codelist) and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> obtainRefsUsingALCodeListOrderedDatefulViaSeq(@Param("today") Date today, @Param("codelist") ArrayList<String> codelist, @Param("sequenceId") Integer sequenceId);

    // signatures called only from 
    // (1) CpscHtsManagementService.simpleUploadAndImport prior to import, AND
    // (2) finalization to see if booleans or notes have changed:
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClause)
    public List<HtsMgmtLookupEntity> findAllFinalizedOmniStatusDateful(@Param("today") Date today);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and " + recentStartDateClauseViaSeq + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> findAllFinalizedOmniStatusDatefulViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId);

    // signatures used instead of the 2 above, for performance. Precondition: For each specific HTS code, the lookup table contains at most one unexpired record.
    // signatures called only from finalization, to see if booleans or notes have changed:
    
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause)
    public List<HtsMgmtLookupEntity> findAllFinalizedOmniStatusDatefulAmongUniqueUnexpireds(@Param("today") Date today);
    @Query("select rha from HtsMgmtLookupEntity rha where " + unexpiredClause + " and rha.sequenceId = :sequenceId")
    public List<HtsMgmtLookupEntity> findAllFinalizedOmniStatusDatefulAmongUniqueUnexpiredsViaSeq(@Param("today") Date today, @Param("sequenceId") Integer sequenceId);

    // ----------------------------
    // END of dateful pairs
    // ----------------------------
    
    @Transactional
    @Modifying
    @Query("insert into CpscHtsManagementEntity (username, htsCode, codeType, cdescription, cpscShortDescription, description, "
			+ "changeStatus, source, jurisdiction, targeted, inheritsChange, notes) "
			+ "select :userforscratch, r.htsCode, length(r.htsCode), r.cpscDescription, r.cpscShortDescription, r.itcDescription, "
			+ "'none', r.source, r.jurisdiction, r.targeted, r.inheritsChange, r.notes "
			+ "from HtsMgmtLookupEntity r where r.endDate is null")
    public Integer copyActivesFromLookupIntoScratch(@Param("userforscratch") String userforscratch);
    
    @Transactional
    @Modifying
    @Query("insert into CpscHtsManagementEntity (username, htsCode, codeType, cdescription, cpscShortDescription, description, "
			+ "changeStatus, source, jurisdiction, targeted, inheritsChange, notes) "
			+ "select :userforscratch, r.htsCode, length(r.htsCode), r.cpscDescription, r.cpscShortDescription, r.itcDescription, "
			+ "'none', r.source, r.jurisdiction, r.targeted, r.inheritsChange, r.notes "
			+ "from HtsMgmtLookupEntity r where r.endDate is null and r.sequenceId = :sequenceId")
    public Integer copyActivesFromLookupIntoScratchViaSeq(@Param("userforscratch") String userforscratch, @Param("sequenceId") Integer sequenceId);
    
    // ----------------------------
    // START of HtsMgmtLookupEntity calls not yet using dateful pairs
    // ----------------------------
    
    @Transactional
    @Modifying
    @Query("insert into HtsMgmtLookupEntity (htsCode, cpscDescription, cpscShortDescription, itcDescription, jurisdiction, targeted, source, n"
    		+ "otes, startDate, endDate) select s.htsCode, s.cdescription, s.cpscShortDescription, s.description, s.jurisdiction, s.targe"
    		+ "ted, :tagforsrc, s.notes, :startdatetime, :enddatetime from CpscHtsManagementEntity s where s.usern"
    		+ "ame = :userforscratch and s.htsCode in (:codelist) and s.htsCode not in (select distinct r.htsCode from HtsMgmtLookupEntity r)")
    public Integer insertPlaceholdersFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, @Param("userforscratch") String userforscratch,
    		@Param("startdatetime") Date startdatetime, @Param("enddatetime") Date enddatetime, @Param("codelist") ArrayList<String> codelist);
    
    @Transactional
    @Modifying
    @Query("insert into HtsMgmtLookupEntity (htsCode, cpscDescription, cpscShortDescription, itcDescription, jurisdiction, targeted, source, n"
    		+ "otes, startDate, endDate) select s.htsCode, s.cdescription, s.cpscShortDescription, s.description, s.jurisdiction, s.targe"
    		+ "ted, :tagforsrc, s.notes, :startdatetime, :enddatetime from CpscHtsManagementEntity s where s.usern"
    		+ "ame = :userforscratch and s.htsCode in (:codelist) and s.htsCode not in (select distinct r.htsCode from HtsMgmtLookupEntity r)")
    public Integer insertNoveltiesFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, @Param("userforscratch") String userforscratch,
    		@Param("startdatetime") Date startdatetime, @Param("enddatetime") Date enddatetime, @Param("codelist") ArrayList<String> codelist);
    
    @Transactional
    @Modifying
    @Query("update HtsMgmtLookupEntity rha set rha.source = :tagforsrc, rha.endDate = :enddatetime where rha.htsCode in (:codelist) and " + recentStartDateClause)
    public Integer updateRetireesFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc,
    		@Param("enddatetime") Date enddatetime, @Param("codelist") ArrayList<String> codelist);
    
    @Transactional
    @Modifying
    @Query("insert into HtsMgmtLookupEntity (htsCode, cpscDescription, cpscShortDescription, itcDescription, jurisdiction, targeted, source, n"
    		+ "otes, startDate, endDate, createTS, createUserId, lastUpdateTS, lastUpdateUserId) select s.htsCode, s.cdescription, s.cpscShortDescription, s.description, s.jurisdiction, s.targe"
    		+ "ted, :tagforsrc, s.notes, :startdatetime, :enddatetime, :auditdatetime, :audituser, :auditdatetime, :audituser from CpscHtsManagementEntity s where s.usern"
    		+ "ame = :userforscratch and s.htsCode = :code and s.reviewStatus <> :excludereviewstatus")
    public Integer insertOnePairedAddFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, @Param("userforscratch") String userforscratch,
    		@Param("startdatetime") Date startdatetime, @Param("enddatetime") Date enddatetime, @Param("code") String code, 
    		@Param("excludereviewstatus") String excludereviewstatus, @Param("auditdatetime") Date auditdatetime, @Param("audituser") String audituser);
    
    @Transactional
    @Modifying
    @Query("update HtsMgmtLookupEntity rha set rha.source = :tagforsrc, rha.endDate = :enddatetime, rha.lastUpdateTS = :auditdatetime, rha.lastUpdateUserId = :audituser where rha.htsCode = :code and " + recentStartDateClause)
    public Integer updateOnePairedRemovalFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc,
    		@Param("enddatetime") Date enddatetime, @Param("code") String code, @Param("auditdatetime") Date auditdatetime, @Param("audituser") String audituser);
    
    @Transactional
    @Modifying
    @Query("update HtsMgmtLookupEntity rha set rha.source = :tagforsrc, rha.endDate = :enddatetime, rha.lastUpdateTS = :auditdatetime, rha.lastUpdateUserId = :audituser where rha.htsCode in (:codelist) and " + recentStartDateClause)
    public Integer updateTheClosingHalfOfOneTweakedIncumbentFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, 
    		@Param("enddatetime") Date enddatetime, @Param("codelist") ArrayList<String> codelist, @Param("auditdatetime") Date auditdatetime, @Param("audituser") String audituser);
    
    @Transactional
    @Modifying
    @Query("insert into HtsMgmtLookupEntity (htsCode, cpscDescription, cpscShortDescription, itcDescription, jurisdiction, targeted, source, n"
    		+ "otes, startDate, endDate, createTS, createUserId, lastUpdateTS, lastUpdateUserId) select s.htsCode, s.cdescription, s.cpscShortDescription, s.description, s.jurisdiction, s.targe"
    		+ "ted, :tagforsrc, s.notes, :startdatetime, :enddatetime, :auditdatetime, :audituser, :auditdatetime, :audituser from CpscHtsManagementEntity s where s.username = :userforscratch and s.htsCode in (:codelist)")
    public Integer insertTheOpeningHalfOfOneTweakedIncumbentFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, @Param("userforscratch") String userforscratch, 
    		@Param("startdatetime") Date startdatetime, @Param("enddatetime") Date enddatetime, @Param("codelist") ArrayList<String> codelist,
    		@Param("auditdatetime") Date auditdatetime, @Param("audituser") String audituser);

    @Transactional
    @Modifying
    @Query("update HtsMgmtLookupEntity rha set rha.source = :tagforsrc, rha.notes = :notes, rha.lastUpdateTS = :auditdatetime, rha.lastUpdateUserId = :audituser where rha.htsCode = :code and " + recentStartDateClause)
    public Integer updateUntweakedIncumbentWithNewNotesFromScratchIntoLookup(@Param("tagforsrc") String tagforsrc, @Param("notes") String notes,
    		@Param("code") String code, @Param("auditdatetime") Date auditdatetime, @Param("audituser") String audituser);
    
    
    // ----------------------------
    // END of HtsMgmtLookupEntity calls not yet using dateful pairs
    // ----------------------------
    
    // signature called only from CpscHtsManagementService.obtainUsingCodeList:    
    // @Query("select rhme from CpscHtsManagementEntity rhme where rhme.username = :username and rhme.htsCode in (:codelist) order by rhme.htsCode asc")
    @Query("select rhme from CpscHtsManagementEntity rhme where rhme.htsCode in (:codelist) order by rhme.htsCode asc")
    public List<CpscHtsManagementEntity> obtainUsingALCodeListOrdered(@Param("codelist") ArrayList<String> codelist);
    
    @Query("select rhme from CpscHtsManagementEntity rhme where rhme.username = :username and " + rhmeSearchClause)
    public List<CpscHtsManagementEntity> obtainByUsernameAndSearchterm(@Param("username") String username, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);

    @Query("select rhme from CpscHtsManagementEntity rhme where rhme.username = :username and rhme.jurisdiction = :jurisdiction and " + rhmeSearchClause)
    public List<CpscHtsManagementEntity> obtainByUsernameAndJurisdictionAndSearchterm(
    		@Param("username") String username, @Param("jurisdiction") Boolean jurisdiction, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);

    @Query("select rhme from CpscHtsManagementEntity rhme where rhme.username = :username and rhme.targeted = :targeted and " + rhmeSearchClause)
    public List<CpscHtsManagementEntity> obtainByUsernameAndTargetedAndSearchterm(
    		@Param("username") String username, @Param("targeted") Boolean targeted, @Param("searchterm") String searchterm, @Param("codesearchterm") String codesearchterm);
    
    @Transactional
    @Modifying
    @Query("update CpscHtsManagementEntity set cpscShortDescription = :cpscShortDescription, cdescription = :cdescription, jurisdiction = :jurisdiction, " 
    		+ "jurisdictionModified = :jurisdictionModified, targeted = :targeted, targetedModified = :targetedModified, reviewStatus = :reviewStatus, notes = :notes, " 
    		+ "modified = :modified where username = :username and htsCode = :htsCode and changeStatus = :changeStatus")
    public Integer uniqueUpdate(@Param("cpscShortDescription") String cpscShortDescription, @Param("cdescription") String cdescription, @Param("jurisdiction") Boolean jurisdiction, 
    		@Param("jurisdictionModified") Boolean jurisdictionModified, @Param("targeted") Boolean targeted, @Param("targetedModified") Boolean targetedModified, 
    		@Param("reviewStatus") String reviewStatus, @Param("notes") String notes, @Param("username") String username, @Param("htsCode") String htsCode, 
    		@Param("changeStatus") String changeStatus, @Param("modified") Boolean modified);
    
    @Transactional
    @Modifying
    @Query("update CpscHtsManagementEntity set username = :userindicatingfinalized where username = :userforscratch")
    public Integer makeFinalCleanByChangingUsername(@Param("userforscratch") String userforscratch, @Param("userindicatingfinalized") String userindicatingfinalized);
    
    @Transactional
    @Modifying
    @Query("delete from CpscHtsManagementEntity where username = :userforscratch")
    public Integer makeFinalCleanByDeleting(@Param("userforscratch") String userforscratch);
    
    @Transactional
    @Modifying
    @Query("delete from CpscHtsManagementEntity where username = :userforscratch and htsCode like :digitInArbitraryPlace%")
    public Integer makeFinalCleanByDeletingBatchwise(@Param("userforscratch") String userforscratch, @Param("digitInArbitraryPlace") String digitInArbitraryPlace);
    
	/*// To get HTS ads totally new
	@Query("select rha from HtsMgmtLookupEntity rha where length(rha.htsCode) = 10 and rha.jurisdiction = true and  (rha.endDate is null or rha.endDate >= :today) and "
			+ "not exists (select prev.htsCode from HtsMgmtLookupEntity prev where prev.htsCode = rha.htsCode and (prev.endDate is not null and (prev.lastUpdateTS >= :dateStart and prev.lastUpdateTS <= :dateEnd ) ) )")
	public List<HtsMgmtLookupEntity> obtainLatestRefAdsforSpecialReport(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("today") Date today);

	// To get HTS removes complete removes
	@Query("select prev from HtsMgmtLookupEntity prev where length(prev.htsCode) = 10 and prev.jurisdiction = true and  (prev.endDate is not null and (prev.lastUpdateTS >= :dateStart and prev.lastUpdateTS <= :dateEnd ) ) and "
			+ "not exists (select latest.htsCode from HtsMgmtLookupEntity latest where latest.htsCode = prev.htsCode and (latest.endDate is null or latest.endDate >= :today ) )")
	public List<HtsMgmtLookupEntity> obtainRefRemovesforSpecialReport(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("today") Date today);*/
	
	// Current Source -- All Adds for AppendixB summary report -- current source
	@Query("select latest from HtsMgmtLookupEntity latest where length(latest.htsCode) = 10 and latest.jurisdiction = true and (latest.endDate is null  or latest.endDate >= :today )  and (latest.startDate >= :dateStart and latest.startDate <= :dateEnd ) and  "
			+ "not exists (select prev.htsCode from HtsMgmtLookupEntity prev where prev.htsCode = latest.htsCode and prev.jurisdiction=true and "
			+ "(prev.endDate is not null and (prev.endDate >= :dateStart and prev.endDate <= :dateEnd ) ) ) ")
	public List<HtsMgmtLookupEntity> obtainRefAllAddsforSpecialReport(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("today") Date today);

	// Current source - all removes
	@Query("select prev from HtsMgmtLookupEntity prev where length(prev.htsCode) = 10 and prev.jurisdiction = true and  (prev.endDate is not null and (prev.endDate >= :dateStart and prev.endDate <= :dateEnd ) ) and "
			+ "not exists (select latest.htsCode from HtsMgmtLookupEntity latest where latest.htsCode = prev.htsCode and latest.jurisdiction = true and (latest.endDate is null  or latest.endDate >= :today) )")
	public List<HtsMgmtLookupEntity> obtainRefAllRemovesforSpecialReport(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("today") Date today);
	
	// 3 months from finalize date  = end date-- sunset codes
	@Query("select prev from HtsMgmtLookupEntity prev where length(prev.htsCode) = 10 and prev.jurisdiction = true and  (prev.endDate is not null and (prev.endDate >= :dateStart and prev.endDate <= :dateEnd ) ) and "
			+ "not exists (select latest.htsCode from HtsMgmtLookupEntity latest where latest.htsCode = prev.htsCode and latest.jurisdiction = true and latest.endDate is null)")
	public List<HtsMgmtLookupEntity> obtainRefsunsetRemovesCurrentforSpecialReport(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);
	
    // only for Appendix B Summary during ITC_UPLOAD_WIP: 10-digit add, where (1) jurisdiction true and (2) no matching jurisdiction-true remove
	@Query("select latest from CpscHtsManagementEntity latest where length(latest.htsCode) = 10 and latest.changeStatus = 'add' and latest.jurisdiction = true and latest.username = :username and "
			+ "not exists (select prev.htsCode from CpscHtsManagementEntity prev where prev.changeStatus = 'remove' and prev.jurisdiction = true and prev.htsCode = latest.htsCode)" )	
	public List<CpscHtsManagementEntity> obtainChmeForStateITCUploadWIPAddsAppendixBSummary(@Param("username") String username);
	
    // only for Appendix B Summary during ITC_UPLOAD_WIP: 10-digit remove, where (1) jurisdiction true and (2) no matching jurisdiction-true add
	@Query("select prev from CpscHtsManagementEntity prev where length(prev.htsCode) = 10 and prev.changeStatus = 'remove' and prev.jurisdiction = true and prev.username = :username and "
			+ "not exists (select latest.htsCode from CpscHtsManagementEntity latest where latest.changeStatus = 'add' and latest.jurisdiction = true and latest.htsCode = prev.htsCode)" )	
	public List<CpscHtsManagementEntity> obtainChmeForStateITCUploadWIPRemovesAppendixBSummary(@Param("username") String username);

    // only for Appendix B Summary during ITC_UPLOAD_WIP: 10-digit none, where (1) jurisdiction true and (2) jurisdictionModified true
	@Query("select chme from CpscHtsManagementEntity chme where length(chme.htsCode) = 10 and chme.changeStatus = 'none' and chme.username = :username "
			+ "and chme.jurisdiction = true and chme.jurisdictionModified = true" )	
	public List<CpscHtsManagementEntity> obtainChmeForStateITCUploadWIPNonesWithJTrueAndModdedJAppendixBSummary(@Param("username") String username);
	
    // only for Appendix B Summary during ITC_UPLOAD_WIP: 10-digit none, where (1) jurisdiction false and (2) jurisdictionModified true
	@Query("select chme from CpscHtsManagementEntity chme where length(chme.htsCode) = 10 and chme.changeStatus = 'none' and chme.username = :username "
			+ "and chme.jurisdiction = false and chme.jurisdictionModified = true" )	
	public List<CpscHtsManagementEntity> obtainChmeForStateITCUploadWIPNonesWithJFalseAndModdedJAppendixBSummary(@Param("username") String username);
	
	// CPSC_CURRENT_WIP Source -- All Adds for AppendixB summary report
	@Query("select scratch from CpscHtsManagementEntity scratch where length(scratch.htsCode) = 10 and scratch.username = :username and "
			+ "not exists (select ref.htsCode from HtsMgmtLookupEntity ref where ref.htsCode = scratch.htsCode and ref.jurisdiction=scratch.jurisdiction and "
			+ "(ref.endDate is null  or ref.endDate >= :today) ) ")
	public List<CpscHtsManagementEntity> obtainRefsForSourceCPSCcurrentWIP(@Param("username") String username,@Param("today") Date today);
	// End of Special Report Queries
	

}