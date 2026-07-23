package com.office.meong.core.designsystem.component.topbar

import androidx.annotation.DrawableRes
import com.office.meong.R

enum class TopbarAction(
    @param:DrawableRes val iconRes: Int
) {
    MORE(R.drawable.ic_more_horiz),
    CLOSE(R.drawable.ic_close)
}
