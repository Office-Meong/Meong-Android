package com.office.meong.presentation.explore.detail.model

import androidx.annotation.DrawableRes
import com.office.meong.R

enum class PetWorkGrade(
    @DrawableRes val imageResId: Int,
    val description: String
) {
    A(
        imageResId = R.drawable.ic_grade_a,
        description = "반려견 동반 조건과 산책 접근성이 좋아\n워케이션 코스에 적합한 장소예요"
    ),
    B(
        imageResId = R.drawable.ic_grade_b,
        description = "반려견 동반 조건과 산책 환경이 좋아\n편하게 방문하기 좋은 장소예요"
    ),
    C(
        imageResId = R.drawable.ic_grade_c,
        description = "반려견 동반이 가능하지만\n방문 전 이용 조건을 확인해주세요"
    ),
    D(
        imageResId = R.drawable.ic_grade_d,
        description = "반려견 동반 조건에 제약이 있는 편이라\n방문 전 꼼꼼한 확인이 필요해요"
    ),
    E(
        imageResId = R.drawable.ic_grade_e,
        description = "반려견 동반이 어렵거나 까다로워\n다른 장소를 함께 살펴보시길 권해요"
    );

    companion object {
        fun from(grade: String): PetWorkGrade {
            return entries.find { it.name == grade.uppercase() } ?: E
        }
    }
}
