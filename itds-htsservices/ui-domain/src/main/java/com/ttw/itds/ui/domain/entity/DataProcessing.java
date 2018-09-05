package com.ttw.itds.ui.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "DATA_PROCESSING")
public class DataProcessing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 10)
    @Column(name = "date")
    private String date;
    @Column(name = "entry_files_received")
    private Long entryFilesReceived;
    @Column(name = "entry_summary_files_received")
    private Long entrySummaryFilesReceived;
    @Column(name = "event_files_received")
    private Long eventFilesReceived;
    @Column(name = "entry_lines_received")
    private Long entryLinesReceived;
    @Column(name = "entry_lines_new")
    private Long entryLinesNew;
    @Column(name = "entry_lines_updated")
    private Long entryLinesUpdated;
    @Column(name = "entry_summary_lines_received")
    private Long entrySummaryLinesReceived;

    public DataProcessing() {
    }

    public DataProcessing(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getEntryFilesReceived() {
        return entryFilesReceived;
    }

    public void setEntryFilesReceived(Long entryFilesReceived) {
        this.entryFilesReceived = entryFilesReceived;
    }

    public Long getEntrySummaryFilesReceived() {
        return entrySummaryFilesReceived;
    }

    public void setEntrySummaryFilesReceived(Long entrySummaryFilesReceived) {
        this.entrySummaryFilesReceived = entrySummaryFilesReceived;
    }

    public Long getEventFilesReceived() {
        return eventFilesReceived;
    }

    public void setEventFilesReceived(Long eventFilesReceived) {
        this.eventFilesReceived = eventFilesReceived;
    }

    public Long getEntryLinesReceived() {
        return entryLinesReceived;
    }

    public void setEntryLinesReceived(Long entryLinesReceived) {
        this.entryLinesReceived = entryLinesReceived;
    }

    public Long getEntryLinesNew() {
        return entryLinesNew;
    }

    public void setEntryLinesNew(Long entryLinesNew) {
        this.entryLinesNew = entryLinesNew;
    }

    public Long getEntryLinesUpdated() {
        return entryLinesUpdated;
    }

    public void setEntryLinesUpdated(Long entryLinesUpdated) {
        this.entryLinesUpdated = entryLinesUpdated;
    }

    public Long getEntrySummaryLinesReceived() {
        return entrySummaryLinesReceived;
    }

    public void setEntrySummaryLinesReceived(Long entrySummaryLinesReceived) {
        this.entrySummaryLinesReceived = entrySummaryLinesReceived;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataProcessing other = (DataProcessing) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DataProcessing{" + "id=" + id + ", date=" + date + ", entryFilesReceived=" + entryFilesReceived + ", entrySummaryFilesReceived=" + entrySummaryFilesReceived + ", eventFilesReceived=" + eventFilesReceived + ", entryLinesReceived=" + entryLinesReceived + ", entryLinesNew=" + entryLinesNew + ", entryLinesUpdated=" + entryLinesUpdated + ", entrySummaryLinesReceived=" + entrySummaryLinesReceived + '}';
    }
}
