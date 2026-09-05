package com.office.meong.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongStepProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$currentStep / $totalSteps 단계",
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray500
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (index < currentStep) MeongTheme.colors.primary else MeongTheme.colors.gray100
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeongStepProgressBarPreview() {
    MeongTheme {
        MeongStepProgressBar(
            currentStep = 1,
            totalSteps = 2,
            modifier = Modifier
        )
    }
}
