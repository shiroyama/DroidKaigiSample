package us.shiroyama.android.fragmentexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Simple Fragment with interaction listener with Activity
 */
public class SimpleFragment extends Fragment {
    private static final String TAG = SimpleFragment.class.getSimpleName();

    private FragmentInteractionListener listener;
    private Button button;

    // must be public
    public SimpleFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement Fragment's callback", e);
            throw new IllegalStateException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFragmentButtonClicked(((Button) v).getText());
                }
            }
        });
        return rootView;
    }

    public interface FragmentInteractionListener {
        void onFragmentButtonClicked(CharSequence buttonLabel);
    }
}
