package com.interview.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.interview.myapplication.ui.navigation.AppNavHost
import com.interview.myapplication.ui.theme.MyApplicationTheme
import com.interview.myapplication.ui.viewmodel.MainActivityViewModel
import com.interview.myapplication.ui.views.dasboard.DashBoardScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private val TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
               val navController = rememberNavController()
                AppNavHost(navController, viewModel)
            }
        }
    }
}