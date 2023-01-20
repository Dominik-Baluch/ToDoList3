package sk.itsovy.android.todolist

interface TaskItemClickListener {

    fun editTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem)
}