package com.webianks.hatkemessenger.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.SMS;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;
import com.webianks.hatkemessenger.utils.Helpers;

import java.util.List;

/**
 * Created by R Ankit on 25-12-2016.
 */

public class SingleGroupAdapter extends RecyclerView.Adapter<SingleGroupAdapter.MyViewHolder> {

    private ColorGeneratorModified generator;
    private Context context;
    private Cursor dataCursor;
    private int color;
    private List<SMS> data;


    public SingleGroupAdapter(List<SMS> data) {
        this.data = data;
    }

    public SingleGroupAdapter(Context context, Cursor dataCursor, int color) {

        this.context = context;
        this.dataCursor = dataCursor;
        this.color = color;

        if (color == 0)
            generator = ColorGeneratorModified.MATERIAL;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_sms_detailed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        dataCursor.moveToPosition(position);
        holder.message.setText(dataCursor.getString(dataCursor.getColumnIndexOrThrow("body")));

        long time = dataCursor.getLong(dataCursor.getColumnIndexOrThrow("date"));
        holder.time.setText(Helpers.getDate(time));

        String name = dataCursor.getString(dataCursor.getColumnIndexOrThrow("address"));
        String firstChar = String.valueOf(name.charAt(0));

        if (color == 0){
            if (generator!=null)
                color = generator.getColor(name);
        }

        holder.message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(context, "alert", Toast.LENGTH_SHORT).show();

                String[] items = {"Delete"};

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                        , android.R.layout.simple_list_item_1, android.R.id.text1, items);

                new MaterialAlertDialogBuilder(context)
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                              //  deleteDialog();
                            }
                        })
                        .show();
                return true;
            }
        });


//        TextDrawable drawable = TextDrawable.builder().buildRound(firstChar, color);
//        holder.image.setImageDrawable(drawable);

    }

    public void swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return;
        }
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView message;
        private ImageView image;
        private TextView time;

        MyViewHolder(View itemView) {
            super(itemView);

            message =  itemView.findViewById(R.id.message);
            image =  itemView.findViewById(R.id.smsImage);
            time =  itemView.findViewById(R.id.time);

        }

        @Override
        public void onClick(View view) {

            Toast.makeText(context, "single click", Toast.LENGTH_SHORT).show();
//            if (itemClickListener != null) {
//                data.get(getAdapterPosition()).setReadState("1");
//                notifyItemChanged(getAdapterPosition());
//                itemClickListener.itemClicked(data.get(getAdapterPosition()).getColor(),
//                        senderContact.getText().toString(),
//                        data.get(getAdapterPosition()).getId(),
//                        data.get(getAdapterPosition()).getReadState());
//            }
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(context, "delete_toast", Toast.LENGTH_SHORT).show();
            String[] items = {"Delete"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                    , android.R.layout.simple_list_item_1, android.R.id.text1, items);

            new MaterialAlertDialogBuilder(context)
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            deleteDialog();
                        }
                    })
                    .show();

            return true;
        }

        private void deleteDialog() {

            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(context);
            alert.setMessage("Are you sure you want to delete this message?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteSMS(data.get(getAdapterPosition()).getId(), getAdapterPosition());
                }

            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alert.create();
            alert.show();
        }
    }

    private void deleteSMS(long messageId, int position) {

        long affected = context.getContentResolver().delete(
                Uri.parse("content://sms/" + messageId), null, null);

        if (affected != 0) {
            data.remove(position);
            notifyItemRemoved(position);

        }

    }
}
