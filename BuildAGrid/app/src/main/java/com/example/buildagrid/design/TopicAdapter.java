package com.example.buildagrid.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.buildagrid.model.Topic;
import com.example.buildagrid.R;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private final List<Topic> topicList;
    private final Context context;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        if (view == null) {
            throw new RuntimeException("Failed to inflate card_layout.xml");
        }

        return new TopicViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return (topicList != null) ? topicList.size() : 0;  // ✅ Prevents NullPointerException
    }
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);

        // Set the title and image
        holder.topicTitle.setText(context.getString(topic.getTitleResId()));
        holder.topicImage.setImageResource(topic.getImageResId());


    }



    public class TopicViewHolder extends RecyclerView.ViewHolder {
        ImageView topicImage;
        TextView topicTitle;


        public TopicViewHolder(View itemView) {
            super(itemView);
            topicImage = itemView.findViewById(R.id.topic_image);
            topicTitle = itemView.findViewById(R.id.topic_title);
        }
    }

}
