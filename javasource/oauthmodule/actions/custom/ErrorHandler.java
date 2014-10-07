package oauthmodule.actions.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.mendix.core.Core;
/**
 * HelperClass for displaying html pages from the resource directory when errors are
 * encountered
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class ErrorHandler {

	protected void processErrorHandler(HttpServletResponse servletResponse,
			String resourceDir, String fileName) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(new File(Core.getConfiguration()
					.getResourcesPath().getPath(), resourceDir), fileName));
			servletResponse.setContentType("text/html");
			PrintWriter writer = servletResponse.getWriter();
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			servletResponse.setContentLength(bytes.length);
			writer.print(new String(bytes));
			writer.flush();
			writer.close();
			is.close();
		} catch (Exception e) {
			if (is != null) {
				is.close();
			}
			Core.getLogger("OauthCallback").error(
					"File: " + fileName + " not found in the resources/"
							+ resourceDir + " directory!");
			e.printStackTrace();
		}
	}
}
