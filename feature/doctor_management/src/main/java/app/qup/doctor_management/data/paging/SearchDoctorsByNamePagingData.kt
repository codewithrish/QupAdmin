package app.qup.doctor_management.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.qup.doctor_management.data.remote.api.DoctorApi
import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import javax.inject.Inject

class SearchDoctorsByNamePagingData @Inject constructor(
    private val doctorApi: DoctorApi,
    private val doctorName: String,
    private val size: Int
): PagingSource<Int, DoctorSearchResource>()  {
    override fun getRefreshKey(state: PagingState<Int, DoctorSearchResource>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DoctorSearchResource> {
        return try {
            val position = params.key ?: 0
            val response = doctorApi.searchDoctorByName(doctorName, position, size).body()
            return LoadResult.Page(
                data = response?._embedded?.doctorSearchResourceList!!,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (position == response.page.totalPages-1) null else position + 1
            )
        }  catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}