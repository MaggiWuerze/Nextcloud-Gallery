package de.maggiwuerze.nextcloudgallery.fragments.onboarding

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.pd.chocobar.ChocoBar
import com.thegrizzlylabs.sardineandroid.DavResource
import com.thegrizzlylabs.sardineandroid.Sardine
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.thegrizzlylabs.sardineandroid.impl.SardineException
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.activity.Onboarding
import de.maggiwuerze.nextcloudgallery.extensions.getBitmapFromDrawable
import de.maggiwuerze.nextcloudgallery.util.BasicAuthInterceptor
import de.maggiwuerze.nextcloudgallery.util.TextValidator
import de.maggiwuerze.nextcloudgallery.util.TreeFolder
import de.maggiwuerze.nextcloudgallery.util.extensions.doAfterDelay
import de.maggiwuerze.nextcloudgallery.util.extensions.showToast
import de.maggiwuerze.nextcloudgallery.util.extensions.showToastShort
import de.maggiwuerze.nextcloudgallery.viewmodel.OnboardingViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.net.SocketTimeoutException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : Fragment() {

    private val DEBUG_TAG = "LoginFragment"

    private lateinit var onboardingViewModel: OnboardingViewModel
    private var usernameValidated = false
    private var passwordValidated = false
    private var urlValidated = false
    private var error = false
    private var focusedElevation: Float = 0F
    private var runningTasks = ConcurrentLinkedQueue<Job>()
    private var folderCount = 0
    private var fileReadSnackbar: Snackbar? = null

    private lateinit var sardine: Sardine

    private lateinit var errorHandler: CoroutineExceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            onboardingViewModel = ViewModelProviders.of(it).get(OnboardingViewModel::class.java)
                .apply {
                    setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
                }
        }
        focusedElevation = 5 * requireContext().resources.displayMetrics.density + 0.5F

        errorHandler = CoroutineExceptionHandler { _, error ->
            requireActivity().runOnUiThread {
                this@LoginFragment.error = true
                when (error) {
                    is SocketTimeoutException -> {
                        showToastShort(requireActivity(), "Timeout")
                    }
                    is SardineException -> {
                        when (error.statusCode) {
                            401 -> showToastShort(requireActivity(), "Wrong Credentials")
                            else -> {
                                showToastShort(
                                    requireActivity(),
                                    "Unknown Error : ${error.statusCode}"
                                )
                            }
                        }

                    }
                    else -> {
                        showToastShort(
                            requireActivity(),
                            "Unknown Error : ${error.cause ?: error.localizedMessage}"
                        )
                        Log.e(DEBUG_TAG, error.localizedMessage)
                        error.printStackTrace()
                    }
                }

                login_btn.doneLoadingAnimation(
                    R.color.red,
                    getBitmapFromDrawable(
                        requireActivity(),
                        R.drawable.ic_close_black_24dp
                    )!!
                )
                doAfterDelay({
                    login_btn.revertAnimation()
                }, 1000L)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        login_btn.isEnabled = false
        //region debug input
        //TODO: REMOVE THIS
        url_edit.setText(getString(R.string.test_url))
        username_edit.setText(getString(R.string.username))
        password_edit.setText(getString(R.string.password))
        //endregion debug input
        //region setListeners
        url_edit.addTextChangedListener(object : TextValidator(username_edit) {
            override fun validate(textView: TextView?, text: String?) {

                if (text.isNullOrBlank()) {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_failed,
                        null
                    )
                    urlValidated = false
                } else {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_successful,
                        null
                    )
                    urlValidated = true
                }
            }

        })
        url_edit.setOnFocusChangeListener { v, hasFocus ->
            v.elevation = if (hasFocus) focusedElevation else 0F
        }
        username_edit.addTextChangedListener(object : TextValidator(username_edit) {
            override fun validate(textView: TextView?, text: String?) {

                if (text.isNullOrBlank()) {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_failed,
                        null
                    )
                    usernameValidated = false
                } else {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_successful,
                        null
                    )
                    usernameValidated = true
                }
                login_btn.isEnabled = usernameValidated && passwordValidated
            }

        })
        username_edit.setOnFocusChangeListener { v, hasFocus ->
            v.elevation = if (hasFocus) focusedElevation else 0F
        }
        password_edit.addTextChangedListener(object : TextValidator(username_edit) {
            override fun validate(textView: TextView?, text: String?) {

                if (text.isNullOrBlank()) {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_failed,
                        null
                    )
                    passwordValidated = false
                } else {
                    textView?.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.input_validation_successful,
                        null
                    )
                    passwordValidated = true
                }
                login_btn.isEnabled = usernameValidated && passwordValidated
            }

        })
        password_edit.setOnFocusChangeListener { v, hasFocus ->
            v.elevation = if (hasFocus) focusedElevation else 0F
        }
        //endregion setListeners
        (activity as Onboarding).login_btn.setOnClickListener {
            /*
                try to login with the provided credentials
                on fail highlight the input and show a message
                on success save the credentials to the database
                and proceed to the next page
            */

            onboardingViewModel.user = username_edit.text.toString()
            onboardingViewModel.password = password_edit.text.toString()
            onboardingViewModel.url = url_edit.text.toString()

            sardine = OkHttpSardine(
                OkHttpClient.Builder()
                    .readTimeout(45, TimeUnit.SECONDS)
                    .addInterceptor(
                        BasicAuthInterceptor(
                            onboardingViewModel.user,
                            onboardingViewModel.password
                        )
                    )
                    .build()
            )

            fileReadSnackbar = showToast(
                requireActivity(),
                "Checking Credentials",
                ChocoBar.LENGTH_INDEFINITE
            )
            login_btn.startAnimation()
            var handler = Handler()
            handler.postDelayed({
                fileReadSnackbar?.setText("Login to your Nextcloud might be delayed.\nPlease allow up to 30 Seconds")
            }, 5000L)

            CoroutineScope(errorHandler).launch {

                Log.d(DEBUG_TAG, "checking credentials:")
                sardine.list("${onboardingViewModel.url}/remote.php/webdav/")
                handler.removeCallbacksAndMessages(null)

                Log.d(DEBUG_TAG, "Starting read, Folders:")
                requireActivity().runOnUiThread {
                    fileReadSnackbar?.setText("Reading folder structure...")
                }
                listFiles("/remote.php/webdav/", null)

                while (runningTasks.any { it.isActive } || runningTasks.size <= 1) {

                }

                if (!error) {
                    requireActivity().runOnUiThread {
                        fileReadSnackbar?.dismiss()
                        login_btn.doneLoadingAnimation(
                            R.color.green,
                            getBitmapFromDrawable(
                                requireContext(),
                                R.drawable.ic_check_black_24dp
                            )!!
                        )
                        doAfterDelay({
                            (activity as Onboarding).moveToPage(1, true)
                        }, 500)
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        login_btn.dispose()
    }

    private fun listFiles(path: String, parent: TreeFolder?) {

        runningTasks.add(CoroutineScope(errorHandler).async {

            var list: List<DavResource>
            val listLoaded = async {
                sardine.list(onboardingViewModel.url + path)
            }
            var parentFolder = parent

            if (listLoaded.await().let { list = it!!; list.isNotEmpty() }) {

                if (onboardingViewModel.remoteRootFolder == null) {
                    folderCount++
                    parentFolder =
                        TreeFolder(
                            list[0]
                        )
                    onboardingViewModel.remoteRootFolder = parentFolder
                    requireActivity().runOnUiThread {
                        fileReadSnackbar!!.setText("Reading folder structure... \nfound: $folderCount Folders")
                    }
                }

                if (list.size == 1 && list[0].path == path) {

                } else {
                    list.forEach {
                        when {
                            it.path == path -> {
                            }
                            it.isDirectory -> {
                                folderCount++
                                val folder =
                                    TreeFolder(
                                        it
                                    )
                                if (parentFolder != null) {
                                    parentFolder.children.add(folder)
                                    requireActivity().runOnUiThread {
                                        fileReadSnackbar!!.setText("Reading folder structure... \nfound: $folderCount Folders")
                                    }
                                    runningTasks.add(this.async {
                                        listFiles(it.path, folder)
                                    })
                                } else {
                                    Log.d(DEBUG_TAG, "no parent to add to")
                                }
                            }
                        }
                    }
                }
                // wait for the other threads to start
            }
            if (path == "/remote.php/webdav/") {
                while (runningTasks.size == 1) {
                    delay(100)
                }
            }

        })
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
        fun newInstance(sectionNumber: Int): LoginFragment {
            return LoginFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                    }
                }
        }
    }

}
