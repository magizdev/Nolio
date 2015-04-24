package com.ca.nolio;

import org.json.JSONException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ca.nolio.interfaces.IJumpable;
import com.ca.nolio.model.ApplicationList;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements IJumpable,
		OnApplicationChangedListener {
	private String[] mPageTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private MenuFragment dashboardFragment;
	private MenuFragment reportFragment;
	private MenuFragment deploymentListFragment;

	private MenuFragment currentFragment;
	private int position;
	private DrawerAdapter adapter;
	private com.ca.nolio.util.Configuration config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		com.ca.nolio.util.Configuration.getConfiguration(this)
				.setApplicationSelectListener(this);
		ApplicationList applications = new ApplicationList();
		try {
			applications.LoadFromJson(this.getIntent().getStringExtra(
					"Applications"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config = com.ca.nolio.util.Configuration.getConfiguration(this);
		mTitle = mDrawerTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.Pages);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer

		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		adapter = new DrawerAdapter(this, applications);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, "55LS9wRp6N3YS37MHTOYSBSg");

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setDisplayShowTitleEnabled(true);
				getActionBar().setNavigationMode(
						ActionBar.NAVIGATION_MODE_STANDARD);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (currentFragment != null) {
			return currentFragment.onCreateOptionsMenu(getMenuInflater(), menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return currentFragment.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		this.position = position;
		FragmentManager fragmentManager = getFragmentManager();
		if (reportFragment == null) {
			reportFragment = new BookmarksFragement();
		}
		if (deploymentListFragment == null) {
			deploymentListFragment = new DeploymentListFragement();
		}
		if (dashboardFragment == null) {
			dashboardFragment = new DashboardFragement();
		}
		switch (position) {
		case 1:
			currentFragment = deploymentListFragment;
			break;
		case 2:
			currentFragment = reportFragment;
			break;
		case 3:
			currentFragment = dashboardFragment;
			break;
		default:
			break;
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, currentFragment).commit();

		mDrawerList.setItemChecked(position, true);
		adapter.select(position);
		setTitle(mPageTitles[position]);
		this.position = position;
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(
				mTitle + "(" + config.getApplicationName() + ")");
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		int target = getIntent().getIntExtra("com.magizdev.dayplan.Target",
				position);
		jumpTo(target);
	}

	@Override
	public void jumpTo(int target) {
		selectItem(target);
		invalidateOptionsMenu();
	}

	@Override
	public void ChangeTo(long newId) {
		((DeploymentListFragement) deploymentListFragment).loadDeployments();

	}

}
