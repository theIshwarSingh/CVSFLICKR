package com.interview.myapplication.ui.views.dasboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.interview.myapplication.R
import com.interview.myapplication.ui.theme.Purple40
import com.interview.myapplication.ui.viewmodel.MainActivityViewModel

@Composable
fun SearchUI(
    recentPostViewModel: MainActivityViewModel,
    modifier: Modifier = Modifier
) {
    val focusHandler = LocalFocusManager.current
    val queryText by recentPostViewModel.tagQuery.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.grid_item_spacing),
            vertical = dimensionResource(R.dimen.spacing_small)
        )
    ) {
        SearchTextField(
            query = queryText,
            onQueryChange = { newQuery ->
                recentPostViewModel.setSearchTag(newQuery)
            },
            onClearClick = {
                recentPostViewModel.setSearchTag("")
            },
            focusManager = focusHandler
        )
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    focusManager: FocusManager
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_radius))
            ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Purple40,
            letterSpacing = 0.15.sp
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder_text),
                fontSize = 18.sp,
                color = Purple40
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_radius)),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.search_clear_description),
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_standard_size))
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon_description),
                tint = Color.Black,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_standard_size))
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            focusedTextColor = Color.DarkGray,
            unfocusedTextColor = Color.DarkGray,
            cursorColor = Color.Black,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search,
            keyboardType = KeyboardType.Text
        )
    )
}
