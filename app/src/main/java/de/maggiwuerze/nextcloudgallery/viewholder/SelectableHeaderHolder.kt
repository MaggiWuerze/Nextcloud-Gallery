package de.maggiwuerze.nextcloudgallery.viewholder

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.viewholder.IconTreeItemHolder.IconTreeItem


/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
class SelectableHeaderHolder(context: Context?) :
    BaseNodeViewHolder<IconTreeItem>(context) {
    private var tvValue: TextView? = null
    private var arrowView: ImageView? = null
    private var nodeSelector: CheckBox? = null
    override fun createNodeView(
        node: TreeNode,
        value: IconTreeItem
    ): View {
        val inflater = LayoutInflater.from(context)
        val view: View =
            inflater.inflate(R.layout.layout_selectable_header, null, false)
        tvValue = view.findViewById<View>(R.id.node_value) as TextView
        tvValue!!.text = value.text
        arrowView = view.findViewById<View>(R.id.node_chevron) as ImageView
        if (node.isLeaf) {
            arrowView!!.visibility = View.GONE
        }
        nodeSelector = view.findViewById<View>(R.id.node_selector) as CheckBox
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
//        arrowView!!.rotation = if (active) 90F else 0F
    }

    override fun toggleSelectionMode(editModeEnabled: Boolean) {
        nodeSelector!!.visibility = if (editModeEnabled) View.VISIBLE else View.GONE
        nodeSelector!!.isChecked = mNode.isSelected
    }
}