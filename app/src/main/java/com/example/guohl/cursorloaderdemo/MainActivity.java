package com.example.guohl.cursorloaderdemo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity implements
        SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    ListView lstContact;
    CustomContactAdapter adapter;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstContact = (ListView) findViewById(R.id.lstContacts);
        getLoaderManager().initLoader(1, null, this);

        SearchView sv = (SearchView) findViewById(R.id.svSearch);
        sv.setOnQueryTextListener(this);
    }

    //--------------------------------------LoaderManager.LoaderCallbacks---------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        }

        CursorLoader cursorLoader = new CursorLoader(this, baseUri, null,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        adapter = new CustomContactAdapter(this, cursor);
        lstContact.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //--------------------------------------SearchView.OnQueryTextListener-----------------------
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
    }

}