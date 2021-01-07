package de.maggiwuerze.nextcloudgallery.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.viewholder.IconTreeItemHolder.IconTreeItem

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
class ProfileHolder(context: Context?) :
    BaseNodeViewHolder<IconTreeItem>(context) {
    override fun createNodeView(
        node: TreeNode,
        value: IconTreeItem
    ): View {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.layout_profile_node, null, false)
        val tvValue = view.findViewById<View>(R.id.node_value) as TextView
        tvValue.text = value.text
        view.setOnClickListener(null)
        return view
    }

    override fun toggle(active: Boolean) {}
    override fun getContainerStyle(): Int {
        return R.style.TreeNodeStyleCustom
    }
}