package gov.cpsc.itds.common.disk;

import gov.cpsc.itds.common.properties.PropertiesHandler;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiskSpaceUtil {
	private static final Logger logger = LoggerFactory.getLogger(DiskSpaceUtil.class);

	public boolean isDiskException() throws IOException {
		throw new IOException();
	}

	public static boolean isDiskAvailable() {
		String homedir = PropertiesHandler.getProperty("DATAMANAGER_HOME");
		File file = new File(homedir);
		long totalSpace = file.getTotalSpace(); 
		long usableSpace = file.getUsableSpace();
		long percentageAvailable = usableSpace * 100 / totalSpace;
		logger.debug("Total percentage of freediskspace--{}", percentageAvailable);
     	String minDiskFree = PropertiesHandler.getProperty("diskFree");
 		Long.parseLong(minDiskFree);
		boolean isDiskAvailable = false;
		if (percentageAvailable > Long.parseLong(minDiskFree)) {
			isDiskAvailable = true;
		}
		logger.debug("isDiskAvailable {} ",isDiskAvailable);
		return isDiskAvailable;

	}

	public long diskFullDelay() {
		long diskFullDelay = PropertiesHandler.getPropertyLong("diskFullDelay");
		logger.debug("Disk is full suspending sftp process for {}",diskFullDelay);
		return diskFullDelay;

	}

}
