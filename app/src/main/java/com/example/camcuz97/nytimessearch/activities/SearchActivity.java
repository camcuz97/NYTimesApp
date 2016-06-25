package com.example.camcuz97.nytimessearch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.camcuz97.nytimessearch.Article;
import com.example.camcuz97.nytimessearch.ArticleArrayAdapter;
import com.example.camcuz97.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.camcuz97.nytimessearch.Filters;
import com.example.camcuz97.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {


    //Set up private instance fields and binds to butterknife
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rvArticles) RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    StaggeredGridLayoutManager gridLayoutManager;
    String searchTerm;
    Filters filter;
    private final int REQUEST_CODE = 200;
    boolean topStories = true;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //binds butterknife
        ButterKnife.bind(this);
        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        //calls first search for top stories
        onArticleSearch(0);
    }



    public void setupViews(){
        //sets up views
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        rvResults.setAdapter(adapter);
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        //defines the filter to be passed between activities
        filter = new Filters();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchTerm = query;
                if(searchTerm.length() != 0){
                    onArticleSearch(0);
                }
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter){
            onClickFilter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void onClickFilter() {
        //creates filter intent
        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        //puts through the filter
        i.putExtra("filter", Parcels.wrap(filter));
        startActivityForResult(i, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            filter = Parcels.unwrap(data.getParcelableExtra("filter"));
            //clears articles and reputs in the ones that fit the filter
            articles.clear();
            adapter.notifyDataSetChanged();
            onArticleSearch(0);
        }
    }


    public void onArticleSearch(int page) {
        if(page == 0){
            //progress bar while search is happening
            pd = new ProgressDialog(this);
            pd.setTitle("Loading...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.show();
            //clears articles and sets up on scroll listener
            articles.clear();
            rvResults.clearOnScrollListeners();
            rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    onArticleSearch(page);
                }
            });
        }
        //calls search helper
        searchUrl(page,searchTerm);
    }


    private void searchUrl(int page, String query) {
        //sets up async client
        AsyncHttpClient client = new AsyncHttpClient();
        //sets up api key
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "67ba0e31ba6a410bb28b49d32c3e5a35");
        params.put("page", page);
        if(!topStories){
            //calls set parameter helper
            setParams(params, query);
            Log.d("DATE", url + params);
        }
        else{
            //puts top stories on screen
            params.put("callback", "callbackTopStories");
            topStories = false;
        }
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                pd.dismiss();
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    private void setParams(RequestParams params, String query){
        //puts params
        params.put("q", query);
        ArrayList<String> queries = new ArrayList<>();
        if (filter.isArts()) {queries.add("Arts ");}
        if (filter.isSports()) {queries.add("Sports ");}
        if (filter.isStyle()) {queries.add("Fashion ");}
        //string manipulation if filter queries required
        if (queries.size() != 0) {
            String tempQuery = "news_desk:(";
            for (int i = 0; i < queries.size(); i++) {tempQuery += queries.get(i);}
            tempQuery += ")";
            params.put("fq", tempQuery);
        }
        //puts sort preference
        params.put("sort", filter.getSort());
        if(filter.getBegin().length() != 0){params.put("begin_date", filter.getBegin());}
    }
}
