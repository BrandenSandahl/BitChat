package io.bitfountain.matthewparker.bitchat;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.bitfountain.matthewparker.bitchat.ContactsFragment.Listener} interface
 * to handle interaction events.
 */
public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ContactsFragment";

    private Listener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, null);

        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        //this is similar to a DB query, it's a projection of the columns I want to return
        String[] columns = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

//        //note that actual query needs the ID b/c of the cursorAdapter
//        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
//                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);

        int[] ids = {R.id.number, R.id.name};

        listView.setAdapter(new SimpleCursorAdapter(
                getActivity(),
                R.layout.contact_list_item,
                null,
                columns,
                ids,
                0));

        getLoaderManager().initLoader(0, null, this);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //ListViews on click listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get the cursor from the parent view
        Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
        cursor.moveToPosition(position);

    }

    /* cursor loader stuff */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /* end of cursor loader */

    //constraint to bring in Listener
    public interface Listener {

    }

}
