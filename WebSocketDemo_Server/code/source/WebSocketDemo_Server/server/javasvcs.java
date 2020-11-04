package WebSocketDemo_Server.server;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class javasvcs

{
	// ---( internal utility methods )---

	final static javasvcs _instance = new javasvcs();

	static javasvcs _newInstance() { return new javasvcs(); }

	static javasvcs _cast(Object o) { return (javasvcs)o; }

	// ---( server methods )---




	public static final void getStockPrice (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getStockPrice)>> ---
		// @sigtype java 3.5
		// [i] field:0:required html
		// [o] field:0:required price
		// [o] field:0:required companyName
		// [o] field:0:required clientMessage
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	html = IDataUtil.getString( pipelineCursor, "html" );
		pipelineCursor.destroy();
		String priceStartIndex = "data-reactid=\"50\">";
		String priceEndIndexStr="</span>";
		String companyNameIndex = "<h1 class=\"D(ib) Fz(18px)\" data-reactid=\"7\">";
		String companyEndIndex= "</h1>";
		String timestartIndex ="<span data-reactid=\"53\">"; 
		String endTimeIndex = "</span>";
		String price= html.substring( html.indexOf(priceStartIndex) + priceStartIndex.length());
		price = price.substring(0, price.indexOf(priceEndIndexStr));
		String companyName= html.substring( html.indexOf(companyNameIndex) + companyNameIndex.length());
		companyName = companyName.substring(0, companyName.indexOf(companyEndIndex));
		
		String time= html.substring( html.indexOf(timestartIndex) + timestartIndex.length());
		time = time.substring(0, time.indexOf(endTimeIndex));
		
		
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "price", price );
		IDataUtil.put( pipelineCursor_1, "companyName", companyName );
		IDataUtil.put( pipelineCursor_1, "clientMessage", companyName + " Stock price is " + price +"; " + time );
		pipelineCursor_1.destroy();
		
			
		// --- <<IS-END>> ---

                
	}
}

