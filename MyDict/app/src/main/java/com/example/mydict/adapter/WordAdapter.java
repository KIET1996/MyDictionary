package com.example.mydict.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mydict.R;
import com.example.mydict.model.Word;


public class WordAdapter extends ArrayAdapter<Word> {
    Activity context;
    int resource;
    public WordAdapter(Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom=context.getLayoutInflater().inflate(resource,null);
        TextView txtWord=custom.findViewById(R.id.textWord);
        TextView txtKind=custom.findViewById(R.id.textKind);
        TextView txtMean=custom.findViewById(R.id.textMean);
        TextView txtExam=custom.findViewById(R.id.textExam);
        TextView txtStatus=custom.findViewById(R.id.textStatus);
        Word word =getItem(position);
        txtWord.setText(word.getWord());
        txtKind.setText(word.getKind());
        txtMean.setText(word.getMean());
        txtExam.setText(word.getExample());
     //   txtStatus.setText(word.getStatus());
        if(Integer.parseInt(String.valueOf(word.getStatus())) == 1){
            txtStatus.setText("Đã thuộc");
        }
        else {
            txtStatus.setText("Chưa thuộc");
        }

        return custom;
    }
}