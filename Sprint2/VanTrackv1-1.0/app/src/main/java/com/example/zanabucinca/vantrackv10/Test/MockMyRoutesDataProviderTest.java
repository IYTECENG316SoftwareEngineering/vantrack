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
		
		String[] exampleData = {"TEST-TEST"};
		String[] examleProjection = new String[] { MyRoutesContract.ROUTE_NAME};
		MatrixCursor matrixCursor = new MatrixCursor(examleProjection);
		matrixCursor.addRow(exampleData);

		
		HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
		mockProvider.addQueryResult(MyRoutesContract.CONTENT_URI, matrixCursor);

		
		MockContentResolver mockResolver = new MockContentResolver();
		mockResolver.addProvider(MyRoutesContract.AUTHORITY, mockProvider);

		
		ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
		mockContext.setContentResolver(mockResolver);

		
		ExampleClassUnderTest underTest = new ExampleClassUnderTest();
		String result = underTest.getRoute(mockContext);
		assertEquals("TEST-TEST",result);
	}

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
		@Override public Context getApplicationContext(){ return this; }
	}

	
	public class ExampleClassUnderTest{
		public  String getRoute(Context context){
			String[] projection = new String[]{ MyRoutesContract.ROUTE_NAME};
			Cursor cursor= context.getContentResolver().query(MyRoutesContract.CONTENT_URI, projection, null, null, null);
			cursor.moveToNext();
			return cursor.getString(cursor.getColumnIndex(MyRoutesContract.ROUTE_NAME));
		}
	}
}
