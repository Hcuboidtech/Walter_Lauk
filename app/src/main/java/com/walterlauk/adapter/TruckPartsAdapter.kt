package com.walterlauk.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walterlauk.R
import com.walterlauk.models.PartData
import com.walterlauk.models.TruckTrailer
import com.walterlauk.utils.Constants

class TruckPartsAdapter(
    var context: Context,
    var truckParentData: ArrayList<TruckTrailer>,
) : ExpandableListAdapter {
    var img_expandColapsee: ImageView? = null
    override fun registerDataSetObserver(p0: DataSetObserver?) {

    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getGroupCount(): Int {
        return truckParentData.size
    }

    override fun getChildrenCount(parentPosition: Int): Int {
        val partList: ArrayList<PartData>? = truckParentData[parentPosition].parts
        return partList!!.size
    }

    override fun getGroup(parentPosition: Int): Any {
        return truckParentData[parentPosition]
    }

    override fun getChild(parentPosition: Int, childPosition: Int): Any {
        val partList: ArrayList<PartData>? = truckParentData[parentPosition].parts
        return partList!![childPosition]
    }

    override fun getGroupId(parentPosition: Int): Long {
        return parentPosition.toLong()
    }

    override fun getChildId(childPosition: Int, p1: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    // this is working as a truck
    override fun getGroupView(
        parentPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val truckTrailer: TruckTrailer = getGroup(parentPosition) as TruckTrailer
        var parentView = convertView
        if (parentView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            parentView = layoutInflater.inflate(R.layout.item_parent_row, null)
        }

        val tv_parent = parentView!!.findViewById(R.id.tv_parent) as TextView
        if (isExpanded) {
            parentView.findViewById<ImageView>(R.id.img_expandColapsee)
                .setImageResource(R.drawable.ic_arrow_down);
        } else {
            parentView.findViewById<ImageView>(R.id.img_expandColapsee)
                .setImageResource(R.drawable.ic_arrow_right);
        }

        tv_parent.text = truckTrailer.category_name
        if (truckTrailer.parts!![parentPosition].departurepart != null || truckTrailer.parts!![parentPosition].damagePart !=null) {
            parentView!!.findViewById<ImageView>(R.id.img_cross)
                .setImageDrawable(context.getDrawable(R.drawable.ic_red_cross))
            println("TESTING - > expandable 12 ")
        } else {
            println("TESTING - > expandable 12")
            parentView!!.findViewById<ImageView>(R.id.img_cross)
                .setImageDrawable(context.getDrawable(R.drawable.ic_green_right))
        }
        return parentView!!
    }

    // this is working as the trailer
    override fun getChildView(
        parentPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var partData: PartData = getChild(parentPosition, childPosition) as PartData
        var childView = convertView
        if (childView == null) {
            val infalInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            childView = infalInflater.inflate(R.layout.item_child_row, null)
        }
        childView!!.findViewById<TextView>(R.id.tv_child).text = partData.part_name
        if (partData.departurepart == null){
            println("TESTING -> data is null")
        }else{
            println("TESTING -> DATA is not null")
        }

        if (partData.departurepart !=null) {
            println("TESTING - > expandable 12")
            childView!!.findViewById<ImageView>(R.id.img_cross).visibility = View.VISIBLE
            childView!!.findViewById<ImageView>(R.id.img_cross)
                .setImageDrawable(context.getDrawable(R.drawable.ic_red_cross))
        } else {
            println("TESTING - > expandable D")
            childView!!.findViewById<ImageView>(R.id.img_cross).visibility = View.GONE
        }
        return childView!!
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun onGroupExpanded(p0: Int) {
        //img_expandColapsee!!.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down))
    }

    override fun onGroupCollapsed(p0: Int) {
        //img_expandColapsee!!.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_right))
    }

    override fun getCombinedChildId(p0: Long, p1: Long): Long {
        return 0
    }

    override fun getCombinedGroupId(p0: Long): Long {
        return 0
    }
}