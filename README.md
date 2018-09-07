itds-htsui : [![Known Vulnerabilities](https://snyk.io/test/github/USCPSC/HTS-Management/badge.svg?targetFile=itds-htsui%2Fpackage.json)](https://snyk.io/test/github/USCPSC/HTS-Management?targetFile=itds-htsui%2Fpackage.json)

itds-htsui/pom.xml : [![Known Vulnerabilities](https://snyk.io/test/github/USCPSC/HTS-Management/badge.svg?targetFile=itds-htsui%2Fpom.xml)](https://snyk.io/test/github/USCPSC/HTS-Management?targetFile=itds-htsui%2Fpom.xml)



Build and Deployment Instructions for HTS Source Code

(I.) Pre-requisites

	(A.) Git (for cloning the itds-htsservices repository)
	(B.) Maven (for building)
	(C.) a Java application lifecycle container such as JBoss Wildfly
	(D.) a relational database server (RDBMS) such as Microsoft SQL Server 
	(E.) (optional) Eclipse or other IDE for invoking Maven. Alternatively, Maven may be used from a shell prompt or command line. 

(II.) Steps to build and deploy HTS Services

	(A.) For building HTS Services

		(1.) The HTS Services build process requires Maven. The following instructions are for launching the Maven build process from within Eclipse, specifically Eclipse JEE Oxygen, available at http://www.eclipse.org .
		
		A zip version can be downloaded from http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/oxygen/1a/eclipse-jee-oxygen-1a-win32-x86_64.zip .

		Sidenote: The Maven build process can also be invoked directly from a shell prompt or command line, rather than within an IDE such as Eclipse. Maven documentation can be found at https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html .
		
		(2.) Using Git, clone the repository named itds-htsservices to your development environment.
		
		(3.) In the Eclipse menubar, go to File > Import > Existing Maven Projects. For "Root Directory", select the folder itds-htsservices at the top of the new Git repository you created when you cloned. Under "Projects", check all 5 checkboxes, then press Finish. This will import the project and its 4 subprojects into Eclipse. As a result, you should see 5 new entries in your Project Explorer view:
		
			(a.) The top-level project, named itds-ui, corresponding to the directory itds-htsservices
			(b.) Subproject ui-shared, corresponding to the directory itds-htsservices/ui-shared
			(c.) Subproject ui-service, corresponding to the directory itds-htsservices/ui-service
			(d.) Subproject ui-domain, corresponding to the directory itds-htsservices/ui-domain
			(e.) Subproject hts-rest-war, corresponding to the directory itds-htsservices/ui-war
		
		(4.) In the Eclipse menubar, go to Run > Run Configurations and create a new "Maven Build" launch configuration. Use the following values:
		
			(a.) Base directory: ${workspace_loc:/itds-ui}
			(b.) Goals: clean compile package
			(c.) Only 2 checkboxes should be checked: "Skip tests" and "Resolve workspace artifacts"
			
		(5.) You are now able to build the warfile by invoking the run configuration you created. The warfile will be named hts-rest-war-1.1.0.war and will be created in the directory itds-htsservices/ui-war/target by the Maven build process.
		
	(B.) For deploying HTS Services
	
		(1.) After invoking the run configuration described above, confirm that Maven has built hts-rest-war-1.1.0.war (the warfile holding the HTS Services application) and that its timestamp is current.
		
		(2.) Manually copy or move the warfile to the deployment directory of your Java application lifecycle container. For example, if you are using JBoss Wildfly, copy it to {WILDFLY_HOME}/standalone/deployments.
		
		(3.) After the container reports successful deployment, verify that the application is running by hitting its /state endpoint. For example, http://localhost:8080/htsservices-war/state . The /state endpoint is, by design, not restricted by the application's SSO guard, nor is it dependent on any running client. Therefore it should immediately return a meaningful JSON response.
		
		Sidenote: the /state endpoint does, however, depend on meaningful data in the DB table specified in the @Table annotation of the Java class CpscHtsMgmtHistEntity.

(III.) Steps to build and deploy HTS UI

	(A.) For building HTS UI

		(1.) The HTS UI build process is almost identical to that for HTS Services, including the Maven requirement. See step (II.A.1.) above for URLs.
		
		(2.) Using Git, clone the repository named itds-htsui to your development environment.
		
		(3.) In the Eclipse menubar, go to File > Import > Existing Maven Projects. For "Root Directory", select the folder itds-htsui at the top of the new Git repository you created when you cloned. Under "Projects", there will be only 1 checkbox, which is already checked. Press Finish. This will import the project into Eclipse. As a result, you should see 1 new project in your Project Explorer view, named hts-management, corresponding to the directory itds-htsui.
		
		(4.) In the Eclipse menubar, go to Run > Run Configurations and create a new "Maven Build" launch configuration. Use the following values:
		
			(a.) Base directory: ${workspace_loc:/hts-management}
			(b.) Goals: package -P npm.prod
			(c.) Only 2 checkboxes should be checked: "Skip tests" and "Resolve workspace artifacts"
			
		(5.) In the filesystem of your development environment, create a directory named dist under the directory itds-htsui.
			
		(6.) You are now able to build the warfile by invoking the run configuration you created. The warfile will be named hts-management.war and will be created in the directory itds-htsui/target by the Maven build process.
		
	(B.) For deploying HTS UI
	
		(1.) After invoking the run configuration described above, confirm that Maven has built hts-management.war (the warfile holding the HTS UI application) and that its timestamp is current.
		
		(2.) Manually copy or move the warfile to the deployment directory of your Java application lifecycle container. For example, if you are using JBoss Wildfly, copy it to {WILDFLY_HOME}/standalone/deployments.
		
		(3.) After the container reports successful deployment, verify that the application is running by hitting its /view endpoint. For example, http://localhost:8080/hts-management/view. You should see a user interface, including a "User ID" login panel. This shows that the HTS UI is running.
		
(IV.) How to use the itds-common repository

	In the pom.xml files at (1) the top level of the HTS Services project and (2) the top level of its ui-service subproject, the following block

		<dependency>
			<groupId>gov.cpsc.itds.common</groupId>
			<artifactId>itdscommon</artifactId>
			<version>2.4.7</version>
		</dependency>

	... requires you to make the itds-common project available to Maven. To do this in Eclipse, import the itds-common repository as a Maven project in the same way as the other projects you imported. Once imported, Maven will detect and use it automatically.

(V.) Steps to load the lookup table in the database

	In order for the HTS application to be useful, the lookup table (for example, REF_HTS_ALL) in the database must first be loaded by means of a snapback script. A snapback script is included in the itds-htsservices repository. It is named final_snapback_CLEANSED_201807260134.sql and is located in itds-htsservices/setup/db/v1.7.

	The first 3 lines have been altered as a safety precaution, so that they read literally ...

		USE <SPECIFY YOUR DB NAME HERE>
		GO
		SET IDENTITY_INSERT itds_exam.<SPECIFY YOUR TABLE NAME HERE SUCH AS REF_HTS_ALL_TEST> ON

	... which is a safeguard against the file ever being executed prematurely.

	Execute the following SQL statements:

		SELECT COUNT(ID) FROM YOUR_DATABASE_NAME.itds_exam.REF_HTS_ALL_TEST
		DELETE FROM YOUR_DATABASE_NAME.itds_exam.REF_HTS_ALL_TEST
		SELECT COUNT(ID) FROM YOUR_DATABASE_NAME.itds_exam.REF_HTS_ALL_TEST

	Confirm the count has dropped to zero.

	Open and execute the final snapback script after fixing the first 3 lines (as mentioned above).
	
(VI.) External configuration files used by Wildfly

	Examples of these are located in itds-htsservices/setup/configuration_templates.
	
