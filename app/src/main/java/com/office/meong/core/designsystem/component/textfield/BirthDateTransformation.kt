package com.office.meong.core.designsystem.component.textfield

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.insert

/** 생년월일 입력칸: 숫자만 남기고 8자리(YYYYMMDD)로 제한한다 */
val BirthDateInputTransformation: InputTransformation =
    InputTransformation.byValue { _, proposed -> proposed.filter { it.isDigit() }.take(8) }

/** 저장된 숫자열(YYYYMMDD)을 화면에는 YYYY-MM-DD로 보여준다. 끝자리엔 '-'를 붙이지 않는다 */
val BirthDateOutputTransformation = OutputTransformation {
    val rawLength = length
    if (rawLength > 6) insert(6, "-")
    if (rawLength > 4) insert(4, "-")
}

/** YYYYMMDD 8자리가 다 채워졌을 때만 "YYYY-MM-DD"로 변환하고, 아니면 선택 입력이므로 null로 취급한다 */
fun String.toIsoBirthDateOrNull(): String? {
    val digits = filter { it.isDigit() }
    return if (digits.length == 8) {
        "${digits.substring(0, 4)}-${digits.substring(4, 6)}-${digits.substring(6, 8)}"
    } else {
        null
    }
}
