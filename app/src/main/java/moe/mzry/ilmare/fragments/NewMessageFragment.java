package moe.mzry.ilmare.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.mzry.ilmare.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewMessageFragment extends Fragment {

  public NewMessageFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_new_message, container, false);
  }
}
