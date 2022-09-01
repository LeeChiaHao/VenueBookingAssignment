package com.example.oop_assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class VenueFragment extends Fragment {
    private Context context;

    public VenueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_, container, false);
        ListView listView = view.findViewById(R.id.listview);

        Bundle bundle = getArguments();
        if(bundle != null)  // Must make sure the are NOT NULL
        {
            final ArrayList<String> student = bundle.getStringArrayList("studentValues");
            // inner class, to customize our list view row
            class VenueAdapter extends  ArrayAdapter<String>{
                public VenueAdapter(Context context, ArrayList<String> venues)
                {
                    super(context,0,venues);
                }
                @NonNull
                @Override  // Customize the list view, the position is based on the venue adapter
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    String vid = getItem(position);

                    if(convertView == null){
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.showvenue_row,parent,false);
                    }

                    TextView textView = convertView.findViewById(R.id.VenueName);
                    textView.setText(vid);

                    Button book = convertView.findViewById(R.id.bookingbtn);
                    book.setTag(position);
                    book.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            int position = (Integer) view.getTag();
                            String getVenue = getItem(position);

                            Toast.makeText(context, getVenue+ "Clicked", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, BookingForm.class);
                            intent.putExtra("venue",getVenue);
                            intent.putStringArrayListExtra("studentValues", student);
                            context.startActivity(intent);
                        }
                    });
                    return convertView;
                }
            }
            ArrayList<String> getVenue = bundle.getStringArrayList("message"); // get the data from VenueAdapter
            VenueAdapter vadapter = new VenueAdapter(context,getVenue);
            listView.setAdapter(vadapter); // set the value to list using our own custom adapter
        }
        return  view;
    }
}