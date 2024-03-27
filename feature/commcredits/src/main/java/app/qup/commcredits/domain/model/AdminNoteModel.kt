package app.qup.commcredits.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminNoteModel(
    val noteAtStage: String,
    val noteDate: Long,
    val notes: String
): Parcelable