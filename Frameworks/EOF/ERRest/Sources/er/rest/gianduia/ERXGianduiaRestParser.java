package er.rest.gianduia;

import java.net.MalformedURLException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import com.webobjects.appserver.WORequest;

import er.extensions.foundation.ERXMutableURL;
import er.rest.ERXRestRequestNode;
import er.rest.format.ERXJSONRestParser;
import er.rest.format.ERXJSONRestWriter;
import er.rest.format.ERXRestFormat;
import er.rest.format.ERXStringRestRequest;
import er.rest.format.ERXWORestRequest;
import er.rest.format.IERXRestParser;
import er.rest.format.IERXRestRequest;

// ignore me .. i don't do anything real yet
public class ERXGianduiaRestParser implements IERXRestParser {
	public ERXRestRequestNode parseRestRequest(WORequest request, ERXRestFormat.Delegate delegate) {
		return parseRestRequest(new ERXWORestRequest(request), delegate);
	}
	
	@Deprecated
	public ERXRestRequestNode parseRestRequest(String contentStr, ERXRestFormat.Delegate delegate) {
		return parseRestRequest(new ERXStringRestRequest(contentStr), delegate);
	}

	public ERXRestRequestNode parseRestRequest(IERXRestRequest request, ERXRestFormat.Delegate delegate) {
		ERXRestRequestNode rootRequestNode = null;
		String contentString = request.stringContent();
		if (contentString != null && contentString.length() > 0) {
			// MS: Support direct updating of primitive type keys -- so if you don't want to
			// wrap your request in XML, this will allow it
			// if (!contentStr.trim().startsWith("<")) {
			// contentStr = "<FakeWrapper>" + contentStr.trim() + "</FakeWrapper>";
			// }

			ERXMutableURL url = new ERXMutableURL();
			try {
				url.setQueryParameters(contentString);
			}
			catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
			String requestJSON = url.queryParameter("requestJSON");
			
			JSON rootJSON = JSONSerializer.toJSON(requestJSON, ERXJSONRestWriter._config);
			System.out.println("ERXGianduiaRestParser.parseRestRequest: " + rootJSON);
			rootRequestNode = ERXJSONRestParser.createRequestNodeForJSON(null, rootJSON, true, delegate);
		}
		else {
			rootRequestNode = new ERXRestRequestNode(null, true);
			rootRequestNode.setNull(true);
		}

		return rootRequestNode;
	}
}
