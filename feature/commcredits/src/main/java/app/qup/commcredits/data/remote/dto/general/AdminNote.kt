package app.qup.commcredits.data.remote.dto.general

import app.qup.commcredits.domain.model.AdminNoteModel

data class AdminNote(
    val noteAtStage: String?,
    val noteDate: Long?,
    val notes: String?,
)

fun AdminNote.toAdminNoteModel(): AdminNoteModel {
    return AdminNoteModel(
        noteAtStage = noteAtStage ?: "",
        noteDate = noteDate ?: 0,
        notes = notes ?: "",
    )
}