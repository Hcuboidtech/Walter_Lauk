package com.walterlauk.api

interface FragmentCallback {
    fun onDataSet(
        isAdded: Boolean,
        parentPosition: Int?,
        childPosition: Int?,
        isTruckClicked: Boolean
    )
}