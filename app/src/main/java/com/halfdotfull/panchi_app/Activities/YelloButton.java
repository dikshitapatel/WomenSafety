package com.halfdotfull.panchi_app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.halfdotfull.panchi_app.R;

public class YelloButton extends AppCompatActivity {
    SharedPreferences mSharedPreferences;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_button);
        CardView message;

        message = (CardView) findViewById(R.id.cardView2);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(YelloButton.this)
                        .title(R.string.input)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Enter Here", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mSharedPreferences=getSharedPreferences("panchi", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=mSharedPreferences.edit();
                                editor.putString("Message",input.toString());
                                editor.apply();
                            }
                        });
                



                MaterialDialog dialog = builder.build();

                dialog.show();

            }
        });


    }
}
