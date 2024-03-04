package app.qup.srk_management.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.qup.srk_management.data.remote.api.SrkApi
import app.qup.srk_management.data.remote.dto.general.SrkResource
import javax.inject.Inject

class GetAllSrkPagingData @Inject constructor(
    private val srkApi: SrkApi,
    private val role: String,
    private val size: Int
): PagingSource<Int, SrkResource>()  {
    override fun getRefreshKey(state: PagingState<Int, SrkResource>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SrkResource> {
        return try {
            val position = params.key ?: 0
            val response = srkApi.getAllSrk(role, position, size).body()
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