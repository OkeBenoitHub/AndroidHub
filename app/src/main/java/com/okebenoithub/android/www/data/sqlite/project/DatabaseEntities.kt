package com.okebenoithub.android.www.data.sqlite.project
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.okebenoithub.android.www.data.models.ProjectModel

/**
 * Database model for users
 */
@Entity(tableName = "projects_table")
data class DbProjectModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val ownerId: String = "",
    val title: String,
    val description: String,
    val docLink: String,
    val startDate: String,
    val endDate: String
)

// convert list of DbUserModel to list of UserModel
fun List<DbProjectModel>.asListProjectModel(): List<ProjectModel> = map(DbProjectModel::asProjectModel)

// convert list of ProjectModel to list of DbUserModel
fun List<ProjectModel>.asListDbUserModel(): List<DbProjectModel> = map(ProjectModel::asDbProjectModel)

// convert list of DbUserModel to array of UserModel
fun List<DbProjectModel>.asArrayDbUserModel(): Array<DbProjectModel> = this.toTypedArray()

// convert ProjectModel to DbProjectModel
fun ProjectModel.asDbProjectModel(): DbProjectModel {
    return DbProjectModel(
        ownerId = ownerId,
        title = title,
        description = description,
        docLink = docLink,
        startDate = startDate,
        endDate = endDate
    )
}

// convert DbProjectModel to ProjectModel
fun DbProjectModel.asProjectModel(): ProjectModel {
    return ProjectModel(
        projectId = "",
        ownerId = ownerId,
        title = title,
        description = description,
        docLink = docLink,
        startDate = startDate,
        endDate = endDate
    )
}