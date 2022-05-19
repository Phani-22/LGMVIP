package com.phani.facedetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OutputDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.output_result_dialog_item, container, false);

        Button actionOKButton = view.findViewById(R.id.actionOKButton);
        TextView outputPlaceHolderTextView = view.findViewById(R.id.noOfFacesPlaceHolder);

        Bundle bundle = getArguments();
        assert bundle != null;
        int numberOfFacesDetected = bundle.getInt("NUMBER_OF_FACES_DETECTED");

        String outputString;
        if (numberOfFacesDetected == 1) {
            outputString = numberOfFacesDetected + " Face detected in Image";
        } else {
            outputString = numberOfFacesDetected + " Faces detected in Image";
        }
        outputPlaceHolderTextView.setText(outputString);

        actionOKButton.setOnClickListener(v -> dismiss());
        return view;
    }
}
