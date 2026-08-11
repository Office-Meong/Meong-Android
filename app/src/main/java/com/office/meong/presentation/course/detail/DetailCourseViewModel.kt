package com.office.meong.presentation.course.detail

import androidx.lifecycle.ViewModel
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.pet.repository.PetRepository
import javax.inject.Inject

class DetailCourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val petRepository: PetRepository
) : ViewModel() {

}
