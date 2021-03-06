package com.example.aiplant.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aiplant.R;
import com.example.aiplant.create_profile.PlantProfileFragment;
import com.example.aiplant.utility_classes.BottomNavigationViewHelper;
import com.example.aiplant.utility_classes.MongoDbSetup;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchAuth;
import com.mongodb.stitch.android.core.auth.StitchUser;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "User_Profile";
    private static final int ACTIVITY_NUM = 1;
    //widgets

    private EditText mSearchParam;
    private ImageView backArrow;
    private ImageButton mSearchButton;
    private ImageView mPlantListButton;
    private ImageView mCreatePlantProfile;

    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;

    private FrameLayout mPlantListFragment;

    //database
    private MongoDbSetup mongoDbSetup;
    private DatabaseReference user_ref;
    private DatabaseReference myRef;
    private GoogleSignInClient mGoogleSignInClient;
    private StitchAuth mStitchAuth;
    private StitchUser mStitchUser;
    private Context mContext;
    private StitchAppClient appClient;

    private LinearLayout mAddPlantFromDBCard;
    private LinearLayout mAddPlantFromScratch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_library_layout);

        mContext = this;

        connectMongoDb();

        initLayout();
        buttonListeners();
        setupBottomNavigationView();
        fragmentManager = getSupportFragmentManager();


    }


    private void connectMongoDb() {
        mongoDbSetup = MongoDbSetup.getInstance(mContext);
        mGoogleSignInClient = mongoDbSetup.googleClient();
        mStitchAuth = mongoDbSetup.getStitchAuth();
        mStitchUser = mongoDbSetup.getStitchUser();
        appClient = mongoDbSetup.getAppClient();

    }




    public MongoDbSetup getMongoDbForLaterUse() {
        return mongoDbSetup;
    }

    /**
     * Method for inflating widgets and adding on click listeners
     * */

    public void initLayout() {
        mSearchParam = findViewById(R.id.search_bar_id);
        mSearchButton = findViewById(R.id.search_button_id);
        mCreatePlantProfile = findViewById(R.id.create_new_plant_button);
        mPlantListButton = findViewById(R.id.library_button);
        recyclerView = findViewById(R.id.recyclerView);
        mPlantListFragment = findViewById(R.id.plant_list_fragment);

        mAddPlantFromDBCard = findViewById(R.id.add_plant_from_database_layout);
        mAddPlantFromScratch = findViewById(R.id.add_plant_from_scratch_card);

        mPlantListButton.setOnClickListener(view -> {
            Fragment searchListFragment = fragmentManager.findFragmentById(R.id.plant_list_fragment);
            if (searchListFragment == null) {
                searchListFragment = new SearchListFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.plant_list_fragment, searchListFragment).commit();
            }
        });

        mCreatePlantProfile.setOnClickListener(view -> {

            Fragment createPlantProfileFragment = fragmentManager.findFragmentById(R.id.plant_list_fragment);
            if (createPlantProfileFragment == null) {
                createPlantProfileFragment = new PlantProfileFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.plant_list_fragment, createPlantProfileFragment).commit();
            }

        });

    }

    public void buttonListeners() {
        mAddPlantFromDBCard.setOnClickListener(this);
        mAddPlantFromScratch.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_plant_from_scratch_card:
                Fragment createPlantProfileFragment = fragmentManager.findFragmentById(R.id.plant_list_fragment);
                if (createPlantProfileFragment == null) {
                    createPlantProfileFragment = new PlantProfileFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.plant_list_fragment, createPlantProfileFragment).commit();
                }

                break;
            case R.id.add_plant_from_database_layout:
                Fragment searchListFragment = fragmentManager.findFragmentById(R.id.plant_list_fragment);
                if (searchListFragment == null) {
                    searchListFragment = new SearchListFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.plant_list_fragment, searchListFragment).commit();
                }

                break;

        }
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {

        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }


}
