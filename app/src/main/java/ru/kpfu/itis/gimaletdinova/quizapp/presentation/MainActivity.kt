package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import ru.kpfu.itis.gimaletdinova.quizapp.R


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel.setTheme()

        setStartDestination()
    }

    private fun setStartDestination() {
        runBlocking {
            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?
            val navController = navHost!!.navController

            val navInflater = navController.navInflater

            val graph = navInflater.inflate(R.navigation.navigation)
            graph.setStartDestination(appViewModel.getStartDestination())
            navController.graph = graph
        }
    }

}
