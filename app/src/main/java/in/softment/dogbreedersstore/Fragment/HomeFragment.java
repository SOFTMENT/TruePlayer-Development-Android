package in.softment.dogbreedersstore.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import in.softment.dogbreedersstore.Adapter.DogAdapter;
import in.softment.dogbreedersstore.MainActivity;
import in.softment.dogbreedersstore.Model.DogListModel;
import in.softment.dogbreedersstore.Model.DogModel;
import in.softment.dogbreedersstore.R;
import in.softment.dogbreedersstore.Util.Constants;
import in.softment.dogbreedersstore.Util.ProgressHud;
import in.softment.dogbreedersstore.Util.Services;


public class HomeFragment extends Fragment {

    ArrayList<String> breeds = new ArrayList<>();
    ArrayList<DogListModel> dogList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private DogAdapter dogAdapter;
    private TextView city, country;
    private ArrayList<DogModel> dogModels;
    private TextView no_items_available;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,breeds);
        AutoCompleteTextView textView= view.findViewById(R.id.breedAutoTV);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDogsByLocation(breeds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        no_items_available = view.findViewById(R.id.no_items_available);

        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        dogModels = new ArrayList<>();
        dogAdapter = new DogAdapter(getContext(), dogModels);
        recyclerView.setAdapter(dogAdapter);
        view.findViewById(R.id.dropDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    textView.showDropDown();
            }
        });

        view.findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.latest) {
                            getDogForSale("All");
                        }
                        else if (item.getItemId() == R.id.hightolow) {
                            getDogForSale("HighToLow");
                        }
                        else if (item.getItemId() == R.id.lowtohigh) {
                            getDogForSale("LowToHigh");
                        }
                        else if (item.getItemId() == R.id.sortatoz) {
                            getDogForSale("AtoZ");
                        }
                        else if (item.getItemId() == R.id.sortztoa) {
                            getDogForSale("ZtoA");
                        }
                        return true;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.filter_menu, popup.getMenu());
                popup.show();
            }
        });

        getDogList();
        return view;
    }

    public void getDogForSale(String category){
        ProgressHud.show(getContext(),"");
        Query query = FirebaseFirestore.getInstance().collection("PupForSale")
                .orderBy("date", Query.Direction.DESCENDING).whereEqualTo("dogType",category);

        if (category.equals("All")) {
            query = FirebaseFirestore.getInstance().collection("PupForSale")
                    .orderBy("date", Query.Direction.DESCENDING);
        }
        else if (category.equals("HighToLow")) {
            query = FirebaseFirestore.getInstance().collection("PupForSale")
                    .orderBy("dogPrice", Query.Direction.DESCENDING);
        }
        else if (category.equals("LowToHigh")) {
            query = FirebaseFirestore.getInstance().collection("PupForSale")
                    .orderBy("dogPrice", Query.Direction.ASCENDING);
        }
        else if (category.equals("AtoZ")) {
            query = FirebaseFirestore.getInstance().collection("PupForSale")
                    .orderBy("dogType", Query.Direction.ASCENDING);
        }
        else if (category.equals("ZtoA")) {
            query = FirebaseFirestore.getInstance().collection("PupForSale")
                    .orderBy("dogType", Query.Direction.DESCENDING);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    dogModels.clear();
                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            DogModel dogModel = documentSnapshot.toObject(DogModel.class);
                            dogModels.add(dogModel);
                        }
                    }
                    no_items_available.setVisibility(dogModels.size() > 0 ? View.GONE : View.VISIBLE);
                    dogAdapter.notifyDataSetChanged();

                }
                else {
                    Services.showDialog(getContext(),"ERROR",task.getException().getMessage());
                }
            }
        });
    }

    public void getDogsByLocation(String category) {

        final GeoLocation center = new GeoLocation(Constants.latitude, Constants.longitude);

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center,  900000);

        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        if (category.equals("All")) {
            for (GeoQueryBounds b : bounds) {
                Query q = FirebaseFirestore.getInstance().collection("PupsForSale")
                        .orderBy("geohash")
                        .startAt(b.startHash)
                        .endAt(b.endHash);

                tasks.add(q.get());
            }

        }
        else {
            for (GeoQueryBounds b : bounds) {
                Query q = FirebaseFirestore.getInstance().collection("PupsForSale")
                        .orderBy("geohash")
                        .whereEqualTo("dogType",category)
                        .startAt(b.startHash)
                        .endAt(b.endHash);

                tasks.add(q.get());
            }

        }

        this.dogModels.clear();

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();


                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                             DogModel dogModel = doc.toObject(DogModel.class);
                                double lat = dogModel.getLat();
                                double lng = dogModel.getLng();

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= 900000) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                        dogModels.clear();

                        for (DocumentSnapshot documentSnapshot : matchingDocs) {
                            DogModel dogModel = documentSnapshot.toObject(DogModel.class);
                           dogModels.add(dogModel);


                        }

                        no_items_available.setVisibility(dogModels.size() > 0 ? View.GONE : View.VISIBLE);

                        ProgressHud.dialog.dismiss();
                        dogAdapter.notifyDataSetChanged();
                    }
                });


    }

    public void load(String sCity, String sCountry) {
        city.setText(sCity+",");
        country.setText(sCountry.toUpperCase());
        getDogsByLocation("All");
    }

    public void getDogList(){
        FirebaseFirestore.getInstance().collection("DogList")
                .orderBy("name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            dogList.clear();
                            breeds.clear();
                            DogListModel dogListModel = new DogListModel();
                            dogListModel.name = "All";
                            breeds.add("All");
                            dogList.add(dogListModel);

                            if (task.getResult()!= null && !task.getResult().isEmpty()) {

                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                    DogListModel dogListModel1 = documentSnapshot.toObject(DogListModel.class);
                                    dogList.add(dogListModel1);
                                    breeds.add(dogListModel1.getName());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }

                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeHomeFragment(this);
    }
}
