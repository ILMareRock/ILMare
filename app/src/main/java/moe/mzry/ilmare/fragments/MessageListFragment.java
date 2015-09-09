package moe.mzry.ilmare.fragments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.Callback;
import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.data.Message;


/**
 * Message list fragment.
 */
public class MessageListFragment extends Fragment implements ServiceConnection {

    private final MessageListAdapter messageListAdapter;
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
        messageListAdapter = new MessageListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MessageListFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        messageListView = (RecyclerView) view.findViewById(R.id.messageListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        messageListView.setLayoutManager(mLayoutManager);

        messageListView.setAdapter(messageListAdapter);
        return view;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MainApp.getDataProvider().addMessageListener(new Callback<List<Message>>() {
            @Override
            public void apply(List<Message> data) {
                Log.i("MessageListFragment", "Update messages");
                messageListAdapter.apply(data);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
