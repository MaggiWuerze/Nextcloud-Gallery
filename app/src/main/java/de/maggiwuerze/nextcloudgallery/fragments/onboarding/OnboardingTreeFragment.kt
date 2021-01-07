package de.maggiwuerze.nextcloudgallery.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.activity.Onboarding
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder
import de.maggiwuerze.nextcloudgallery.util.TreeFolder
import de.maggiwuerze.nextcloudgallery.viewholder.SelectableItemHolder
import de.maggiwuerze.nextcloudgallery.viewmodel.OnboardingViewModel
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_tree.*


/**
 * A placeholder fragment containing a simple view.
 */
@Suppress("DEPRECATION")
class OnboardingTreeFragment : Fragment() {

    private val DEBUG_TAG = "OnboardingTreeFragment"
    private lateinit var onboardingViewModel: OnboardingViewModel
    var folderCount = 1
    var atv: AndroidTreeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            onboardingViewModel = ViewModelProviders.of(it).get(OnboardingViewModel::class.java)
                .apply {
                    setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tree, container, false)

    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!userVisibleHint) {
            return
        }

        var treeView: View?

        var treeRootNode = TreeNode.root()

        val rootNode =
            TreeNode(onboardingViewModel.remoteRootFolder).apply {
                viewHolder = SelectableItemHolder(activity)
                isExpanded = true
                treeRootNode?.addChild(this)
            }


        if (onboardingViewModel.remoteRootFolder != null) {
            fillFolderStructure(onboardingViewModel.remoteRootFolder!!, rootNode)
        } else {
            fillFolderStructureDebug(rootNode)
        }

        treeView = AndroidTreeView(activity, treeRootNode)
            .apply {
                setDefaultAnimation(true)
                isSelectionModeEnabled = true
                atv = this
            }
            .view
            .apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            }

        initTree(rootNode, atv)
        (activity as Onboarding).intro_btn_next.setOnClickListener {
            val selectedValues = atv!!.getSelectedValues(TreeFolder::class.java)
            onboardingViewModel.folderList = selectedValues
            (activity as Onboarding).moveToPage(2, true)
        }

        tree_layout.addView(treeView)

        ConstraintSet()
            .apply {
                clone(tree_layout)
                connect(
                    treeView!!.id,
                    ConstraintSet.START,
                    tree_layout.id,
                    ConstraintSet.START,
                    16
                )
                connect(
                    treeView.id,
                    ConstraintSet.END,
                    tree_layout.id,
                    ConstraintSet.END,
                    16
                )
                connect(
                    treeView.id,
                    ConstraintSet.TOP,
                    tree_layout.id,
                    ConstraintSet.TOP,
                    16
                )
                connect(
                    treeView.id,
                    ConstraintSet.BOTTOM,
                    tree_layout.id,
                    ConstraintSet.BOTTOM,
                    16
                )
                applyTo(tree_layout)
            }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tState", atv!!.saveState)
    }

    private fun initTree(rootNode: TreeNode, atv: AndroidTreeView?) {
        atv?.expandAll()
        atv?.collapseAll()
        atv?.expandNode(rootNode)
        rootNode.children.forEach {
            atv?.collapseNode(it)
        }


    }

    private fun fillFolderStructureDebug(parentNode: TreeNode) {

        val file1 = TreeNode("File1")
            .setViewHolder(SelectableItemHolder(activity)).apply {
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                )
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                )
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                        .apply {
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                        }
                )
                addChild(
                    TreeNode("File1")
                        .setViewHolder(SelectableItemHolder(activity))
                )
            }
        val file2 = TreeNode("File1")
            .setViewHolder(SelectableItemHolder(activity))
            .apply {
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                )
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                        .apply {
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                                    .apply {
                                        addChild(
                                            TreeNode("File4")
                                                .setViewHolder(SelectableItemHolder(activity))
                                        )
                                        addChild(
                                            TreeNode("File4")
                                                .setViewHolder(SelectableItemHolder(activity))
                                        )
                                        addChild(
                                            TreeNode("File4")
                                                .setViewHolder(SelectableItemHolder(activity))
                                        )
                                        addChild(
                                            TreeNode("File4")
                                                .setViewHolder(SelectableItemHolder(activity))
                                        )
                                    }
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                        }
                )
            }
        val file3 = TreeNode("File1")
            .setViewHolder(SelectableItemHolder(activity))
            .apply {
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                        .apply {
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                            addChild(
                                TreeNode("File3")
                                    .setViewHolder(SelectableItemHolder(activity))
                            )
                        }
                )
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                )
                addChild(
                    TreeNode("File2")
                        .setViewHolder(SelectableItemHolder(activity))
                )
            }

        parentNode.addChildren(file1, file2, file3)

    }

    private fun fillFolderStructure(remoteFolder: TreeFolder, parentNode: TreeNode) {

        remoteFolder.children.forEach {

            folderCount++
            val treeNode =
                TreeNode(it)
                    .setViewHolder(SelectableItemHolder(activity))


            if (it.children.isNotEmpty()) {
                treeNode.isExpanded = false
                parentNode.addChild(treeNode)
                fillFolderStructure(it, treeNode)
            } else {
                parentNode.addChild(treeNode)
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
        fun newInstance(sectionNumber: Int): OnboardingTreeFragment {
            return OnboardingTreeFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                    }
                }
        }
    }
}