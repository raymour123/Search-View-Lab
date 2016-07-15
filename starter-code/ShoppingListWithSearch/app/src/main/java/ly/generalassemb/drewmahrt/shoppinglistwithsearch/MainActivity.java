package ly.generalassemb.drewmahrt.shoppinglistwithsearch;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ly.generalassemb.drewmahrt.shoppinglistwithsearch.setup.DBAssetHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        ShoppingSQLiteOpenHelper instance = new ShoppingSQLiteOpenHelper(this);
        ListView mListView = (ListView) findViewById(R.id.list_view);

        Cursor cursor = instance.showAllGroceries();
        CursorAdapter adapter = new CursorAdapter(MainActivity.this, cursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context).inflate(
                        android.R.layout.simple_list_item_1, viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String grocery = cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME));
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(grocery);
            }
        };
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.i("TEST", "onNewIntent: ");

            String query = intent.getStringExtra(SearchManager.QUERY);
            ShoppingSQLiteOpenHelper instance = new ShoppingSQLiteOpenHelper(this);
            ListView mListView = (ListView) findViewById(R.id.list_view);

            Cursor cursor = instance.searchGroceryNames(query);
            CursorAdapter adapter = new CursorAdapter(MainActivity.this, cursor,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                    return LayoutInflater.from(context).inflate(
                            android.R.layout.simple_list_item_1, viewGroup, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    String grocery = cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME));
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setText(grocery);
                    Log.i("TEST", grocery);
                }
            };
            mListView.setAdapter(adapter);
        }

    }
}



