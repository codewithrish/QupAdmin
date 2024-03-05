package app.qup.entity_management.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.qup.entity_management.data.remote.api.EntityApi
import app.qup.entity_management.data.remote.dto.general.EntityResource
import javax.inject.Inject

class SearchEntitiesPagingData @Inject constructor(
    private val entityApi: EntityApi,
    private val entityNames: List<String>,
    private val size: Int
): PagingSource<Int, EntityResource>()  {
    override fun getRefreshKey(state: PagingState<Int, EntityResource>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EntityResource> {
        return try {
            val position = params.key ?: 0
            val response = entityApi.searchEntity(entityNames, position, size).body()
            return LoadResult.Page(
                data = response?._embedded?.entitySearchResourceList!!,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (position == response.page.totalPages-1) null else position + 1
            )
        }  catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}