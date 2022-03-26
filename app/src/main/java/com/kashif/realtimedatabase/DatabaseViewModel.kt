package com.kashif.realtimedatabase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    //get reference will get the reference of the name specified
    //if it's left empty, it will give the reference of root node
    //if child of this name does not exist, than it will create the child with this name and return the reference
    private val notesRef = database.getReference(NOTE_ITEMS)
    private val _status = MutableLiveData<String>()
    private val _notesList = MutableLiveData<List<NoteItem>>()
    val notesList: LiveData<List<NoteItem>> get() = _notesList
    val status: LiveData<String> get() = _status

    fun addNoteItemToDatabase(item: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //using currentTimeMillis() as child identifier so that it will be unique and will make updating and deletion easier
                notesRef.child(item.itemId.toString())
                    .setValue(item)
                    .addOnSuccessListener {
                        _status.postValue("Item added to database.")
                    }.addOnFailureListener {
                        _status.postValue("Item addition failed.")
                    }
            } catch (e: Exception) {
                _status.postValue("Unexpected error occurred.")
                Log.e(VIEW_MODEL_TAG, e.message.toString())
            }
        }
    }

    fun getNoteItemsFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //it will notify the frontend everytime a value is added, updated or deleted in database.
                notesRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tempList = arrayListOf<NoteItem>()
                        snapshot.children.forEach {
                            val item = it.getValue<NoteItem>()
                            if (item != null) {
                                tempList.add(item)
                            }
                        }
                        tempList.sortByDescending {
                            it.itemId
                        }
                        _notesList.postValue(tempList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _status.postValue("Connection Cancelled.")
                    }
                })
            } catch (E: Exception) {
                _status.postValue("Unexpected error occurred.")
                Log.e(VIEW_MODEL_TAG, E.message.toString())
            }
        }
    }

    fun deleteNoteItem(item: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //setting an item to null in realtime database will delete it from database
                notesRef.child(item.itemId.toString())
                    .setValue(null)
                    .addOnSuccessListener {
                        _status.postValue("Item Deleted.")
                    }.addOnFailureListener {
                        _status.postValue("An error occurred.")
                    }
            } catch (e: Exception) {
                _status.postValue("Unexpected error occurred.")
                Log.e(VIEW_MODEL_TAG, e.message.toString())
            }
        }
    }

    fun updateNoteItem(item: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //there's no out of the box updating function available in realtime database
                //so we replace the node with updated values
                item.done = !item.done
                notesRef.child(item.itemId.toString())
                    .setValue(item)
                    .addOnSuccessListener {
                        _status.postValue("Item Updated.")
                    }.addOnFailureListener {
                        _status.postValue("An error occurred.")
                    }
            } catch (e: Exception) {
                _status.postValue("Unexpected error occurred.")
                Log.e(VIEW_MODEL_TAG, e.message.toString())
            }
        }
    }

}