package com.example.challengelsitec;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		StringBuilder book = new StringBuilder();
		InputStream inputStream = null;
		try {
	        AssetManager assetManager = getAssets();
	        inputStream = assetManager.open("livro.xml");
		} catch (IOException e) {
			Log.e("HW"," IOException" + e.getMessage());
		} finally{
			 book = parseXML(inputStream);
			 Log.i("HW", book.toString());
		}
		TextView txtView = (TextView) findViewById(R.id.text_id);
		txtView.setText(book);
	}
	
	/**
	 * This method parses the xml file and returns all text (XmlPullParser.TEXT) that belongs to a paragraph (<p>) 
	 * which is followed by a <pagenum> tag.
	 * @param inputStream InputStream of the input xml file
	 * @return Returns a String containing all text inside paragraph which are followed by a <pagenum> tag
	 */
	public StringBuilder parseXML(InputStream inputStream) {
		StringBuilder book = new StringBuilder("");
        StringBuilder paragraphs = new StringBuilder("");
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true); 
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inputStream, "UTF-8");
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
            	if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("p"))
            		 paragraphs.setLength(0);
            	else if(eventType == XmlPullParser.TEXT) 
            	     paragraphs.append(xpp.getText());
	            else if ((eventType == XmlPullParser.END_TAG && xpp.getName().equals("p"))){
	            	 if (lookAhead(xpp))
	            		 book.append(paragraphs + " ");
	            	 else
	            		 paragraphs.setLength(0);
	             }
	             eventType = xpp.next();
            }
        }catch(UnsupportedEncodingException e){
            Log.e("HW"," UnsupportedEncodingException" + e.getMessage());
        }catch (XmlPullParserException e) {
			Log.e("HW", "XmlPullParserException" + e.getMessage());
		}catch (IOException e) {
			Log.e("HW", "IOException" + e.getMessage());
		}
		return book;
	}
	
	/**
	 * This method verifies whether the next element is a pagenum tag  (<pagenum>1</pagenum>)
	 * @param xpp The XmlPullParser object used to parse the xml file
	 * @return returns true if the next element is a pagenum tag. False otherwise
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Boolean lookAhead(XmlPullParser xpp) throws XmlPullParserException, IOException {
		int eventType = xpp.next(); 
		if(eventType == XmlPullParser.TEXT && xpp.getText().trim().equals(""))
			eventType = xpp.next();
		return (eventType == XmlPullParser.START_TAG && xpp.getName().equals("pagenum")) ? true:false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_book, container,
					false);
			return rootView;
		}
	}

}
