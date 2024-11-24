package com.okebenoithub.android.www.components.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.data.models.ProjectModel
import com.okebenoithub.android.www.databinding.ProjectItemBinding
import com.okebenoithub.android.www.utils.PhotoUtil

class ProjectsListAdapter(val context: Context) :
    RecyclerView.Adapter<ProjectsListAdapter.ProjectsListViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ProjectModel>() {
        override fun areItemsTheSame(oldItem: ProjectModel, newItem: ProjectModel): Boolean {
            return oldItem.projectId == newItem.projectId
        }

        override fun areContentsTheSame(oldItem: ProjectModel, newItem: ProjectModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsListViewHolder {
        val binding = ProjectItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectsListViewHolder, position: Int) {
        val projectItem = differ.currentList[position]
        holder.bind(projectItem)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProjectsListViewHolder(private val binding: ProjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(projectModel: ProjectModel) {
            val itemThumbnail = ""
            PhotoUtil().loadPhotoFileWithGlide(
                context,
                null,
                itemThumbnail,
                binding.itemThumbnail,
                R.mipmap.ic_launcher
            )

            // set the item title
            binding.itemTitle.text = projectModel.title

            // set the publish date
            binding.itemPublishDate.text = projectModel.startDate

            // set the click listener on the item
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(projectModel)
                }
            }

            // set the click listener on the project open info web
            binding.openProjectInfoWeb.setOnClickListener {
                // open project info web
                onItemInfoBtnClickListener?.let {
                    it(projectModel)
                }
            }
        }
    }

    private var onItemClickListener: ((ProjectModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProjectModel) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemInfoBtnClickListener: ((ProjectModel) -> Unit)? = null

    fun setOnItemInfoBtnClickListener(listener: (ProjectModel) -> Unit) {
        onItemInfoBtnClickListener = listener
    }

    /**
     * Get projects list
     * This method is used to get projects list
     */
    fun getProjectsList(): List<ProjectModel> {
        return listOf(
            ProjectModel(
                "1",
                "2",
                "Live Data and ViewModel",
                "LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.",
                "https://developer.android.com/topic/libraries/architecture/livedata",
                "Wed Jun 26 2024",
                ""
            ),
            ProjectModel(
                "2",
                "2",
                "Kotlin Flows",
                "Kotlin Flow is a powerful tool for managing streams of data in a reactive programming style. It's part of Kotlin Coroutines and is designed to handle asynchronous data streams.",
                "https://developer.android.com/kotlin/flow",
                "Wed Jul 3 2024",
                ""
            ),
            ProjectModel(
                "3",
                "2",
                "Kotlin Coroutines",
                "A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.",
                "https://developer.android.com/kotlin/coroutines",
                "Wed Jul 3 2024",
                ""
            ),
            ProjectModel(
                "4",
                "2",
                "Jetpack DataStore",
                "Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers. DataStore uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.",
                "https://developer.android.com/topic/libraries/architecture/datastore",
                "Wed Jul 4 2024",
                ""
            ),
            ProjectModel(
                "5",
                "2",
                "Play Audio and Video",
                "Jetpack Media3 defines a Player interface that outlines basic functionality for playback of video and audio files. ExoPlayer is the default implementation of this interface in Media3.",
                "https://developer.android.com/media/implement/playback-app",
                "Sun Jul 21 2024",
                ""
            ),
            ProjectModel(
                "6",
                "2",
                "CameraX and Gallery",
                "CameraX is a Jetpack library, built to help make camera app development easier. For new apps, we recommend starting with CameraX. It provides a consistent, easy-to-use API that works across the vast majority of Android devices, with backward-compatibility to Android 5.0 (API level 21).",
                "https://developer.android.com/media/camera/camerax",
                "Sun Jul 21 2024",
                ""
            ),
            ProjectModel(
                "7",
                "2",
                "Download Files",
                "Kdownloader can be used to download any type of files like image, video, pdf, apk and etc.",
                "https://github.com/varungulatii/Kdownloader",
                "Mon Jul 22 2024",
                ""
            ),
            ProjectModel(
                "8",
                "2",
                "Notifications",
                "A notification is a message that Android displays outside your app's UI to provide the user with reminders, communication from other people, or other timely information from your app.",
                "https://developer.android.com/develop/ui/views/notifications",
                "Mon Jul 22 2024",
                ""
            ),
            ProjectModel(
                "9",
                "2",
                "Work and Alarm Manager",
                "WorkManager is the recommended library for persistent work. Scheduled work is guaranteed to execute sometime after its Constraints are met.",
                "https://developer.android.com/reference/androidx/work/WorkManager",
                "Sun Jul 28 2024",
                ""
            ),
            ProjectModel(
                "10",
                "2",
                "Room Database",
                "The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite",
                "https://developer.android.com/jetpack/androidx/releases/room",
                "Sun Jul 28 2024",
                ""
            )
        )
    }
}