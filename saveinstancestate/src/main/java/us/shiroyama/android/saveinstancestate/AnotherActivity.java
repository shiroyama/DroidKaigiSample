package us.shiroyama.android.saveinstancestate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnotherActivity extends AppCompatActivity {
    private static final String KEY_TEXT = "key_text";

    private TextView textView;
    private Button button;
    private String text;

    public static Intent newIntent(Context context, String text) {
        Intent intent = new Intent(context, AnotherActivity.class);
        intent.putExtra(KEY_TEXT, text);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TEXT)) {
            text = savedInstanceState.getString(KEY_TEXT);
        } else if (getIntent().hasExtra(KEY_TEXT)) {
            text = getIntent().getStringExtra(KEY_TEXT);
        } else {
            throw new IllegalStateException("Unexpected access");
        }

        textView = (TextView) findViewById(R.id.text);
        textView.setText(text);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnotherActivity.this, YetAnotherActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TEXT, text);
    }
}
