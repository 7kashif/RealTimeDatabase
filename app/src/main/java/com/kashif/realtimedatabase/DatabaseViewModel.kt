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

class DatabaseViewModel: ViewModel() {
    private val database  = FirebaseDatabase.getInstance()
    private val notesRef = database.getReference(NOTE_ITEMS)
    private val _status = MutableLiveData<String>()
    private val _notesList = MutableLiveData<List<NoteItem>>()
    val notesList: LiveData<List<NoteItem>> get() = _notesList
    val status: LiveData<String> get() = _status

    fun addItemToDatabase(item: NoteItem) {
        _status.postValue("database function called.")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notesRef.child(item.itemTitle)
                    .setValue(item)
                    .addOnSuccessListener {
                        _status.postValue("Item added to database.")
                    }.addOnFailureListener {
                        _status.postValue("Item addition failed.")
                    }
            } catch (e: Exception) {
                Log.e(VIEW_MODEL_TAG,e.message.toString())
            }
        }
    }

    fun getNoteItemsFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notesRef.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tempList = arrayListOf<NoteItem>()
                        snapshot.children.forEach{
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
                _status.postValue("An error occurred while fetching values.")
                Log.e(VIEW_MODEL_TAG, E.message.toString())
            }
        }
    }

}