package com.okebenoithub.android.www.utils.youtube

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.data.models.DownloadModel
import com.okebenoithub.android.www.data.models.ProjectModel
import com.okebenoithub.android.www.databinding.ListYtvideoItemBinding
import com.okebenoithub.android.www.utils.CoroutineUtil
import com.okebenoithub.android.www.utils.CoroutineUtil.launchOnIO
import com.okebenoithub.android.www.utils.MainUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This class is used to search videos on Youtube.
 */
class YoutubeSearchUtil(
    private val context: Context,
    private val apiKey: String,
    private val recyclerView: RecyclerView,
) {

    /**
     * This interface is used to search videos on Youtube.
     */
    interface YouTubeApiService {
        @GET("search")
        suspend fun searchVideos(
            @Query("part") part: String,
            @Query("q") query: String,
            @Query("key") apiKey: String,
            @Query("type") type: String = "video",
            @Query("maxResults") maxResults: Int = 10
        ): YouTubeSearchResponse
    }

    // this class is used to parse search response
    data class YouTubeSearchResponse(
        @SerializedName("items") val items: List<VideoItem>
    )

    // this class is used to parse video item
    data class VideoItem(
        @SerializedName("id") val id: VideoId,
        @SerializedName("snippet") val snippet: VideoSnippet
    )

    // this class is used to parse video id
    data class VideoId(
        @SerializedName("videoId") val videoId: String
    )

    // this class is used to parse video snippet
    data class VideoSnippet(
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("thumbnails") val thumbnails: Thumbnails
    )

    // this class is used to parse thumbnails
    data class Thumbnails(
        @SerializedName("default") val default: Thumbnail
    )

    // this class is used to parse thumbnail
    data class Thumbnail(
        @SerializedName("url") val url: String
    )

    /**
     * Create a Retrofit instance and a YouTubeApiService instance.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService by lazy {
        retrofit.create(YouTubeApiService::class.java)
    }

    /**
     * Search and display videos on Youtube.
     * @param query search query
     */
    fun searchAndDisplayVideos(
        query: String,
        onClick: (VideoItem) -> Unit,
        onAddFavorite: (VideoItem) -> Unit,
        onRemoveFavorite: (VideoItem) -> Unit,
        favorites: ArrayList<VideoItem> = ArrayList()
    ) {
         launchOnIO {
            try {
                val response = apiService.searchVideos(
                    part = "snippet",
                    query = query,
                    apiKey = apiKey
                )
                val videoAdapter = VideoAdapter(response.items, onClick, onAddFavorite, onRemoveFavorite, favorites)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = videoAdapter
            } catch (e: Exception) {
                MainUtil().showToastMessage(context, "Failed to load videos")
            }
        }
    }

    /**
     * Video adapter class.
     * @param videoItems list of video items
     * @param onClick callback function when a video item is clicked
     */
    class VideoAdapter(
        private val videoItems: List<VideoItem>,
        private val onClick: (VideoItem) -> Unit,
        private val onAddFavorite: (VideoItem) -> Unit,
        private val onRemoveFavorite: (VideoItem) -> Unit,
        private val favorites: ArrayList<VideoItem>
    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

        private val callback = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem == newItem
            }
        }

        private val differ = AsyncListDiffer(this, callback)

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): VideoViewHolder {
            val binding = ListYtvideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return VideoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            val videoItem = differ.currentList[position]
            holder.bind(videoItem)
        }

        override fun getItemCount(): Int = differ.currentList.size

        inner class VideoViewHolder(val binding: ListYtvideoItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(videoItem: VideoItem) {
                val thumbnail = videoItem.snippet.thumbnails.default
                val title = videoItem.snippet.title
                val description = videoItem.snippet.description

                binding.ytVideoTitle.text = title
                Glide.with(itemView.context).load(thumbnail).into(binding.ytVideoThumbnail)

                // set click listener on item view
                binding.root.setOnClickListener { onClick(videoItem) }

                // set click listener on play button
                binding.iconVideoPlay.visibility = View.GONE
                binding.iconVideoPlay.setOnClickListener { onClick(videoItem) }

                // check if video is in favorites
                val isFavorite = favorites.contains(videoItem)
                if (isFavorite) {
                    binding.iconRemoveFavorite.visibility = View.VISIBLE
                    binding.iconAddFavorite.visibility = View.GONE
                } else {
                    binding.iconAddFavorite.visibility = View.VISIBLE
                    binding.iconRemoveFavorite.visibility = View.GONE
                }

                // set click listener on add to favorite button
                binding.iconAddFavorite.setOnClickListener { onAddFavorite(videoItem) }

                // set click listener on remove from favorite button
                binding.iconRemoveFavorite.setOnClickListener { onRemoveFavorite(videoItem) }
            }
        }
    }
}