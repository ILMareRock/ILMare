package moe.mzry.ilmare.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.MessageComparators;

/**
 * Message list adapter
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private static int MAX_ITEM = 10;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageContent;
        private TextView messageInfo;

        public MessageViewHolder(View messageView) {
            super(messageView);
            messageContent = (TextView) messageView.findViewById(R.id.message_content);
            messageInfo = (TextView) messageView.findViewById(R.id.list_item_info);
        }

        public void setMessage(Message message) {
            messageContent.setText(message.getContent());
            messageInfo.setText(message.getCreationTime().toString() + " "
                + message.getLocationSpec().toString());
        }
    }

    private List<Message> messageList;

    public MessageListAdapter() {
        messageList = new ArrayList<>();
    }

    public void apply(List<Message> messages) {
        messages = new ArrayList<>(messages);
        Collections.sort(messages, new MessageComparators.MessageCreationTimeComparator());
        messageList = messages.subList(0, Math.min(messages.size(), MAX_ITEM));
        this.notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item,
                viewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int i) {
        viewHolder.setMessage(messageList.get(i));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
