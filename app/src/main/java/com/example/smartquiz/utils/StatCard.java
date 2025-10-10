package com.example.smartquiz.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.smartquiz.R;

public class StatCard {

    public static CardView createStatCard(LayoutInflater inflater, View parent,
                                          int iconRes, String label, String value) {
        CardView cardView = (CardView) inflater.inflate(R.layout.stat_card, (android.view.ViewGroup) parent, false);

        ImageView statIcon = cardView.findViewById(R.id.statIcon);
        TextView statLabel = cardView.findViewById(R.id.statLabel);
        TextView statValue = cardView.findViewById(R.id.statValue);

        statIcon.setImageResource(iconRes);
        statLabel.setText(label);
        statValue.setText(value);

        return cardView;
    }

    // Alternative method with custom icon color
    public static CardView createStatCard(LayoutInflater inflater, View parent,
                                          int iconRes, String label, String value, int iconColor) {
        CardView cardView = createStatCard(inflater, parent, iconRes, label, value);

        ImageView statIcon = cardView.findViewById(R.id.statIcon);
        statIcon.setColorFilter(ContextCompat.getColor(parent.getContext(), iconColor));

        return cardView;
    }
}
