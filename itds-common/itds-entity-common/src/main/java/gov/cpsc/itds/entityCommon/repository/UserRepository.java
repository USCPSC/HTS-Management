package gov.cpsc.itds.entityCommon.repository;

import gov.cpsc.itds.entityCommon.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long>{

    User findOneByUsername(String username);
    List<User> findByUsername(String username);
    Page<User> findByUsernameContainingAndFirstnameContainingAndLastnameContainingAndEmailContainingAndOrgcodeContaining(
        String username, String firstname, String lastname, String email, String orgcode,
        Pageable pageable);
    Long deleteByUsername(String username);
	List<User> findAllByOrderByFirstnameAsc();
	
	List<User> findByEmailAndLastnameOrderByCreateTimestampDesc(String email, String lastname);

	@Query("select distinct s.id.subordinateUsername from User u, UserSubordinateEntity s where u.username in :usernames and s.id.supervisorUsername = u ")
    List<User> findSubordinates(@Param("usernames") List<String> usernames); // list the subordinates
	
	@Query("select distinct s.id.subordinateUsername from User u, UserSubordinateEntity s where u in :users and s.id.supervisorUsername = u ")
    List<User> findSubordinatesByUser(@Param("users") List<User> users); // list the subordinates

}
