package com.interview.myapplication.ui.views.dasboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.interview.myapplication.R
import com.interview.myapplication.ui.theme.BackgroundColor
import com.interview.myapplication.ui.theme.Purple40
import com.interview.myapplication.ui.viewmodel.MainActivityViewModel

@Composable
fun DashBoardScreen( navController: NavController,
                     recentPostViewModel: MainActivityViewModel,
                     modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize().background(BackgroundColor)
    ) {

        val (title, searchField, photoList) = createRefs()

        val guideLineFromTop = createGuidelineFromTop(0.25f)
        Text(
            text = stringResource(R.string.home_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Purple40,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.spacing_doubleExtraLarge))
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .testTag("imageSearchTitle")
        )

        Box(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.spacing_extraSmall))
                .constrainAs(searchField) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(guideLineFromTop)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .testTag("searchBar")
        ) {
            SearchUI(recentPostViewModel)
        }

        PostGridUI(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.spacing_doubleExtraLarge))
                .constrainAs(photoList) {
                    top.linkTo(searchField.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .testTag("photoList"),
            navController = navController,
            postViewModel = recentPostViewModel
        )
    }
}
