package cn.robotbros.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import cn.robotbros.actionsheet.QActionSheet;
import cn.robotbros.demo.R;

/**
 * Created by enix on 29/8/2017.
 */

public class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";
    private Button button;
    private ArrayList<String> choices;
    private int currentChoice = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        choices = new ArrayList<>();
        choices.add("Choice 1");
        choices.add("Choice 2");
        choices.add("Choice 3");

        button = (Button) findViewById(R.id.showButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QActionSheet actionSheet = new QActionSheet.Builder(DemoActivity.this)
                        .data(choices)
                        .initialSelection(currentChoice)
                        .title("Select a choice")
                        .onItemSelectListener(new QActionSheet.OnItemSelectListener() {
                            @Override
                            public void onItemSelected(int index, String item) {
                                currentChoice = index;
                                Log.e(TAG, String.format("Select item: %d, %s", index, item));
                            }
                        })
                        .build();

                actionSheet.show();
            }
        });
    }
}
