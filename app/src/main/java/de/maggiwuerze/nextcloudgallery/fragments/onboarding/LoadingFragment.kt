package de.maggiwuerze.nextcloudgallery.fragments.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import com.thegrizzlylabs.sardineandroid.DavResource
import com.thegrizzlylabs.sardineandroid.Sardine
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.activity.Main
import de.maggiwuerze.nextcloudgallery.model.account.Account
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.PhotoRepository
import de.maggiwuerze.nextcloudgallery.persistence.model.DatabaseManager
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolderRepository
import de.maggiwuerze.nextcloudgallery.util.BasicAuthInterceptor
import de.maggiwuerze.nextcloudgallery.util.extensions.doAfterDelay
import de.maggiwuerze.nextcloudgallery.viewmodel.OnboardingViewModel
import kotlinx.android.synthetic.main.fragment_loading.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import java.net.SocketTimeoutException

/**
 * A placeholder fragment containing a simple view.
 */
class LoadingFragment : Fragment() {

    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var sardine: Sardine
    private var imageCount = 0
    private val baseURl = "https://cloud.maggiwuerze.de"
    private var imageExtensions = arrayOf("jpg", "png", "bmp", "gif", "webp")
    private lateinit var remoteFolderRepository: RemoteFolderRepository
    private lateinit var photoRepo: PhotoRepository


    private val errorHandler = CoroutineExceptionHandler { _, error ->
        when (error) {
            is SocketTimeoutException -> {
                MainScope().launch {
                    Toast.makeText(
                        requireContext(),
                        "Could not reach Nextcloud Server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            onboardingViewModel = ViewModelProviders.of(it).get(OnboardingViewModel::class.java)
                .apply {
                    setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
                }
        }

        //save account to db
        var acc = Account(
            onboardingViewModel.user,
            onboardingViewModel.password,
            true,
            onboardingViewModel.url
        )

        GlobalScope.async {

            val database = DatabaseManager.getInstance().initialize(requireContext())
            photoRepo = database.photoRepository
            remoteFolderRepository = database.remoteFolderRepository
            database.accountRepository.insert(acc)

        }.invokeOnCompletion()
        {

            //TODO:save selected folders to db
            remoteFolderRepository.insertAll(onboardingViewModel.folderList)


            val okhttp = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(acc.username, acc.password))
                .cache(CoilUtils.createDefaultCache(context = requireContext()))
                .build()

            Coil.setDefaultImageLoader {
                ImageLoader(requireContext()) {
                    crossfade(true)
                    okHttpClient { okhttp }
                }
            }
            sardine = OkHttpSardine(okhttp)
            //read images from cloud and save to db
            listFiles("/remote.php/webdav/")

            //switch to main
//            switchToMain()

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_loading, container, false)

        doAfterDelay({ loading_detail.text = "Save account to DB" }, 1000)
        doAfterDelay({ loading_detail.text = "Save selected folders to DB" }, 3000)
        doAfterDelay({
            loading_detail.text = "Read images from cloud and save to DB\nThis might take a while"
        }, 5000)
        doAfterDelay({ loading_detail.text = "Done" }, 10000)
        return root
    }


    private fun switchToMain() {
        //mark onboarding as complete
        PreferenceManager
            .getDefaultSharedPreferences(requireContext())
            .edit()
            .putString(getString(R.string.onboarding_complete), "true")
            .apply()

        var intent = Intent(requireContext(), Main::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        requireActivity().finish()
    }

    private fun listFiles(path: String) {
        CoroutineScope(errorHandler).launch {
            var listLoaded = async { sardine.list(baseURl + path) }
            var list: List<DavResource>

            if (listLoaded.await().let { list = it!!; true }) {
                if (list.size == 1 && list[0].path == path) {
                    coroutineContext.cancel() //return
                } else {
                    list.forEach {
                        when {
                            it.path == path -> {
                            }
                            it.isDirectory -> {
                                GlobalScope.async {
                                    listFiles(path = it.path)
                                }
                            }
                            imageExtensions.contains(it.path.substringAfterLast(".").toLowerCase()) -> {
                                val date = Instant.ofEpochMilli(it.modified.time)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                                photoRepo.insert(Photo(it.name, it.path, it.path, date))
                                imageCount++
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): LoadingFragment {
            return LoadingFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                    }
                }
        }
    }
}