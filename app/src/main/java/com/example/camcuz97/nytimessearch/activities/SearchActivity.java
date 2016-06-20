package com.example.camcuz97.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.camcuz97.nytimessearch.Article;
import com.example.camcuz97.nytimessearch.ArticleArrayAdapter;
import com.example.camcuz97.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    Button btnSearch;
    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create intent to display article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                Article article = articles.get(position);
                //pass article into intent
                i.putExtra("article", article);
                //launch the activity
                startActivity(i);
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
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


    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","67ba0e31ba6a410bb28b49d32c3e5a35");
        params.put("page",0);
        params.put("q", query);
        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
                    //adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }
}
