package moe.mzry.ilmare.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.IlMareDataProvider;


/**
 * Message list fragment.
 */
public class MessageListFragment extends Fragment {

    private ListView messageListView;
    // TODO: set the data provider and fill content when needed.
    private IlMareDataProvider dataProvider;

    public static MessageListFragment newInstance(IlMareDataProvider provider) {
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
        messageListView = (ListView) view.findViewById(R.id.messageListView);
        fillContent(this.getActivity());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void fillContent(Activity activity) {
        SimpleAdapter adapter = new SimpleAdapter(activity, getData(),
                R.layout.message_list_item, new String[] { "content", "info" },
                new int[] { R.id.list_item_content, R.id.list_item_info });
        messageListView.setAdapter(adapter);
    }

    private ArrayList<HashMap<String, String>> getData() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        ArrayList<String> contentArrayList = new ArrayList<>();
        ArrayList<String> infoArrayList = new ArrayList<>();
        contentArrayList.add(
                "Some body left their phone in the micro kitchen. Sent to the reception! :)");
        contentArrayList.add("Anybody up for a game of ping pong right now?!!");
        contentArrayList.add("23333 the test of ILMare :(");
        infoArrayList.add("23 min ago");
        infoArrayList.add("1 day ago");
        infoArrayList.add("3 days ago");
        for (int i = 0; i < contentArrayList.size(); ++i) {
            HashMap<String, String> tempHashMap = new HashMap<String, String>();
            tempHashMap.put("content", contentArrayList.get(i));
            tempHashMap.put("info", infoArrayList.get(i));
            arrayList.add(tempHashMap);
        }
        return arrayList;
    }
}
