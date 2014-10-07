package oauthmodule.actions.custom;

import oauthmodule.proxies.OAuthLog;
import oauthmodule.proxies.OAuthMessage;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
/**
 * HelperClass for creating logrecords in the Mendix application
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class LogRecordHandler {

	protected void createLogRecord(String msgData, OAuthMessage msgType) throws CoreException{
		try {
			IContext context = Core.createSystemContext();
			OAuthLog logMessage = new OAuthLog(context);
			logMessage.setIncomingDataDetail(msgData);
			logMessage.setMessage(msgType);
			logMessage.commit();
		} catch (Exception e) {
			Core.getLogger("OauthCallback").info("An error occured when creating the log record. /n"+e.getStackTrace());
			e.printStackTrace();
		}
	}
}
