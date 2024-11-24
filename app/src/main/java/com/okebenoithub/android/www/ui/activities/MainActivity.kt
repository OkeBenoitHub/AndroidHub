package com.okebenoithub.android.www.ui.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.components.NETWORK_CELLULAR
import com.okebenoithub.android.www.components.NETWORK_STATUS_PREF_KEY
import com.okebenoithub.android.www.components.NETWORK_WIFI
import com.okebenoithub.android.www.components.NO_NETWORK
import com.okebenoithub.android.www.databinding.ActivityMainBinding
import com.okebenoithub.android.www.ui.fragments.download.viewModel.DownloadFilesVMFactory
import com.okebenoithub.android.www.ui.fragments.home.viewModel.HomeViewModelFactory
import com.okebenoithub.android.www.ui.fragments.project.viewModel.ProjectsViewModelFactory
import com.okebenoithub.android.www.ui.media.viewModel.MediaFileViewModelFactory
import com.okebenoithub.android.www.utils.ConnectionType
import com.okebenoithub.android.www.utils.DialogUtil
import com.okebenoithub.android.www.utils.MainUtil
import com.okebenoithub.android.www.utils.NetworkUtil
import com.okebenoithub.android.www.utils.PdfUtil
import com.okebenoithub.android.www.utils.PhotoUtil
import com.okebenoithub.android.www.utils.SharedPrefUtil
import com.okebenoithub.android.www.utils.firebase.FirebaseAuthUtil
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * Inject all utils dependencies
     */
    @Inject
    lateinit var mainUtil: MainUtil
    @Inject lateinit var networkUtil: NetworkUtil
    @Inject lateinit var sharedPrefUtil: SharedPrefUtil
    @Inject lateinit var dialogUtil: DialogUtil
    @Inject lateinit var photoUtil: PhotoUtil
    @Inject lateinit var pdfUtil: PdfUtil
    @Inject lateinit var firestoreDbUtil: FirestoreDbUtil
    @Inject lateinit var firebaseAuthUtil: FirebaseAuthUtil

    /**
     * Inject all view model and factory dependencies
     */
    @Inject lateinit var homeViewModelFactory: HomeViewModelFactory
    @Inject lateinit var projectsViewModelFactory: ProjectsViewModelFactory
    @Inject lateinit var mediaFileViewModelFactory: MediaFileViewModelFactory
    @Inject lateinit var downloadFilesVMFactory: DownloadFilesVMFactory

    // Navigation controller
    private lateinit var mNavController: NavController

    // View Data Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add support for up button for fragment navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        mNavController = navHostFragment.navController

        // Set up action bar with nav controller
        setupActionBarWithNavController(mNavController)

        // Change action Bar background color
        mainUtil.setActionBarBackgroundColor(this, supportActionBar, R.color.colorPrimary)

        // Set navigation bottom background color
        mainUtil.setBottomBarNavigationBackgroundColor(
            window = window,
            this,
            R.color.colorPrimary,
            R.color.bottom_black_color
        )

        /**
         * Monitor network status
         * Save status state to Shared preferences
         */
        networkUtil.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                sharedPrefUtil.writeDataStringToSharedPreferences(this,
                                    NETWORK_STATUS_PREF_KEY, NETWORK_WIFI
                                )
                            }
                            ConnectionType.Cellular -> {
                                sharedPrefUtil.writeDataStringToSharedPreferences(this,
                                    NETWORK_STATUS_PREF_KEY, NETWORK_CELLULAR
                                )
                            }
                            else -> { }
                        }
                    }
                    false -> {
                        sharedPrefUtil.writeDataStringToSharedPreferences(this,
                            NETWORK_STATUS_PREF_KEY, NO_NETWORK
                        )
                    }
                }
            }
        }
        networkUtil.register()
    }

    override fun onResume() {
        super.onResume()
        networkUtil.register()
    }

    override fun onStop() {
        super.onStop()
        networkUtil.unregister()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController,null)
    }
}