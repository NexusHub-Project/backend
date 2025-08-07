package com.nexushub.NexusHub.Web.PatchNote.repository;

import com.nexushub.NexusHub.Web.PatchNote.domain.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {
}
