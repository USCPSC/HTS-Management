package gov.cpsc.itds.entityCommon.entity;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseItdsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public BaseItdsEntity() {
		super();
	}


}