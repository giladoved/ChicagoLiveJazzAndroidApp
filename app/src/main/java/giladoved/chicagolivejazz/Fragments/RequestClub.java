package giladoved.chicagolivejazz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import giladoved.chicagolivejazz.R;

public class RequestClub extends Fragment {

    InputMethodManager inputManager;

    EditText requestClubEditText;
    Button requestClubButton;

    public RequestClub() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_club, container, false);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        requestClubEditText = view.findViewById(R.id.request_club_edittext);
        requestClubButton = view.findViewById(R.id.request_club_button);
        requestClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestClubEditText.getText().toString().trim().length() > 3) {
                    Toast.makeText(getActivity(), "Thanks for your feedback! We got your request", Toast.LENGTH_SHORT).show();
                    requestClubEditText.setText("");
                    requestClubEditText.clearFocus();
                    hideKeyboard();
                }
            }
        });

        return view;
    }

    public void hideKeyboard() {
        inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
