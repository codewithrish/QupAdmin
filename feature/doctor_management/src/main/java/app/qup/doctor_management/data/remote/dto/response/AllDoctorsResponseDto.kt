package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.general.Page

data class AllDoctorsResponseDto(
    val _embedded: AllDoctorsEmbedded,
    val page: Page
) {
    data class AllDoctorsEmbedded(
        val doctorSearchResourceList: List<DoctorSearchResource>
    )
}
