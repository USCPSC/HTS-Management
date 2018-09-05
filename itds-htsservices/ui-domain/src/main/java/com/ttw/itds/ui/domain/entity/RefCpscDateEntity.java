package com.ttw.itds.ui.domain.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ttw.itds.ui.domain.entity.BaseItdsEntity;

@Entity
@Table(name = "REF_CPSC_DATE")
public class RefCpscDateEntity extends BaseItdsEntity{
	private static final long serialVersionUID = 1449382955446L;
	
	public RefCpscDateEntity() {
		super();
	}
	
	@Id
	@Column(name = "ID", nullable = false)
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "DAY_DATE", nullable = false)
	private Date dayDate;
	public Date getDayDate() {
		return dayDate;
	}
	public void setDayDate(Date dayDate) {
		this.dayDate = dayDate;
	}

	@Column(name = "DAY_NAME", nullable = false)
	private String dayName;
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	@Column(name = "DAY_OF_MONTH_NUMBER", nullable = false)
	private int dayOfMonthNumber;
	public int getDayOfMonthNumber() {
		return dayOfMonthNumber;
	}
	public void setDayOfMonthNumber(int dayOfMonthNumber) {
		this.dayOfMonthNumber = dayOfMonthNumber;
	}

	@Column(name = "DAY_OF_WEEK_NUMBER", nullable = false)
	private int dayOfWeekNumber;
	public int getDayOfWeekNumber() {
		return dayOfWeekNumber;
	}
	public void setDayOfWeekNumber(int dayOfWeekNumber) {
		this.dayOfWeekNumber = dayOfWeekNumber;
	}

	@Column(name = "DAY_OF_YEAR_NUMBER", nullable = false)
	private int dayOfYearNumber;
	public int getDayOfYearNumber() {
		return dayOfYearNumber;
	}
	public void setDayOfYearNumber(int dayOfYearNumber) {
		this.dayOfYearNumber = dayOfYearNumber;
	}

	@Column(name = "DAYS_IN_MONTH_QTY", nullable = false)
	private int daysInMonthQty;
	public int getDaysInMonthQty() {
		return daysInMonthQty;
	}
	public void setDaysInMonthQty(int daysInMonthQty) {
		this.daysInMonthQty = daysInMonthQty;
	}

	@Column(name = "FISCAL_QUARTER", nullable = false)
	private int fiscalQuarter;
	public int getFiscalQuarter() {
		return fiscalQuarter;
	}
	public void setFiscalQuarter(int fiscalQuarter) {
		this.fiscalQuarter = fiscalQuarter;
	}
	
	@Column(name = "FISCAL_QUARTER_PRETTY", nullable = false)
	private String fiscalQuarterPretty;
	public String getFiscalQuarterPretty() {
		return fiscalQuarterPretty;
	}
	public void setFiscalQuarterPretty(String fiscalQuarterPretty) {
		this.fiscalQuarterPretty = fiscalQuarterPretty;
	}
	
	@Column(name = "FISCAL_YEAR", nullable = false)
	private int fiscalYear;
	public int getFiscalYear() {
		return fiscalYear;
	}
	public void setFiscalYear(int fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	
	@Column(name = "MONTH_NAME", nullable = false)
	private String monthName;
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	
	@Column(name = "MONTH_NUMBER", nullable = false)
	private int monthNumber;
	public int getMonthNumber() {
		return monthNumber;
	}
	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	@Column(name = "QUARTER_NAME", nullable = false)
	private String quarterName;
	public String getQuarterName() {
		return quarterName;
	}
	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}
	
	@Column(name = "QUARTER_NUMBER", nullable = false)
	private int quarterNumber;
	public int getQuarterNumber() {
		return quarterNumber;
	}
	public void setQuarterNumber(int quarterNumber) {
		this.quarterNumber = quarterNumber;
	}

	@Column(name = "WEEK_OF_MONTH_NUMBER", nullable = false)
	private int weekOfMonthNumber;
	public int getWeekOfMonthNumber() {
		return weekOfMonthNumber;
	}
	public void setWeekOfMonthNumber(int weekOfMonthNumber) {
		this.weekOfMonthNumber = weekOfMonthNumber;
	}

	@Column(name = "WEEK_OF_YEAR_NUMBER", nullable = false)
	private int weekOfYearNumber;
	public int getWeekOfYearNumber() {
		return weekOfYearNumber;
	}
	public void setWeekOfYearNumber(int weekOfYearNumber) {
		this.weekOfYearNumber = weekOfYearNumber;
	}

	@Column(name = "WEEKEND_INDICATOR", nullable = false)
	private int weekendIndicator;
	public int getWeekendIndicator() {
		return weekendIndicator;
	}
	public void setWeekendIndicator(int weekendIndicator) {
		this.weekendIndicator = weekendIndicator;
	}

	@Column(name = "YEAR_NUMBER", nullable = false)
	private int yearNumber;
	public int getYearNumber() {
		return yearNumber;
	}
	public void setYearNumber(int yearNumber) {
		this.yearNumber = yearNumber;
	}

	@Column(name = "CREATE_USER_ID", nullable = false)
	private String createUserId;
	public String getCreateUserId() {    
		return createUserId;    
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId; 
	}

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;
	public Date getCreateTimestamp() {    
		return createTimestamp;    
	}
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp; 
	}

	@Column(name = "LAST_UPDATE_USER_ID", nullable = false)
	private String lastUpdateUserId;
	public String getLastUpdateUserId() {    
		return lastUpdateUserId;    
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId; 
	}

	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimestamp;
	public Date getLastUpdateTimestamp() {    
		return lastUpdateTimestamp;    
	}
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp; 
	}

}