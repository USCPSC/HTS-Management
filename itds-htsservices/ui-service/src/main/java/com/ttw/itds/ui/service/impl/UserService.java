package com.ttw.itds.ui.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.text.SimpleDateFormat; //gz
import java.util.Date;  //gz

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttw.itds.ui.domain.entity.RefApplicationAction;
import com.ttw.itds.ui.domain.entity.RefRoleEntity;
import com.ttw.itds.ui.domain.entity.User;
import com.ttw.itds.ui.domain.repository.UserRepository;

@Service
public class UserService extends BaseItdsServiceImpl {

    private final Logger log = Logger.getLogger(UserService.class);

    @Inject
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
            log.error("error getting user list", e);
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
            log.error("error getting single user: " + username, e);
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
            log.error("error checking username uniqueness: " + username, e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }

        response.setValue(isUnique);
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
            log.error("error saving user", e);
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
            log.error("error saving user", e);
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
            log.error("Authentication Error: " + username);
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
    	return true;
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
