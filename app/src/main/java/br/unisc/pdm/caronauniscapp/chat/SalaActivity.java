package br.unisc.pdm.caronauniscapp.chat;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import br.unisc.pdm.caronauniscapp.R;

public class SalaActivity extends ListActivity {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://caronaunisc-chat.firebaseio.com";

    private String mUsername = "Voce";
    private Firebase mFirebaseRef,mFirebaseRef2;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    String chat_usr_nome, chat_usr_mat, matricula, nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            chat_usr_nome = extras.getString("chat_usr_nome");
            chat_usr_mat = extras.getString("chat_usr_mat");
            matricula = extras.getString("matricula");
            nome = extras.getString("nome");
        }

        // Make sure we have a mUsername
        //setupUsername();

        setTitle("Chat com " + chat_usr_nome);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat/"+matricula+"/"+chat_usr_mat);
        mFirebaseRef2 = new Firebase(FIREBASE_URL).child("chat/"+chat_usr_mat+"/"+matricula);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(SalaActivity.this, "Conectado ao chat", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(SalaActivity.this, "Desconectado ao chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "Diego" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Chat chat = new Chat(input, mUsername, sdf1.format(new Date()).toString());
            Chat chat2 = new Chat(input, nome, sdf1.format(new Date()).toString());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            mFirebaseRef2.push().setValue(chat2);
            inputText.setText("");
        }
    }
}
