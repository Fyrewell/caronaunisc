package br.unisc.pdm.caronauniscapp.chat;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;

import br.unisc.pdm.caronauniscapp.R;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        //TextView authorText = (TextView) view.findViewById(R.id.author);
        //authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently

        if (author != null && author.equals(mUsername)) {

            ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());

            if (((TextView) ((SalaActivity) view.getContext()).findViewById(R.id.friendLabel)).getText().equals("")) {
                ((TextView) ((SalaActivity) view.getContext()).findViewById(R.id.friendLabel)).setText(author);
            }

            setAlignment(view, false);

        } else {

            ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());

            if (((TextView) ((SalaActivity) view.getContext()).findViewById(R.id.meLbl)).getText().equals("")) {
                ((TextView) ((SalaActivity) view.getContext()).findViewById(R.id.meLbl)).setText(author);
            }

            setAlignment(view, true);
        }
        String[] t = chat.getQuando().split(" ")[0].split("-");
        ((TextView) view.findViewById(R.id.txtInfo)).setText(t[2]+"/"+t[1]+"/"+t[0]+" "+chat.getQuando().split(" ")[1]);

    }


    private void setAlignment(View v, boolean isMe) {
        TextView message = ((TextView)v.findViewById(R.id.message));
        LinearLayout contentWithBackground = ((LinearLayout)v.findViewById(R.id.contentWithBackground));
        LinearLayout content = ((LinearLayout)v.findViewById(R.id.content));
        TextView txtInfo = ((TextView)v.findViewById(R.id.txtInfo));
        if (!isMe) {
            contentWithBackground.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentWithBackground.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            contentWithBackground.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) message.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            message.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            txtInfo.setLayoutParams(layoutParams);
        } else {
            contentWithBackground.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentWithBackground.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            contentWithBackground.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) message.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            message.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            txtInfo.setLayoutParams(layoutParams);
        }
    }

}
