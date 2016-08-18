package io.bitfountain.matthewparker.bitchat;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by branden on 8/10/16.
 */
public class ContactDataSource implements LoaderManager.LoaderCallbacks<Cursor> {
    private static Contact sCurrentUser;

    private Context mContext;
    private Listener mListener;

    ContactDataSource(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    public static Contact getCurrentUser() {
        if (sCurrentUser == null && ParseUser.getCurrentUser() != null) {
            sCurrentUser = new Contact();
            sCurrentUser.setPhoneNumber(ParseUser.getCurrentUser().getUsername());
            sCurrentUser.setName((String) ParseUser.getCurrentUser().get("name"));
        }
        return sCurrentUser;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                null,
                null,
                null
        );
    }

    //when the cursor comes back with all the contacts
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mCursorAdapter.swapCursor(data);
        List<String> numbers = new ArrayList<>();

        data.moveToFirst();
        while (!data.isAfterLast()) {
            String phoneNumber = data.getString(1);
            phoneNumber = phoneNumber.replaceAll("-", "").replaceAll(" ", "");
            numbers.add(phoneNumber);
            data.moveToNext();
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("username", numbers);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    ArrayList<Contact> contacts = new ArrayList<>();
                    for (ParseUser parseUser : list) {
                        Contact contact = new Contact();
                        contact.setName((String) parseUser.get("name"));
                        contact.setPhoneNumber(parseUser.getUsername());
                        contacts.add(contact);
                    }
                    if (mListener != null) {
                        mListener.onFetchedContacts(contacts);
                    }
                } else {
                    Log.d("ContactDataSource", e.getMessage().toString());
                }
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    //constraint to bring in Listener
    public interface Listener {
        void onFetchedContacts(ArrayList<Contact> contacts);
    }





    //this is similar to a DB query, it's a projection of the columns I want to return
//        String[] columns = {ContactsContract.CommonDataKinds.Phone.NUMBER,
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

//        //note that actual query needs the ID b/c of the cursorAdapter
//        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
//                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);

//        int[] ids = {R.id.number, R.id.name};

//        mCursorAdapter = new SimpleCursorAdapter(
//                getActivity(),
//                R.layout.contact_list_item,
//                null,
//                columns,
//                ids,
//                0);



}
