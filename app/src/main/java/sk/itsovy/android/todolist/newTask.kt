package sk.itsovy.android.todolist

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sk.itsovy.android.todolist.databinding.FragmentNewTaskBinding


class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {
    val fireStoreDatabase = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentNewTaskBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if(taskItem != null){
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.taskName.text = editable.newEditable(taskItem!!.name)
            binding.taskDescription.text = editable.newEditable(taskItem!!.desc)
        }else{
            binding.taskTitle.text = "Edit Task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.SaveButton.setOnClickListener{

            saveAction()


        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun saveAction(){

        val name = binding.taskName.text.toString()
        val desc = binding.taskDescription.text.toString()
        if(taskItem == null){
            val newTask = TaskItem(name,desc,null,null)
            taskViewModel.addTaskItem(newTask)
        }else{
            taskViewModel.updateTaskItem(taskItem!!.id,name,desc,null)
        }
        val hashMap = hashMapOf(
            "nameNote" to name,
            "descriptionNote" to desc

        )
        fireStoreDatabase.collection("notes")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d("TAG", "Added document")
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error adding document $exception")
            }
        binding.taskName.setText("")
        binding.taskDescription.setText("")
        dismiss()
    }

}