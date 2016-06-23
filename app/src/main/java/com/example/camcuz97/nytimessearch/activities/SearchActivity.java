package com.example.camcuz97.nytimessearch.activities;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    //Button btnSearch;
    //GridView gvResults;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rvArticles) RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    int currPage = 0;
    StaggeredGridLayoutManager gridLayoutManager;
    String searchTerm;
    String sort = "newest";
    String begin = "";
    Filters filter;
    private final int REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews(){
        //etQuery = (EditText) findViewById(R.id.etQuery);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        //rvResults = (RecyclerView) findViewById(R.id.rvArticles);
        //gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        rvResults.setAdapter(adapter);
        //rvResults.setLayoutManager(new StaggeredGridLayoutManager(this));
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        filter = new Filters();
        //gvResults.setAdapter(adapter);

        //hook up listener for grid click
//        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //create intent to display article
//                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//                //get the article to display
//                Article article = articles.get(position);
//                //pass article into intent
//                i.putExtra("article", article);
//                //launch the activity
//                startActivity(i);
//            }
//        });
//        gvResults.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                customLoadMoreDataFromApi(page);
//                return false;
//            }
//
//            @Override
//            public int getFooterViewType() {
//                return -1;
//            }
//        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_filter){
            onClickFilter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickFilter() {
        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            sort = data.getExtras().getString("sort");
            begin = data.getExtras().getString("date");
            filter = (Filters) data.getSerializableExtra("filter");
        }
    }

    //    public void customLoadMoreDataFromApi(int offset) {
//        // This method probably sends out a network request and appends new data items to your adapter.
//        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
//        // Deserialize API response and then construct new objects to append to the adapter
//        String query = etQuery.getText().toString();
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
//        RequestParams params = new RequestParams();
//        params.put("api-key","67ba0e31ba6a410bb28b49d32c3e5a35");
//        params.put("page",offset);
//        params.put("q", query);
//        client.get(url,params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("DEBUG", response.toString());
//                JSONArray articleJsonResults = null;
//                try{
//                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
//                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
//                    //adapter.notifyDataSetChanged();
//                    Log.d("DEBUG", articles.toString());
//                } catch(JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }


    public void onArticleSearch(int page) {
        if(page == 0){
            articles.clear();
            rvResults.clearOnScrollListeners();
            rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    onArticleSearch(page);
                }
            });
        }
        searchUrl(page,searchTerm,filter);
    }

    private void searchUrl(int page, String query, Filters filt) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "67ba0e31ba6a410bb28b49d32c3e5a35");
        params.put("page", page);
        params.put("q", query);
        ArrayList<String> queries = new ArrayList<>();
        if (filt.isArts()) {
            queries.add("Arts ");
        }
        if (filt.isSports()) {
            queries.add("Sports ");
        }
        if (filt.isStyle()) {
            queries.add("Fashion ");
        }
        if (queries.size() != 0) {
            String tempQuery = "news_desk:(";
            for (int i = 0; i < queries.size(); i++) {
                tempQuery += queries.get(i);
            }
            tempQuery += ")";
            params.put("fq", tempQuery);
        }
        params.put("sort", sort);
        if(begin.length() != 0){
            params.put("begin_date", begin);
        }
        Log.d("DATE", url + params);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
                    //adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
//    public void customLoadMoreDataFromApi(int page){
//        //String query = etQuery.getText().toString();
//        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
//        RequestParams params = new RequestParams();
//        params.put("api-key","67ba0e31ba6a410bb28b49d32c3e5a35");
//        params.put("q", searchTerm);
//        params.put("page",page);
//        client.get(url,params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("DEBUG", response.toString());
//                JSONArray articleJsonResults = null;
//                try{
//                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    articles.addAll(Article.fromJSONArray(articleJsonResults));
//                    adapter.notifyDataSetChanged();
//                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
//                    //adapter.notifyDataSetChanged();
//                    Log.d("DEBUG", articles.toString());
//                } catch(JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        //int curSize = adapter.getItemCount();
//        //adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
//    }

}
