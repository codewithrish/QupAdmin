package app.qup.reception_management.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.qup.reception_management.data.remote.api.ReceptionApi
import app.qup.reception_management.data.remote.dto.general.ReceptionResource
import javax.inject.Inject

class GetAllReceptionsPagingData @Inject constructor(
    private val receptionApi: ReceptionApi,
    private val role: String,
    private val size: Int
): PagingSource<Int, ReceptionResource>()  {
    override fun getRefreshKey(state: PagingState<Int, ReceptionResource>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReceptionResource> {
        return try {
            val position = params.key ?: 0
            val response = receptionApi.getAllReceptions(role, position, size).body()
            return LoadResult.Page(
                data = response?._embedded?.appUserDetailsResourceList!!,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (position == response.page.totalPages-1) null else position + 1
            )
        }  catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}