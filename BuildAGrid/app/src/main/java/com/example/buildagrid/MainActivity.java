package com.example.buildagrid;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.buildagrid.design.TopicAdapter;
import com.example.buildagrid.model.Topic;
import com.example.buildagrid.R;
import com.example.buildagrid.data.DataSource;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private List<Topic> topicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        topicList = DataSource.getInstance().getTopics();


        adapter = new TopicAdapter(this, topicList);

        recyclerView.setAdapter(adapter);
    }
}
