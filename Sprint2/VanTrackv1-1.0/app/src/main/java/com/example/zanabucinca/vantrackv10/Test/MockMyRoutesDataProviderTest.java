package com.example.zanabucinca.vantrackv10.Test;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import com.example.zanabucinca.vantrackv10.Operators.MyRoutesContract;

import java.util.HashMap;


public class MockMyRoutesDataProviderTest extends AndroidTestCase {
	public void testMockRoutesFromMyRoutes(){
		//Step 1: Create data you want to return and put it into a matrix cursor
		//In this case I am mocking getting phone numbers from Contacts Provider
		String[] exampleData = {"TEST-TEST"};
		String[] examleProjection = new String[] { MyRoutesContract.ROUTE_NAME};
		MatrixCursor matrixCursor = new MatrixCursor(examleProjection);
		matrixCursor.addRow(exampleData);

		//Step 2: Create a stub content provider and add the matrix cursor as the expected result of the query
		HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
		mockProvider.addQueryResult(MyRoutesContract.CONTENT_URI, matrixCursor);

		//Step 3: Create a mock resolver and add the content provider.
		MockContentResolver mockResolver = new MockContentResolver();
		mockResolver.addProvider(MyRoutesContract.AUTHORITY /*Needs to be the same as the authority of the provider you are mocking */, mockProvider);

		//Step 4: Add the mock resolver to the mock context
		ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
		mockContext.setContentResolver(mockResolver);

		//Example Test
		ExampleClassUnderTest underTest = new ExampleClassUnderTest();
		String result = underTest.getRoute(mockContext);
		assertEquals("TEST-TEST",result);
	}

	//Specialized Mock Content provider for step 2.  Uses a hashmap to return data dependent on the uri in the query
	public class HashMapMockContentProvider extends MockContentProvider {
		private HashMap<Uri, Cursor> expectedResults = new HashMap<Uri, Cursor>();
		public void addQueryResult(Uri uriIn, Cursor expectedResult){
			expectedResults.put(uriIn, expectedResult);
		}
		@Override
		public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
			return expectedResults.get(uri);
		}
	}

	public class ContextWithMockContentResolver extends RenamingDelegatingContext {
		private ContentResolver contentResolver;
		public void setContentResolver(ContentResolver contentResolver){ this.contentResolver = contentResolver;}
		public ContextWithMockContentResolver(Context targetContext) { super(targetContext, "test");}
		@Override public ContentResolver getContentResolver() { return contentResolver; }
		@Override public Context getApplicationContext(){ return this; } //Added in-case my class called getApplicationContext()
	}

	//An example class under test which queries the populated cursor to get the expected phone number
	public class ExampleClassUnderTest{
		public  String getRoute(Context context){//Query for  phone numbers from contacts
			String[] projection = new String[]{ MyRoutesContract.ROUTE_NAME};
			Cursor cursor= context.getContentResolver().query(MyRoutesContract.CONTENT_URI, projection, null, null, null);
			cursor.moveToNext();
			return cursor.getString(cursor.getColumnIndex(MyRoutesContract.ROUTE_NAME));
		}
	}
}
