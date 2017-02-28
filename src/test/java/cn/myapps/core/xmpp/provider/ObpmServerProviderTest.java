package cn.myapps.core.xmpp.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import junit.framework.TestCase;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ObpmServerProviderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParseIQ() {
		URL url = ObpmServerProviderTest.class.getResource("service.xml");
		if (url != null) {
			try {
				InputStream is = url.openStream();
				InputStreamReader reader = new InputStreamReader(is, "UTF-8");
				BufferedReader bfReader = new BufferedReader(reader);
				
				StringBuffer xml = new StringBuffer();
				String tmp = null;
				do {
					tmp = bfReader.readLine();
					if (tmp != null) {
						xml.append(tmp).append('\n');
					}
				} while (tmp != null);
				
				XmlPullParser parser = new MXParser();
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
				parser.setInput(new StringReader(xml.toString()));
				
				ObpmServerProvider provider = new ObpmServerProvider();
				provider.parseIQ(parser);
			} catch (IOException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
	}

}
