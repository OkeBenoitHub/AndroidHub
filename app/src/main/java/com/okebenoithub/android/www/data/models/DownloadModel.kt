package com.okebenoithub.android.www.data.models

data class DownloadModel(
    var id: Int,
    var fileName: String,
    var url: String,
    var status: String = "",
    var progress: Int = 0,
    var error: String = "",
    var startOrCancel: String = "Start",
    var resumeOrPause: String = "",
    var isCompleted: Boolean = false
)