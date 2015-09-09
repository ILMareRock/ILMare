package moe.mzry.ilmare.fragments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.Callback;
import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.eddystone.Beacon;


/**
 * Message list fragment.
 */
public class MessageListFragment extends Fragment {

    private static final MessageListAdapter messageListAdapter = new MessageListAdapter();
    private static final BeaconListAdapter beaconListAdapter = new BeaconListAdapter();
    private RecyclerView messageListView;
    private RecyclerView beaconListView;
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
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        messageListView = (RecyclerView) view.findViewById(R.id.messageListView);
        beaconListView = (RecyclerView) view.findViewById(R.id.beaconListView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this.getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this.getActivity());
        messageListView.setLayoutManager(mLayoutManager1);
        beaconListView.setLayoutManager(mLayoutManager2);

        messageListView.setAdapter(messageListAdapter);
        beaconListView.setAdapter(beaconListAdapter);
        return view;
    }

    public void renderMessage(List<Message> data) {
        messageListAdapter.apply(data);
    }

    public void renderBeacon(List<Beacon> data) {
        beaconListAdapter.apply(data);
    }
}
