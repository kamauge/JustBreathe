package com.regera.justbreathe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import kotlinx.android.synthetic.main.activity_add_rooms.view.*
import kotlinx.android.synthetic.main.single_room.view.*

class RoomsAdapter(val context: Context, val rooms: List<Room>):
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_room,parent,false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(room: Room) {
            itemView.roomTitle.text = room.roomName.toString()
            Glide.with(context).load(room.roomLink).into(itemView.roomDashboardImage)

            itemView.setOnClickListener {


            }
        }


    }

}