package moe.mzry.ilmare.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.data.Message;

/**
 * Message list adapter
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

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
            messageInfo.setText(message.getCreationTime().toString());
        }
    }

    private List<Message> messageList;

    public MessageListAdapter() {
        messageList = new ArrayList<>();

        // TODO: use service instead
        Firebase firebaseMessagesRef = new Firebase("https://ilmare.firebaseio.com/")
                .child("messages");
        firebaseMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }
                apply(messages);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void apply(List<Message> messages) {
        messageList = messages;
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
