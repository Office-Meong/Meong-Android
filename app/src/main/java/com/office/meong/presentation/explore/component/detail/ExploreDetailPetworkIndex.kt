package com.office.meong.presentation.explore.component.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.explore.model.PetWorkGrade

@Composable
fun ExploreDetailPetWorkIndex(
    grade: String,
    modifier: Modifier = Modifier
) {
    val petWorkGrade = PetWorkGrade.from(grade)

    ExploreDetailSectionContainer(title = "펫-워크 지수", modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = petWorkGrade.imageResId),
                contentDescription = "펫-워크 지수 ${petWorkGrade.name}등급",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = petWorkGrade.description,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray700
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailPetWorkIndexPreview() {
    MeongTheme {
        ExploreDetailPetWorkIndex(
            grade = "A",
            modifier = Modifier.padding(20.dp)
        )
    }
}
