package com.example.buildagrid.data;

import java.util.Arrays;
import java.util.List;
import com.example.buildagrid.R;
import com.example.buildagrid.model.Topic;
public class DataSource {
    private static final DataSource INSTANCE = new DataSource();

    private final List<Topic> topics;

    private DataSource() {
        topics = Arrays.asList(
                new Topic(R.string.architecture, 58, R.drawable.architecture),
                new Topic(R.string.crafts, 121, R.drawable.crafts),
                new Topic(R.string.business, 78, R.drawable.business),
                new Topic(R.string.culinary, 118, R.drawable.culinary),
                new Topic(R.string.design, 423, R.drawable.design),
                new Topic(R.string.fashion, 92, R.drawable.fashion),
                new Topic(R.string.film, 165, R.drawable.film),
                new Topic(R.string.gaming, 164, R.drawable.gaming),
                new Topic(R.string.drawing, 326, R.drawable.drawing),
                new Topic(R.string.lifestyle, 305, R.drawable.lifestyle),
                new Topic(R.string.music, 212, R.drawable.music),
                new Topic(R.string.painting, 172, R.drawable.painting),
                new Topic(R.string.photography, 321, R.drawable.photography),
                new Topic(R.string.tech, 118, R.drawable.tech)
        );
    }

    public static DataSource getInstance() {
        return INSTANCE;
    }

    public List<Topic> getTopics() {
        return topics;
    }
}
