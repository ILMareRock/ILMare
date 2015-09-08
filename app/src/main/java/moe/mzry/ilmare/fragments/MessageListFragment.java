package moe.mzry.ilmare.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.data.Message;


/**
 * Message list fragment.
 */
public class MessageListFragment extends Fragment {

    private MessageListAdapter messageListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView messageListView;
    // TODO: set the data provider and fill content when needed.
    private IlMareDataProvider dataProvider;

    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    public MessageListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("haha", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        messageListView = (RecyclerView) view.findViewById(R.id.messageListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        messageListView.setLayoutManager(mLayoutManager);

        messageListAdapter = new MessageListAdapter();
        messageListView.setAdapter(messageListAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
