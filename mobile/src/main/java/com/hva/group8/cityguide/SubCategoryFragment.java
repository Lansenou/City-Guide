package com.hva.group8.cityguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SubCategoryFragment extends Fragment {

    private static SubCategoryFragment instance;

    public static SubCategoryFragment getInstance() {
        if (instance == null)
            instance = new SubCategoryFragment();
        return instance;
    }

    public static SubCategoryFragment newInstance() {
        return (instance = new SubCategoryFragment());
    }

    public String tableName;
    public int number;

    private List<HomeGroupItem> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        final ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setDividerHeight(2);

        //Load List
        fillSubCategoryList();

        //Load Adapter with list
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(getActivity().getApplicationContext(), R.layout.group_row, categoryList);

        //Set adapter to our list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), categoryList.get(position).Title, Toast.LENGTH_SHORT).show();
                if (tableName.equals("musea_galleries") && position == 2)
                    tableName = "tentoonstellingen";
                ActivityListFragment fragment = ActivityListFragment.getInstance();
                fragment.info = categoryList.get(position);
                fragment.tableName = tableName;
                ((MainActivity)getActivity()).SwitchFragment(fragment);
            }
        });
        return view;
    }

    private void fillSubCategoryList() {
        categoryList = new ArrayList<>();
        switch (number) {
            case 0: //From the table activities
                tableName = "activiteiten";         //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.categories, getString(R.string.sub_arrangement), "WHERE Types = 'Arrangement' OR Types = 'Arrangement,Excursie' OR Types = 'Actief en Sportief'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_excursion, getString(R.string.sub_excercion), "WHERE Types = 'Excursie'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_guided_tour, getString(R.string.sub_guidedtour), "WHERE Types = 'Rondleiding'"));
                break;
            case 1: //From the table attractions
                tableName = "attracties";           //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.sub_architecture, getString(R.string.sub_architecture), "WHERE Types = 'Architectuur' OR Types = 'Architectuur,Bezienswaardigheid' OR Types = 'Bezienswaardigheid' OR Types = 'Kerk' OR Types = 'Molen of Gemaal' OR Types = 'Monument'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_attraction, getString(R.string.sub_attraction), "WHERE Types = 'Attractie' OR Types = 'Casino' OR Types = 'Attractie,Museum' OR Types = 'Rondvaart' OR Types = 'Rondvaart,Kano- en bootverhuurbedrijf'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_open_air, getString(R.string.sub_outside), "WHERE Types = 'Dierentuin' OR Types = 'Kinderboerderij' OR Types = 'Park/Tuin' OR Types = 'Speeltuin' OR Types = 'Strand'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_wellness, getString(R.string.sub_wellness), "WHERE Types = 'Wellness' OR Types = 'Zwembad'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_museum, getString(R.string.sub_museums), "WHERE Types = 'Museum' OR Types = 'Galerie'"));
                break;
            case 2: //From the table food and drinks
                tableName = "eten_drinken";         //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.sub_brasserie, getString(R.string.sub_brasserie), "WHERE Types = 'Brasserie/Lunchroom' OR Types = 'Broodjeszaak' Or Types = 'Voeding/drank,Cadeau,Theetuin' Or Types = 'IJssalon'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_pub_cafe, getString(R.string.sub_eatery), "WHERE Types = 'EetcafÃ©' OR Types = 'Restaurant,CafÃ© of Bar'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_restaurant, getString(R.string.sub_restaurant), "WHERE Types = 'Restaurant' OR Types = 'Pannenkoekenrestaurant'"));
                break;
            case 3: //From the table museums
                tableName = "musea_galleries";      //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.sub_museum, getString(R.string.sub_museums), "WHERE Types = 'Museum' OR Types = 'Attractie,Museum'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_gallery, getString(R.string.sub_gallery), "WHERE Types = 'Galerie'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_exhibition, getString(R.string.sub_exhibition), "WHERE Types = 'Tentoonstelling' OR Types LIKE '%Tentoonstelling%'"));
                break;
            case 4: //From the table theater
                tableName = "theater";              //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.sub_theatre, getString(R.string.sub_theater), "WHERE Types = 'Theater en Toneel' OR Types LIKE '%Toneel%' OR Types LIKE '%Film%'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_show, getString(R.string.cat_cabaret), "WHERE Types = 'Cabaret' OR Types Like '%Cabaret'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_youth_theatre, getString(R.string.sub_youththeater), "WHERE Types = 'Jeugdtheater' OR Types LIKE '%JeugdTheater%'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_dance_music, getString(R.string.sub_dance), "WHERE Types = 'Opera' OR Types LIKE '%Dans%' OR Types LIKE '%Musical%' OR Types LIKE '%uziek%'"));
                break;
            case 5: //From the table nightlife
                tableName = "uitgaansgelegenheden"; //TODO: Don't change this name, it's the SQL tablename
                categoryList.add(new HomeGroupItem(R.drawable.sub_pub_cafe, getString(R.string.sub_cafe), "WHERE Types = 'CafÃ© of Bar' OR Types LIKE '%CafÃ©%'"));
                categoryList.add(new HomeGroupItem(R.drawable.sub_nightclub, getString(R.string.sub_nightclub), "WHERE Types = 'Uitgaanscentrum' OR Types = 'Casino' OR Types LIKE '%Uitgaanscentrum%'"));
                break;
        }
    }
}
