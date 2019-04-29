
package gov.cpsc.hts.itds.ui.shared.dto;

import java.util.Date;
import java.util.List;

/**
 * @author hzhao
 * 
 * This parameterized DTO object is designed to carry a change request for a single resource.
 * The REST API can accept a list of these DTOs as input to support bulk operations, with transaction support.
 * 
 * Each object carries all the information needed to make updates to a resource/sub-resources
 * to include add, delete and modify. Each instance represents an operation for a ***single*** resource/sub-resource, denoted by its path("URI").
 * 
 * T is the top level resource, which references sub-resources
 * 
 * Examples:
 * 
 change exam detail, change number field: 
    {
        "op": "update",
        "path" : "exams/M9874654652",  
        "lastModifiedTimestamp": "2012-04-23T18:25:43.511Z",
        "nullValuedNonStringFields": ["travelTime"],
        "value" : {
              "remarks" : "remarks in EXAM table",
              "activityTime" : "1.5"
      	}
    }

 change exam detail, change string to empty: 
    {
        "op": "update",
        "path" : "exams/M9874654652",  
        "lastModifiedTimestamp": "2012-04-23T18:25:43.511Z",
        "value" : {
              "examDate" : "10/27/2011",
              "investigator" : "nwright",
              "entryNumber" : "M9874654652",
              "remarks" : "remarks texxxxst in EXAM table vvv",
              "activityTime" : "1.5",
              "travelTime" : "1.0",
              "labelMissingIncomplete" : "No",
              "brokerImporterNotified" : "No",
              "portCode" : ""
            }
        },


  Add a new contact:
    {
      "op" : "add",
      "path" : "exams/M9874654652/examContacts",
      "value" : {
            "entryNumber" : "M9874654652",
            "examContacts" : [ 
                {
                  "contactType" : "IMPTR",
                  "name" : "John Doe, Director of Merchandising",
                  "phoneNumber" : "123-456-7890",
                  "email" : "JDoe@fabulous.com"
                }
            ]
        }
    } ,

 Add another new contact:
 {
  "op" : "add",
  "path" : "exams/M9874654652/examContacts",
  "value" : {
    "entryNumber" : "M9874654652",
    "examContacts" : [ {
      "contactType" : "IMPTR",
      "name" : "firstname,last",
      "phoneNumber" : "888-111-2222",
      "email" : "a@b.com"
     } ]
   }
 }  
 
  
  A full request:
[ 
    {
      "op" : "add",
      "path" : "exams/M9874654652/examContacts",
      "value" : {
            "entryNumber" : "M9874654652",
            "examContacts" : [ 
                {
                  "contactType" : "IMPTR",
                  "name" : "John Doe, Director of Merchandising",
                  "phoneNumber" : "123-456-7890",
                  "email" : "JDoe@fabulous.com"
                }
            ]
        }
    } ,

    {
        "op": "update",
        "path" : "exams/M9874654652",  
        "lastModifiedTimestamp": "2012-04-23T18:25:43.511Z",
        "value" : {
              "examId" : "6",
              "examDate" : "10/27/2011",
              "investigator" : "nwright",
              "entryNumber" : "M9874654652",
              "remarks" : "remarks texxxxst in EXAM table vvv",
              "activityTime" : "1.5",
              "travelTime" : "1.0",
              "labelMissingIncomplete" : "No",
              "brokerImporterNotified" : "No",
              "portCode" : ""
            }
        },

    {
        "op": "update",
        "path" : "exams/M9874654652/examSamples/1",  
        "lastModifiedTimestamp": "1000-04-23T18:25:43.511Z",
        "value" : {
              "entryNumber" : "M9874654652",
              "examSamples" : [ 
                  {
                    "id":"1",
                    "sampleNumber" : "12345",
                    "numberProductsScreened" : "1",
                    "itemModel" : "109218",
                    "productDescription" : "test 2.0",
                    "manufacturerId": ""
                  }
                ]
        }
    } ,

    {
        "op": "update",
        "path" : "exams/M9874654652",  
        "lastModifiedTimestamp": "2012-04-23T18:25:43.511Z",
        "nullValuedNonStringFields": ["travelTime"],
        "value" : {
              "remarks" : "remarks in EXAM table",
              "activityTime" : "1.5"
      	}
    }
]  
 */

public class ResourceChangeRequest<T extends ItdsUiBaseDto> {
	/*
	 * change request operation types: add/create, delete/remove, update/modify/destroy
	 */
	public enum ResourceChangeRequestOp  {
			create, delete, update
			//add, create, delete, remove, update, modify, destroy
	}
	
	/*
	 * operation on the resource
	 */
	private ResourceChangeRequestOp op;
	
	/*
	 * this is similar to URI. It points to the target resource, which must be reachable from T
	 */
	private String path;
	
	/*
	 * A DTO object represents the root of the tree, which contains the data to be added, modified or removed. 
	 * In the case of remove, only the resource ID is needed. For sub-resource, it still must start from T
	 */
	private T value;
	
	/* 
	 * prevent updating stale data
	 */
	private Date lastModifiedTimestamp;
	
	/*
	 * flags the date, number types that are modified to be null, but can not make distinction between "not changed" vs "value remove by user"
	 */
	@Deprecated
	private List<String> nullValuedNonStringFields;  
	
	/**
	 * @return the op
	 */
	public ResourceChangeRequestOp getOp() {
		return op;
	}
	/**
	 * @param op the op to set
	 */
	public void setOp(ResourceChangeRequestOp op) {
		this.op = op;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}
	/**
	 * @return the lastModifiedTimestamp
	 */
	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}
	/**
	 * @param lastModifiedTimestamp the lastModifiedTimestamp to set
	 */
	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	/**
	 * @return the nullValuedNonStringFields
	 */
	public List<String> getNullValuedNonStringFields() {
		return nullValuedNonStringFields;
	}
	/**
	 * @param nullValuedNonStringFields the nullValuedNonStringFields to set
	 */
	public void setNullValuedNonStringFields(List<String> nullValuedNonStringFields) {
		this.nullValuedNonStringFields = nullValuedNonStringFields;
	}
	
	
}
