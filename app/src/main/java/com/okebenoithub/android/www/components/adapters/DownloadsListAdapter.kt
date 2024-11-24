package com.okebenoithub.android.www.components.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.okebenoithub.android.www.data.models.DownloadModel
import com.okebenoithub.android.www.databinding.DownloadItemBinding
import com.okebenoithub.android.www.ui.fragments.download.viewModel.DownloadFilesViewModel
import com.okebenoithub.android.www.utils.MainUtil

class DownloadsListAdapter(val context: Context, private val viewModel: DownloadFilesViewModel): RecyclerView.Adapter<DownloadsListAdapter.DownloadsListViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<DownloadModel>() {
        override fun areItemsTheSame(oldItem: DownloadModel, newItem: DownloadModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DownloadModel, newItem: DownloadModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
    private var kDownloader = viewModel.kDownloader

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadsListAdapter.DownloadsListViewHolder {
        val binding = DownloadItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadsListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DownloadsListAdapter.DownloadsListViewHolder,
        position: Int
    ) {
        val downloadItem = differ.currentList[position]
        holder.bind(downloadItem)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class DownloadsListViewHolder(val binding: DownloadItemBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(downloadModel: DownloadModel) {
            binding.fileName.text = downloadModel.fileName
            binding.progressBar.progress = downloadModel.progress
            binding.progressText.text = "${downloadModel.progress}%"
            binding.status.text = downloadModel.status

            val request = viewModel.createDownloadRequest(downloadModel, "xyz", false)

            binding.fileName.text = downloadModel.fileName
            binding.startCancelButton.setOnClickListener {
                if (binding.startCancelButton.text.equals("Start")) {
                    MainUtil().showToastMessage(context, "Starting download...")
                    downloadModel.id = kDownloader.enqueue(request,
                        onStart = {
                            binding.status.text = "Started"
                            binding.startCancelButton.text = "Cancel"
                            binding.resumePauseButton.isEnabled = true
                            binding.resumePauseButton.visibility = View.VISIBLE
                            binding.resumePauseButton.text = "Pause"
                        },
                        onProgress = {
                            binding.status.text = "In Progress"
                            binding.progressBar.progress = it
                            binding.progressText.text = "$it%"
                        },
                        onCompleted = {
                            binding.status.text = "Completed"
                            binding.progressText.text = "100%"
                            binding.startCancelButton.visibility = View.GONE
                            binding.resumePauseButton.visibility = View.GONE
                        },
                        onError = {
                            binding.status.text = "Error : $it"
                            binding.resumePauseButton.visibility = View.GONE
                            binding.progressBar.progress = 0
                            binding.progressText.text = "0%"
                        },
                        onPause = {
                            binding.status.text = "Paused"
                        }
                    )
                } else {
                    kDownloader.cancel(downloadModel.id)
                    binding.startCancelButton.text = "Start"
                }
            }
        }
    }
}