package sk.itsovy.android.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sk.itsovy.android.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),TaskItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var navigation_bottom: BottomNavigationView
     val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener{
            NewTaskSheet(null).show(supportFragmentManager,"newTask")
        }


        setRecyclerVier()
        /*setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val blankFragment = BlankFragment()


        setCurrentFragment(homeFragment)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, homeFragment).commit()
        navigation_bottom = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation_bottom.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.notes -> {
                    setCurrentFragment(blankFragment)
                    return@setOnItemSelectedListener true
                }

            }
            false
        }*/
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    private fun setRecyclerVier() {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it,mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
    }
}