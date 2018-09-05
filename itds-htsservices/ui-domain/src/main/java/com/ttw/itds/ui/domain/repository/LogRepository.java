package com.ttw.itds.ui.domain.repository;

import com.ttw.itds.ui.domain.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository  extends JpaRepository<LogEntity,Long>{
    
}
