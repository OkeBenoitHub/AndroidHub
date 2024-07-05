package com.okebenoithub.android.www.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a project model.
 */
@Parcelize
data class ProjectModel(
    val projectId: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val docLink: String,
    val startDate: String,
    val endDate: String
) : Parcelable
