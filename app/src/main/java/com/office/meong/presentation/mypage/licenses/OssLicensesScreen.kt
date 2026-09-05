package com.office.meong.presentation.mypage.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun OssLicensesScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    val libraries by produceLibraries()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MeongTheme.colors.white
            )
            .padding(paddingValues)
    ) {
        MeongTopbar(
            title = "오픈소스 라이선스",
            onBackClick = onBackClick,
        )

        LibrariesContainer(
            libraries = libraries,
            colors = LibraryDefaults.libraryColors(
                libraryContentColor = MeongTheme.colors.black,
                libraryBackgroundColor = MeongTheme.colors.gray50,
                dialogContentColor = MeongTheme.colors.black,
                dialogConfirmButtonColor = MeongTheme.colors.black
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MeongTheme.colors.gray50
                )
        )
    }
}

@Preview
@Composable
private fun OssLicensesScreenPreview() {
    MeongTheme {
        OssLicensesScreen(
            paddingValues = PaddingValues(),
            onBackClick = {}
        )
    }
}