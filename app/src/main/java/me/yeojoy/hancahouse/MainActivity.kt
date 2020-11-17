package me.yeojoy.hancahouse;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.app.adapter.HouseAdapter
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseDBRepository
import me.yeojoy.hancahouse.util.AlarmUtil
import me.yeojoy.hancahouse.util.AppPreferences

class MainActivity : AppCompatActivity(), MainContract.View, Constants {

    private val TAG = MainActivity::class.simpleName

    private lateinit var presenter: MainContract.Presenter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPresenter(MainPresenter(this))

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = HouseAdapter(presenter)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val floatActionButton = findViewById<FloatingActionButton>(R.id.floating_action_button_refresh)
        val houseDBRepository = HouseDBRepository(application)
        floatActionButton.setOnClickListener { presenter.retrieveHouses(houseDBRepository) }

        presenter.retrieveHouses()
        // TODO if this is first launch, load page 2 and 3.

        if (AppPreferences.crawlerStatus.equals("on")) {
            // TODO start Crawler
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        if (BuildConfig.DEBUG) {
            menu?.add(0, R.id.check_alarm_manager, 2, R.string.check_crawler)
            menu?.add(0, R.id.delete_all_from_table, 3, R.string.delete_all_from_table)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // TODO check running status
        // and then button visible
//        boolean isRunning = AlarmUtil.isAlarmManagerRunning(this);
//        MenuItem startItem = menu.findItem(R.id.start_crawler);
//        MenuItem stopItem = menu.findItem(R.id.stop_crawler);
//        startItem.setVisible(!isRunning);
//        stopItem.setVisible(isRunning);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.go_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.start_crawler -> {
                // TODO startCrawler and then notify start
                return true
            }
            R.id.stop_crawler -> {
                // TODO stopCrawler and then notify start
                return true
            }
            R.id.check_alarm_manager -> {
                // TODO call checkCrawler()
                return true
            }
            R.id.delete_all_from_table -> {
                presenter.optionDeleteAllClicked()
                AppPreferences.isFirstLaunch = true
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCrawler() {
        AlarmUtil.startCrawlerWithTime(this);
        AppPreferences.crawlerStatus = "on"
    }

    private fun stopCrawler() {
        AlarmUtil.stopCrawler(this);
        AppPreferences.crawlerStatus = "off"
    }


    private fun checkCrawler() {
        // TODO check crawler isRunning ask to AlarmUtil.
//        Toast.makeText(this, AlarmUtil.isAlarmManagerRunning(this) ?
//                        R.string.toast_alarm_running : R.string.toast_alarm_stopped,
//                Toast.LENGTH_SHORT).show();
    }

    override fun onGetHouses(houses: List<House>) {
    }

    override fun turnedOnAlarmManager() {
    }

    override fun turnedOffAlarmManager() {
    }

    override fun onItemClicked(house: House) {
        // TODO set image transition animation
        val intent = Intent(this, DetailHouseActivity::class.java)
        intent.putExtra(House::class.simpleName, house)
//        startActionMode(intent /* , activitonOptions.toBundle()*/)
        startActivity(intent)
    }

    override fun refreshResult(houses: List<House>?) {

    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }
}
