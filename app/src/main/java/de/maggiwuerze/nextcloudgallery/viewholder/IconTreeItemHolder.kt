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
 * Created by Bogdan Melnychuk on 2/12/15.
 */
class IconTreeItemHolder(context: Context?) :
    BaseNodeViewHolder<IconTreeItem>(context) {
    private var tvValue: TextView? = null
    override fun createNodeView(
        node: TreeNode,
        value: IconTreeItem
    ): View {
        val inflater = LayoutInflater.from(context)
//        val view: View = inflater.inflate(R.layout.layout_icon_node, null, false)
        tvValue = view.findViewById<View>(R.id.node_value) as TextView
        tvValue!!.text = value.text
//        val iconView: PrintView = view.findViewById<View>(R.id.icon) as PrintView
//        iconView.setIconText(context.resources.getString(value.icon))
//        arrowView = view.findViewById<View>(R.id.arrow_icon) as PrintView
//        view.findViewById<View>(R.id.btn_addFolder)
//            .setOnClickListener {
//                val newFolder =
//                    TreeNode(
//                        IconTreeItem(
//                            R.string.ic_folder,
//                            "New Folder"
//                        )
//                    )
//                treeView.addNode(node, newFolder)
//            }
//        view.findViewById<View>(R.id.btn_delete)
//            .setOnClickListener { treeView.removeNode(node) }
//        //if My computer
//        if (node.level == 1) {
//            view.findViewById<View>(R.id.btn_delete).visibility = View.GONE
//        }
        return view
    }

    override fun toggle(active: Boolean) {
//        arrowView.setIconText(context.resources.getString(if (active) R.string.ic_keyboard_arrow_down else R.string.ic_keyboard_arrow_right))
    }

    class IconTreeItem(var icon: Int, var text: String)
}