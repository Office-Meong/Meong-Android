package com.office.meong.presentation.course.result.model

enum class CurrentDialogType (
    val title: String,
) {
    BACK_PRESS_EXIT("이전으로 돌아가시겠어요?"),
    COURSE_DELETE("코스 생성을 종료할까요?"),
}
