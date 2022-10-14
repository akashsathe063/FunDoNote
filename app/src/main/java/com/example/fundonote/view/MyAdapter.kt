package com.example.fundonote.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.R
import com.example.fundonote.R.*
import com.example.fundonote.HomeFragment
import com.example.fundonote.model.Notes

class MyAdapter(var noteList: ArrayList<Notes>, var context: Context) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    var allNote = mutableListOf<Notes>().apply {
        Log.d("MyAdapter", noteList.size.toString())
        addAll(noteList)
        notifyDataSetChanged()
    }

    //    fun deleteNote(position: Int){
//        noteList.removeAt(position)
//        notifyItemRemoved(position)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout.note_item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var note: Notes = allNote[position]


        holder.noteTitle.text = note.noteTitle
        holder.noteDescription.text = note.noteDescription
        holder.mMenues.setOnClickListener {
            val popupMenu = PopupMenu(it.context, holder.mMenues)

            popupMenu.menuInflater.inflate(R.menu.cardview_list_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    id.editTv -> {
                        val fragment = UpdateNoteFragment()
                        val transaction =
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        var bundle = Bundle()
                        bundle.putString("noteId", note.noteId)
                        fragment.arguments = bundle
                        transaction.replace(R.id.fragmaintContainer, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                        Log.d("MyAdapter", "${note.noteId}")


                    }

                    id.Delete -> {

                        //   noteViewModel.deleteNotes.observe()
                        val fragment = HomeFragment()
                        val transaction =
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        var bundle = Bundle()
                        bundle.putString("noteId", note.noteId)
                        Log.d("MyAdapter", "note id in delete ${note.noteId}")
                        fragment.arguments = bundle
                        transaction.replace(R.id.fragmaintContainer, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    }

                }
                true
            }

            popupMenu.show()
            val popUp = PopupMenu::class.java.getDeclaredField("mPopup")
            popUp.isAccessible = true
            val menu = popUp.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun getItemCount(): Int {
        return allNote.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView
        var noteDescription: TextView
        var mMenues: ImageView


        init {
            noteTitle = itemView.findViewById(id.tvNoteTitle)
            noteDescription = itemView.findViewById(id.NoteDescription)
            mMenues = itemView.findViewById(id.mMenus)


        }


    }


}