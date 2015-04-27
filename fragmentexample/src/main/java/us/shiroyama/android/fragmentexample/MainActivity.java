package us.shiroyama.android.fragmentexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SimpleFragment.FragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SimpleFragment())
                    .commit();
        }
    }

    @Override
    public void onFragmentButtonClicked(CharSequence buttonLabel) {
        final String text = "Fragment's button's label is: " + buttonLabel;
        Log.d(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
