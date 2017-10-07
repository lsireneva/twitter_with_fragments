package com.example.luba.twitterwithfragments.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.TwitterApplication;
import com.example.luba.twitterwithfragments.models.Message;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.NewMessageRequest;
import com.example.luba.twitterwithfragments.network.TwitterClient;
import com.example.luba.twitterwithfragments.network.callbacks.NewPostMessageCallback;

/**
 * Created by luba on 10/7/17.
 */

public class NewMessageDialogFragment extends BottomSheetDialogFragment {

    Button btnSend, btnCancel;
    EditText etUserName, etMessage;
    private User mUser = new User();


    public NewMessageDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public interface OnNewMessageDialogFragmentListener {

        void onMessageListChanged(Message message);

    }

    private NewMessageDialogFragment.OnNewMessageDialogFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_new_message, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        etUserName = (EditText) view.findViewById(R.id.et_user_name);
        etMessage = (EditText) view.findViewById(R.id.et_message);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewMessageDialogFragmentListener) {
            mListener = (OnNewMessageDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " OnNewMessageDialogFragmentListener should be implemented");
        }
    }

    private void sendMessage () {
        NewMessageRequest request = new NewMessageRequest();
        request.setTextOfMessage(etMessage.getText().toString());
        request.setScreenName(etUserName.getText().toString());
        Log.d("DEBUG", "Message"+etMessage.getText().toString());
        Log.d("DEBUG", "To"+etUserName.getText().toString());


        TwitterClient client = TwitterApplication.getRestClient();
        Log.d("DEBUG", "request"+request);
        client.postMessages(request, new NewPostMessageCallback() {
            @Override
            public void onSuccess(Message message) {
                mListener.onMessageListChanged(message);
                dismiss();
            }

            @Override
            public void onError(Error error) {

            }
        });
    }
}
