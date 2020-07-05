package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.smile.R;
import com.example.smile.adapters.Product_List_Adapter;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.ProductNameModel;
import com.example.smile.dataObjects.Product_List_Model;
import com.example.smile.utils.NetworkConnectivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Product_List_Activity extends AppCompatActivity {

    RecyclerView proRecyclerView;
    ArrayList<Product_List_Model> productListModelArrayList;
    int page = 0;
    Product_List_Adapter product_list_adapter;
    String purl = BaseURL+"ProductGrid";
    SwipyRefreshLayout mpagination;
    Spinner mProductName;
    String spinurl = BaseURL+"ProductNameDD";
    ArrayList<String> productNameList;
    String memberProductName;
    ProgressBar progressBar;
    ArrayList<ProductNameModel> mProductnameModel;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mpagination = findViewById(R.id.pagination);

        searchBtn = findViewById(R.id.search_btn);
        progressBar = findViewById(R.id.pb);
        mpagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mpagination.setRefreshing(false);
                page = page + 1;
                Grid(purl,"");

            }
        });


        mProductName = findViewById(R.id.productSpinner);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        mProductnameModel = new ArrayList<>();
        productNameList = new ArrayList<String>();
        productNameList.add("Select");

        mProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memberProductName = productNameList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        proRecyclerView = findViewById(R.id.pro_recyclerView);
        productListModelArrayList = new ArrayList<Product_List_Model>();
        proRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        product_list_adapter = new Product_List_Adapter(Product_List_Activity.this, productListModelArrayList);
        proRecyclerView.setHasFixedSize(true);
        proRecyclerView.setAdapter(product_list_adapter);


        run(spinurl);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_group = new Intent(Product_List_Activity.this, Add_Product_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Product_List_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_group, options.toBundle());

            }
        });

        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(purl,"");
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(productListModelArrayList != null && productListModelArrayList.size() > 0)
                    productListModelArrayList.clear();

                if(memberProductName != null && !memberProductName.isEmpty() && memberProductName.equals("0")) {
                    memberProductName = "";
                }
                Grid(purl,memberProductName);
            }
        });
    }



    public void Grid(String purl,final String productName) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Pagenation", String.valueOf(page))
                .header("ProductName", productName)
                .url(purl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Product_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("ProductData");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject eqpobject = jsonArray.getJSONObject(i);
                                String id = eqpobject.getString("id");
                                String productname = eqpobject.getString("product_id");
                                String prdname = eqpobject.getString("prdname");
                                String pono = eqpobject.getString("pono");
                                String supliername = eqpobject.getString("supliername");
                                String details = eqpobject.getString("details");
                                String rate = eqpobject.getString("rate");
                                String qty = eqpobject.getString("qty");
                                String sub_total = eqpobject.getString("sub_total");
                                String tax = eqpobject.getString("tax");
                                String taxpercent = eqpobject.getString("taxper");
                                String tax_amount = eqpobject.getString("tax_amount");
                                String grandTotal = eqpobject.getString("total");

                                Product_List_Model product_list_model = new Product_List_Model();
                                product_list_model.setId(id);
                                product_list_model.setId(productname);
                                product_list_model.setProname(prdname);
                                product_list_model.setPpNo(pono);
                                product_list_model.setsName(supliername);
                                product_list_model.setDescription(details);
                                product_list_model.setUnitRate(rate);
                                product_list_model.setProQty(qty);
                                product_list_model.setProSubtotal(sub_total);
                                product_list_model.setSelectTax(tax);
                                product_list_model.setTaxPercent(taxpercent);
                                product_list_model.setTaxAmount(tax_amount);
                                product_list_model.setGrandTotal(grandTotal);

                                if (productListModelArrayList != null && productListModelArrayList.size() > 0) {
                                    int size = productListModelArrayList.size();
                                    product_list_model.setSrno(String.valueOf(size + 1));
                                    productListModelArrayList.add(size, product_list_model);


                                } else {
                                    product_list_model.setSrno(String.valueOf(i + 1));
                                    productListModelArrayList.add(product_list_model);
                                }

                            }
                            product_list_adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //for Get Spinner data
    public  void run(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Product_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("ProNamDD");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String productName = object.getString("prdname");
                                String productId = object.getString("product_id");
                                productNameList.add(productName);

                                ProductNameModel productNameModel = new ProductNameModel();
                                productNameModel.productId = productId;
                                productNameModel.productName = productName;
                                mProductnameModel.add(productNameModel);

                            }

                            ProductNameModel productNameModel = new ProductNameModel();
                            productNameModel.productName = "Select";
                            productNameModel.productId = "0";

                            mProductnameModel.add(0,productNameModel);
                            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(Product_List_Activity.this,
                                    android.R.layout.simple_list_item_1,productNameList);
                            yearAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            mProductName.setAdapter(yearAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

}