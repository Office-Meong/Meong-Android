package com.office.meong.presentation.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreSearchBar(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholderText: String = "장소명, 카페, 산책코스를 검색해보세요",
) {
    BasicTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MeongTheme.colors.gray50, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        textStyle = MeongTheme.typography.body.body14M.copy(
            color = MeongTheme.colors.gray500
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        cursorBrush = SolidColor(MeongTheme.colors.gray500),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = "검색",
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(4.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = placeholderText,
                            color = MeongTheme.colors.gray500,
                            style = MeongTheme.typography.body.body14M
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewExploreSearchBar() {
    MeongTheme {
        val searchState = rememberTextFieldState()

        ExploreSearchBar(
            state = searchState,
        )
    }
}
