package gov.cpsc.hts.itds.ui.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.cpsc.hts.itds.ui.domain.entity.RefApplicationAction;
import gov.cpsc.hts.itds.ui.domain.entity.RefRoleEntity;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponseCodeEnum;

import gov.cpsc.hts.itds.ui.domain.entity.User;
import gov.cpsc.hts.itds.ui.domain.repository.UserRepository;

@Service
public class UserService extends BaseItdsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    public ServiceResponse<Page<User>> getUsersListService(Map<String, String> queries, Pageable pageable) {
        ServiceResponse<Page<User>> response = new ServiceResponse<>();
        try {
            Page<User> listUser = userRepository.findByUsernameContainingAndFirstnameContainingAndLastnameContainingAndEmailContainingAndOrgcodeContaining(
                    queries.get("username"),queries.get("firstname"),queries.get("lastname"),queries.get("email"),queries.get("orgcode"),pageable);

            List<User> userContents = new ArrayList<>();

                for (User user : listUser) {
                    User newUser = new User();
                    newUser.setUsername(user.getUsername());
                    newUser.setFirstname(user.getFirstname());
                    newUser.setLastname(user.getLastname());
                    newUser.setEmail(user.getEmail());
                    newUser.setOrgcode(user.getOrgcode());
                    userContents.add(newUser);
                }
            Page<User> page = new PageImpl<>(userContents, pageable, listUser.getTotalElements());

            response.setValue(page);
        } catch (Exception e) {
            logger.error("error getting user list", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponse<User> getUserByUsername(String username) {
        ServiceResponse<User> response = new ServiceResponse<>();
        try {
            User user = userRepository.findOneByUsername(username);
            response.setValue(user);
        } catch (Exception e) {
            logger.error("error getting single user: " + username, e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional(readOnly = true)
    public ServiceResponse<User> getUserWithPortsByUsername(String username) {
        ServiceResponse<User> response = new ServiceResponse<>();
        try {
            User user = userRepository.findOneByUsername(username);
            user.setPassword(null);
            response.setValue(user);
        } catch (Exception e) {
            logger.error("error getting single user with ports: " + username, e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponse<Boolean> isUsernameUnique(String username) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        boolean isUnique = false;

        try {
            List<User> users = userRepository.findByUsername(username);
            if (users.isEmpty())
                isUnique = true;
        } catch (Exception e) {
            logger.error("error checking username uniqueness: " + username, e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }

        response.setValue(isUnique);
        return response;
    }

    @Transactional
    public ServiceResponse<User> saveUser(User user) {
        ServiceResponse<User> response = new ServiceResponse<>();
        boolean isNewRcd = true; //gz++
        try {
            if (user.getPassword() != null && user.getPassword() != "") {
                String hashedPassword = new Sha1Hash(user.getPassword()).toBase64();
                user.setPassword(hashedPassword);
            } else {
                User oldUser = userRepository.findOneByUsername(user.getUsername());
                user.setPassword(oldUser.getPassword());
            }
            
            //gz++
            boolean activeStatusChanged = false;
            String activeChange = "";
            User oldUser = userRepository.findOneByUsername(user.getUsername());
            if(  oldUser!=null)
            {
            	isNewRcd = false;
            	if (oldUser.getActive() != user.getActive())
            	{
            		activeStatusChanged = true;
            		if (user.getActive() == true)
            			activeChange = "ACTIVE";
            		else
            			activeChange = "INACTIVE";
            	}
             }
            
            User userRtn = userRepository.saveAndFlush(user);
            response.setValue(userRtn);
            Set<User> supervisors = userRtn.getSupervisors();
            User supervisor;
            String supvEmail ="";
            if (!supervisors.isEmpty())
                supvEmail = supervisors.iterator().next().getEmail();
         
            
            //gz++ send UserAccountChangeEmail
            if (isNewRcd)
            {	 
            	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            	String toUserEmailAddress = user.getEmail();
            	String subject="User Account Change Notification";
       		    String body="The following RAM 2.0 user account has been CREATED by "+user.getLastupdateuserid()+ " at "
       		                + timeStamp+ "\nUser ID: "+user.getUsername()+"\tLast Name: "+user.getLastname()
       		                +"\tFirst Name: "+user.getFirstname();
            }
            if (!isNewRcd && activeStatusChanged)
            {	 
            	String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            	String toUserEmailAddress = user.getEmail();
       		    String subject="User Account Change Notification";
       		    String body="The following RAM 2.0 user account status has been changed to " + activeChange + " by "+user.getLastupdateuserid()
       		                 + " at "+ timeStamp+ "\nUser ID:"+user.getUsername()+"\tLast Name: "+user.getLastname()
       		                +"\tFirst Name: "+user.getFirstname();
            }
            //gz
        } catch (Exception e) {
            logger.error("error saving user", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional
    public ServiceResponse<User> saveSelf(User user) {
        User existingUser = userRepository.findOneByUsername(user.getUsername());
        user.setOrgcode(existingUser.getOrgcode());
        user.setExisLocation(existingUser.getExisLocation());
        user.setCreateTimestamp(existingUser.getCreateTimestamp());
        user.setCreateuserid(existingUser.getCreateuserid());
        user.setRefRoles(existingUser.getRefRoles());
        user.setSupervisors(existingUser.getSupervisors());
        ServiceResponse<User> response = saveUser(user);

        return response;
    }

    @Transactional
    public ServiceResponse<User> updateLoginTime(String username) {
        ServiceResponse<User> response = new ServiceResponse<>();
        try {
            User user = userRepository.findOneByUsername(username);
            DateTime date = new DateTime();
            user.setLastlogintimestamp(date);
            User userRtn = userRepository.saveAndFlush(user);
            response.setValue(userRtn);
        } catch (Exception e) {
            logger.error("error saving user", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional
    public ServiceResponse<User> updateLogoutTime(String username) {
        ServiceResponse<User> response = new ServiceResponse<>();
        try {
            User user = userRepository.findOneByUsername(username);
            DateTime date = new DateTime();
            user.setLastlogouttimestamp(date);
            User userRtn = userRepository.saveAndFlush(user);
            response.setValue(userRtn);
        } catch (Exception e) {
            logger.error("error saving user", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional
   //gz-- public ServiceResponse deleteUser(String username) {
      public ServiceResponse deleteUser(String username,User actingUser) {    //gz++
        ServiceResponse response = new ServiceResponse<>();
        try {
        	//gz send delete notification email
        	User user = userRepository.findOneByUsername(username);
        	String toUserEmailAddress = user.getEmail();
        	String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        	String subject="User Account Change Notification";
   		    String body="The following RAM 2.0 user account has been DELETED by " + actingUser.getUsername() + " at "
   		                + timeStamp+ "\nUser ID: "+user.getUsername()+"\tLast Name: "+user.getLastname()
   		                +"\tFirst Name: "+user.getFirstname();
        	Set<User> supervisors = user.getSupervisors();
            String supvEmail ="";
            if (!supervisors.isEmpty())
                supvEmail = supervisors.iterator().next().getEmail(); 
            
            userRepository.deleteByUsername(username);
            response.setResponseCode(ServiceResponseCodeEnum.OK);
        } catch (Exception e) {
            logger.error("error saving user", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponse<User> authenticateUser( String username, String password) {
        ServiceResponse<User> response = new ServiceResponse<>();

        if (isUserAuthenticated(username, password)) {
            User user = userRepository.findOneByUsername(username);
            user.setPassword(null);
            response.setValue(user);
        } else {
            logger.error("Authentication Error: " + username);
            response.setResponseCode(ServiceResponseCodeEnum.UNAUTHORIZED);
        }

        return response;

    }

    public boolean isUserAuthenticated(String username, String password){
        boolean authenticated = false;

        try {
            User user = userRepository.findOneByUsername(username);
            String hashedPassword = new Sha1Hash(password).toBase64();
            if (user.getPassword().equalsIgnoreCase(hashedPassword)) {
                user.setPassword(null);
                authenticated = true;
            }
        } catch (Exception e) {
            // user not found
        }

        return authenticated;
    }

    public boolean isUserAuthorized(String username, String actionCode){
        boolean authorized = false;

        try {
            User user = userRepository.findOneByUsername(username);
            Set<RefRoleEntity> roles = user.getRefRoles();
            for (RefRoleEntity role : roles) {
                Set<RefApplicationAction> actions = role.getRefApplicationActions();
                for (RefApplicationAction action : actions) {
                    if (actionCode.equals(action.getActionCode())) {
                        authorized = true;
                        break;
                    }
                }
                if (authorized)
                    break;
            }
        } catch (Exception e) {
            // user not found
        }

        return authorized;
    }

   public List<User> findSubordinates(List<String> usernames) { 
	   // list the supervisors
	   List<User> retUsers = new ArrayList<User>();
	   List<User> uList1 = null;
	   List<User> uList2 = null;
	   List<User> uList3 = null;
	   
       try {
    	   uList1 = userRepository.findSubordinates(usernames);
    	   if(uList1 != null && uList1.size()>0) {
    		   retUsers.addAll(uList1);
    		   uList2 = userRepository.findSubordinatesByUser(uList1);   
    	   }
    	   if(uList2 != null && uList2.size()>0) {
    		   retUsers.addAll(uList2);
    		   uList3 = userRepository.findSubordinatesByUser(uList3);   
    	   }
    	   if(uList3 != null && uList3.size()>0) {
    		   retUsers.addAll(uList3);
    	   }
    	   
           
       } catch (Exception e) {
           // user not found
       
       }
       return retUsers;
   }
   
   public List<String> findSubordinateUsernames(List<String> usernames) { 
	   // list the supervisors
	   Set<String> userNameSet = new HashSet<String>();

	   List<User> userList = new ArrayList<User>();
	   List<User> uList1 = null;
	   List<User> uList2 = null;
	   List<User> uList3 = null;
	   
       try {
    	   uList1 = userRepository.findSubordinates(usernames);
    	   if(uList1 != null && uList1.size()>0) {
    		   userList.addAll(uList1);
    		   uList2 = userRepository.findSubordinatesByUser(uList1);   
    	   }
    	   if(uList2 != null && uList2.size()>0) {
    		   userList.addAll(uList2);
    		   uList3 = userRepository.findSubordinatesByUser(uList3);   
    	   }
    	   if(uList3 != null && uList3.size()>0) {
    		   userList.addAll(uList3);
    	   }
		
    	   for(User u: userList) {
    		   userNameSet.add(u.getUsername());
    	   }
           
       } catch (Exception e) {
           // user not found
       
       }
       return new ArrayList<>(userNameSet);
   }

   public List<String> findAdditionalSubodinateUsernames(String username) { 
	   List<String> usernames = new ArrayList<String>();
	   return usernames;
	   
   }
   
}
