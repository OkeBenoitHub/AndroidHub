package com.okebenoithub.android.www.data.sqlite.user
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.okebenoithub.android.www.data.models.UserModel
import java.util.Date

/**
 * Database model for users
 */
@Entity(tableName = "users_table")
data class DbUserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
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
)

// convert list of DbUserModel to list of UserModel
fun List<DbUserModel>.asListUserModel(): List<UserModel> = map(DbUserModel::asUserModel)

// convert list of UserModel to list of DbUserModel
fun List<UserModel>.asListDbUserModel(): List<DbUserModel> = map(UserModel::asDbUserModel)

// convert list of DbUserModel to array of UserModel
fun List<DbUserModel>.asArrayDbUserModel(): Array<DbUserModel> = this.toTypedArray()

// convert UserModel to DbUserModel
fun UserModel.asDbUserModel(): DbUserModel {
    return DbUserModel(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        country = country,
        email = email,
        bio = bio,
        age = age,
        gender = gender,
        profilePic = profilePic,
        timeCreated = timeCreated
    )
}

// convert DbUserModel to UserModel
fun DbUserModel.asUserModel(): UserModel {
    return UserModel(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        country = country,
        email = email,
        bio = bio,
        age = age,
        gender = gender,
        profilePic = profilePic,
        timeCreated = timeCreated
    )
}