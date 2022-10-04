package adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.makingfight.gymapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_button_shet_dialog,container,false);
        return view;
    }
}
