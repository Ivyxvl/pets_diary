package com.example.gafandfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ReminderAdapter extends FirestoreRecyclerAdapter<Note, ReminderAdapter.ReminderViewHolder> {
    Context context;

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.dateTextView.setText(note.date);
        holder.timeTextView.setText(note.time);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, ReminderDetailsActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("date", note.date);
            intent.putExtra("time", note.time);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);
        return new ReminderViewHolder(view);
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, dateTextView, timeTextView, timestampTextView;


        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txtTitle);
            dateTextView = itemView.findViewById(R.id.txtDate);
            timeTextView = itemView.findViewById(R.id.txtTime);
            timestampTextView = itemView.findViewById(R.id.reminder_timestamp_text_view);
        }
    }
}
