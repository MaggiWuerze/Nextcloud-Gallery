package de.maggiwuerze.nextcloudgallery.viewholder

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder
import de.maggiwuerze.nextcloudgallery.util.TreeFolder

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
class SelectableItemHolder(context: Context?) :
    BaseNodeViewHolder<TreeFolder>(context) {
    private var tvValue: TextView? = null
    private var nodeSelector: CheckBox? = null
    private var arrowView: ImageView? = null
    override fun createNodeView(
        node: TreeNode,
        value: TreeFolder
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_selectable_item, null, false)

        RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = (node.level - 2) * 70
            view.layoutParams = this
        }
        view.findViewById<View>(R.id.itemnode_divider).visibility =
            if (node.level == 1) View.INVISIBLE else View.VISIBLE
        tvValue = view.findViewById<View>(R.id.itemnode_value) as TextView
        tvValue!!.text = value.toString()
        nodeSelector = view.findViewById<View>(R.id.itemnode_selector) as CheckBox

        arrowView = view.findViewById<View>(R.id.itemnode_chevron) as ImageView
        if (node.children.isEmpty()) {
            arrowView!!.visibility = View.GONE
        }
        nodeSelector!!.setOnCheckedChangeListener { buttonView, isChecked ->
            node.isSelected = isChecked
            for (n in node.children) {
                treeView.selectNode(n, isChecked)
            }
        }
        nodeSelector!!.isChecked = node.isSelected
        return view
    }

    override fun toggle(active: Boolean) {

        if (active) {

            val imageViewObjectAnimator = ObjectAnimator.ofFloat(
                arrowView!!,
                "rotation", 0f, 90f
            )
            imageViewObjectAnimator.duration = 300 // miliseconds
            imageViewObjectAnimator.start()
        } else {
            val imageViewObjectAnimator = ObjectAnimator.ofFloat(
                arrowView!!,
                "rotation", 90f, 0f
            )
            imageViewObjectAnimator.duration = 300 // miliseconds
            imageViewObjectAnimator.start()
        }
    }

    override fun toggleSelectionMode(editModeEnabled: Boolean) {
        nodeSelector!!.visibility = if (editModeEnabled) View.VISIBLE else View.GONE
        nodeSelector!!.isChecked = mNode.isSelected
    }
}