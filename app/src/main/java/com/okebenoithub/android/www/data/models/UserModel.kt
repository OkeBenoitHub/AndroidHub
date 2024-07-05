package com.okebenoithub.android.www.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Represents User Model
 * Created By : Oke Benoit
 */
@Parcelize
data class UserModel (
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val email: String = "",
    val bio: String = "",
    val age: Int = 18,
    val gender: String = "",
    val profilePic: String = "",
    val timeCreated: Long = Date().time
):Parcelable