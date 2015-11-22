package com.example.randy.graphicdemo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.randy.graphicdemo.R;

import java.util.List;

/**
 * Created by randy on 2015/11/21.
 * 抽象类，用于提供格式化处理的图片以及下拉框，用于选择不同的处理方式
 */
public abstract class SpinnerImageActivity extends Activity {

    protected ImageView imageView;
    private Spinner spinner;
    private int lastSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spinner_image);

        imageView = (ImageView)findViewById(R.id.image);
        spinner = (Spinner)findViewById(R.id.spinner);

        //获取Imageview中的位图对象
        Bitmap original = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        final List<Option> options = getOptions(original);
        //将列表数据适配到spinner中
        ArrayAdapter<Option> adapter = new ArrayAdapter<Option>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Option option = options.get(position);
                imageView.setImageBitmap(option.bitmap);
                if (position != 0) {
                    lastSelection = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(getInitialSelection(options.size()));
        lastSelection = spinner.getSelectedItemPosition();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() == 0) {
                    spinner.setSelection(lastSelection);
                } else {
                    spinner.setSelection(0);
                }
            }
        });
    }


    protected abstract List<Option> getOptions(Bitmap original);

    protected int getInitialSelection(int size){
        return 1;
    }

    //为每个bitmap对象设置不同的title，创建一个类
    protected static class Option{

        public final String title;
        public final Bitmap bitmap;

        public Option(String title, Bitmap bitmap) {
            this.title = title;
            this.bitmap = bitmap;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
