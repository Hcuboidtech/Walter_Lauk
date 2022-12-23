package com.walterlauk.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.walterlauk.R
import com.walterlauk.models.PartData
import com.walterlauk.models.TruckTrailer


class TrailerPartsAdapter(
    var context: Context,
    var trailerParentData: ArrayList<TruckTrailer>
) : ExpandableListAdapter {
    override fun registerDataSetObserver(p0: DataSetObserver?) {

    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getGroupCount(): Int {
        return trailerParentData.size
    }

    override fun getChildrenCount(parentPosition: Int): Int {
        val partList: ArrayList<PartData>? = trailerParentData[parentPosition].parts
        return partList!!.size
    }

    override fun getGroup(parentPosition: Int): Any {
        return trailerParentData[parentPosition]
    }

    override fun getChild(parentPosition: Int, childPosition: Int): Any {
        val partList: ArrayList<PartData>? = trailerParentData[parentPosition].parts
        return partList!![childPosition]
    }

    override fun getGroupId(parentPosition: Int): Long {
        return parentPosition.toLong()
    }

    override fun getChildId(parentPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

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

        println("TESTING TRAILER  -> ")

        if (truckTrailer.parts!![parentPosition].departurepart ==null || truckTrailer.parts!![parentPosition].damagePart == null){
            println("TESTING TRAILER  -> IS NULL")
            parentView!!.findViewById<ImageView>(R.id.img_cross)
             .setImageDrawable(context.getDrawable(R.drawable.ic_green_right))
        }else{
            parentView!!.findViewById<ImageView>(R.id.img_cross)
                .setImageDrawable(context.getDrawable(R.drawable.ic_red_cross))
            println("TESTING TRAILER  -> IS NOT NULL")
        }

//        if (truckTrailer.isPartAdded) {
//            parentView!!.findViewById<ImageView>(R.id.img_cross)
//                .setImageDrawable(context.getDrawable(R.drawable.ic_red_cross))
//        } else {
//            parentView!!.findViewById<ImageView>(R.id.img_cross)
//                .setImageDrawable(context.getDrawable(R.drawable.ic_green_right))
//        }
        return parentView!!
    }

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

        if (partData.departurepart !=null || partData.damagePart !=null) {
            childView!!.findViewById<ImageView>(R.id.img_cross).visibility = VISIBLE
            childView!!.findViewById<ImageView>(R.id.img_cross)
                .setImageDrawable(context.getDrawable(R.drawable.ic_red_cross))
        } else {
            childView!!.findViewById<ImageView>(R.id.img_cross).visibility = GONE
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

    }

    override fun onGroupCollapsed(p0: Int) {

    }

    override fun getCombinedChildId(p0: Long, p1: Long): Long {
        return 0
    }

    override fun getCombinedGroupId(p0: Long): Long {
        return 0
    }
}